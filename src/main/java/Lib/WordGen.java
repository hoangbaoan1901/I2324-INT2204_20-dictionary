package Lib;

import javafx.util.Pair;
import com.jayway.jsonpath.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class WordGen {
    // JSON format: Every multivalued fields will be stored in an ARRAY, even the JSON itself at first.
    public static Word WordFromJSON(String JSONWord) {
        Word result;
        String input = JSONWord.substring(1, JSONWord.length() - 1); // Remove the square brackets.
        // Get word
        String word = JsonPath.read(input, "$.word");
        // Get phonetic
        ArrayList phonetics = JsonPath.read(input, "$.phonetics.*.text");
        String phonetic = (String) phonetics.get(0);
        // Get meanings
        ArrayList meanings = JsonPath.read(input, "$.meanings.*");
        HashMap<String, ArrayList<Pair<String, String>>> meaningsMap = new HashMap<>();
        for (int i = 0; i < meanings.size(); i++) {
            LinkedHashMap meaningEntries = (LinkedHashMap) meanings.get(i);
            String wordType = (String) meaningEntries.get("partOfSpeech");
            // Definitions are also stored in another array, so we have to traverse it.
            ArrayList defintions = (ArrayList) meaningEntries.get("definitions");
            ArrayList<Pair<String, String>> definitionAndExample = new ArrayList<Pair<String, String>>();
            for (int j = 0; j < defintions.size(); j++) {
                LinkedHashMap definitionEntries = (LinkedHashMap) defintions.get(j);
                String definition = (String) definitionEntries.get("definition");
                String example = (String) definitionEntries.get("example");
                Pair<String, String> definitionAndExampleEntry = new Pair<>(definition, example);
                definitionAndExample.add(definitionAndExampleEntry);
            }
            meaningsMap.put(wordType, definitionAndExample);
        }
        // Get antonyms, synonyms
        ArrayList tsynonyms = JsonPath.read(input, "$.meanings.*.synonyms");
        ArrayList tantonyms = JsonPath.read(input, "$.meanings.*.antonyms");
        ArrayList synonyms = new ArrayList();
        ArrayList antonyms = new ArrayList();
        for (Object arr : tsynonyms) {
            ArrayList tarr = (ArrayList) arr;
            synonyms.addAll(tarr);
        }
        for (Object arr : tantonyms) {
            ArrayList tarr = (ArrayList) arr;
            antonyms.addAll(tarr);
        }
        result = new Word(word, meaningsMap);
        result.setPhonetic(phonetic);
        result.setAntonyms(antonyms);
        result.setSynonyms(synonyms);
        return result;
    }

    public static void main(String[] args) {
        String word = "youth";
        try {
            Pair<Integer,String> request = DictionaryGetAPI.getWord(word);
            if(!request.getKey().equals(200)) {
                throw new Exception("Word not found in API");
            }
            String JSONWord = request.getValue();
            Word w = WordFromJSON(JSONWord);
            System.out.println(w);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
