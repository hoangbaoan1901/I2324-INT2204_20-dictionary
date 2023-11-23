package DictionaryApplication.Controllers;

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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

public class TranslationController implements Initializable {

    private String sourceLanguage = "eng";
    private String toLanguage = "vie";
    private boolean isToVietnameseLang = true;


    @FXML
    private void handleSwitchLanguageToggle(){
        sourceLangField.clear();
        toLangField.clear();
        if(isToVietnameseLang){
            englishLabel.setLayoutX(426);
            vietnameseLabel.setLayoutX(104);
            sourceLanguage = "vie";
            toLanguage = "eng";
        }else {
            englishLabel.setLayoutX(100);
            vietnameseLabel.setLayoutX(426);
            sourceLanguage = "eng";
            toLanguage = "vie";
        }
        isToVietnameseLang = !isToVietnameseLang;
    }

    @FXML
    private TextArea sourceLangField, toLangField;

    @FXML
    private Button translate;

    @FXML
    private Label englishLabel , vietnameseLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}