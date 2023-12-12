package Exceptions;
import Game.Colour;

public class NoSavedGamesException extends Exception{
    public NoSavedGamesException() {
        super(Colour.RED.getCode() + "There are no registered games yet..." + Colour.WHITE.getCode());
    }
}
