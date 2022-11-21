package org.catb0t;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Game {

    protected Scanner playerInput;
    protected PrintStream gameOutput;
    protected GameInfo gameInfo;
    protected GameState gameState;
    public Game() { this.gameState = new GameState(); }
    public Game(Scanner playerInput, GameInfo gameInfo) {
        this.playerInput = playerInput;
        this.gameInfo = gameInfo;
        this.gameState = new GameState();
    }

    public PrintStream gameOutput() {
        return gameOutput;
    }

    public @NotNull Game setGameOutput(PrintStream gameOutput) {
        this.gameOutput = gameOutput;
        return this;
    }

    public Scanner playerInput() {
        return playerInput;
    }

    public @NotNull Game setPlayerInput(Scanner playerInput) {
        this.playerInput = playerInput;
        return this;
    }

    public GameInfo gameInfo() {
        return gameInfo;
    }

    public @NotNull Game setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
        return this;
    }

    public abstract Game playGame() throws NoSuchFieldException;

    protected void printStory(@NotNull ArrayList<String> story) {
        System.out.println(
                String.join("\n", story)
        );
    }
}
