package org.catb0t;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

enum GameLogicAction {
    STORY,
    SET_ROOM_LOGIC,
    ROOM
}
class GameLogicValue {
    protected String name;
    protected GameLogicAction action;
    protected String room;
    protected String story;
    protected String prompt;
    protected ArrayList<GameLogicValue> value_of_function;
    protected ArrayList<GameLogicValue> function;
}

public class GameInfo {
    protected HashMap<String, ArrayList<String>> storyStrings;
    protected HashMap<String, ArrayList<String>> promptStrings;
    protected ArrayList<GameLogicValue> gameLogicSeq;

    public GameInfo(HashMap<String, ArrayList<String>> storyStrings, HashMap<String, ArrayList<String>> promptStrings, ArrayList<GameLogicValue> gameLogicSeq) {
        this.storyStrings = storyStrings;
        this.promptStrings = promptStrings;
        this.gameLogicSeq = gameLogicSeq;
    }
}

class GameState {
    public GameState() {
        this.lastPlayerInput = "";
        this.namedPlayerInputs = new HashMap<>();
        this.roomBehaviours = new HashMap<>();
    }

    public String lastPlayerInput() {
        return lastPlayerInput;
    }

    public @NotNull GameState setLastPlayerInput(String lastPlayerInput) {
        this.lastPlayerInput = lastPlayerInput;
        return this;
    }

    protected String lastPlayerInput;
    protected HashMap<String, ArrayList<String>> namedPlayerInputs;

    /**
     * Map room names to their current state. GameLogicV
     */
    protected HashMap<String, ArrayList<GameLogicValue>> roomBehaviours;

    protected void updateRoomBehaviour (String roomName, ArrayList<GameLogicValue> newLogic) {
        this.roomBehaviours.put(roomName, newLogic);
    }

    public void addNamedPlayerInput (String inputNameKey, String inputContents) {
        ArrayList<String> gotten = this.namedPlayerInputs
                .getOrDefault(inputNameKey, new ArrayList<>());
        if (gotten == null) {
            throw new IllegalArgumentException(inputNameKey);
        }
        gotten.add(inputContents);
    }

    public String getLastPlayerInputNamed (String inputNameKey) throws IndexOutOfBoundsException {
        ArrayList<String> allInputs = namedPlayerInputs.getOrDefault(inputNameKey, new ArrayList<>());

        return allInputs.get(allInputs.size() - 1);
    }
}
