package org.catb0t.project5human;

import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    private static final String wordsName = "words";

    private static final int wordsMeanLineLength        = 9;
    private static final int wordsCountDifferentLengths = 20;

    public static void main (final String[] args) throws URISyntaxException, IOException {
        final var config = Configuration.loadDefaultWords(
            Main.wordsName, Main.wordsMeanLineLength, Main.wordsCountDifferentLengths
        );

        final var human = new Human(System.in, System.out);
        final var executioner = new Bot();

        final var host = new CanonicalHost(executioner, List.of(human), config.dictionary(),
            false, false);

        for (final var word :
            config.dictionary().entrySet()) {
            System.out.println(word);
        }
    }

}
