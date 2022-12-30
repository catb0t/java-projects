package org.catb0t.project5human;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * Store game configuration that is loaded from disk or set at game run-time.
 */
public class Configuration {
    private final Map<Byte, List<String>> dictionary;

    Configuration (final Stream<String> words, final int meanLineLength, final int lengthsCount) {
        this.dictionary = Configuration.mapWordsToLengths(words, meanLineLength, lengthsCount);
    }

    static Map<Byte, List<String>> mapWordsToLengths (
        final Stream<String> input,
        final int meanLineLength,
        final int lengthsCount
    ) {
        final Map<Byte, List<String>> result = new HashMap<>(lengthsCount);

        input.forEach((final String line) -> {
            final var length = Byte.valueOf((byte) line.length());
            if (result.containsKey(length)) {
                final var val = result.get(length);
                val.add(line);
            } else {
                final List<String> val = new ArrayList<>(meanLineLength);
                val.add(line);
                result.put(length, val);
            }
        });

        return result;
    }

    public static Configuration loadDefaultWords (
        final String wordsName,
        final int wordsMeanLineLength,
        final int wordsCountDifferentLengths
    ) throws IOException, URISyntaxException {
        final URI wordsResource = Objects.requireNonNull(
            Thread.currentThread().getContextClassLoader().getResource(wordsName)
        ).toURI();

        try ( final var stream = Files.lines(Paths.get(wordsResource)) ) {
            return new Configuration(stream, wordsMeanLineLength,
                wordsCountDifferentLengths);
        }
    }

    public Map<Byte, List<String>> dictionary () {
        return Collections.unmodifiableMap(this.dictionary);
    }
}
