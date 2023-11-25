package Lib;

import java.sql.*;
import java.util.Scanner;

import static Lib.SQLiteConnect.getSQLiteConnection;

public class UserDefinedWord implements WordInterface {
    private String word;
    private String meaning;

    public UserDefinedWord() {
        this.word = new String();
        this.meaning = new String();
    }

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

    public static UserDefinedWord getUserDefinedWordFromDatabase(String word) {
        String path = "src/main/resources/Databases/Dictionary.db";
        String definition = "";

        // Check if the word is empty or null
        if (word == null || word.isEmpty()) {
            System.out.println("Word is empty or null.");
            return null;
        }

        try (Connection c = getSQLiteConnection(path);
             Statement statement = c.createStatement()) {

            // Use a prepared statement to avoid SQL injection
            String query = "SELECT * FROM usersWords WHERE word LIKE ?";
            try (PreparedStatement preparedStatement = c.prepareStatement(query)) {
                preparedStatement.setString(1, word);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        definition = resultSet.getString("definition");
                        if (!definition.equals("")) {
                            return new UserDefinedWord(word, definition);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Definition not found for word: " + word);
        return null; // Return null if no definition is found
    }

    public static void removeWordInDatabase(String word) {
        String path = "src/main/resources/Databases/Dictionary.db";

        try (Connection c = getSQLiteConnection(path);
             Statement statement = c.createStatement()) {

            String query = String.format("DELETE FROM usersWords WHERE word LIKE '%s';", word);

            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getKey() {
        return this.word;
    }

    @Override
    public String getContent() {
        return this.meaning;
    }

    public static void main(String[] args) {
        UserDefinedWord w = getUserDefinedWordFromDatabase("Trunk");
        System.out.println(w.getContent());
    }
}
