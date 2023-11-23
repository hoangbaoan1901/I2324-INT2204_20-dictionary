package Lib;

import javafx.util.Pair;
import com.jayway.jsonpath.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import static Lib.SQLiteConnect.getSQLiteConnection;

public class WordGet {
    // JSON format: Every multivalued fields will be stored in an ARRAY, even the JSON itself at first.
    public static Word WordFromAPI(String input_word) {
        Pair<Integer, String> request = new Pair<>(null, null);
        try {
            request = DictionaryGetAPI.getWord(input_word);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String JSONWord = request.getValue();
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

    public static Word WordFromInstalledDatabase(String word) {
        String path = "src/main/resources/Databases/Dictionary.db";
        Connection c = getSQLiteConnection(path);
        String query = String.format("SELECT * FROM WORDS WHERE word LIKE '%s'", word);
        HashMap<String, ArrayList<Pair<String, String>>> meanings = new HashMap<>();
        Statement statement = null;
        try {
            statement = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                if (!resultSet.next()) {
                    break;
                } else {
                    String wordType = resultSet.getString("wordtype");
                    String definition = resultSet.getString("definition");
                    definition.replaceAll("[\\t\\n\\r]+", " ");
                    Pair<String, String> p = new Pair<>(definition, null);
                    if (meanings.containsKey(wordType)) {
                        meanings.get(wordType).add(p);
                    } else {
                        ArrayList<Pair<String, String>> arr = new ArrayList<>();
                        arr.add(p);
                        meanings.put(wordType, arr);
                    }
                    System.out.println(String.format(
                            "word: %s%npos: %s%ndefinition: %s%n", word, wordType, definition));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return new Word(word, meanings);
    }

    public static void getPhrasalVerb(String phrasalVerb) {
        String phrasalVerbFile = null;
        try {
            phrasalVerbFile = new String(
                    Files.readAllBytes(Paths.get("src/main/resources/Databases/phrasal_verbs.json")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Objects definition = JsonPath.read(phrasalVerbFile, "$.phrasalVerb");
        System.out.println(definition);
    }

    public static void testAPI(String word) {
        Word w = WordFromAPI(word);
        System.out.println(w);
    }

    public static void testDB(String word) {
        Word w = WordFromInstalledDatabase(word);
        System.out.println(w);
    }

    public static void main(String[] args) {
        getPhrasalVerb("zone in");
    }
}
