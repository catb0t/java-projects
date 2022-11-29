package org.catb0t;

import java.util.*;

interface GameState {
    String lastPlayerInput ();

    String lastPlayerInput (String prompt);

    /**
     * Each response the player gave to the named prompt. The most recent responses appear at the
     * end.
     * If the player has never seen this prompt, the result will be empty.
     *
     * @param prompt the name of the prompt the inputs were responding to.
     *
     * @return a list of responses, or an empty list
     */
    List<String> playerInputs (String prompt);

    /**
     * Find only the most recent {@code lastN} responses the player gave to the named prompt.
     * If {@code lastN} is larger than the number of responses, the result may have fewer than
     * {@code lastN} indices.
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
