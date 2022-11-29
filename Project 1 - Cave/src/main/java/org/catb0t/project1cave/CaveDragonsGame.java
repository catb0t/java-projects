package org.catb0t;

import java.util.*;

import static org.catb0t.GameLogicAction.*;


/**
 * Implement GameInfo logic and formatting unique to the Cave Dragons game.
 */
public class CaveDragonsGame extends Game {

    /**
     * If the room key starts with %, it means the name of the room is the most recent answer to
     * the prompt with that name.
     */
    private static final String PROMPT_ANSWER_REFERENCE   = "%";
    private static final String PROMPT_DOES_NOT_NAME_ROOM = (
        "\"%s\": cannot find a room named by a blank prompt value (did the player ever see this " +
        "prompt?)"
    );

    private CaveDragonsGame (final Game other) {
        super(other);
    }

    CaveDragonsGame () { }

    /**
     * For each entry in the gameLogicSeq field, dispatch the appropriate engine function, such
     * as displaying story, a prompt, setting game state, or reading game state set previously.
     *
     * @return the final Game state.
     */
    @Override
    public CaveDragonsGame playGame () throws NoSuchFieldException {

        // System.out.println("<playGame>");

        Playable.initialiseRooms(this.gameState(), this.gameInfo());

        for (final GameLogicValue logicStep :
            Playable.logicSteps(this.gameInfo())) {

            if (logicStep.action() != INNER_FUNCTION) {
                Objects.requireNonNull(
                    logicStep.room(),
                    "\"room\" key must exist for all gameLogicSeq entries"
                );
            }

            // TODO improve inner_function functionality
            switch ( logicStep.action() ) {
                case INNER_FUNCTION, STORY -> this.actionStory(logicStep);
                case ROOM -> this.actionRoom(logicStep);
                case SET_ROOM_LOGIC -> this.actionSetRoomLogic(logicStep);
                default -> throw new IllegalArgumentException(String.valueOf(logicStep.action()));
            }
        }

        return this;
    }

    // TODO: Feature envy: consider moving this to new interface GameInfo (mutating api)
    private void actionStory (final GameLogicValue logicStep) throws IllegalArgumentException {
        if ((logicStep.story() == null) && (logicStep.prompt() == null)) {
            throw new NoSuchElementException(
                "at least one of \"prompt\" or \"story\" must appear in each gameLogicSeq entry");
        }

        this.maybeDoStory(logicStep);

        final var gameInfo = this.gameInfo();

        final String promptKey = logicStep.prompt();
        if (promptKey != null) {

            Objects.requireNonNull(gameInfo, "actionStory: info is null");
            Objects.requireNonNull(gameInfo, "actionStory: state is null");

            // refactor into LoDable method
            this.gameOutput().print(
                this.gameInfo().promptStrings().get(promptKey)
            );

            String nextLine;

            final var constraint = logicStep.constrain_prompt();

            if (Playable.ROOMS_FLAT_ACCESS.equals(constraint)) {

                final var rooms = gameInfo.roomsFlat();

                do {
                    nextLine = this.playerInput().nextLine();
                } while (!InputHandler.isInputOk(
                    nextLine,
                    rooms,
                    () -> this.gameOutput().printf(
                        "Oops! Answer must be one of: '%s' ",
                        String.join("', '", rooms)
                    )
                ));

            } else {
                nextLine = this.playerInput().nextLine();
            }

            this.gameState().pushPlayerInput(nextLine, promptKey);
        }
    }

    private void actionRoom (final GameLogicValue logicStep) throws NoSuchFieldException {
        this.maybeDoStory(logicStep);

        this.doRoomBehaviour(logicStep);
    }

    private void actionSetRoomLogic (final GameLogicValue logicStep)
        throws NoSuchFieldException {

        if (logicStep.room().isBlank()) {
            throw new IllegalArgumentException("cannot set state of room with a blank name");
        }

        this.gameState().setRoomBehaviour(
            // the room name to apply the logic to, either directly or indirectly with % prefix
            this.resolveRoomName(logicStep),
            // the new logic to apply TODO: complete function implementation
            this.doValueOfFunction(logicStep.value_of_function())
        );
    }

    private boolean maybeDoStory (final GameLogicValue logicStep) {
        final var doStory = logicStep.story() != null;
        if (doStory) {
            this.printStory(this.gameInfo().storyStrings().get(logicStep.story()));
        }
        return doStory;
    }

    private void doRoomBehaviour (final GameLogicValue logicStep) throws NoSuchFieldException {
        final var roomName = this.resolveRoomName(logicStep);

        final var roomBehaviour = this.gameState().roomBehaviour(roomName);

        final ReadonlyGameInfo newGameInfo = new GameInfoImpl(this.gameInfo())
            .setGameLogicSeq(roomBehaviour);

        final var newScope = new CaveDragonsGame(this)
            .setGameInfo(newGameInfo)
            .playGame();

        this.setGameState(new GameStateImpl(newScope.gameState()));
    }

    private String resolveRoomName (final GameLogicValue logicStep) throws NoSuchFieldException {

        if (logicStep.room().startsWith(CaveDragonsGame.PROMPT_ANSWER_REFERENCE)) {
            // the room is named by the response to the prompt named after the %
            final var roomNamedByPromptKey = logicStep.room().substring(1);
            final var promptRoomName = this.gameState().lastPlayerInput(roomNamedByPromptKey);

            if (promptRoomName.isBlank()) {
                throw new NoSuchFieldException(String.format(CaveDragonsGame.PROMPT_DOES_NOT_NAME_ROOM,
                    logicStep.room()));
            }

            return promptRoomName;
        }
        return logicStep.room();
    }

    private GameLogicValue doValueOfFunction (
        final List<GameLogicValue> valueOfFunction
    ) throws IllegalArgumentException {

        final GameLogicValue callee = valueOfFunction.get(0);
        final var functionSize = valueOfFunction.size();

        switch ( callee.name() ) {
            case "random_choice" -> {
                if (functionSize <= 2) {
                    throw new IllegalArgumentException(String.format(
                        "not enough choices for random_choice: %s",
                        functionSize
                    ));
                }

                return valueOfFunction.subList(1, functionSize).get(
                    this.rand().nextInt(functionSize - 1)
                );
            }
            default -> throw new IllegalArgumentException(
                String.format("'%s': unknown built-in function",
                    callee.name())
            );
        }
    }
}
