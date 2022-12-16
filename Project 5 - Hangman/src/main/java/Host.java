package project5human;

import java.util.*;

/**
 * Host is the state machine that runs the Hangman game. Players and the "hangman" player
 * connect, and the game proceeds as usual.
 *
 * Host has ultimate control over all game state and the final decision on all player input.
 */
public interface Host {
    byte hangedManAmount ();
    void stepIncreaseHangedManAmount ();
    void stepDecreaseHangedManAmount ();
    List<String> phrasebook ();
    void addPhraseToBook (String phrase);
    void removePhraseFromBook (String phrase);
    boolean isCheatingWithPhrases ();
    void changeCheatingWithPhrases (boolean val);
    boolean isCheatingWithHangedManAmount ();
    void changeCheatingWithHangedManAmount (boolean val);

    void doGameIteration ();

    String chooseGamePhrase (final Byte phraseLength);
    String instantaneousCheatedPhrase ();

    boolean isCorrectCharacterGuess (final GuessValue guess);
}
