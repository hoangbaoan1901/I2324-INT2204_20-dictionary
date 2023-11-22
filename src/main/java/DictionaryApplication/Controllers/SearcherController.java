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
    private Label englishWord, resultListHeader, notAvailableAlert;

    @FXML
    private TextArea meaningArea;

    @FXML
    private ListView<String> resultsListView;

    @FXML
    private Pane explanationHeaderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dictionaryManagement.insertFromFile(myDictionary, path);
        dictionaryManagement.setTrie(myDictionary);
        setDefaultSearchResultsList();
        cancel.setVisible(false);

        searchField.setOnKeyTyped(keyEvent -> {
            if (searchField.getText().isEmpty()) {
                cancel.setVisible(false);
                setDefaultSearchResultsList();
            } else {
                cancel.setVisible(true);
                handleOnKeyTyped();
            }
        });

        cancel.setOnAction(event -> {
            searchField.clear();
            notAvailableAlert.setVisible(false);
            cancel.setVisible(false);
            setDefaultSearchResultsList();
        });

        meaningArea.setEditable(false);
        save.setVisible(false);
        cancel.setVisible(false);
        notAvailableAlert.setVisible(false);
    }

    @FXML
    private void handleOnKeyTyped() {
        list.clear();
        String searchKey = searchField.getText().trim();
        List<String> resultList = dictionaryManagement.dictionaryLookup(myDictionary, searchKey);
        list = FXCollections.observableArrayList(resultList);
        if (list.isEmpty()) {
            notAvailableAlert.setVisible(true);
            setDefaultSearchResultsList();
        } else {
            notAvailableAlert.setVisible(false);
            resultListHeader.setText("Kết quả");
            resultsListView.setItems(FXCollections.observableArrayList(list));
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
            dictionaryManagement.updateWord(myDictionary, indexOfSelectedWord, meaningArea.getText(), path);
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
            dictionaryManagement.deleteWord(myDictionary, indexOfSelectedWord, path);
            refreshAfterDeleting();
            alerts.showAlertInfo("Information", "Xóa thành công");
        } else {
            alerts.showAlertInfo("Information", "Thay đổi không được công nhận");
        }
    }

    private void refreshAfterDeleting() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(englishWord.getText())) {
                list.remove(i);
                break;
            }
        }
        resultsListView.setItems(list);
        explanationHeaderPane.setVisible(false);
        meaningArea.setVisible(false);
    }

    private void setDefaultSearchResultsList() {
        Iterator<Word> iterator = myDictionary.Words.iterator();
        ArrayList<Word> wordList = new ArrayList<>(myDictionary.Words);

        if (firstIndexOfListFound == 0) {
            resultListHeader.setText("Các từ đầu tiên");
        } else {
            resultListHeader.setText("Kết quả liên quan");
        }

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


    public void handleClickSound(ActionEvent actionEvent) {
    }
}