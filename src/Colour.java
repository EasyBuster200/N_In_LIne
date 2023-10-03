public enum Colour {
    RED("\u001B[31m"),
    ORANGE("\u001B[38;5;208m"),
    YELLOW("\u001B[33m"),
    GREEN("\u001B[32m"),
    BLUE("\u001B[34m"),
    INDIGO("\u001B[36m"),
    VIOLET("\u001B[35m");

    private String code;

    Colour(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
