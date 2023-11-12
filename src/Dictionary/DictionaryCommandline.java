package src.Dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DictionaryCommandline {
    private Dictionary myDictionary;
    private DictionaryManagement dictionaryManagement;

    public DictionaryCommandline(Dictionary myDictionary) {
        this.myDictionary = myDictionary;
        this.dictionaryManagement = new DictionaryManagement(myDictionary);
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
    public static void main(String[] args) {
        Dictionary eng = new Dictionary();
        DictionaryCommandline test = new DictionaryCommandline(eng);
        test.dictionaryBasic();
        test.insertFromFile("dictionaries.txt");
        test.showAllWords();
        test.dictionarySearcher("you");
    }
}