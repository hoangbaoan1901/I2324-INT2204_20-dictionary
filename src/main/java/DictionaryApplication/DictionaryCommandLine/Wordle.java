package DictionaryApplication.DictionaryCommandLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Wordle {
    private int tries;
    private String key;
    private boolean isPlaying;
    private boolean won = false;

    public static String WIN = "WIN";
    public static String LOST = "LOST";

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public static String getRandomLineFromFile(Path filePath) {
        long lineCount = 0;
        try {
            lineCount = Files.lines(filePath).count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Generate a random line number
        long randomLineNumber = ThreadLocalRandom.current().nextLong(1, lineCount + 1);

        // Read the file and return the random line
        try (var lines = Files.lines(filePath)) {
            return lines.skip(randomLineNumber - 1).findFirst().orElse(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Wordle() {
        this.tries = 0;
        this.key = getRandomLineFromFile(Path.of("src/main/resources/Utils/wordle.txt"));
        this.isPlaying = true;
        this.won = false;
    }


    public static boolean verifyWord(String s) {
        if (s.length() != 5) {
            return false;
        }
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("src/main/resources/Utils/wordle.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNextLine()) {
            String wordle = scanner.nextLine();
            if (s.equals(wordle)) {
                return true;
            }
        }
        return false;
    }


    // Player will try to guess the word
    public String attempt(String s) {
        if (this.isPlaying) {
            if (s.equals(this.key)) {
                this.isPlaying = false;
                this.won = true;
                return Wordle.WIN;
            } else {
                String states = "";
                for (int i = 0; i < 5; i++) {
                    // If key doesn't contain the character from user's answer
                    if (this.key.indexOf(s.charAt(i)) == -1) {
                        states += "n";
                    } else {
                        if (this.key.charAt(i) != s.charAt(i)) {
                            states = states + "f";
                        } else {
                            states = states + "t";
                        }
                    }
                }
                this.tries += 1;
                if (this.tries > 6) {
                    System.out.println(this.getKey());
                    this.isPlaying = false;
                }
                return states;
            }
        } else {
            return Wordle.LOST;
        }
    }

    public static void main(String[] args) {
        Wordle w = new Wordle();
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 6; i++) {
            System.out.println("Enter your word or type '.exit' to exit:");
            String guess = sc.nextLine().substring(0, 5);
            if (guess.equals(".exit")) {
                w.setPlaying(false);
                break;
            }
            while (!verifyWord(guess)) {
                System.out.println("Word not found!. Enter another word or type '.exit' to exit:");
                if (guess.equals(".exit")) {
                    w.setPlaying(false);
                    break;
                }
                guess = sc.nextLine().substring(0, 5);
            }
            System.out.println(w.attempt(guess));
            if (!w.isPlaying()) {
                break;
            }
        }
    }
}
