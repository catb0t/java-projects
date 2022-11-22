package org.catb0t;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.catb0t.GameLogicAction.INNER_FUNCTION;


/**
 * Implement GameInfo logic and formatting unique to the Cave Dragon game.
 */
public class CaveDragonGame extends Game {

    /**
     * If the room key starts with %, it means the name of the room is the most recent answer to
     * the prompt with that name.
     */
    private static final String PROMPT_ANSWER_REFERENCE   = "%";
    private static final String PROMPT_DOES_NOT_NAME_ROOM = (
            "\"%s\": cannot find a room named by a blank prompt value (did the player ever see this prompt?)"
    );

    CaveDragonGame (final Game other) {
        super(other);
    }

    CaveDragonGame () { }

    /**
     * For each entry in the gameLogicSeq field, dispatch the appropriate engine function, such
     * as displaying story, a prompt, setting game state, or reading game state set previously.
     *
     * @return the final Game state.
     */
    @Override
    public CaveDragonGame playGame () throws NoSuchFieldException {

        // System.out.println("<playGame>");

        Playable.initialiseRooms(this.gameState(), this.gameInfo());

        for (final GameLogicValue logicStep :
                Playable.logicSteps(this.gameInfo())) {

            if (!INNER_FUNCTION.equals(logicStep.action)) {
                Objects.requireNonNull(
                        logicStep.room,
                        "\"room\" key must exist for all gameLogicSeq entries"
                );
            }

            // TODO improve inner_function functionality
            switch ( logicStep.action ) {
                case INNER_FUNCTION:
                    // NOTE: fall through
                case STORY: {
                    this.actionStory(logicStep);
                    break;
                }
                case ROOM: {
                    this.actionRoom(logicStep);
                    break;
                }
                case SET_ROOM_LOGIC: {
                    this.actionSetRoomLogic(logicStep);
                    break;
                }
                default: {
                    throw new IllegalArgumentException(String.valueOf(logicStep.action));
                }
            }
        }

        return this;
    }

    // TODO: Feature envy: consider moving to new interface GameInfo (mutating api)
    private void actionStory (final GameLogicValue logicStep) throws IllegalArgumentException {

        if ((logicStep.story == null) && (logicStep.prompt == null)) {
            throw new NoSuchElementException(
                    "at least one of \"prompt\" or \"story\" must appear in each gameLogicSeq entry");
        }

        this.maybeDoStory(logicStep);

        if (logicStep.prompt != null) {
            final String promptKey = logicStep.prompt;

            Objects.requireNonNull(this.gameInfo(), "actionStory: info is null");
            Objects.requireNonNull(this.gameState(), "actionStory: state is null");

            // refactor into LoDable method
            this.gameOutput().print(
                    this.gameInfo().promptStrings().get(promptKey)
            );

            String nextLine;

            final var constraint = logicStep.constrain_prompt;

            if (Playable.ROOMS_FLAT_ACCESS.equals(constraint)) {

                final var rooms = this.gameInfo().roomsFlat();

                do {
                    nextLine = this.playerInput().nextLine();
                } while (!InputMan.isInputOk(
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

        if (logicStep.room.isBlank()) {
            throw new IllegalArgumentException("cannot set state of room with a blank name");
        }

        this.gameState().setRoomBehaviour(
                // the room name to apply the logic to, either directly or indirectly with % prefix
                this.resolveRoomName(logicStep),
                // the new logic to apply TODO: complete function implementation
                this.doValueOfFunction(logicStep.value_of_function)
        );
    }

    private boolean maybeDoStory (final GameLogicValue logicStep) {
        var doStory = logicStep.story != null;
        if (doStory) {
            this.printStory(this.gameInfo().storyStrings().get(logicStep.story));
        }
        return doStory;
    }

    private void doRoomBehaviour (final GameLogicValue logicStep) throws NoSuchFieldException {
        final var wantBehaviour = this.resolveRoomName(logicStep);

        final var roomBehaviour = this.gameState()
                                      .roomBehaviour(wantBehaviour);

        final var newGameInfo = new GameInfoImpl(this.gameInfo())
                                        .setGameLogicSeq(roomBehaviour);

        final var newScope = new CaveDragonGame(this)
                                     .setGameInfo(newGameInfo)
                                     .playGame();

        this.setGameState(new GameStateImpl(newScope.gameState()));
    }

    private String resolveRoomName (final GameLogicValue logicStep) throws NoSuchFieldException {

        if (logicStep.room.startsWith(CaveDragonGame.PROMPT_ANSWER_REFERENCE)) {
            // the room is named by the response to the prompt named after the %
            final var roomNamedByPromptKey = logicStep.room.substring(1);
            final var promptRoomName = this.gameState().lastPlayerInput(roomNamedByPromptKey);

            if (promptRoomName.isBlank()) {
                throw new NoSuchFieldException(String.format(CaveDragonGame.PROMPT_DOES_NOT_NAME_ROOM, logicStep.room));
            }

            return promptRoomName;
        }
        return logicStep.room;
    }

    private GameLogicValue doValueOfFunction (
            final List<? extends GameLogicValue> valueOfFunction
    ) throws IllegalArgumentException {

        final GameLogicValue callee = valueOfFunction.get(0);
        final var functionSize = valueOfFunction.size();

        switch ( callee.name ) {
            case "random_choice": {
                if (functionSize <= 2) {
                    throw new IllegalArgumentException(String.format(
                            "not enough choices for random_choice: %s",
                            functionSize
                    ));
                }

                return valueOfFunction.subList(1, functionSize).get(
                        this.rand.nextInt(functionSize - 1)
                );
            }
            default: {
                throw new IllegalArgumentException(
                        String.format("'%s': unknown built-in function",
                                callee.name)
                );
            }
        }
    }
}
