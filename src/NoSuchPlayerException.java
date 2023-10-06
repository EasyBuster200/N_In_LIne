
public class NoSuchPlayerException extends Exception{

    public NoSuchPlayerException(String name){
        super("No player with name: " + name + " was found!");
    }

}
