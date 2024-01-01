package Exceptions;
import Classes.Colour;

public class NoSavedGamesException extends Exception{
    public NoSavedGamesException() {
        super(Colour.RED.getCode() + "There are no registered games yet..." + Colour.WHITE.getCode());
    }
}
