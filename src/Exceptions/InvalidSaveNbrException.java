package Exceptions;

public class InvalidSaveNbrException extends Exception {
    public InvalidSaveNbrException() {
        super("Please insert a valid number for a save file!");
    }

}
