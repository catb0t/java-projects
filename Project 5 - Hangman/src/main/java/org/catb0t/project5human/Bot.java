package org.catb0t.project5human;

import java.util.*;

/**
 * A player controlled automatically by the computer, but without access to all the internal game
 * state that a {@link project5human.Host} is aware of. Bot players are able to be the "hangman",
 * or be players making guesses. This player can cheat as hangman, if the current
 * {@link project5human.Host} supports cheating hangmen.
 */
public class Bot implements Player {
    /**
     * @param phraseLength    the number of characters in the phrase
     * @param hangedManAmount how dead the hangman already is
     *
     * @return the player's guess, either a full guess
     */
    @Override
    public GuessValue sendGuessMessage (
        final byte phraseLength,
        final List<Character> phraseState,
        final char hangedManAmount
    ) {
        return null;
    }

    /**
     * @return the name of the player
     */
    @Override
    public String name () {
        return "a robot";
    }
}
