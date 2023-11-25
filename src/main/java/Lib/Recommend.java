package Lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import static Lib.SQLiteConnect.getSQLiteConnection;

public class Recommend {

    /**
     * First, attempt to get 5 words that match in the database.
     * But then we will not be searching it in the database first, instead, we'll be looking for those in the API.
     * If we cannot find enough 5 words, we will be searching for it in the database.
     * If it is, then we'll search in the phrasal verb and idioms table.
     *
     * @param input
     * @return an array list of word that you want to recommend to the user
     */
    public static ArrayList<WordInterface> getRecommendation(String input) {
        String word = input.toLowerCase().trim().replaceAll(" +", " ");
        String path = "src/main/resources/Databases/Dictionary.db";
        HashSet<String> taken = new HashSet<>(); // for seeing if we've taken those words.
        ArrayList<WordInterface> result = new ArrayList<>();
        String query = "SELECT idiom FROM idioms WHERE idiom LIKE ?";
        try (Connection connection = getSQLiteConnection(path)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + word + "%");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String resultEntry = resultSet.getString("idiom");
                    if (!taken.contains(resultEntry)) {
                        result.add(Idiom.getIdiomFromInstalledDatabase(resultEntry));
                        taken.add(resultEntry);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query = "SELECT phrasalVerb FROM phrasalVerbsDefinitions WHERE phrasalVerb LIKE ?";
        try (Connection connection = getSQLiteConnection(path)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + word + "%");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String resultEntry = resultSet.getString("phrasalVerb");
                    if (!taken.contains(resultEntry)) {
                        result.add(
                                PhrasalVerb.getPhrasalVerbFromInstalledDatabase(
                                        resultSet.getString("phrasalVerb")));
                        taken.add(resultEntry);
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query = "SELECT word FROM words WHERE word LIKE ?";
        try (Connection connection = getSQLiteConnection(path)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + word + "%");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String resultEntry = resultSet.getString("word");
                    if (!taken.contains(resultEntry)) {
                        System.out.println(resultEntry);
                        Integer responseCode = 0;
                        if (!resultEntry.contains(" ")) {
                            responseCode = DictionaryGetAPI.getWord(resultEntry).getKey();
                        }
                        if (responseCode.equals(200)) {
                            Word w = Word.getWordFromAPI(resultEntry);
                            result.add(w);
                        } else {
                            result.add(
                                    Word.getWordFromInstalledDatabase(
                                            resultSet.getString("word")));
                        }
                        taken.add(resultEntry);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        query = "SELECT word FROM usersWords WHERE word LIKE ?";
        try (Connection connection = getSQLiteConnection(path)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + word + "%");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String resultEntry = resultSet.getString("word");
                    result.add(UserDefinedWord.getUserDefinedWordFromDatabase(resultEntry));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        ArrayList<WordInterface> p = getRecommendation("trunk");
        for (WordInterface w : p) {
            if (w != null) {
                System.out.println(w.getKey());
                System.out.println(w.getContent());
            }

        }
    }
}
