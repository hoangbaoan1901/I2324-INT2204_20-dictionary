package Lib;

import java.sql.Connection;
import java.sql.Statement;

public class UserDefinedWord {
    private String word;
    private String meaning;

    public UserDefinedWord(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public static void addWordToDatabase(UserDefinedWord word) {
        String key = word.getWord();
        String definition = word.getMeaning();
        Connection c = SQLiteConnect.getSQLiteConnection("src/main/resources/Databases/Dictionary.db");
        Statement statement = null;
        try {
            statement = c.createStatement();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
