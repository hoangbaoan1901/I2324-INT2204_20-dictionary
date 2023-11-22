package DictionaryApplication.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DictionaryController implements Initializable {

	@FXML
	private Tooltip tooltip1, tooltip2, tooltip3;

	@FXML
	private Button addWord, translate, searchWord, close;

	@FXML
	private AnchorPane container;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		searchWord.setOnAction(this::handleSearchWord);
		addWord.setOnAction(this::handleAddWord);
		translate.setOnAction(this::handleTranslate);
		tooltip1.setShowDelay(Duration.seconds(0.5));
		tooltip2.setShowDelay(Duration.seconds(0.5));
		tooltip3.setShowDelay(Duration.seconds(0.5));
		showComponent("/Fxmls/SearcherGui.fxml");
		close.setOnMouseClicked(e -> System.exit(0));
	}

	private void setNode(Node node) {
		container.getChildren().clear();
		container.getChildren().add(node);
	}

	@FXML
	private void handleSearchWord(ActionEvent event) {
		showComponent("/Fxmls/SearcherGui.fxml");
	}

	@FXML
	private void handleAddWord(ActionEvent event) {
		showComponent("/Fxmls/AdditionGui.fxml");
	}

	@FXML
	private void handleTranslate(ActionEvent event) {
		showComponent("/Fxmls/TranslationGui.fxml");
	}

	@FXML
	private void showComponent(String path) {
		try {
			AnchorPane Component = FXMLLoader.load(getClass().getResource(path));
			setNode(Component);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
