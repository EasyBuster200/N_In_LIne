package Exceptions;
import Game.Colour;

public class NameAlreadyResgisteredException extends Exception {
    public NameAlreadyResgisteredException() {
        super(Colour.RED.getCode() + "The given name is already registered, and wasn't added!"+ Colour.WHITE.getCode() + " If you wish to edit a players settings or see their stats, do so in the player menu.");
    }

}
