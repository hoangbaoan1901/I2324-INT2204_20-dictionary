package Lib;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static Lib.SQLiteConnect.getSQLiteConnection;

public class Idiom implements WordInterface {
    private String idiom;
    private String definition;
    private String example;

    public String getIdiom() {
        return idiom;
    }

    public void setIdiom(String idiom) {
        this.idiom = idiom;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public Idiom() {
        this.idiom = "";
        this.definition = "";
        this.example = "";
    }

    public Idiom(String idiom, String definition, String example) {
        this.idiom = idiom;
        this.definition = definition;
        this.example = example;
    }

    public static Idiom getIdiomFromInstalledDatabase(String word) {
        String path = "src/main/resources/Databases/Dictionary.db";
        Idiom result = null;
        try (Connection c = getSQLiteConnection(path);
             Statement statement = c.createStatement()) {
            String query = String.format("SELECT * FROM idioms WHERE idiom LIKE '%s'", word);
            try (ResultSet resultSet = statement.executeQuery(query)) {
                String definition = "";
                String example = "";
                while (resultSet.next()) {
                    definition = resultSet.getString("definition");
                    example = resultSet.getString("example");
                }
                if (!definition.isEmpty()) {
                    result = new Idiom(word, definition, example);
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
    public String getKey() {
        return this.getIdiom();
    }

    @Override
    public String getContent() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Definition:%n"));
        stringBuilder.append(String.format("\t• %s%n", this.getDefinition()));
        stringBuilder.append(String.format("Example:%n"));
        stringBuilder.append(String.format("\t\"%s\"%n", this.getExample()));
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        Idiom i = getIdiomFromInstalledDatabase("a piece of cake");
        System.out.println(i.getContent());
    }
}
