package org.catb0t;

import com.google.gson.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;


public class Main {

    private static final String gameDataFilename = "/game_data.json";

    /**
     * Run the game based on data from game_data.json.
     *
     * @param args args[1] may optionally specify a different game data file path than the default.
     *
     * @throws IOException          indicates a failure to read the game data file.
     * @throws NoSuchFieldException indicates a missing or misdirected field in the game data.
     */
    public static void main (final String[] args) throws IOException, NoSuchFieldException {

        // TODO: game data path from argv[1]
        // TODO: game data handler interface
        final @Nullable var dataResource = Main.class.getResource(Main.gameDataFilename);
        if (dataResource == null) {
            System.err.println("game data resource not found");
            System.exit(1);
        }

        final URI gameDataURI;
        try {
            gameDataURI = dataResource.toURI();
        } catch (final URISyntaxException e) {
            System.err.println(e);
            System.exit(1);
            return;
        }

        final String rawGameData = Files.readString(Path.of(gameDataURI));

        final Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                GameLogicAction.class,
                new GameLogicActionDeserializer()
            ).create();
        final ReadonlyGameInfo gameInfo = gson.fromJson(rawGameData, GameInfoImpl.class);

        final Scanner input = new Scanner(System.in, StandardCharsets.UTF_8);
        new CaveDragonsGame()
            .setGameOutput(System.out)
            .setPlayerInput(input)
            .setGameInfo(gameInfo)
            .playGame();
    }
}