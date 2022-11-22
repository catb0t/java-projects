package org.catb0t;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The action to be taken to process a GameLogicValue.
 */
enum GameLogicAction {
    STORY,
    SET_ROOM_LOGIC,
    ROOM,
    INNER_FUNCTION
}


interface ReadonlyGameInfo {
    Map<String, List<String>> storyStrings ();

    Map<String, List<String>> promptStrings ();

    List<String> roomsFlat ();

    List<GameLogicValue> gameLogicSeq ();
}


/**
 * A step in the execution of a Game. This object is not currently delegated, so it has keys common to all step objects,
 * including those which will be null depending on the action.
 */
class GameLogicValue {
    String                    name;
    GameLogicAction           action;
    String                    room;
    String                    story;
    String                    prompt;
    String                    constrain_prompt;
    List<GameLogicValue>      value_of_function;
    ArrayList<GameLogicValue> function;
}


class GameInfoImpl implements ReadonlyGameInfo {
    Map<String, List<String>> storyStrings;
    Map<String, List<String>> promptStrings;
    List<String>              roomsFlat;
    List<GameLogicValue>      gameLogicSeq;

    GameInfoImpl (
            final HashMap<String, ? extends ArrayList<String>> storyStringMap,
            final Map<String, ? extends ArrayList<String>> promptStringMap,
            final List<String> rooms,
            final List<? extends GameLogicValue> gameLogic
    ) {
        this.storyStrings  = new HashMap<>(storyStringMap);
        this.promptStrings = new HashMap<>(promptStringMap);
        this.roomsFlat     = new ArrayList<>(rooms);
        this.gameLogicSeq  = new ArrayList<>(gameLogic);
    }

    GameInfoImpl (final ReadonlyGameInfo other) {
        this.storyStrings  = new HashMap<>(other.storyStrings());
        this.promptStrings = new HashMap<>(other.promptStrings());
        this.roomsFlat     = new ArrayList<>(other.roomsFlat());
        this.gameLogicSeq  = new ArrayList<>(other.gameLogicSeq());
    }

    @Override
    public Map<String, List<String>> storyStrings () {
        return this.storyStrings;
    }

    @Override
    public Map<String, List<String>> promptStrings () {
        return this.promptStrings;
    }

    @Override
    public List<String> roomsFlat () {
        return this.roomsFlat;
    }

    @Override
    public List<GameLogicValue> gameLogicSeq () {
        return this.gameLogicSeq;
    }

    GameInfoImpl setGameLogicSeq (final List<GameLogicValue> logic) {
        this.gameLogicSeq = logic;
        return this;
    }
}


