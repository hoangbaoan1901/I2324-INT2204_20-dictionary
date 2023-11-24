package Lib;

import javafx.util.Pair;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class WordCreate {
    public static void addWordToDatabase(Word w) {
        String word = w.getWord();
        HashMap<String, ArrayList<Pair<String, String>>> meanings = w.getMeanings();
        Connection c = SQLiteConnect.getSQLiteConnection("src/main/resources/Databases/Dictionary.db");

    }
}
