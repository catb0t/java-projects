package project5human;

import java.util.*;

/**
 * The CanonicalHost is the built-in default implementation of a Host for Hangman.
 * <p>
 * CanonicalHost can support an arbitrary number of players versus one hangman (the player who
 * knows the word), trying to give each other player a turn to guess a letter.
 * <p>
 * CanonicalHost supports hangmen cheating at the game.
 */
public class CanonicalHost implements project5human.Host {
    private final Random                     rand = new Random();
    private final Map<Byte, List<String>>    dictionary;
    private final List<project5human.Player> guessers;
    private final project5human.Player       executioner;
    project5human.HangedManState hangedMan = new project5human.HangedManState();
    private Byte    chosenPhraseLength;
    private String  chosenPhrase;
    private boolean isCheatingWithPhrases         = false;
    private boolean isCheatingWithHangedManAmount = false;

    CanonicalHost (
        final project5human.Player executionerPlayer,
        final List<project5human.Player> guesserPlayers,
        final Map<Byte, List<String>> phraseDictionary,
        final boolean canCheatAtDrawing,
        final boolean canCheatPhrases
    ) {
        this.dictionary                    = Collections.unmodifiableMap(phraseDictionary);
        this.isCheatingWithPhrases         = canCheatPhrases;
        this.isCheatingWithHangedManAmount = canCheatAtDrawing;
        if ((guesserPlayers == null) || guesserPlayers.isEmpty() || (executionerPlayer == null) ||
            guesserPlayers.contains(executionerPlayer)) {
            throw new IllegalArgumentException("assertion 'guesserPlayers is not null or empty " +
                                               "and executionerPlayer is not null and " +
                                               "guesserPlayers does not contain " +
                                               "executionerPlayer' failed");
        }
        this.guessers    = guesserPlayers;
        this.executioner = executionerPlayer;
    }

    @Override
    public byte hangedManAmount () {
        return this.hangedMan.hangedManState;
    }

    @Override
    public void stepIncreaseHangedManAmount () {
        // TODO: cheat at drawing the hangman
        // final boolean isCheating = this.isCheatingWithHangedManAmount();
        // if (isCheating) { }
        this.hangedMan.increaseStateByDefault();
    }

    @Override
    public void stepDecreaseHangedManAmount () {
        throw new IllegalArgumentException("unimplemented");
    }

    @Override
    public List<String> phrasebook () {
        return Collections.unmodifiableList(this.dictionary.get(this.chosenPhraseLength));
    }

    @Override
    public boolean isCheatingWithPhrases () {
        return this.isCheatingWithPhrases;
    }

    @Override
    public void doGameIteration () {
        // ...??
        final var currentTurnPlayer = this.guessers.get(0);
        final var turnGuess = currentTurnPlayer.sendGuessMessage(
            this.chosenPhraseLength.byteValue(),
            this.hangedManAmount()
        );

        // TODO: handle cheating host
        if (! this.isCheatingWithPhrases()) {
            if (turnGuess.isFullGuess() && this.chosenPhrase.equals(turnGuess.fullGuess)) {
                System.out.println("PLAYER WINS!! " + currentTurnPlayer.name());
            } else if (this.isCorrectCharacterGuess(turnGuess)) {
                System.out.println("guessed right! :) " + currentTurnPlayer.name());
            } else {
                System.out.println("wrong guess :( " + currentTurnPlayer.name());
                // TODO wrong guess
            }
        }
    }

    @Override
    public boolean isCorrectCharacterGuess (GuessValue guess) {
        return this.chosenPhrase.contains(guess.guessedCharacter);
    }

    @Override
    public String chooseGamePhrase (final Byte phraseLength) {
        // exception is thrown here because throwing it later would put this
        // object into an invalid state, where this.chosenPhraseLength is set
        // but no phrase was actually chosen
        this.throwIfInvalidLength(phraseLength);
        this.chosenPhraseLength = phraseLength;
        this.chosenPhrase       = this.chooseRandomPhraseOfLength();
        return this.chosenPhrase;
    }

    private void throwIfInvalidLength (final Byte phraseLength) {
        if (! this.dictionary.containsKey(phraseLength)) {
            throw new IllegalArgumentException(
                "attempt to pick non-existent phrase of length " + phraseLength +
                " from dictionary");
        }
    }

    private String chooseRandomPhraseOfLength () {
        this.throwIfInvalidLength(this.chosenPhraseLength);
        final var choices = this.dictionary.get(this.chosenPhraseLength);
        return choices.get(this.rand.nextInt(choices.size()));
    }

    @Override
    public String instantaneousCheatedPhrase () {
        if (this.chosenPhraseLength.intValue() == 0) {
            throw new IllegalArgumentException(
                "can't get instantaneous cheated phrase when the current phrase was not initialised"
            );
        }
        return this.chooseRandomPhraseOfLength();
    }
}
