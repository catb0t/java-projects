package project5human;

public record GuessValue( String fullGuess, char characterGuess ) {
    boolean isFullGuess () {
        return (this.fullGuess != null) && this.fullGuess.isEmpty();
    }
}
