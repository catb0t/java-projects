import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import org.assertj.core.api.*;
import org.catb0t.project5human.*;

import java.io.*;
import java.net.*;

public class CanonicalHostTest {

    @Example
    void pass () { }

    @Group
    class increment_player_index {

        @Example
        void player_index_is_properly_wrapped_to_zero () {
            final var c = new CanonicalHost();

            for (int i = 0; i < 3; i++) {
                c.incrementAndWrapPlayerIndex();
            }

            Assertions.assertThat(c.currentPlayerIndex()).isEqualTo(0);
        }
    }


    @Group
    class find_guessed_characters_in_phrases {
        @Example
        void all_guessed_characters_are_found_in_the_phrase (
            @ForAll @AlphaChars Character guessedChar,
            @ForAll @ByteRange ( min = 3, max = 22 ) final Byte phraseLength
        ) throws IOException, URISyntaxException {
            final var c = new CanonicalHost();
            c._changeDictionary(
                Configuration.loadDefaultWords("words", 9, 20)
                             .dictionary()
            );
            final var gc = new GuessValue("", guessedChar);

            final var phrase = c.chooseGamePhrase(phraseLength);

            final var positions = c.characterGuessLocations(gc);

            Assertions.assertThat(positions.size())
                      .isEqualTo(phrase.chars().filter(ch -> ch == guessedChar).count());

            Assertions.assertThatCollection(positions)
                      .allMatch(i -> phrase.charAt(i) == guessedChar);
        }
    }
}
