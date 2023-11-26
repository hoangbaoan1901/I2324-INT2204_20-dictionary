package Application;

import DictionaryApplication.DictionaryCommandLine.Dictionary;
import DictionaryApplication.DictionaryCommandLine.Word;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;

public class CommandlineStart {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Dictionary myDictionary = new Dictionary();

    public static void main(String[] args) {
        System.out.println("Welcome to My Application!");

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Your action: ");
            String userInput = scanner.nextLine();

            try {
                int action = Integer.parseInt(userInput);
                switch (action) {
                    case 0:
                        running = false;
                        break;
                    case 1:
                        addWords();
                        break;
                    case 2:
                        removeWord();
                        break;
                    case 3:
                        updateWord();
                        break;
                    case 4:
                        displayWords();
                        break;
                    case 5:
                        lookupWord();
                        break;
                    case 6:
                        searchWords();
                        break;
                    case 7:
                        // Implement game logic here
                        break;
                    case 8:
                        importFromFile();
                        break;
                    case 9:
                        exportToFile();
                        break;
                    default:
                        System.out.println("Action not supported.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        scanner.close();
        System.out.println("Exiting the application. Goodbye!");
    }

    private static void removeWord() {
        System.out.print("Enter the English word to remove: ");
        String word_target = scanner.nextLine();

        myDictionary.removeWord(word_target);
        System.out.println("Word removed successfully.");
    }

    private static void updateWord() {
        System.out.print("Enter the English word to update: ");
        String word_target = scanner.nextLine();

        System.out.print("Enter the new meaning in Vietnamese: ");
        String word_explain = scanner.nextLine();

        System.out.print("Enter the new word type (optional): ");
        String word_type = scanner.nextLine();

        myDictionary.removeWord(word_target);
        myDictionary.insertWord(word_target, word_explain, word_type);
        System.out.println("Word updated successfully.");
    }

    private static void displayWords() {
        int index = 1;
        for (Word word : myDictionary.Words) {
            System.out.println(index + ". " + word.getWord_target() + " - " + word.getWord_explain() + (word.getWord_type().isEmpty() ? "" : " (" + word.getWord_type() + ")"));
            index++;
        }
    }

    private static void lookupWord() {
        System.out.print("Enter a word to lookup: ");
        String wordToLookup = scanner.nextLine();

        boolean found = false;
        for (Word word : myDictionary.Words) {
            if (word.getWord_target().equalsIgnoreCase(wordToLookup)) {
                System.out.println("Meaning in Vietnamese: " + word.getWord_explain());
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Word not found in the dictionary.");
        }
    }

    private static void searchWords() {
        System.out.print("Enter a prefix to search: ");
        String prefix = scanner.nextLine();

        HashSet<Word> results = myDictionary.searchWords(prefix);
        if (results.isEmpty()) {
            System.out.println("No words found with the prefix: " + prefix);
        } else {
            for (Word word : results) {
                System.out.println("English: " + word.getWord_target() + ", Vietnamese: " + word.getWord_explain());
            }
        }
    }

    private static void addWords() {
        System.out.print("Nhập số lượng từ bạn muốn thêm: ");
        int numWords = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numWords; i++) {
            System.out.print("Nhập từ tiếng Anh: ");
            String word_target = scanner.nextLine();

            System.out.print("Nhập nghĩa tiếng Việt: ");
            String word_explain = scanner.nextLine();

            System.out.print("Nhập loại từ (không bắt buộc): ");
            String word_type = scanner.nextLine();

            myDictionary.insertWord(word_target, word_explain, word_type);
        }
    }

    private static void importFromFile() {
        String path = "./src/main/resources/Utils/dictionary.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            String word_target = null;
            String word_explain = "";
            String word_type = "";

            while ((line = br.readLine()) != null) {
                if (line.startsWith("|")) {
                    if (word_target != null) {
                        myDictionary.insertWord(word_target, word_explain.trim(), word_type.trim());
                    }
                    word_target = line.substring(1).trim();
                    word_explain = "";
                    word_type = "";
                } else if (!line.isEmpty() && line.charAt(0) == '*') {
                    word_type = line.trim();
                } else {
                    word_explain += line + "\n";
                }
            }

            if (word_target != null) {
                myDictionary.insertWord(word_target, word_explain.trim(), word_type.trim());
            }
            System.out.println("Đã nhập từ từ file.");
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
        }
    }

    private static void exportToFile() {
        String path = "./src/main/resources/Utils/dictionary.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Word word : myDictionary.Words) {
                bw.write("|" + word.getWord_target());
                bw.newLine();
                if (!word.getWord_type().isEmpty()) {
                    bw.write("* " + word.getWord_type());
                    bw.newLine();
                }
                bw.write(word.getWord_explain().trim());
                bw.newLine();
            }
            System.out.println("Đã xuất từ điển ra file thành công.");
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi ra file: " + e.getMessage());
        }
    }


    private static void printMenu() {
        System.out.println("[0] Exit");
        System.out.println("[1] Add words");
        System.out.println("[2] Remove word");
        System.out.println("[3] Update word");
        System.out.println("[4] Display all words");
        System.out.println("[5] Lookup a word");
        System.out.println("[6] Search words with prefix");
        System.out.println("[7] Play a game");
        System.out.println("[8] Import from file");
        System.out.println("[9] Export to file");
    }
}