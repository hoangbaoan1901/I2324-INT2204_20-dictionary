package src.Dictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DictionaryCommandline {
    private Dictionary myDictionary;

    public DictionaryCommandline(Dictionary dictionary) {
        this.myDictionary = dictionary;
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

    public void dictionaryBasic() {
        DictionaryManagement dictionaryManagement = new DictionaryManagement(myDictionary);
        dictionaryManagement.insertFromCommandLine();
        showAllWords();
    }
    public static void main(String[] args){
        Dictionary eng = new Dictionary();
        DictionaryCommandline test = new DictionaryCommandline(eng);
        test.dictionaryBasic();
    }

}
