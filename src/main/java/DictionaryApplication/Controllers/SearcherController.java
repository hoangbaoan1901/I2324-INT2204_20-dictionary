package DictionaryApplication.Controllers;

import DictionaryApplication.Alerts.Alerts;
import DictionaryApplication.DictionaryCommandLine.Dictionary;
import DictionaryApplication.DictionaryCommandLine.DictionaryManagement;
import DictionaryApplication.DictionaryCommandLine.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.net.URL;
import java.util.*;

public class SearcherController implements Initializable {
    private Dictionary myDictionary = new Dictionary();
    private DictionaryManagement dictionaryManagement = new DictionaryManagement();
    private final String path = "src/main/resources/Utils/dictionary.txt";
    ObservableList<String> list = FXCollections.observableArrayList();
    private Alerts alerts = new Alerts();
    private int indexOfSelectedWord;
    private int firstIndexOfListFound = 0;
    @FXML
    private TextField searchField;

    @FXML
    private Button cancel, save, volume;

    @FXML
    private Label englishWord, notAvailableAlert;

    @FXML
    private TextArea meaningArea;

    @FXML
    private ListView<String> resultsListView;

    @FXML
    private Pane explanationHeaderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization of the dictionary
        dictionaryManagement.insertFromFile(myDictionary, path);
        dictionaryManagement.setTrie(myDictionary);
        setDefaultSearchResultsList();
        cancel.setVisible(false);

        // Search field event handler
        searchField.setOnKeyTyped(keyEvent -> {
            if (searchField.getText().isEmpty()) {
                cancel.setVisible(false);
                setDefaultSearchResultsList();
            } else {
                cancel.setVisible(true);
                handleOnKeyTyped();
            }
        });

        // Cancel button event handler
        cancel.setOnAction(event -> {
            searchField.clear();
            notAvailableAlert.setVisible(false);
            cancel.setVisible(false);
            setDefaultSearchResultsList();
        });

        // Configuration of meaning area and related components
        configureMeaningArea();
    }

    private void configureMeaningArea() {
        meaningArea.setEditable(false);
        save.setVisible(false);
        cancel.setVisible(false);
        notAvailableAlert.setVisible(false);
    }

    @FXML
    private void handleClickSound() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice[] voices = voiceManager.getVoices();

        if (voices.length > 0) {
            Voice voice = voices[0];
            voice.allocate();

            for (Word word : myDictionary.Words) {
                if (isWordSelected(word)) {
                    voice.speak(word.getWord_target());
                    return;
                }
            }

            throw new IllegalStateException("Cannot find selected word in the dictionary.");
        } else {
            throw new IllegalStateException("No voices available.");
        }
    }

    private boolean isWordSelected(Word word) {
        return word.getWord_target().equals(englishWord.getText());
    }

    @FXML
    private void handleOnKeyTyped() {
        clearListAndSearchKey();
        String searchKey = getTrimmedSearchKey();
        updateResultList(searchKey);
        updateResultsViewAndAlert();
    }

    private void clearListAndSearchKey() {
        list.clear();
    }

    private String getTrimmedSearchKey() {
        return searchField.getText().trim();
    }

    private void updateResultList(String searchKey) {
        List<String> resultList = dictionaryManagement.dictionaryLookup(myDictionary, searchKey);
        list = FXCollections.observableArrayList(resultList);
    }

    private void updateResultsViewAndAlert() {
        if (list.isEmpty()) {
            notAvailableAlert.setVisible(true);
            setDefaultSearchResultsList();
        } else {
            notAvailableAlert.setVisible(false);
            updateResultsListView();
            updateFirstIndexOfListFound();
        }
    }

    private void updateResultsListView() {
        resultsListView.setItems(FXCollections.observableArrayList(list));
    }

    private void updateFirstIndexOfListFound() {
        if (!list.isEmpty()) {
            firstIndexOfListFound = dictionaryManagement.searchWord(myDictionary.Words, list.get(0));
        }
    }

    @FXML
    private void handleMouseClickAWord(MouseEvent arg0) {
        String selectedWord = resultsListView.getSelectionModel().getSelectedItem();
        if (selectedWord != null) {
            for (Word word : myDictionary.Words) {
                if (word.getWord_target().equals(selectedWord)) {
                    indexOfSelectedWord = dictionaryManagement.searchWord(myDictionary.Words, word.getWord_target());
                    englishWord.setText(word.getWord_target());
                    meaningArea.setText(word.getWord_explain());
                    explanationHeaderPane.setVisible(true);
                    meaningArea.setVisible(true);
                    meaningArea.setEditable(false);
                    save.setVisible(false);
                    break;
                }
            }
        }
    }

    @FXML
    private void handleClickEdit() {
        // update status
        meaningArea.setEditable(true);
        save.setVisible(true);
        alerts.showAlertInfo("Information", "Bạn đã cho phép chỉnh sửa nghĩa từ này!");
    }

    @FXML
    private void handleClickSave() {
        Alert alertConfirmation = alerts.alertConfirmation("Update", "Bạn chắc chắn muốn cập nhật nghĩa từ này ?");
        // option != null.
        Optional<ButtonType> option = alertConfirmation.showAndWait();
        if (option.get() == ButtonType.OK) {
            String newMeaning = meaningArea.getText();
            String wordToUpdate = englishWord.getText();

            // Remove the old word from the list
            list.remove(wordToUpdate);

            // Update the meaning of the selected word
            dictionaryManagement.updateWord(myDictionary, wordToUpdate, newMeaning, path);

            // Add the updated word back to the list
            list.add(wordToUpdate);

            // successfully
            alerts.showAlertInfo("Information", "Cập nhập thành công!");
        } else {
            alerts.showAlertInfo("Information", "Thay đổi không được công nhận!");
        }
        // update status
        save.setVisible(false);
        meaningArea.setEditable(false);
    }

    @FXML
    private void handleClickDelete() {
        Alert alertWarning = alerts.alertWarning("Delete", "Bạn chắc chắn muốn xóa từ này?");
        alertWarning.getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> option = alertWarning.showAndWait();
        if (option.get() == ButtonType.OK) {
            String wordToDelete = englishWord.getText();
            dictionaryManagement.deleteWord(myDictionary, wordToDelete, path);
            refreshAfterDeleting();
            alerts.showAlertInfo("Information", "Xóa thành công");
        } else {
            alerts.showAlertInfo("Information", "Thay đổi không được công nhận");
        }
    }


    private void refreshAfterDeleting() {
        list.remove(englishWord.getText());
        resultsListView.setItems(list);

        explanationHeaderPane.setVisible(false);
        meaningArea.setVisible(false);
    }

    private void setDefaultSearchResultsList() {
        Iterator<Word> iterator = myDictionary.Words.iterator();
        ArrayList<Word> wordList = new ArrayList<>(myDictionary.Words);

        list.clear();

        int i = 0;
        while (iterator.hasNext() && i < firstIndexOfListFound + 20) {
            Word currentWord = iterator.next();
            if (i >= firstIndexOfListFound) {
                list.add(currentWord.getWord_target());
            }
            i++;
        }

        resultsListView.setItems(list);

        if (!list.isEmpty()) {
            englishWord.setText(list.get(0));
        }

        if (firstIndexOfListFound < wordList.size()) {
            meaningArea.setText(wordList.get(firstIndexOfListFound).getWord_explain());
        }
    }

}