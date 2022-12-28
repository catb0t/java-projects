package org.catb0t.project5human;

import java.io.*;
import java.util.*;

/**
 * A player controlled interactively by a person using a keyboard. Human players are able to be
 * the "hangman", or be players making guesses. This player can cheat as hangman, if the current
 * {@link project5human.Host} supports cheating hangmen. Similar to the real game, cheating is
 * done by deciding the word at the last possible moment.
 */
public class Human implements Player {

    private final String name;
    private final InputStream in;
    private final PrintStream out;

    Human (final InputStream inReader, final PrintStream outWriter) {
        this.name = "UNSET NAME";
        this.in   = inReader;
        this.out  = outWriter;
    }

    @Override
    public GuessValue sendGuessMessage (
        final byte phraseLength,
        final List<Character> phraseState,
        final char hangedManAmount
    ) {
        return null;
    }

    @Override
    public String name () { return this.name; }

}
