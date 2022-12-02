package org.catb0t;

import java.util.*;


interface ReadonlyGameInfo {
    Map<String, List<String>> storyStrings ();

    Map<String, List<String>> promptStrings ();

    List<String> roomsFlat ();

    List<GameLogicValue> gameLogicSeq ();
}


