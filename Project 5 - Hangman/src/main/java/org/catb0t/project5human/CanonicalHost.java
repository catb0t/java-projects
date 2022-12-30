package org.catb0t.project5human;

import java.util.*;
import java.util.stream.*;

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
    private final List<? extends Player>  guessers;
    private final Player                  executioner;
    HangedManState hangedMan = new HangedManState();
    private       Map<Byte, List<String>> dictionary;
    private Byte            chosenPhraseLength;
    private String          chosenPhrase;
    private List<Character> phraseGuessState;
    private boolean         isCheatingWithPhrases         = false;
    private boolean         isCheatingWithHangedManAmount = false;
    private int             currentPlayerIndex            = 0;

    public CanonicalHost () {
        this.executioner = new Bot();
        this.guessers    = List.of(new Bot(), new Bot());
        this.dictionary  = Map.ofEntries(
            Map.entry((byte) 2, List.of("aa", "bb")),
            Map.entry((byte) 3, List.of("abc")),
            Map.entry((byte) 4, List.of("data", "adad"))
        );

        this.setupPhraseFields();
    }

    private void setupPhraseFields () {
        final var phraseLengths = CanonicalHost.phraseLengthRange(this.dictionary.keySet());

        this.chosenPhraseLength = (byte) this.rand.nextInt(
            phraseLengths.get(0),
            phraseLengths.get(1) + 1
        );

        this.phraseGuessState = new ArrayList<>(this.chosenPhraseLength);
        for (byte n = 0; n < this.chosenPhraseLength; n++) {
            this.phraseGuessState.add('.');
        }
    }

    private static List<Byte> phraseLengthRange (final Collection<Byte> dictKeys) {
        return List.of(
            dictKeys.stream().min(Comparator.naturalOrder())
                    .orElseThrow(),
            dictKeys.stream().max(Comparator.naturalOrder())
                    .orElseThrow()
        );
    }

    public CanonicalHost (
        final Player executionerPlayer,
        final List<? extends Player> guesserPlayers,
        final Map<Byte, List<String>> phraseDictionary,
        final boolean canCheatAtDrawing,
        final boolean canCheatPhrases
    ) {
        Host.throwIfInvalidPlayerLayout(guesserPlayers, executionerPlayer);

        this.dictionary                    = Collections.unmodifiableMap(phraseDictionary);
        this.isCheatingWithPhrases         = canCheatPhrases;
        this.isCheatingWithHangedManAmount = canCheatAtDrawing;

        this.guessers    = Collections.unmodifiableList(guesserPlayers);
        this.executioner = executionerPlayer;

        this.setupPhraseFields();
    }

    public void _changeDictionary (Map<Byte, List<String>> replacement) {
        this.dictionary = Collections.unmodifiableMap(replacement);

        this.setupPhraseFields();
    }

    public List<? extends Player> guessers () {
        return this.guessers;
    }

    public int currentPlayerIndex () {
        return this.currentPlayerIndex;
    }

    public void _setGamePhrase (final String phrase) {
        final var length = Byte.valueOf((byte) phrase.length());

        this.throwIfInvalidLength(length);
        this.chosenPhraseLength = length;
        this.chosenPhrase       = phrase;
    }

    private void throwIfInvalidLength (final Byte phraseLength) {
        if (! this.dictionary.containsKey(phraseLength)) {
            throw new IllegalArgumentException(
                "attempt to pick non-existent phrase of length " + phraseLength +
                " from dictionary");
        }
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
        final var currentTurnPlayer = this.guessers.get(this.currentPlayerIndex);
        this.incrementAndWrapPlayerIndex();

        final var turnGuess = currentTurnPlayer.sendGuessMessage(
            this.chosenPhraseLength,
            this.phraseGuessState,
            this.hangedManAmount()
        );

        // TODO: handle cheating host
        if (! this.isCheatingWithPhrases) {
            if (turnGuess.isFullGuess() && this.chosenPhrase.equals(turnGuess.fullGuess())) {
                System.out.println("PLAYER WINS!! " + currentTurnPlayer.name());
            } else if (this.isCorrectCharacterGuess(turnGuess)) {
                System.out.println("guessed right! :) " + currentTurnPlayer.name());

                final var positions = this.characterGuessLocations(turnGuess);
                for (final var pos :
                    positions) {
                    this.phraseGuessState.set(pos, turnGuess.characterGuess());
                }

            } else {
                System.out.println("wrong guess :( " + currentTurnPlayer.name());
                // TODO wrong guess
            }
        }
    }

    public void incrementAndWrapPlayerIndex () {
        if (this.currentPlayerIndex >= this.guessers.size()) {
            this.currentPlayerIndex = 0;
        } else {
            this.currentPlayerIndex++;
        }
    }

    /**
     * Reveal all the places a character guess is found within the chosen phrase. Cheating is not
     * currently implemented for this method.
     *
     * @param guess the player's guess information
     *
     * @return all the correct guess locations in the phrase
     */
    public List<Integer> characterGuessLocations (GuessValue guess) {
        if (this.isCheatingWithPhrases) {
            throw new IllegalStateException("unimplemented");
        } else {
            return
                IntStream.range(0, this.chosenPhrase.length())
                         .filter(i -> this.chosenPhrase.charAt(i)
                                      == guess.characterGuess()
                         )
                         .boxed()
                         .collect(Collectors.toCollection(
                             () -> new ArrayList<>(this.chosenPhraseLength))
                         );
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
