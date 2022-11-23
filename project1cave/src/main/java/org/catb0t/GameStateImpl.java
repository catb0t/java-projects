package org.catb0t;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<GameLogicValue> roomBehaviour (final String roomName) {
        return this.roomBehaviours().getOrDefault(roomName, this.roomListNotFound);
    }

    @Override
    public Map<String, List<String>> allNamedPlayerInputs () {
        return this.namedPlayerInputs;
    }

    @Override
    public String toString () {
        return "GameStateImpl{" +
               "roomBehaviours=" + this.roomBehaviours +
               ", roomListNotFound=" + this.roomListNotFound +
               ", lastPlayerInput='" + this.lastPlayerInput + '\'' +
               ", namedPlayerInputs=" + this.namedPlayerInputs +
               '}';
    }
}