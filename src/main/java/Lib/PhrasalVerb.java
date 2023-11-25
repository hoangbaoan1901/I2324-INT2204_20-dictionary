package Lib;

import javafx.util.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import static Lib.SQLiteConnect.getSQLiteConnection;


public class PhrasalVerb implements WordInterface {

    private String words;
    private ArrayList<String> meanings;
    private ArrayList<String> examples;

    public PhrasalVerb() {
        words = "";
        meanings = new ArrayList<>();
        examples = new ArrayList<>();
    }

    public PhrasalVerb(String words, ArrayList<String> meanings, ArrayList<String> examples) {
        this.words = words;
        this.meanings = meanings;
        this.examples = examples;
    }

    public PhrasalVerb(String words, String meaning, String example) {
        this.words = words;
        this.meanings = new ArrayList<>();
        this.examples = new ArrayList<>();
        this.meanings.add(meaning);
        this.examples.add(example);
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public ArrayList<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(ArrayList<String> meanings) {
        this.meanings = meanings;
    }

    public ArrayList<String> getExamples() {
        return examples;
    }

    public void setExamples(ArrayList<String> examples) {
        this.examples = examples;
    }

    public static ArrayList<String> getPhrasalVerbDefinitionsFromInstalledDatabase(String word) {
        String path = "src/main/resources/Databases/Dictionary.db";
        ArrayList<String> result = null;
        try (Connection c = getSQLiteConnection(path);
             Statement statement = c.createStatement()) {
            String query = String.format("SELECT * FROM phrasalVerbsDefinitions WHERE phrasalVerb LIKE '%s'", word);
            try (ResultSet resultSet = statement.executeQuery(query)) {
                result = new ArrayList<>();
                while (resultSet.next()) {
                    String definition = resultSet.getString("definition");
                    result.add(definition);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<String> getPhrasalVerbExamplesFromInstalledDatabase(String word) {
        String path = "src/main/resources/Databases/Dictionary.db";
        ArrayList<String> result = null;
        try (Connection c = getSQLiteConnection(path);
             Statement statement = c.createStatement()) {
            String query = String.format("SELECT * FROM phrasalVerbsExamples WHERE phrasalVerb LIKE '%s'", word);
            try (ResultSet resultSet = statement.executeQuery(query)) {
                result = new ArrayList<>();
                while (resultSet.next()) {
                    String definition = resultSet.getString("example");
                    result.add(definition);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String toString() {
        return "PhrasalVerb{" +
                "words='" + words + '\'' +
                ", meanings=" + meanings +
                ", examples=" + examples +
                '}';
    }

    public static PhrasalVerb getPhrasalVerbFromInstalledDatabase(String word) {
        ArrayList<String> definitions = getPhrasalVerbDefinitionsFromInstalledDatabase(word);
        if (definitions == null || definitions.isEmpty()) {
            return null;
        } else {
            ArrayList<String> examples = getPhrasalVerbExamplesFromInstalledDatabase(word);
            return new PhrasalVerb(word, definitions, examples);
        }
    }

    @Override
    public String getKey() {
        return String.format("%s", this.getWords());
    }

    public String getContent() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!this.getMeanings().isEmpty()) {
            stringBuilder.append(String.format("Definition(s):%n"));
            for (String s : this.getMeanings()) {
                stringBuilder.append(String.format("\t• %s%n", s));
            }
        }
        if (!this.getExamples().isEmpty()) {
            stringBuilder.append(String.format("Example(s):%n"));
            for (String s : this.getExamples()) {
                stringBuilder.append(String.format("\t• \'%s\'%n", s));
            }
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        PhrasalVerb p = getPhrasalVerbFromInstalledDatabase("jerk off");
        System.out.println(p.getContent());
    }
}
