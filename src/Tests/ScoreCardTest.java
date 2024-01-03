package Tests;

import static org.junit.Assert.*;
import org.junit.Test;

import Game.Colour;
import Game.GameManager;
import Game.GameManagerClass;

public class ScoreCardTest {

    @Test
    public void ScoreCardTest() {
        GameManager gm = new GameManagerClass(); //TODO: Finish

        gm.registerUser("Test1", Colour.BLUE);
        gm.registerUser("Test2", Colour.RED);
        gm.registerUser("Test3", Colour.RED);
        gm.registerUser("Test4", Colour.RED);
        gm.registerUser("Test5", Colour.RED);
    }

}
