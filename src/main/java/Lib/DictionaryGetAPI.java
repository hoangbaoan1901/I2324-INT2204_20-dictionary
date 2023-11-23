package Lib;

import javafx.util.Pair;

import java.io.EOFException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DictionaryGetAPI {

    /**
     * Return a JSON formatted String of FreeDictionaryAPI.
     *
     * @param word word to parse in
     * @throws Exception for debugging
     * @return A pair with key of HTTP response code, value is JSON string.
     */
    public static Pair<Integer, String> getWord(String word) throws Exception {
        HttpRequest getRequest = HttpRequest.newBuilder().
                uri(new URI("https://api.dictionaryapi.dev/api/v2/entries/en/" + word.toLowerCase())).
                header("Authorization", "")
                .GET().build(); // Request declare
        HttpClient httpClient = HttpClient.newHttpClient(); // Object to make request
        HttpResponse<String> getResponse = httpClient.send(getRequest,
                HttpResponse.BodyHandlers.ofString()); // Send the request
        int responseCode = getResponse.statusCode(); // Status code
        return new Pair<Integer,String>(responseCode, getResponse.body());
    }

    public static void main(String[] args) {
        String JSONword = "";
        Integer responseCode = 0;
        try {
            /*
            If you look at the output demonstrated in "API_Demo.txt",
            the JSON string returned will be contained in square brackets "[]".
            So if you wanted to use GSON on it, just remove them.
             */
            Pair<Integer, String> response = getWord("mate");
            JSONword = response.getValue();
            responseCode = response.getKey();

            System.out.println(String.format(
                    "Response code: %d%nJSON response: %s", responseCode, JSONword));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
