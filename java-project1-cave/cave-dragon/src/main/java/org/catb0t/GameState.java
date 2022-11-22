package org.catb0t;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface GameState {
    String lastPlayerInput ();

    String lastPlayerInput (String prompt);

    /**
     * Each response the player gave to the named prompt. The most recent responses appear at the end.
     * If the player has never seen this prompt, the result will be empty.
     *
     * @param prompt the name of the prompt the inputs were responding to.
     *
     * @return a list of responses, or an empty list
     */
    List<String> playerInputs (String prompt);

    /**
     * Find only the most recent {@code lastN} responses the player gave to the named prompt.
     * If {@code lastN} is larger than the number of responses, the result may have fewer than {@code lastN} indices.
     *
     * @param prompt the name of the prompt the inputs were responding to
     * @param lastN  the number of recent responses to return.
     *
     * @return a list of responses, or an empty list
     */
    List<String> playerInputs (String prompt, int lastN);

    GameState pushPlayerInput (String input, String prompt);

    GameState setRoomBehaviour (String roomName, List<GameLogicValue> newLogic);

    GameState setRoomBehaviour (String roomName, GameLogicValue newLogic);

    String toString ();

    void initialiseRooms (List<String> roomNames);

    Map<String, List<GameLogicValue>> roomBehaviours ();

    List<GameLogicValue> roomBehaviour (String roomName);

    Map<String, List<String>> allNamedPlayerInputs ();
}


class GameStateImpl implements GameState {
    /**
     * Map room names to their current state.
     */
    private final Map<String, List<GameLogicValue>> roomBehaviours;
    private final List<GameLogicValue>              roomListNotFound = new ArrayList<>(0);
    /**
     * A list of each response given by the player for each named prompt.
     */
    private final Map<String, List<String>>         namedPlayerInputs;
    /**
     * The most recent prompt response by the player.
     */
    private       String                            lastPlayerInput;

    GameStateImpl () {
        this.lastPlayerInput   = "";
        this.namedPlayerInputs = new HashMap<>(0);
        this.roomBehaviours    = new HashMap<>(0);
    }

    GameStateImpl (final GameState other) {
        this.roomBehaviours    = new HashMap<>(other.roomBehaviours());
        this.namedPlayerInputs = new HashMap<>(other.allNamedPlayerInputs());
        this.lastPlayerInput   = other.lastPlayerInput();
    }

    @Override
    public String lastPlayerInput () {
        return this.lastPlayerInput;
    }

    @Override
    public String lastPlayerInput (final String prompt) {
        final var last = this.playerInputs(prompt, 1);
        return last.isEmpty() ? "" : last.get(0);
    }

    @Override
    public List<String> playerInputs (final String prompt) {
        return this.namedPlayerInputs
                       .getOrDefault(prompt, new ArrayList<>(0));
    }

    @Override
    public List<String> playerInputs (final String prompt, int lastN) {
        final var inputs = this.playerInputs(prompt);
        final var size = inputs.size();
        if (inputs.isEmpty() || (lastN >= size)) {
            return inputs;
        }
        final var deltaEnd = size - lastN;
        return inputs.subList(deltaEnd, size);
    }

    @Override
    public GameState pushPlayerInput (final String input, final String prompt) {
        final var inputs = this.namedPlayerInputs;
        if (inputs.containsKey(prompt)) {
            inputs.get(prompt).add(input);
        } else {
            inputs.put(prompt, new ArrayList<>(List.of(input)));
        }
        this.lastPlayerInput = input;
        return this;
    }

    @Override
    public GameState setRoomBehaviour (final String roomName, final List<GameLogicValue> newLogic) {
        this.roomBehaviours.put(roomName, newLogic);
        return this;
    }

    @Override
    public GameState setRoomBehaviour (final String roomName, final GameLogicValue newLogic) {
        this.setRoomBehaviour(roomName, List.of(newLogic));
        return this;
    }

    @Override
    public void initialiseRooms (final List<String> roomNames) {
        for (final var name :
                roomNames) {
            this.roomBehaviours.put(name, new ArrayList<>(0));
        }
    }

    @Override
    public Map<String, List<GameLogicValue>> roomBehaviours () {
        return this.roomBehaviours;
    }

    @Override
    public List<GameLogicValue> roomBehaviour (String roomName) {
        return this.roomBehaviours().getOrDefault(roomName, this.roomListNotFound);
    }

    @Override
    public Map<String, List<String>> allNamedPlayerInputs () {
        return this.namedPlayerInputs;
    }

    @Override
    public String toString () {
        return "GameStateImpl{" +
               "roomBehaviours=" + roomBehaviours +
               ", roomListNotFound=" + roomListNotFound +
               ", lastPlayerInput='" + lastPlayerInput + '\'' +
               ", namedPlayerInputs=" + namedPlayerInputs +
               '}';
    }
}
