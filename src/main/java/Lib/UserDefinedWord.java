package Lib;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static Lib.SQLiteConnect.getSQLiteConnection;

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

    public static boolean isInDatabase(UserDefinedWord word) {
        String key = word.getWord();
        String path = "src/main/resources/Databases/Dictionary.db";

        try (Connection c = getSQLiteConnection(path);
             Statement statement = c.createStatement()) {

            String query = String.format("SELECT * FROM usersWords WHERE word LIKE '%s';", key);

            try (ResultSet resultSet = statement.executeQuery(query)) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void addWordToDatabase(UserDefinedWord word) {
        String key = word.getWord();
        String definition = word.getMeaning();

        if (isInDatabase(word)) {
            alterWordInDatabase(key, definition);
            return;
        }

        String path = "src/main/resources/Databases/Dictionary.db";

        try (Connection c = getSQLiteConnection(path);
             Statement statement = c.createStatement()) {

            String query = String.format("INSERT INTO usersWords (word, definition) VALUES ('%s', '%s');", key, definition);

            statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void alterWordInDatabase(String word, String definition) {
        String path = "src/main/resources/Databases/Dictionary.db";

        try (Connection c = getSQLiteConnection(path);
             Statement statement = c.createStatement()) {

            String query = String.format("UPDATE usersWords SET word = '%s', definition = '%s' WHERE word LIKE '%s';", word, definition, word);

            statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        UserDefinedWord w = new UserDefinedWord(
                "bomba", "an expression when Gragas players kaboom someone hihihihaw");
        addWordToDatabase(w);
    }
}
