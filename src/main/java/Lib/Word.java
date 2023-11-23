package Lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javafx.util.Pair;

public class Word {
    private String word; // The word itself
    private String phonetic;
    private HashMap<String,
            ArrayList<Pair<String, String>>> meanings; // Key: word type, Value: a pair of meaning & example.

    private ArrayList<String> synonyms;
    private ArrayList<String> antonyms;


    public Word() {
        this.word = "";
        this.phonetic = "";
        this.meanings = new HashMap<>();
        this.synonyms = new ArrayList<>();
        this.antonyms = new ArrayList<>();

    }

    /**
     * Create Word from given data structures.
     *
     * @param word
     * @param meanings
     */
    public Word(String word, HashMap<String, ArrayList<Pair<String, String>>> meanings) {
        this.word = word;
        this.meanings = meanings;
    }

    /**
     * Create words from fracture elements.
     *
     * @param word key for object
     * @param type word type
     * @param definition
     * @param example the example realted to the definition
     */
    public Word(String word, String type, String definition, String example) {
        this.word = word;
        this.meanings = new HashMap<>();
        ArrayList<Pair<String, String>> meaningEntry = new ArrayList<>();
        Pair<String, String> p = new Pair<>(definition, example);
        meaningEntry.add(p);
        this.meanings.put(type, meaningEntry);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public ArrayList<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(ArrayList<String> synonyms) {
        this.synonyms = synonyms;
    }

    public ArrayList<String> getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(ArrayList<String> antonyms) {
        this.antonyms = antonyms;
    }

    public HashMap<String, ArrayList<Pair<String, String>>> getMeanings() {
        return meanings;
    }

    public void setMeanings(HashMap<String, ArrayList<Pair<String, String>>> meanings) {
        this.meanings = meanings;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equals(word, word1.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }


    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", phonetic='" + phonetic + '\'' +
                ", meanings=" + meanings +
                ", synonyms=" + synonyms +
                ", antonyms=" + antonyms +
                '}';
    }
}
