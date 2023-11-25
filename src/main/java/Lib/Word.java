package Lib;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.jayway.jsonpath.JsonPath;
import javafx.util.Pair;

import static Lib.SQLiteConnect.getSQLiteConnection;

public class Word implements WordInterface {
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
        this.synonyms = new ArrayList<>();
        this.antonyms = new ArrayList<>();
        this.phonetic = "";
    }

    /**
     * Create words from fracture elements.
     *
     * @param word       key for object
     * @param type       word type
     * @param definition
     * @param example    the example related to the definition
     */
    public Word(String word, String type, String definition, String example) {
        this.word = word;
        this.meanings = new HashMap<>();
        this.phonetic = "";
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

    public static Word getWordFromAPI(String input_word) {
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
        // This should fix some error.
        String phonetic = "";
        if (phonetics.size() > 0) {
            phonetic = (String) phonetics.get(0);
        }

        // Get meanings
        ArrayList meanings = JsonPath.read(input, "$.meanings.*");
        HashMap<String, ArrayList<Pair<String, String>>> meaningsMap = new HashMap<>();
        for (int i = 0; i < meanings.size(); i++) {
            LinkedHashMap meaningEntries = (LinkedHashMap) meanings.get(i);
            String wordType = (String) meaningEntries.get("partOfSpeech");
            // Definitions are also stored in another array, so we have to traverse it.
            ArrayList definitions = (ArrayList) meaningEntries.get("definitions");
            ArrayList<Pair<String, String>> definitionAndExample = new ArrayList<Pair<String, String>>();
            for (int j = 0; j < definitions.size(); j++) {
                LinkedHashMap definitionEntries = (LinkedHashMap) definitions.get(j);
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

    public static Word getWordFromInstalledDatabase(String word) {
        String path = "src/main/resources/Databases/Dictionary.db";

        try (Connection c = getSQLiteConnection(path);
             Statement statement = c.createStatement()) {

            String query = String.format("SELECT * FROM WORDS WHERE word LIKE '%s';", word);

            try (ResultSet resultSet = statement.executeQuery(query)) {
                HashMap<String, ArrayList<Pair<String, String>>> meanings = new HashMap<>();

                while (resultSet.next()) {
                    String wordType = resultSet.getString("wordtype");
                    String definition = resultSet.getString("definition").replaceAll("[\\t\\n\\r]+", " ");
                    definition = definition.trim().replaceAll(" +", " ");
                    Pair<String, String> p = new Pair<>(definition, null);

                    meanings.computeIfAbsent(wordType, k -> new ArrayList<>()).add(p);
                }

                if (meanings.isEmpty()) {
                    System.out.printf("Can't find definition for %s%n", word);
                    return null;
                }

                return new Word(word, meanings);

            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void testAPI(String word) {
        Word w = getWordFromAPI(word);
        System.out.println(w);
    }

    public static void testDB(String word) {
        Word w = getWordFromInstalledDatabase(word);
        System.out.println(w);
    }

    @Override
    public String getKey() {
        return this.word;
    }

    @Override
    public String getContent() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!this.getPhonetic().isEmpty()) {
            stringBuilder.append(String.format("%s%n", this.getPhonetic()));
        }
        for (Map.Entry<String,
                ArrayList<Pair<String, String>>> entry : this.meanings.entrySet()) {
            stringBuilder.append(String.format("%s%n", entry.getKey())); // print word type
            for (Pair<String, String> p : entry.getValue()) {
                stringBuilder.append(String.format("\t• %s%n", p.getKey())); // print definition
                if (p.getValue() != null) {
                    stringBuilder.append(String.format("\t\tE.g. \"%s\"%n", p.getValue())); // print example if exists
                }
            }
        }
        if (!this.getSynonyms().isEmpty()) {
            stringBuilder.append(String.format("Synonyms:%n"));
            for (String s : this.getSynonyms()) {
                stringBuilder.append(String.format("\t%s%n", s));
            }
        }
        if (!this.getAntonyms().isEmpty()) {
            stringBuilder.append(String.format("Antonyms:%n"));
            for (String s : this.getAntonyms()) {
                stringBuilder.append(String.format("\t%s%n", s));
            }
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        Word w = getWordFromAPI("trunk");
        System.out.println(w.getContent());
    }
}
