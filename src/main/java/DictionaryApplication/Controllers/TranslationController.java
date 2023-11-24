package DictionaryApplication.Controllers;

import DictionaryApplication.DictionaryCommandLine.Word;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;


public class TranslationController implements Initializable {

    private String sourceLanguage = "en";
    private String targetLanguage = "vi";
    private String sourceSpeechLanguage = "en-us";
    private String targetSpeechLanguage = "vi-vn";
    private boolean isTargetedToVietnameseLanguage = true;

    private static final String TRANSLATE_KEY = "b8b14c82c8mshbbbb520484c1199p16ec37jsn4157e23c25ba";


    @FXML
    private TextArea sourceLanguageField, targetLanguageField;

    @FXML
    private Button translate, volume;

    @FXML
    private Label englishLabel, vietnameseLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        translate.setOnAction(event -> {
            try {
                handleOnClickTranslate();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        sourceLanguageField.setOnKeyTyped(keyEvent -> {
            translate.setDisable(sourceLanguageField.getText().trim().isEmpty());
        });

        // Initial state
        translate.setDisable(true);
        targetLanguageField.setEditable(false);
    }

    @FXML
    private void handleOnClickTranslate() throws IOException, InterruptedException {
        String rootAPI;
        if (isTargetedToVietnameseLanguage) {
            rootAPI = "https://microsoft-translator-text.p.rapidapi.com/translate?api-version=3.0&to%5B0%5D=vi&textType=plain&profanityAction=NoAction&from=en";
            sourceLanguage = "en";
            targetLanguage = "vi";
            sourceSpeechLanguage = "en-us";
            targetSpeechLanguage = "vi-vn";
        } else {
            rootAPI = "https://microsoft-translator-text.p.rapidapi.com/translate?api-version=3.0&to%5B0%5D=en&textType=plain&profanityAction=NoAction&from=vi";
            sourceLanguage = "vi";
            targetLanguage = "en";
            sourceSpeechLanguage = "vi-vn";
            targetSpeechLanguage = "en-us";
        }

        String srcText = sourceLanguageField.getText();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(rootAPI))
                .header("content-type", "application/json")
                .header("X-RapidAPI-Key", TRANSLATE_KEY)
                .header("X-RapidAPI-Host", "microsoft-translator-text.p.rapidapi.com")
                .POST(HttpRequest.BodyPublishers.ofString("[\r\n    {\r\n        \"Text\": \"" + srcText + "\"\r\n    }\r\n]"))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        String result = response.body();
        JSONArray jsonArray = new JSONArray(result);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        JSONArray translationsArray = jsonObject.getJSONArray("translations");
        JSONObject translationsObject = translationsArray.getJSONObject(0);
        String trans = translationsObject.getString("text");

        targetLanguageField.setText(trans);
    }

    @FXML
    private void handleSwitchLanguageToggle() {
        sourceLanguageField.clear();
        targetLanguageField.clear();
        if (isTargetedToVietnameseLanguage) {
            englishLabel.setLayoutX(426);
            vietnameseLabel.setLayoutX(104);
            sourceLanguage = "vi";
            targetLanguage = "en";
            sourceSpeechLanguage = "vi-vn";
            targetSpeechLanguage = "en-us";
        } else {
            englishLabel.setLayoutX(100);
            vietnameseLabel.setLayoutX(426);
            sourceLanguage = "en";
            targetLanguage = "vi";
            sourceSpeechLanguage = "en-us";
            targetSpeechLanguage = "vi-vn";

        }
        isTargetedToVietnameseLanguage = !isTargetedToVietnameseLanguage;
    }

    @FXML
    private void handleClickSoundSourceLanguage() {

        String language;
        String textToSpeak;
        textToSpeak = sourceLanguageField.getText();
        if (!textToSpeak.isEmpty()) {
            speech(textToSpeak, sourceSpeechLanguage, 50);
        } else {
            // Handle the case where there is no text to speak
            System.out.println("No text to speak");
        }
    }

    @FXML
    private void handleClickSoundTargetLanguage() {

        String language;
        String textToSpeak;
        textToSpeak = targetLanguageField.getText();
        if (!textToSpeak.isEmpty()) {
            speech(textToSpeak, targetSpeechLanguage, 50);
        } else {
            // Handle the case where there is no text to speak
            System.out.println("No text to speak");
        }
    }

    public static void speech(String text, String language, int speechRate) {
        speechRate = convertSpeedRate(50); // The speed of the speech in the range 0 to 100
        String rapidKey = "a3c9ad3dbemshf0d0293637a2c13p1ec157jsn471a26834d72"; // Your API rapidKey
        String voiceRSSKey = "3a396a04478e45e9ba3d47021c359be4";
        String audioFormat = "wav"; // Audio format of the returned audio "mp3" or "wav
        String audioFormatCode = "8khz_8bit_stereo"; // Audio format code of the returned audio
        String encodedText;
        encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://voicerss-text-to-speech.p.rapidapi.com/?key=" + voiceRSSKey))
                .header("content-type", "application/x-www-form-urlencoded")
                .header("X-RapidAPI-Key", rapidKey)
                .header("X-RapidAPI-Host", "voicerss-text-to-speech.p.rapidapi.com")
                .method("POST", HttpRequest.BodyPublishers.ofString("f=" + audioFormatCode
                        + "&c=" + audioFormat
                        + "&r=" + speechRate
                        + "&hl=" + language
                        + "&src=" + encodedText))
                .build();
        HttpResponse<byte[]> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofByteArray());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            Path tempFile = Files.createTempFile("audio", ".wav");
            Files.write(tempFile, response.body(), StandardOpenOption.CREATE);
            Media hit = new Media(tempFile.toUri().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int convertSpeedRate(int oldSpeedRate) {
        int oldMin = 0;
        int oldMax = 100;
        int newMin = -10;
        int newMax = 10;
        return (oldSpeedRate - oldMin) * (newMax - newMin) / (oldMax - oldMin) + newMin;
    }
}