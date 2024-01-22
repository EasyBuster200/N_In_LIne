package Tests;

import Game.Colour;
import Game.Game;
import Game.GameManager;
import Game.GameManagerClass;
import Game.Player;
import Exceptions.NameAlreadyResgisteredException;
import Exceptions.NoRegisteredPlayersException;
import Exceptions.NoSavedGamesException;
import Exceptions.NoSuchGameException;
import Exceptions.NoSuchPlayerException;

import org.junit.Before;
import org.junit.Test;
import java.util.Iterator;
import java.util.UUID;
import static org.junit.Assert.*;

public class GameManagerClassTest {

    private GameManager gameManager;

    @Before
    public void setUp() {
        gameManager = new GameManagerClass();
    }

    @Test
    public void registerUser() {
        try {
            gameManager.registerUser("Player1", Colour.RED);
            gameManager.registerUser("Player2", Colour.BLUE);
        } catch (NameAlreadyResgisteredException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertThrows(NameAlreadyResgisteredException.class, () -> gameManager.registerUser("Player1", Colour.GREEN));
    }

    @Test
    public void newGame() {
        try {
            gameManager.registerUser("Player1", Colour.RED);
            gameManager.registerUser("Player2", Colour.BLUE);
        } catch (NameAlreadyResgisteredException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            Game game = gameManager.newGame(3, 3, 3, "Player1", "Player2");
            assertNotNull(game);
            assertNotNull(game.getGameId());
        } catch (NoSuchPlayerException | NoRegisteredPlayersException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertThrows(NoSuchPlayerException.class, () -> gameManager.newGame(3, 3, 3, "Player1", "Player3"));
    }

    @Test
    public void getGame() {
    
        try {
            gameManager.registerUser("Player1", Colour.RED);
            gameManager.registerUser("Player2", Colour.BLUE);
            Game game = gameManager.newGame(3, 3, 3, "Player1", "Player2");
            UUID gameId = game.getGameId();
            assertNotNull(gameManager.getGame(gameId));
        } catch (NoSuchPlayerException | NoRegisteredPlayersException | NoSuchGameException | NameAlreadyResgisteredException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertThrows(NoSuchGameException.class, () -> gameManager.getGame(UUID.randomUUID()));
    }

    @Test
    public void savedGames() {
        
        try {
            gameManager.registerUser("Player1", Colour.RED);
            gameManager.registerUser("Player2", Colour.BLUE);
            gameManager.newGame(3, 3, 3, "Player1", "Player2");

            Iterator<Game> savedGames = gameManager.savedGames();
            assertTrue(savedGames.hasNext());
        } catch (NoSuchPlayerException | NoRegisteredPlayersException | NoSavedGamesException | NameAlreadyResgisteredException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void getScoreCardNoPlayers() {
        assertThrows(NoRegisteredPlayersException.class, () -> gameManager.getScoreCard());
    }

    @Test
    public void getScoreCardWithPlayers() {
        
        try {
            gameManager.registerUser("Player1", Colour.RED);
            gameManager.registerUser("Player2", Colour.BLUE);

            Iterator<Player> scoreCard = gameManager.getScoreCard();
            assertTrue(scoreCard.hasNext());
        } catch (NoRegisteredPlayersException | NameAlreadyResgisteredException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testScoreCardOrder() {
        try {
            
            gameManager.registerUser("Player1", Colour.RED);
            gameManager.registerUser("Player2", Colour.BLUE);
            gameManager.registerUser("Player3", Colour.GREEN);

            Iterator<Player> players = gameManager.getPlayersIterator();

            
            Player player1 = players.next();
            Player player2 = players.next();
            Player player3 = players.next();

            player1.wonGame();
            player1.wonGame();
            player2.wonGame();  

            Iterator<Player> scoreCardIterator = gameManager.getScoreCard();


            assertEquals(player1, scoreCardIterator.next());  
            assertEquals(player2, scoreCardIterator.next());
            assertEquals(player3, scoreCardIterator.next());

        } catch (NameAlreadyResgisteredException | NoRegisteredPlayersException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }


    @Test
    public void getPlayersIteratorNoPlayers() {
        assertThrows(NoRegisteredPlayersException.class, () -> gameManager.getPlayersIterator());
    }

    @Test
    public void getPlayersIteratorWithPlayers() {
        Iterator<Player> playersIterator;
        
        try {
            gameManager.registerUser("Player1", Colour.RED);
            gameManager.registerUser("Player2", Colour.BLUE);

            playersIterator = gameManager.getPlayersIterator();
            assertTrue(playersIterator.hasNext());
        } catch (NoRegisteredPlayersException | NameAlreadyResgisteredException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void hasPlayers() {
        assertFalse(gameManager.hasPlayers());

        try {
            gameManager.registerUser("Player1", Colour.RED);
            assertTrue(gameManager.hasPlayers());
        } catch (NameAlreadyResgisteredException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void removeGame() {

        try {
            gameManager.registerUser("Player1", Colour.RED);
            gameManager.registerUser("Player2", Colour.BLUE);
            Game game = gameManager.newGame(3, 3, 3, "Player1", "Player2");
            UUID gameId = game.getGameId();

            assertTrue(gameManager.hasPlayers());
            gameManager.removeGame(gameId);
            assertTrue(gameManager.hasPlayers());
        } catch (NameAlreadyResgisteredException | NoSuchPlayerException | NoRegisteredPlayersException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
