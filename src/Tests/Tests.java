package Tests;

import static org.junit.Assert.*;
import org.junit.Test;

import Game.Colour;
import Game.GameManager;
import Game.GameManagerClass;

public class Tests {

    @Test
    public void ScoreCardTest() {
        GameManager gm = new GameManagerClass(); //TODO: Finish

        try {
            gm.registerUser("Test1", Colour.BLUE);
            gm.registerUser("Test2", Colour.RED);
            gm.registerUser("Test3", Colour.RED);
            gm.registerUser("Test4", Colour.RED);
            gm.registerUser("Test5", Colour.RED);

            //TODO: Need a way to add a win to each of the players and make sure that the order is correct 
            //! For the order I already get the list or wtv, just need to iterate it.
            
            
        } catch (Exception e) {
            System.out.println("There was a problem");
        }

    }

}

/* 
Player P1 = new PlayerClass("Test1", Colour.RED);
            Player P2 = new PlayerClass("Test2", Colour.GREEN);
            Player P3 = new PlayerClass("Test3", Colour.BLUE);
            Player P4 = new PlayerClass("Test4", Colour.INDIGO);
            Player P5 = new PlayerClass("Test5", Colour.ORANGE);
            Player P6 = new PlayerClass("Test6", Colour.VIOLET);
 */