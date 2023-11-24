package Application;

import DictionaryApplication.DictionaryCommandLine.Dictionary;
import DictionaryApplication.DictionaryCommandLine.Word;
import DictionaryApplication.DictionaryCommandLine.DictionaryManagement;
import javafx.application.Application;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;


public class CommandlineStart {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Dictionary myDictionary = new Dictionary();
        DictionaryManagement dictionaryManagement = new DictionaryManagement();

        System.out.println("Welcome to My Application!");

        while (true) {
            printMenu();
            System.out.print("Your action: ");
            String userInput = scanner.nextLine();

            try {
                int action = Integer.parseInt(userInput);

                switch (action) {
                    case 0:
                        System.out.println("Exiting the application. Goodbye!");
                        System.exit(0);
                        break;
                    case 1:
                        System.out.print("Enter the number of words to insert: ");

                        while (!scanner.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a valid integer.");
                            scanner.next();
                        }

                        int numWords = scanner.nextInt();

                        if (numWords < 0) {
                            System.out.println("Number of words cannot be negative. Exiting...");
                            scanner.close();
                            System.exit(0);
                        }

                        scanner.nextLine(); // consume the newline

                        for (int i = 0; i < numWords; i++) {
                            System.out.print("Enter a word in English: ");
                            String word_target = scanner.nextLine();

                            System.out.print("Enter the meaning in Vietnamese: ");
                            String word_explain = scanner.nextLine();

                            myDictionary.insertWord(word_target, word_explain);
                        }

                        break;
                    case 2:
                        // Remove word
                        System.out.print("Enter a word in English to delete: ");
                        String inputWordTarget = scanner.nextLine();

                        System.out.print("Enter the meaning in Vietnamese: ");
                        String inputWordExplain = scanner.nextLine();

                        System.out.print("Enter the word type (optional): ");
                        String inputWordType = scanner.nextLine();

                        Word targetWord = new Word(inputWordTarget, inputWordExplain, inputWordType);

                        Iterator<Word> iterator = myDictionary.Words.iterator();
                        boolean found = false;

                        while (iterator.hasNext()) {
                            Word w = iterator.next();
                            if (targetWord.equals(w)) {
                                iterator.remove();
                                found = true;
                                break;
                            }
                        }

                        if (found) {
                            System.out.println("Word deleted successfully.");
                        } else {
                            System.out.println("Word not found in the dictionary.");
                        }
                        break;
                    case 3:
                        // Update word
                        System.out.print("Enter a word in English to update: ");
                        String word_target = scanner.nextLine();

                        System.out.print("Enter the new meaning in Vietnamese: ");
                        String word_explain = scanner.nextLine();

                        System.out.print("Enter the new word type (optional): ");
                        String word_type = scanner.nextLine();

                        myDictionary.removeWord(word_target, "", ""); // Remove the existing word

                        if (word_type.isEmpty()) {
                            myDictionary.insertWord(word_target, word_explain);
                        } else {
                            myDictionary.insertWord(word_target, word_explain, word_type);
                        }

                        System.out.println("Word updated successfully.");
                        break;
                    case 4:
                        // Display words
                        try {
                            System.out.println("No" + "\t |" + "English" + "\t |" + "Vietnamese");
                            int index = 1;
                            for (Word word : myDictionary.Words) {
                                System.out.println(index + "\t |" + word.getWord_target() + "\t\t |" + word.getWord_explain());
                                index++;
                            }
                        } catch (NullPointerException e) {
                            System.out.println("NullPointerException: " + e);
                        }

                        break;
                    case 5:
                        // Lookup
                        System.out.print("Enter a word to lookup: ");
                        String wordToLookup = scanner.nextLine();

                        boolean founded = false;

                        for (Word word : myDictionary.Words) {
                            if (word.getWord_target().equalsIgnoreCase(wordToLookup)) {
                                System.out.println("Meaning in Vietnamese: " + word.getWord_explain());
                                founded = true;
                                break;
                            }
                        }

                        if (!founded) {
                            System.out.println("Word not found in the dictionary.");
                        }
                        break;
                    case 6:
                        // Search
                        System.out.print("Enter a prefix to search: ");
                        String prefix = scanner.nextLine();

                        HashSet<Word> searchResults = myDictionary.searchWords(prefix);

                        if (searchResults.isEmpty()) {
                            System.out.println("No words found with the prefix: " + prefix);
                        } else {
                            System.out.println("Words starting with '" + prefix + "':");
                            searchResults.forEach(word -> {
                                System.out.println("English: " + word.getWord_target() + ", Vietnamese: " + word.getWord_explain());
                            });
                        }
                        break;
                    case 7:
                        // Game
                        // Implement game logic
                        break;
                    case 8:
                        // Import from file
                        try (BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/Utils/dictionary.txt"))) {
                            String line;
                            String word_target_1 = null;
                            StringBuilder word_explain_1 = new StringBuilder();

                            while ((line = reader.readLine()) != null) {
                                if (line.startsWith("|")) {
                                    // If the line starts with "|", it's a new word
                                    if (word_target_1 != null) {
                                        // Insert the previous word into the dictionary
                                        myDictionary.insertWord(word_target_1, word_explain_1.toString().trim());
                                    }
                                    // Reset for the new word
                                    word_target_1 = line.replace("|", "").trim();
                                    word_explain_1 = new StringBuilder();
                                } else {
                                    // Append the non "|"-starting lines to the explanation
                                    word_explain_1.append(line).append("\n");
                                }
                            }

                            // Insert the last word after the loop
                            if (word_target_1 != null) {
                                myDictionary.insertWord(word_target_1, word_explain_1.toString().trim());
                            }
                        } catch (IOException e) {
                            System.out.println("Error reading file! " + e.getMessage());
                        }
                        break;
                    case 9:
                        // Export to file
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/Utils/dictionary.txt"))) {
                            for (Word word : myDictionary.Words) {
                                writer.write("|" + word.getWord_target());
                                writer.newLine();

                                String[] lines = word.getWord_explain().split("\n");
                                for (String line : lines) {
                                    writer.write(line);
                                    writer.newLine();
                                }

                                if (word.getWord_type() != null && !word.getWord_type().isEmpty()) {
                                    writer.write("|" + word.getWord_type());
                                    writer.newLine();
                                }
                            }
                            System.out.println("Dictionary exported to file successfully.");

                        } catch (IOException e) {
                            System.out.println("Error exporting dictionary to file: " + e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    default:
                        System.out.println("Action not supported.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }


    private static void printMenu() {
        System.out.println("[0] Exit");
        System.out.println("[1] Add");
        System.out.println("[2] Remove");
        System.out.println("[3] Update");
        System.out.println("[4] Display");
        System.out.println("[5] Lookup");
        System.out.println("[6] Search");
        System.out.println("[7] Game");
        System.out.println("[8] Import from file");
        System.out.println("[9] Export to file");
    }
}
