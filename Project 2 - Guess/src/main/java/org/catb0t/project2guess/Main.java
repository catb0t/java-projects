/*
 * Copyright (c) 2022 Cat Stevens. All rights reserved.
 */

package org.catb0t.project2guess;

import java.io.*;
import java.net.*;
import java.util.*;


public class Main {

    static final String JSON_EXT              = ".json";
    static final String GAME_RULES_BASE       = "game_rules";
    static final String GAME_STRING_KEYS_BASE = "guess_strings";

    public static void main (final String[] args) throws IOException, URISyntaxException {

        // TODO: user select locale
        final Locale currentLocale = new Locale("en", "US");

        //FileSystems.getDefault().getSeparator();
        // you can't use Windows file separator inside Resource path, it's just an escape
        final var pathSep = "/";
        final Configurable config = new Configuration(
            Main.GAME_RULES_BASE, pathSep, Main.JSON_EXT
        );

        final var localise = new Localiser(
            Main.GAME_STRING_KEYS_BASE, currentLocale, pathSep, Main.JSON_EXT
        );

        final GuessGame game = new GuessGame(config, localise);

        final Map<String, DefinedName> defs = Map.of(
            "$min_target", new DefinedName("$min_target", 1L),
            "$max_target", new DefinedName("$min_target", 20L),
            "$player_name", new DefinedName("$player_name", "Cat")
        );

        game.playGame();
    }

}