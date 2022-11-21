package org.catb0t;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;


public class Main {

    private static final String gameDataFilename = "/game_data.json";

    /**
     * Run the game based on data from game_data.json.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, NoSuchFieldException {

        URI gameDataURI;
        try {
            gameDataURI = Objects.requireNonNull(
                    Main.class.getResource(gameDataFilename)
            ).toURI();
        } catch (URISyntaxException e) {
            System.err.println(e.toString());
            System.exit(1);
            return;
        }
        String rawGameData = Files.readString(Path.of(gameDataURI));

        @NotNull Gson gson = new GsonBuilder().registerTypeAdapter(
                GameLogicAction.class,
                new GameLogicActionDeserializer()
        ).create();
        GameInfo gameInfo = gson.fromJson(rawGameData, GameInfo.class);

        //System.out.println(gameInfo.gameLogicSeq.get(0).function);

        @NotNull Scanner input = new Scanner(System.in);
        new CaveDragonGame()
                .setGameOutput(System.out)
                .setPlayerInput(input)
                .setGameInfo(gameInfo)
                .playGame();
    }
}