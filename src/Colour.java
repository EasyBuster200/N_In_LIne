public enum Colour {
    RED("\u001B[31m", "red"),
    ORANGE("\u001B[38;5;208m", "orange"),
    YELLOW("\u001B[33m", "yellow"),
    GREEN("\u001B[32m", "green"),
    BLUE("\u001B[34m", "blue"),
    INDIGO("\u001B[36m", "indigo"),
    VIOLET("\u001B[35m", "violet");
    //TODO: Later add white/default color excluding it when printing for new players

    private String code;
    private String toString;

    Colour(String code, String toString) {
        this.code = code;
        this.toString = toString;
    }

    public Colour getColour(String chosenColour) throws UnknownColourException {
        for(Colour colour: Colour.values())
            if (colour.toString.equals(chosenColour))
                return colour;

        throw new UnknownColourException();
    }

    public String getCode() {
        return code;
    }

    public String toString() {
        return toString;
    }

}
