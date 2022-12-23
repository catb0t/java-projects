package org.catb0t.project5human;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    private static final String wordsName = "words";

    private static final int wordsMeanLineLength        = 9;
    private static final int wordsCountDifferentLengths = 21;

    public static void main (String[] args) throws URISyntaxException, IOException {

        final URI wordsResource = Objects.requireNonNull(
            Thread.currentThread().getContextClassLoader().getResource(Main.wordsName)
        ).toURI();

        final Configuration config;
        try ( final var stream = Files.lines(Paths.get(wordsResource)) ) {
            config = new Configuration(stream, Main.wordsMeanLineLength,
                Main.wordsCountDifferentLengths);
        }
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
