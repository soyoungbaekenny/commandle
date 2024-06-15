package edu.monash.commandle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandleTest {
    // See https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println and
    // https://stackoverflow.com/questions/1647907/junit-how-to-simulate-system-in-testing
    // for more information on how to test with system input & output
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private List<String> wordList;
    private ByteArrayOutputStream testOut;
    String target = "brave"; /////Target word for all test cases
    String guess1 = "bravo";
    String guess2 = "bread";

    @BeforeEach
    void setUp() {
        wordList = new ArrayList<String>();
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void tearDown() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    private String getOutput() {
        return testOut.toString();
    }

    @Test
    void gameCanBeWon() throws IOException {
        // set up a one-word list for easy testing

        wordList.add(target);

        // provide the correct guess, and then followed by "N" to signal not wanting to play again
        provideInput("M\nbravo\nbravo\nbravo\nbravo\nbravo\nbrave\nN");

        // simulate the gameplay start
        Commandle.start(System.in, System.out, wordList);

        // get output
        String result = getOutput();

        // verify that the output contains the word "won"
        assertTrue(result.contains("Congratulations, you won!"), "You won");
    }

//    @Test
//    void gameCanBeLost() throws IOException {
//        wordList.add(target);
//        wordList.add(guess1);
//        wordList.add(guess2);
//        wordList.add("broth");
//        wordList.add("beard");
//        wordList.add("bonus");
//
//        provideInput("M\n2\nbravo\nbread\nN");
//        Commandle.start(System.in, System.out, wordList);
//        String result = getOutput();
//        assertTrue(result.contains("Sorry, you lost!"), "You lost");
//    }

    @Test //TC-ID: TC2.1
    void defaultMaxTries() throws IOException{
        wordList.add(target);
        wordList.add(guess1);
        provideInput("S\nbravo\nbravo\nbravo\nbravo\nbravo\nbrave\nN");
        Commandle.start(System.in, System.out, wordList);
        String result = getOutput();
        assertTrue(result.contains("Congratulations, you won!"), "You won with the default number of tries");
    }

    @Test //TC-ID: TC2.2
    void changeMaxTries() throws IOException {
        wordList.add(target);
        wordList.add(guess1);
        provideInput("M\n3\nbravo\nbravo\nbrave\nN");
        Commandle.start(System.in, System.out, wordList);
        String result = getOutput();
        assertTrue(result.contains("Congratulations, you won!"), "You won with the default number of tries");
    }

    @Test //TC-ID: TC2.3
    void invalidInputMaxTries() throws IOException {
        wordList.add(target);
        wordList.add(guess1);
        provideInput("o\nS\n\nbravo\nbravo\nbravo\nbravo\nbravo\nbrave\nN");
        Commandle.start(System.in, System.out, wordList);
        String result = getOutput();
        assertTrue(result.contains("Invalid input. Please enter M or S"), "Not M or S as expected");
        assertTrue(result.contains("Congratulations, you won!"), "You won after correcting the input");
    }

    @Test //TC-ID: TC2.4
    void changeMaxTriesWithNonInteger() throws IOException {
        wordList.add(target);
        wordList.add(guess1);
        provideInput("M\na\nN\n3\nbravo\nbravo\nbrave\nN");
        Commandle.start(System.in, System.out, wordList);
        String result = getOutput();
        assertTrue(result.contains("Invalid input. Please enter a number"), "non-numeric input");
    }

    @Test //TC-ID: TC2.5
    void negativeMaxTries() throws IOException {
        wordList.add(target);
        wordList.add(guess1);
        provideInput("M\n-3\nN\n3\nbravo\nbravo\nbrave\nN");
        Commandle.start(System.in, System.out, wordList);
        String result = getOutput();
        assertTrue(result.contains("Please enter a positive number"), "negative input");
    }

    @Test //TC-ID: TC2.6
    void newGameContinuesAfterMaxTriesChange() throws IOException {
        wordList.add(target);
        wordList.add(guess1);
        provideInput("M\n3\nN\nbravo\nbravo\nbrave\nN\nY\nM\n3\nN\nbravo\nbravo\nbrave\nN");
        Commandle.start(System.in, System.out, wordList);
        String result = getOutput();
        assertTrue(result.contains("Congratulations, you won!"), "You won with 3 tries");
        assertTrue(result.contains("Press M to change max tries (default = 6), or S to start"), "Game prompts max tries again after continuing to play more");
    }

    @Test //TC-ID: TC3.1
    void notEqualToFiveLetters() throws IOException {
        wordList.add(target);
        wordList.add(guess1);
        provideInput("S\nBraves\nBravo\nBrave\nN");
        Commandle.start(System.in, System.out, wordList);
        String result = getOutput();
        assertTrue(result.contains("Please enter a word of 5 letters"), "Guess with letters not equal to 5 should prompt for 5 letter input.");
    }

    @Test //TC-ID: TC3.2
    void repeatedWords() throws IOException {
        wordList.add(target);
        wordList.add(guess1);
        provideInput("S\nbravo\nbravo\nbravo\nbravo\nbravo\nbrave\nN");
        Commandle.start(System.in, System.out, wordList);
        String result = getOutput();
        assertTrue(result.contains("Please enter a new word"), "Repeated word guess should prompt for a new word.");

    }

    @Test //TC-ID: TC3.3
    void invalidWord() throws IOException {
        wordList.add(target);
        provideInput("S\nabcde\nBrave\nN");
        Commandle.start(System.in, System.out, wordList);
        String result = getOutput();
        assertTrue(result.contains("Please enter a valid word"), "Invalid word guess should prompt for a valid word.");
    }
    
    @Test //TC-ID: TC4.1
    void wordsAreCaseInsensitive() throws IOException{
        wordList.add(target);
        wordList.add(guess1);
        provideInput("S\nbravo\nBRAVO\nBRaVO\nbrave\nN");
        Commandle.start(System.in, System.out, wordList);
        String result = getOutput();
        assertTrue(result.contains("Please enter a new word"), "Different case with the same word should not be recognised as different.");
    } ///checks to see if 'bravo', 'BRAVO' and 'BRaVO' are regarded the same and trigger "Please enter a new word" message.

    private void provideInput(String guess) {
        System.setIn(new ByteArrayInputStream(guess.getBytes()));
    }
}