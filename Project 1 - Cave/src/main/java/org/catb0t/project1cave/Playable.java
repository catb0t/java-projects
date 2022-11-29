package org.catb0t;

import org.jetbrains.annotations.*;

import java.util.*;

interface Playable {

    @NonNls String ROOMS_FLAT_ACCESS = "roomsFlat";

    static void initialiseRooms (final GameState state, final ReadonlyGameInfo info) {
        state.initialiseRooms(info.roomsFlat());
    }

    static List<GameLogicValue> logicSteps (final ReadonlyGameInfo info) {
        return info.gameLogicSeq();
    }

    Playable playGame () throws NoSuchFieldException;

    void printStory (Iterable<String> storyParts);

    GameState gameState ();
}
