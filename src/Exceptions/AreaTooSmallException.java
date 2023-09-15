package Exceptions;

public class AreaTooSmallException extends Exception {

    public AreaTooSmallException() {
        super("The given dimensions are too small for the number of chips needed to win!"); 
    }
}
