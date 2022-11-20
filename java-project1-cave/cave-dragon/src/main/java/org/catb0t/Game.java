package org.catb0t;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Game {

    protected Scanner playerInput;
    protected HashMap<String, ArrayList<String>> storyStrings;
    protected HashMap<String, ArrayList<String>> promptStrings;
    public Game () {}
    public Game (Scanner playerInput) {
        this.playerInput = playerInput;
    }

    public void playGame () {}

    public void setPromptStrings(HashMap<String, ArrayList<String>> promptStrings) {
        this.promptStrings = promptStrings;
    }

    public void setStoryStrings(HashMap<String, ArrayList<String>> storyStrings) {
        this.storyStrings = storyStrings;
    }

    public void getStoryStrings(HashMap<String, ArrayList<String>> storyStrings) {
        this.storyStrings = storyStrings;
    }

    public void getPromptStrings(HashMap<String, ArrayList<String>> storyStrings) {
        this.storyStrings = storyStrings;
    }
}
