package edu.monash.commandle;

import java.io.*;
import java.util.*;

import static java.lang.System.out;


public class Commandle {
    public static final int MAX_TRIES = 6;

    /**
     * Requirements:
     * 1. Different words in consecutive games in the same session
     * 2. Default number of tries is 6, but can be changed if specified by the user ***********TC2.1, TC2.2, TC2.3, TC2,4
     * 3. Only valid words in the list are accepted ***********TC-3.1, TC-3.3
     * 4. Words are case-insensitive ***********TC-4.1
     * 5. The same word cannot be played again in a game ***********TC-3.2
     * <p>
     * Hints:
     * * A "?" flags that the input letter is in the word but not in the right position.
     * * A "#" flags the input letter is not in the word.
     * * The actual letter shows that it's in the right position.
     *
     * @param args Optional argument that points to a dictionary file of allowed words.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        start(System.in, out, args);
    }

    static void start(InputStream in, PrintStream out, String[] args) throws IOException {
        String dictionaryFileName = "dictionary.txt";
        if (null != args && args.length > 0) {
            dictionaryFileName = args[0];
        }

        List<String> wordList = getWordList(dictionaryFileName);
        start(in, out, wordList);
    }

    static void start(InputStream in, PrintStream out, List<String> wordList) throws IOException {
        GameBoard gameBoard = new GameBoard(wordList);
        Scanner scanner = new Scanner(in);
        boolean playAgain = true;
        do {
            gameBoard.startGame();
            int maxTries = MAX_TRIES; //////////////implementation for requirement 2. Default number of tries is 6, but can be changed if specified by the user
            out.println("Press M to change max tries (default = 6), or S to start: ");
            String choice = scanner.nextLine().trim().toUpperCase();
            if (choice.equals("M")) {
                out.println("Enter the number of max tries: ");
                while (true)
                    try {
                        maxTries = Integer.parseInt(scanner.nextLine().trim());
                        if (maxTries > 0) {
                            break;
                        } else {
                            out.println("Please enter a positive number bigger than 0: ");
                        }
                    } catch (NumberFormatException e) {
                        out.println("Invalid input. Please enter a number: ");
                    }

            } else if (!choice.equals("S")){
                out.println("Invalid input. Please enter M or S: ");
                continue;
            }
            boolean result = playOneGame(out, MAX_TRIES, gameBoard, scanner);
            if (result) {
                out.println("Congratulations, you won!");
            } else {
                out.println("Sorry, you lost!");
            }

            out.println("Play again? (Y/N)");
            String playChoice = scanner.nextLine().trim().toUpperCase();
            if (playChoice.equals("N")) {
                playAgain = false;
            }
        } while (playAgain);
        out.println("See you next time!");
        scanner.close();
        }

    private static List<String> getWordList(String dictionaryFileName) throws IOException {
        final List<String> wordList;
        File file = new File(Commandle.class.getClassLoader().getResource(dictionaryFileName).getFile());
        //reads the file
        try (FileReader fr = new FileReader(file)) {
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
            wordList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                wordList.add(line.trim().toLowerCase());
            }
        }
        return wordList;

    }

    private static String getNextValidGuess(Scanner scanner, Set<String> guesses, GameBoard gameBoard) {
        String guess = scanner.nextLine().trim().toLowerCase();

        if (guess.length() != 5) {
            out.print("Please enter a word of 5 letters: ");
        } else if (guesses.contains(guess)) {
            out.print("Please enter a new word: ");
        } else if (!gameBoard.containsWord(guess)) {
            out.print("Please enter a valid word: ");
        } else {
            guesses.add(guess);
            return guess;
        }
        return getNextValidGuess(scanner, guesses, gameBoard);
    }

    private static boolean playOneGame(PrintStream out, int rounds, GameBoard gameBoard, Scanner scanner) {
        Set<String> guesses = new HashSet<>();

        for (int i = 0; i < rounds; i++) {
            out.print("Please enter your guess: ");
            String guess = getNextValidGuess(scanner, guesses, gameBoard);

            // Check for correctness here
            GameBoard.Status[] result = gameBoard.isInTarget(guess.toLowerCase().toCharArray());
            String hint = "";
            for (int j = 0; j < result.length; j++) {
                switch (result[j]) {
                    case correct -> hint += guess.charAt(j);
                    case wrong -> hint += "#";
                    case partial -> hint += "?";
                }
            }
            int round = i + 1;
            out.println(round + ": " + guess + "  " + round + ": " + hint);
            if (gameBoard.hasWon(result)) {
                return true;
            }
        }
        return false;
    }
}
