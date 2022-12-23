package org.catb0t.project5human;

import java.util.*;

/**
 * The CanonicalHost is the built-in default implementation of a Host for Hangman.
 * <p>
 * CanonicalHost can support an arbitrary number of players versus one hangman (the player who
 * knows the word), trying to give each other player a turn to guess a letter.
 * <p>
 * CanonicalHost supports hangmen cheating at the game.
 */
public class CanonicalHost implements Host {
    private final Random                  rand = new Random();
    private final Map<Byte, List<String>> dictionary;
    private final List<? extends Player>  guessers;
    private final Player                  executioner;
    HangedManState hangedMan = new HangedManState();
    private Byte    chosenPhraseLength;
    private String  chosenPhrase;
    private boolean isCheatingWithPhrases         = false;
    private boolean isCheatingWithHangedManAmount = false;

    CanonicalHost (
        final Player executionerPlayer,
        final List<? extends Player> guesserPlayers,
        final Map<Byte, List<String>> phraseDictionary,
        final boolean canCheatAtDrawing,
        final boolean canCheatPhrases
    ) {
        this.dictionary                    = Collections.unmodifiableMap(phraseDictionary);
        this.isCheatingWithPhrases         = canCheatPhrases;
        this.isCheatingWithHangedManAmount = canCheatAtDrawing;

        Host.throwIfInvalidPlayerLayout(guesserPlayers, executionerPlayer);

        this.guessers    = Collections.unmodifiableList(guesserPlayers);
        this.executioner = executionerPlayer;
    }

    @Override
    public char hangedManAmount () {
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
    public void changeCheatingWithPhrases (final boolean val) {
        this.isCheatingWithPhrases = val;
    }

    @Override
    public boolean isCheatingWithHangedManAmount () {
        return this.isCheatingWithHangedManAmount;
    }

    @Override
    public void changeCheatingWithHangedManAmount (final boolean val) {
        this.isCheatingWithHangedManAmount = val;
    }

    @Override
    public void doGameIteration () {
        // ...??
        final var currentTurnPlayer = this.guessers.get(0);
        final var turnGuess = currentTurnPlayer.sendGuessMessage(
            this.chosenPhraseLength,
            this.hangedManAmount()
        );

        // TODO: handle cheating host
        if (! this.isCheatingWithPhrases) {
            if (turnGuess.isFullGuess() && this.chosenPhrase.equals(turnGuess.fullGuess())) {
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
        return this.chosenPhrase.indexOf(guess.characterGuess()) != - 1;
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
