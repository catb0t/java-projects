package org.catb0t;

import java.io.PrintStream;
import java.util.Scanner;

public interface Playable {
    Playable play ();

    Playable setOutput (PrintStream output);

    Playable setInput (Scanner scanner);

    Scanner input ();

    PrintStream output ();

    boolean isWinState ();

    // TODO: ranged playable consideration??
    Score currentScore ();

    boolean isFinalScore ();

    Playable pause ();

    Playable stop ();
}
