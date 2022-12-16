package project5human;
/**
 * A Player participates in the Hangman game, either as the "hangman" or as a player guessing
 * letters. Players receive game information, and send a reply based on their knowledge of the
 * game state.
 */
public interface Player {
    project5human.GuessValue sendGuessMessage (final char phraseLength, final char hangedManAmount);

    String name ();
}
