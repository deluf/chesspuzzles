package it.unipi.chesspuzzles.client;

import com.google.gson.Gson;
import it.unipi.chesspuzzles.client.shared.Attemp;
import it.unipi.chesspuzzles.client.shared.Favourite;
import it.unipi.chesspuzzles.client.shared.Puzzle;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChessPuzzlesApiTest {
    
    public ChessPuzzlesApiTest() {}
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of authenticate method, of class ChessPuzzlesApi.
     */
    @Test
    public void tesLogin() throws Exception {
        System.out.println("Loggin in with the test user");

        int expectedResponse = 1;
        int response = ChessPuzzlesApi.authenticate(
                "test", "test", ChessPuzzlesApi.AUTH_TYPE.LOGIN);

        if (response != expectedResponse) {
            fail("Test user is either wrong, not existent, or does not have id 1");
        }
    }

    /**
     * Test of authenticate method, of class ChessPuzzlesApi.
     */
    @Test
    public void testRegistration() throws Exception {
        System.out.println("Registering the test user again");

        int expectedResponse = 0;
        int response = ChessPuzzlesApi.authenticate(
                "test", "test", ChessPuzzlesApi.AUTH_TYPE.REGISTER);

        if(response != expectedResponse) {
            fail("You shouldn't be able to register users twice");
        }
    }

    /**
     * Test of addPuzzleToFavourites method, of class ChessPuzzlesApi.
     */
    @Test
    public void testAddPuzzleToFavourites() {
        System.out.println("Addinga a puzzle to test's user favourites");

        Gson gson = new Gson();
        Puzzle puzzle = gson.fromJson(
                "{\"title\":\"Test puzzle\",\"publishTime\":1713842000,\"puzzleStates\":[{\"fen\":\"5rk1/1pp3b1/np5p/4Npp1/BPP5/P3RPP1/1K5P/8 b - - 0 1\",\"correctMove\":\"f4\"},{\"fen\":\"5rk1/1pp3b1/np5p/4N1p1/BPP2p2/P3RPP1/1K5P/8 w - - 0 2\",\"correctMove\":\"gxf4\"},{\"fen\":\"5rk1/1pp3b1/np5p/4N1p1/BPP2P2/P3RP2/1K5P/8 b - - 0 2\",\"correctMove\":\"gxf4\"},{\"fen\":\"5rk1/1pp3b1/np5p/4N3/BPP2p2/P3RP2/1K5P/8 w - - 0 3\",\"correctMove\":\"Re4\"},{\"fen\":\"5rk1/1pp3b1/np5p/4N3/BPP1Rp2/P4P2/1K5P/8 b - - 1 3\",\"correctMove\":\"Rf5\"},{\"fen\":\"6k1/1pp3b1/np5p/4Nr2/BPP1Rp2/P4P2/1K5P/8 w - - 2 4\",\"correctMove\":\"#\"}]}",
                Puzzle.class
        );

        try {
            ChessPuzzlesApi.addPuzzleToFavourites(1, puzzle);
        }
        catch (Exception e) {
            fail("Unable to add favourite puzzle to test user");
        }
    }

    /**
     * Test of getFavourites method, of class ChessPuzzlesApi.
     */
    @Test
    public void testGetFavourites() throws Exception {
        System.out.println("Getting test user's favourites");

        Favourite[] response = ChessPuzzlesApi.getFavourites(1);
        int expectedLenght = 5;

        if (response.length < expectedLenght) {
            fail("Test user has fewer favourites than expected");
        }
    }

    /**
     * Test of getRandomPuzzle method, of class ChessPuzzlesApi.
     */
    @Test
    public void testGetRandomPuzzle() {
        System.out.println("Getting a random puzzle");

        try {
            ChessPuzzlesApi.getRandomPuzzle();
        }
        catch (Exception e) {
            fail("Unable to get a random puzzle");
        }
    }

    /**
     * Test of drawBoard method, of class ChessPuzzlesApi.
     */
    @Test
    public void testDrawBoard() {
        System.out.println("Drawing a borad from default fen");

        String defaultFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        try {
            ChessPuzzlesApi.drawBoard(defaultFen);
        }
        catch (Exception e) {
            fail("Unable to draw board from default fen");
        }
    }

    /**
     * Test of addAttemp method, of class ChessPuzzlesApi.
     */
    @Test
    public void testAddAttemp() {
        System.out.println("Adding an attemp to the test user");

        try {
            ChessPuzzlesApi.addAttemp(1, Attemp.TYPE.SOLVE);
        }
        catch (Exception e) {
            fail("Unable to add an attemp to the test user");
        }
    }

    /**
     * Test of getAttempsAfter method, of class ChessPuzzlesApi.
     */
    @Test
    public void testGetAttempsAfter() throws Exception {
        System.out.println("Getting test user's attemps");

        Attemp[] response = ChessPuzzlesApi.getAttempsAfter(1, LocalDateTime.parse("2024-01-01T00:00:00"));
        int expectedLenght = 73;

        if (response.length < expectedLenght) {
            fail("Test user has fewer attemps than expected");
        }
    }
    
}
