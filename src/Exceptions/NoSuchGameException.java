package Exceptions;
import Game.Colour;

public class NoSuchGameException extends Exception {
    public NoSuchGameException() {
        super(Colour.RED.getCode() + "There is no game registered with the given number!" + Colour.WHITE.getCode());
    }
}
