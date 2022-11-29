package org.catb0t;

import java.util.*;


/**
 * A step in the execution of a Game. This object is not currently delegated, so it has keys
 * common to all step objects,
 * including those which will be null depending on the action.
 */
record GameLogicValue(
    String name,
    GameLogicAction action,
    String room,
    String story,
    String prompt,
    String constrain_prompt,
    List<GameLogicValue> value_of_function,
    ArrayList<GameLogicValue> function ) { }
