package src.Dictionary;

import java.util.Scanner;

public class DictionaryManagement {
    private Dictionary myDictionary = new Dictionary();

    public DictionaryManagement(Dictionary myDictionary) {
        this.myDictionary = myDictionary;
    }

    public void insertFromCommandLine() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of words to insert: ");
        int numWords = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numWords; i++) {
            System.out.print("Enter a word in English: ");
            String word_target = scanner.nextLine();

            System.out.print("Enter the meaning in Vietnamese: ");
            String word_explain = scanner.nextLine();

            myDictionary.insertWord(word_target, word_explain);
        }
    }
}
