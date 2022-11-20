package org.catb0t;

import java.util.*;

import static java.util.Map.entry;

public class Main {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        CaveDragonGame caveGame = new CaveDragonGame(input);

        Map<String, ArrayList<String>> storyStrings = Map.ofEntries(
                entry("introduction", new ArrayList<String>()),
                entry("ending_good", new ArrayList<String>()),
                entry("ending_bad", new ArrayList<String>())
        );
        caveGame.setStoryStrings((HashMap<String, ArrayList<String>>) storyStrings);

        String str = """In this game, the player is in a land full of dragons. The dragons all live in caves with their large piles of collected treasure.
        Some dragons are friendly and share their treasure. Other dragons are hungry and eat anyone who enters their cave.
        The player approaches two caves, one with a friendly dragon and the other with a hungry dragon, but doesnâ\\u0080\\u0099t know which dragon is in which cave.
        The player must choose between the two.\\n\\n\\n\\nThe program should look like this in the console, the player input is in bold:\\n

        \\nYou are in a land full of dragons. In front of you,
        you see two caves. In one cave, the dragon is friendly
        and will share his treasure with you. The other dragon
        is greedy and hungry and will eat you on sight.

        You approach the cave...
        It is dark and spooky...
        A large dragon jumps out in front of you! He opens his jaws and...
        Gobbles you down in one bite!
        Process finished with exit code 0\""""

        Map<String, ArrayList<String>> promptStrings = Map.ofEntries(
                entry("choose_room", new ArrayList<String>("Which cave will you go into? (1 or 2)")),
                entry("enter_close_game_ended", new ArrayList<String>("Press any key to continue..."))
        );
        caveGame.setPromptStrings(new HashMap<String, ArrayList<String>>());

        caveGame.playGame();
    }
}