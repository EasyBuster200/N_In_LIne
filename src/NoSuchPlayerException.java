
public class NoSuchPlayerException extends Exception{

    public NoSuchPlayerException(int player){
        super("No player with number " + player + " was found!");
    }

}
