package DictionaryApplication.Controllers;

import DictionaryApplication.DictionaryCommandLine.Wordle;
import javafx.fxml.FXML;

//import java.awt.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameController {
    @FXML
    private TextField guessInput;
    @FXML
    private Label box00;
    @FXML
    private Label box01;
    @FXML
    private Label box02;
    @FXML
    private Label box03;
    @FXML
    private Label box04;

    @FXML
    private Label box10;
    @FXML
    private Label box11;
    @FXML
    private Label box12;
    @FXML
    private Label box13;
    @FXML
    private Label box14;

    @FXML
    private Label box20;
    @FXML
    private Label box21;
    @FXML
    private Label box22;
    @FXML
    private Label box23;
    @FXML
    private Label box24;

    @FXML
    private Label box30;
    @FXML
    private Label box31;
    @FXML
    private Label box32;
    @FXML
    private Label box33;
    @FXML
    private Label box34;

    @FXML
    private Label box40;
    @FXML
    private Label box41;
    @FXML
    private Label box42;
    @FXML
    private Label box43;
    @FXML
    private Label box44;

    @FXML
    private Label box50;
    @FXML
    private Label box51;
    @FXML
    private Label box52;
    @FXML
    private Label box53;
    @FXML
    private Label box54;

    private Wordle wd = new Wordle();

    @FXML
    protected void checkGuess() {
        Label[][] boxesArray = {
                {box00, box01, box02, box03, box04},
                {box10, box11, box12, box13, box14},
                {box20, box21, box22, box23, box24},
                {box30, box31, box32, box33, box34},
                {box40, box41, box42, box43, box44},
                {box50, box51, box52, box53, box54}
        };
        String guess = guessInput.getText();
        int row = wd.getTries();
        if (Wordle.verifyWord(guess)) {
            String attemptResult = wd.attempt(guess);
            for (int i = 0; i < 5; i++) {
                boxesArray[row][i].setText(guess.substring(i, i + 1));
            }
            for (int i = 0; i < 5; i++) {
                if (attemptResult.charAt(i) == 'n') {
                    boxesArray[row][i].setStyle("-fx-background-color: #808080;");
                } else if (attemptResult.charAt(i) == 't') {
                    boxesArray[row][i].setStyle("-fx-background-color: #79b851;");
                } else {
                    boxesArray[row][i].setStyle("-fx-background-color: #f3c237;");
                }
            }
            if (attemptResult.equals(Wordle.WIN)) {
                // Handle win case
                return;
            } else {
                if (attemptResult.equals(Wordle.LOST)) {
                    // Handle lose case
                } else {
                    // Handle guess case:

                }
            }
        } else {
            // Show error in Textbox
        }
    }

    public void handleOnEnterPress(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            checkGuess();
        }
    }

}

