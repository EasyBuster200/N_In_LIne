package Exceptions;
public class OutOfBoundsException extends Exception {
    public OutOfBoundsException() {
        super("\nThe inserted position is outside the bounds of the board!");
    }
}
