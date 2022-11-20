package org.catb0t;

import java.util.Scanner;

public class CaveDragonGame extends Game {

    public CaveDragonGame () {}
    public CaveDragonGame (Scanner playerInput) {
        this.playerInput = playerInput;
    }
    @Override
    public void playGame() {
        System.out.println("<playGame>");
    }
}
