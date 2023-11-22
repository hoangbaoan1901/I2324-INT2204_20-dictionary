package DictionaryApplication.DictionaryCommandLine;

import java.util.ArrayList;
import java.util.List;

public class DictionaryCommandline {
    public void showAllWords(Dictionary dictionary) {
        try {
            System.out.println("No" + "\t |" + "English" + "\t |" + "Vietnamese");
            int index = 1;
            for (Word word : dictionary.Words) {
                System.out.println(index + "\t |" + word.getWord_target() + "\t\t |" + word.getWord_explain());
                index++;
            }
        } catch (NullPointerException e) {
            System.out.println("NullPointerException: " + e);
        }
    }

    public DictionaryManagement dictionaryBasic(){
        return new DictionaryManagement();
    }
}