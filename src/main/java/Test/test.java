package Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class test {

    private List<String> wordsFoundInAPI = new ArrayList<>();
    private List<String> wordsNotFoundInAPI = new ArrayList<>();
    private HttpClient httpClient = HttpClient.newHttpClient();

    public void testWordsInAPI() {
        System.out.println("Starting the test...");

        try (InputStream inputStream = getClass().getResourceAsStream("/Utils/english.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            if (inputStream == null) {
                System.out.println("Resource file not found");
                return;
            }

            String word;
            while ((word = reader.readLine()) != null) {
                word = word.trim();
                if (!word.contains(" ")) {
                    testWord(word);
                    Thread.sleep(10);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception while setting up the test or reading the file.");
        }

        System.out.println("Words found in API: " + wordsFoundInAPI);
        System.out.println("Words not found in API: " + wordsNotFoundInAPI);
    }

    private void testWord(String word) {
        try {
            String encodedWord = URLEncoder.encode(word, StandardCharsets.UTF_8.toString());
            URI uri = new URI("https://api.dictionaryapi.dev/api/v2/entries/en/" + encodedWord);
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Found: " + word);
                wordsFoundInAPI.add(word);
            } else {
                System.out.println("Not found: " + word);
                wordsNotFoundInAPI.add(word);
            }
        } catch (Exception e) {
            System.out.println("Error during API call for word: " + word + " - " + e.getMessage());
            wordsNotFoundInAPI.add(word);
        }
    }

    public static void main(String[] args) {
        new test().testWordsInAPI();
    }
}
