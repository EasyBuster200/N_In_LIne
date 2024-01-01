package Exceptions;

import Game.Colour;

public class NoSuchColourException extends Exception{
    public NoSuchColourException() {
        super(Colour.RED.getCode() + "The given colour does not exist!" + Colour.WHITE.getCode());
    }
}
