package edu.monash.commandle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.monash.commandle.GameBoard.Status.correct;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;



class GameBoardUnitTest {
//    private final InputStream systemIn = System.in;
//    private final PrintStream systemOut = System.out;
//
//    //private List<String> wordList;
//    private ByteArrayOutputStream testOut;
private GameBoard gameBoard;
//    String target = "brave";
//    List<String> wordList = Arrays.asList("bravo", "brave", "broom", "bread", "break");

    @BeforeEach
    void setUp() {
 //       wordList = new ArrayList<String>();
//        testOut = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(testOut));
//        gameBoard = new GameBoard(wordList);
//        gameBoard.startGame();
    }

    @AfterEach
    void tearDown() {
//        System.setOut(systemOut);
    }

    @Test
    void gameCanStart(){
//        gameBoard.startGame();
//        String target = gameBoard.getTarget();
//        assertNotNull(target);
//        assertTrue(gameBoard.getWordList().contains(target));
    }

    @Test
    void isInTarget() {
    }

    @Test
    void hasWon() {
        gameBoard = new GameBoard(new ArrayList<>());

        GameBoard.Status[] result = new GameBoard.Status[]{correct, correct, correct, correct, correct};
        assertTrue(gameBoard.hasWon(result), "game is won");
    }

    @Test
    void containsCorrectWord() {
        ArrayList<String> list = new ArrayList<>();
        String word = "prone";
        list.add(word);
        gameBoard = new GameBoard(list);

        assertTrue(gameBoard.containsWord(word), "contains word");
    }
}