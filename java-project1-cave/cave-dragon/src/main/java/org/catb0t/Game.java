package org.catb0t;

import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

interface Playable {

    static String ROOMS_FLAT_ACCESS = "roomsFlat";

    static void initialiseRooms (final GameState state, final ReadonlyGameInfo info) {
        state.initialiseRooms(info.roomsFlat());
    }

    static Iterable<GameLogicValue> logicSteps (final ReadonlyGameInfo info) {
        return info.gameLogicSeq();
    }

    Playable playGame () throws NoSuchFieldException;

    void printStory (Iterable<String> storyParts);

    GameState gameState ();
}


public abstract class Game implements Playable {

    GameState gameState;
    Random    rand;
    private Scanner          playerInput;
    private PrintStream      gameOutput;
    private ReadonlyGameInfo gameInfo;

    Game () {
        this.gameState = new GameStateImpl();
        this.rand      = new Random();
    }

    protected Game (final Scanner scanner, final ReadonlyGameInfo info) {
        this.setPlayerInput(scanner);
        this.setGameInfo(info);
        this.gameState = new GameStateImpl();
        this.rand      = new Random();
    }

    public final Game setPlayerInput (final Scanner scanner) {
        this.playerInput = scanner;
        return this;
    }

    public final Game setGameInfo (final ReadonlyGameInfo info) {
        this.gameInfo = info;
        return this;
    }

    Game (final Game other) {
        this.playerInput = other.playerInput();
        this.gameOutput  = other.gameOutput();
        this.gameInfo    = new GameInfoImpl(other.gameInfo());
        this.gameState   = new GameStateImpl(other.gameState());
        this.rand        = other.rand;
    }

    Scanner playerInput () {
        return this.playerInput;
    }

    PrintStream gameOutput () {
        return this.gameOutput;
    }

    ReadonlyGameInfo gameInfo () {
        return this.gameInfo;
    }

    Game setGameOutput (final PrintStream stream) {
        this.gameOutput = stream;
        return this;
    }

    @Override
    public String toString () {
        return "Game{" +
               "playerInput=" + this.playerInput() +
               ", gameOutput=" + this.gameOutput() +
               ", gameInfo=" + this.gameInfo() +
               ", gameState=" + this.gameState() +
               '}';
    }

    @Override
    public void printStory (Iterable<String> storyParts) {
        this.gameOutput().println(String.join("\n", storyParts));
    }

    @Override
    public GameState gameState () {
        return this.gameState;
    }

    void setGameState (final GameState state) {
        this.gameState = state;
    }
}
