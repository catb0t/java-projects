package org.catb0t;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Implement GameInfo logic and formatting unique to the Cave Dragon game.
 */
public class CaveDragonGame extends Game {

    /**
     * If the room key starts with %, it means the name of the room is the most recent answer to the prompt with that name.
     */
    private final String PROMPT_ANSWER_REFERENCE = "%";
    private final String PROMPT_DOES_NOT_NAME_ROOM = "\"%s\": cannot find a room named by a blank prompt value (did the player ever see this prompt?)";

    static Random rand;

    /**
     * For each entry in the gameLogicSeq field, dispatch the appropriate engine function, such as displaying story, a prompt, setting game state, or reading game state set previously.
     * @return the final Game state.
     */
    public @NotNull CaveDragonGame playGame() throws NoSuchFieldException {

        System.out.println("<playGame>");

        for (@NotNull GameLogicValue logicStep:
             this.gameInfo.gameLogicSeq) {

            if (logicStep.room == null) {
                throw new NoSuchElementException("\"room\" key must exist for all gameLogicSeq entries");
            }

            switch (logicStep.action) {
                case STORY:
                    this.doStory(logicStep);
                    break;

                case ROOM:
                    this.doRoom(logicStep);
                    break;
                    
                case SET_ROOM_LOGIC:
                    this.setRoomLogic(logicStep);
                    break;
            }
        }

        return this;
    }

    private void setRoomLogic(@NotNull GameLogicValue logicStep) throws NoSuchFieldException {
        if ( logicStep.room.isBlank() ) {
            throw new IllegalArgumentException("cannot set state of room with a blank name");
        }

        Gson gson = new Gson();
        System.out.println(gson.toJson(this.gameState.roomBehaviours));

        String roomName;
        if ( logicStep.room.startsWith(PROMPT_ANSWER_REFERENCE) ) {
            // the room is named by the response to the prompt named after the %
            @NotNull String roomNamedByPromptKey = logicStep.room.substring(1);
            String promptRoomName;
            try {
                promptRoomName = this.gameState.getLastPlayerInputNamed(roomNamedByPromptKey);
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchFieldException(String.format(PROMPT_DOES_NOT_NAME_ROOM, logicStep.room));
            }
            if (promptRoomName.isBlank()) {
                throw new NoSuchFieldException(String.format(PROMPT_DOES_NOT_NAME_ROOM, logicStep.room));
            }

            roomName = promptRoomName;
        } else {
            roomName = logicStep.room;
        }
        this.gameState.updateRoomBehaviour(roomName, new ArrayList<>(logicStep.value_of_function));
    }

    private GameLogicValue doValueOfFunction(@NotNull ArrayList<GameLogicValue> valueOfFunction) {
        GameLogicValue callee = valueOfFunction.get(0);

        switch (callee.name) {
            case "random_choice":
                return valueOfFunction.get(
                        rand.nextInt( valueOfFunction.size() )
                );
            default:
                throw new IllegalArgumentException(String.format("'%s': unknown built-in function", callee.name));
        }
    }

    private void doRoom(@NotNull GameLogicValue logicStep) {

    }

    private void doStory(@NotNull GameLogicValue logicStep) {
        if (logicStep.story == null && logicStep.prompt == null) {
            throw new NoSuchElementException("at least one of \"prompt\" or \"story\" must appear in each gameLogicSeq entry");
        }

        if (logicStep.story != null) {
            this.printStory(
                    gameInfo.storyStrings.get(logicStep.story)
            );
        }

        if (logicStep.prompt != null) {
            @NotNull String promptKey = logicStep.prompt;

            if (gameInfo == null) {
                throw new IllegalArgumentException("info");
            } else if (gameState == null) {
                throw new IllegalArgumentException("state");
            }

            this.gameOutput.print(
                    gameInfo.promptStrings.get(promptKey)
            );

            String nextLine = this.playerInput.nextLine();
            if (nextLine == null) {
                throw new IllegalArgumentException("line");
            }
            this.gameState.addNamedPlayerInput(
                    promptKey, nextLine
            );
        }
    }
}
