package Dictionary;

import java.util.HashSet;

public class Dictionary {
    public HashSet<Word> Words;

    public Dictionary() {
        Words = new HashSet<>();
    }

    public void insertWord(String word_target, String word_explain) {
        Word newWord = new Word(word_target, word_explain);
        this.Words.add(newWord);
    }

    public void insertWord(String word_target, String word_explain, String word_type) {
        Word newWord = new Word(word_target, word_explain, word_type);
        this.Words.add(newWord);
    }

    public void setWordType(String word_target, String word_explain, String word_type) {
        boolean found = false;
        Word target_word = new Word(word_target, word_explain);
        for (Word w : Words) {
            if (target_word.equals(w)) {
                target_word = w;
                found = true;
                break;
            }
        }
        if (found) {
            target_word.setWord_type(word_type);
            return;
        } else {
            System.out.println("Cannot define word type, word not found in dictionary.");
        }
    }

    public void removeWord(String word_target, String word_explain, String word_type) {
        Word target_word = new Word(word_target, word_explain, word_type);
        for (Word w : Words) {
            if (target_word.equals(w)) {
                Words.remove(target_word);
            }
        }
    }

    public void removeWord(String word_target, String word_explain) {
        Word target_word = new Word(word_target, word_explain);
        for (Word w : Words) {
            if (target_word.equals(w)) {
                Words.remove(target_word);
            }
        }
    }

    public HashSet<Word> searchWords(String prefix) {
        HashSet<Word> result = new HashSet<>();

        for (Word word : Words) {
            if (word.getWord_target().startsWith(prefix)) {
                result.add(word);
            }
        }

        return result;
    }
}
