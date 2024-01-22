package Game;

import Exceptions.UnknownColourException;

/**
 * Enum for all the available colours for the players
 */
public enum Colour {
    RED("\u001B[31m", "red"),
    ORANGE("\u001B[38;5;208m", "orange"),
    YELLOW("\u001B[33m", "yellow"),
    GREEN("\u001B[32m", "green"),
    BLUE("\u001B[34m", "blue"),
    INDIGO("\u001B[36m", "indigo"),
    VIOLET("\u001B[35m", "violet"),
    WHITE("\033[0;0m", "white"),
    DARK_GREY("\u001B[30m", "dark grey");

    private String code; //ANSI code of the colour 
    private String toString; //Name of the colour

    Colour(String code, String toString) {
        this.code = code;
        this.toString = toString;
    }

    /**
     * Returns the respective colour from the given string
     * @param chosenColour name of the colour we are looking for
     * @return color equivalent to the string 
     * @throws UnknownColourException if there are no colours with the given name
     */
    public Colour getColour(String chosenColour) throws UnknownColourException {
        for(Colour colour: Colour.values())
            if (colour.toString.equals(chosenColour))
                return colour;

        throw new UnknownColourException();
    }

    /**
     * @return ANSI code of the color
     */
    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return toString;
    }
    
    /**
     * @return a single String with the names of all the available colours
     */
    public static String getColours() {
        String colours = "";
        for(Colour colour: Colour.values())
            if (!(colour.equals(Colour.WHITE) || colour.equals(Colour.DARK_GREY)))
                colours += (colour.getCode() + colour + WHITE.getCode() + " ");

        return colours;
    }

}
