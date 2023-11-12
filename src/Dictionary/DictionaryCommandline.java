package src.Dictionary;

import java.io.*;
import java.util.*;

public class DictionaryCommandline {
    private Dictionary myDictionary;

    public DictionaryCommandline(Dictionary myDictionary) {
        this.myDictionary = myDictionary;
    }
    public void showAllWords() {
        List<Word> wordsList = new ArrayList<>(myDictionary.Words);
        wordsList.sort((word1, word2) -> word1.getWord_target().compareToIgnoreCase(word2.getWord_target()));

        System.out.println("No | English | Vietnamese");
        for (int i = 0; i < wordsList.size(); i++) {
            Word word = wordsList.get(i);
            System.out.printf("%d | %s | %s%n", i + 1, word.getWord_target(), word.getWord_explain());
        }
    }

    public void insertFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String word_target = parts[0];
                    String word_explain = parts[1];
                    myDictionary.insertWord(word_target, word_explain);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file! " + e.getMessage());
        }
    }

    public void dictionaryBasic() {
        DictionaryManagement dictionaryManagement = new DictionaryManagement(myDictionary);
        dictionaryManagement.insertFromCommandLine();
        showAllWords();
    }

    /**
     * This method is used to display a set of words. Each word is displayed in both English and Vietnamese.
     *
     * @param words This is a HashSet of Word objects that needs to be displayed.
     */
    private void displayWords(HashSet<Word> words) {
        words.forEach(word -> {
            System.out.println("English: " + word.getWord_target() + ", Vietnamese: " + word.getWord_explain());
        });
    }

    public void dictionarySearcher(String prefix) {
        HashSet<Word> searchResults = myDictionary.searchWords(prefix);

        if (searchResults.isEmpty()) {
            System.out.println("No words found with the prefix: " + prefix);
        } else {
            System.out.println("Words starting with '" + prefix + "':");
            displayWords(searchResults);
        }
    }
    public static void printMenu() {
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

    public void insertFromCommandLine() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the number of words to insert: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();
            }

            int numWords = scanner.nextInt();

            if (numWords < 0) {
                System.out.println("Number of words cannot be negative. Exiting...");
                return;
            }

            scanner.nextLine();

            for (int i = 0; i < numWords; i++) {
                System.out.print("Enter a word in English: ");
                String word_target = scanner.nextLine();

                System.out.print("Enter the meaning in Vietnamese: ");
                String word_explain = scanner.nextLine();

                myDictionary.insertWord(word_target, word_explain);
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void insertWordFromCommandLine() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a word in English: ");
            String word_target = scanner.nextLine();

            System.out.print("Enter the meaning in Vietnamese: ");
            String word_explain = scanner.nextLine();

            myDictionary.insertWord(word_target, word_explain);
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateWordFromCommandLine() {
        try (Scanner scanner = new Scanner(System.in)) {
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

        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteWordFromCommandLine() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a word in English to delete: ");
            String word_target = scanner.nextLine();

            System.out.print("Enter the meaning in Vietnamese: ");
            String word_explain = scanner.nextLine();

            System.out.print("Enter the word type (optional): ");
            String word_type = scanner.nextLine();

            Word target_word = new Word(word_target, word_explain, word_type);

            Iterator<Word> iterator = myDictionary.Words.iterator();
            boolean found = false;

            while (iterator.hasNext()) {
                Word w = iterator.next();
                if (target_word.equals(w)) {
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

        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void dictionaryLookup() {
        try (Scanner scanner = new Scanner(System.in)) {
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

        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void dictionaryExportToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Word word : myDictionary.Words) {
                writer.write(word.getWord_target() + "\t" + word.getWord_explain());
                if (word.getWord_type() != null && !word.getWord_type().isEmpty()) {
                    writer.write("\t" + word.getWord_type());
                }
                writer.newLine();
            }
            System.out.println("Dictionary exported to file successfully.");

        } catch (IOException e) {
            System.out.println("Error exporting dictionary to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
//    public static void main(String[] args) {
//        Dictionary eng = new Dictionary();
//        DictionaryCommandline test = new DictionaryCommandline(eng);
//        test.dictionaryBasic();
//        test.insertFromFile("dictionaries.txt");
//        test.showAllWords();
//        test.dictionarySearcher("you");
//    }
}