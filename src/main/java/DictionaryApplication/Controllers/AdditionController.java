package DictionaryApplication.Controllers;


import DictionaryApplication.Alerts.Alerts;
import DictionaryApplication.DictionaryCommandLine.Dictionary;
import DictionaryApplication.DictionaryCommandLine.DictionaryManagement;
import DictionaryApplication.DictionaryCommandLine.Word;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.ButtonType.CANCEL;

public class AdditionController {
    private Dictionary myDictionary = new Dictionary();
    private DictionaryManagement dictionaryManagement = new DictionaryManagement();

    private final String path = "src/main/resources/Utils/dictionary.txt";
    private Alerts alerts = new Alerts();

    @FXML
    private Button add;
    @FXML
    private TextField wordTargetInput;
    @FXML
    private TextArea meaningAreaTextArea;
    @FXML
    private Label successAlert;

    @FXML
    public void initialize() {
        dictionaryManagement.insertFromFile(myDictionary, path);
        if (meaningAreaTextArea.getText().isEmpty() || wordTargetInput.getText().isEmpty()) {
            add.setDisable(true);
        }
        wordTargetInput.setOnKeyTyped(this::handleInputText);
        meaningAreaTextArea.setOnKeyTyped(this::handleInputText);

        successAlert.setVisible(false);
    }

    @FXML
    private void handleAddButtonClick() {
        Alert alertConfirmation = alerts.alertConfirmation("Add word",
                "Bạn chắc chắn muốn thêm từ này?");
        Optional<ButtonType> option = alertConfirmation.showAndWait();

        String englishWord = wordTargetInput.getText().trim();
        String meaning = meaningAreaTextArea.getText().trim();

        option.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                Word word = new Word(englishWord, meaning);

                if (myDictionary.Words.contains(word)) {
                    handleExistingWord(word);
                } else {
                    handleNewWord(word);
                }

                add.setDisable(true);
                resetInput();
            } else if (buttonType == CANCEL) {
                alerts.showAlertInfo("Information", "Thay đổi không được công nhận.");
            }
        });
    }

    private void handleExistingWord(Word word) {
        int indexOfWord = dictionaryManagement.searchWord(myDictionary.Words, word.getWord_target());
        Alert selectionAlert = alerts.alertConfirmation("This word already exists",
                "Từ này đã tồn tại.\n" +
                        "Thay thế hoặc bổ sung nghĩa vừa nhập cho nghĩa cũ.");

        ButtonType replace = new ButtonType("Thay thế");
        ButtonType insert = new ButtonType("Bổ sung");
        selectionAlert.getButtonTypes().setAll(replace, insert, CANCEL);
        Optional<ButtonType> selection = selectionAlert.showAndWait();

        if (selection.isPresent()) {
            ButtonType buttonType = selection.get();
            if (buttonType.equals(replace)) {
                myDictionary.Words.removeIf(w -> w.equals(word));
                myDictionary.Words.add(word);
                dictionaryManagement.exportToFile(myDictionary, path);
                showSuccessAlert();
            } else if (buttonType.equals(insert)) {
                Word existingWord = myDictionary.Words.stream()
                        .filter(w -> w.equals(word))
                        .findFirst()
                        .orElse(null);

                if (existingWord != null) {
                    String oldMeaning = existingWord.getWord_explain();
                    existingWord.setWord_explain(oldMeaning + "\n=" + word.getWord_explain());
                    dictionaryManagement.exportToFile(myDictionary, path);
                    showSuccessAlert();
                }
            } else if (buttonType.equals(CANCEL)) {
                alerts.showAlertInfo("Information", "Thay đổi không được công nhận.");
            }
        }
    }

    private void handleNewWord(Word word) {
        myDictionary.Words.add(word);
        dictionaryManagement.insertWord(word, path);
        showSuccessAlert();
    }

    private void resetInput() {
        wordTargetInput.setText("");
        meaningAreaTextArea.setText("");
    }

    private void showSuccessAlert() {
        successAlert.setVisible(true);
        dictionaryManagement.setTimeout(() -> successAlert.setVisible(false), 1500);
    }

    private void handleInputText(KeyEvent event) {
        if (meaningAreaTextArea.getText().isEmpty() || wordTargetInput.getText().isEmpty()) {
            add.setDisable(true);
        } else {
            add.setDisable(false);
        }
    }
}