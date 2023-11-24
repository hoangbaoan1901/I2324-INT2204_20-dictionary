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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class TranslationController implements Initializable {

    private String sourceLanguage = "en ";
    private String targetLanguage = "vi";
    private boolean isTargetedToVietnameseLanguage = true;

    private static final String TRANSLATE_KEY = "b8b14c82c8mshbbbb520484c1199p16ec37jsn4157e23c25ba";


    @FXML
    private TextArea sourceLanguageField, targetLanguageField;

    @FXML
    private Button translate,volume;

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
            if (sourceLanguageField.getText().trim().isEmpty()) {
                translate.setDisable(true);
            } else {
                translate.setDisable(false);
            }
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
        } else {
            rootAPI = "https://microsoft-translator-text.p.rapidapi.com/translate?api-version=3.0&to%5B0%5D=en&textType=plain&profanityAction=NoAction&from=vi";
            sourceLanguage = "vi";
            targetLanguage = "en";
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
            sourceLanguage = "vie";
            targetLanguage = "eng";
        } else {
            englishLabel.setLayoutX(100);
            vietnameseLabel.setLayoutX(426);
            sourceLanguage = "eng";
            targetLanguage = "vie";
        }
        isTargetedToVietnameseLanguage = !isTargetedToVietnameseLanguage;
    }

    @FXML
    private void handleClickSound() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice[] voices = voiceManager.getVoices();

        if (voices.length > 0) {
            Voice voice = voices[0];
            voice.allocate();
            String textToSpeak;
            if(isTargetedToVietnameseLanguage) {
                textToSpeak = sourceLanguageField.getText();
            } else {
                textToSpeak = targetLanguageField.getText();
            }

            if (!textToSpeak.isEmpty()) {
                voice.speak(textToSpeak);
            } else {
                // Handle the case where there is no text to speak
                System.out.println("No text to speak");
            }

            voice.deallocate();
        } else {
            // Handle the case where no voices are available
            System.out.println("No voices available.");
        }
    }



}