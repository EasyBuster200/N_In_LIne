package Exceptions;
import Game.Colour;

public class NoRegisteredPlayersException extends Exception {
    public NoRegisteredPlayersException() {
        super(Colour.RED.getCode() + "There are no registered players yet..." + Colour.WHITE.getCode());
    }
}
