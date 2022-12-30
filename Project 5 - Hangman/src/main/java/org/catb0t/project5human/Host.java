package org.catb0t.project5human;

import java.util.*;

/**
 * Host is the state machine that runs the Hangman game. Players and the "hangman" player
 * connect, and the game proceeds as usual.
 * <p>
 * Host has ultimate control over all game state and the final decision on all player input.
 */
public interface Host {
    /**
     * Constructor validation utility method. Ensure that there are players guessing, and there is
     * an executioner, and the executioner must NOT be one of the guessing player.
     *
     * @param executionerPlayer an object to check for validity
     * @param guesserPlayers    an object to check for validity
     */
    static void throwIfInvalidPlayerLayout (
        final List<? extends Player> guesserPlayers,
        final Player executionerPlayer
    ) {
        assert (
            (guesserPlayers != null) || ! guesserPlayers.isEmpty() ||
            (executionerPlayer != null) ||
            ! guesserPlayers.contains(executionerPlayer)) :
            (
                "(bug) assertion 'guesserPlayers is not null or empty " +
                "and executionerPlayer is not null and " +
                "guesserPlayers does not contain " +
                "executionerPlayer' failed"
            );

    }

    /**
     * Constructor validation utility method. Ensure that the dictionary is valid. All of the
     * strings in valid dictionaries are the length of their key.
     *
     * @param dictionary a length-keyed dictionary
     */
    static void throwIfInvalidDictionary (Map<Byte, List<String>> dictionary) {
        assert (
            dictionary.entrySet()
                      .stream()
                      .anyMatch(
                          e -> e.getValue()
                                .stream()
                                .allMatch(s -> s.length() == e.getKey())
                      )
        ) :
            "(bug) invalid dictionary for Host object construction (length keys do not match all " +
            "string values): " +
            dictionary.toString();
    }

    char hangedManAmount ();

    void stepIncreaseHangedManAmount ();

    void stepDecreaseHangedManAmount ();

    List<String> phrasebook ();

    boolean isCheatingWithPhrases ();

    void changeCheatingWithPhrases (boolean val);

    boolean isCheatingWithHangedManAmount ();

    void changeCheatingWithHangedManAmount (boolean val);

    void doGameIteration ();

    String chooseGamePhrase (final Byte phraseLength);

    String instantaneousCheatedPhrase ();

    boolean isCorrectCharacterGuess (final GuessValue guess);
}
