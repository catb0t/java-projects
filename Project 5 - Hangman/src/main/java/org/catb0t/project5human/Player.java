package org.catb0t.project5human;

import java.util.*;

/**
 * A Player participates in the Hangman game, either as the "hangman" or as a player guessing
 * letters. Players receive game information, and send a reply based on their knowledge of the
 * game state.
 */
public interface Player {

    GuessValue sendGuessMessage (
        byte phraseLength,
        List<Character> phraseState,
        char hangedManAmount
    );

    String name ();
}
