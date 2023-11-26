package DictionaryApplication.Controllers;

import DictionaryApplication.DictionaryCommandLine.Wordle;
import javafx.fxml.FXML;

//import java.awt.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javax.swing.*;

public class GameController {
    public Button reset;
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

    @FXML
    private Label logger;


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
        logger.setText("");
        logger.setWrapText(true);
        String guess = guessInput.getText();
        int row = wd.getTries();
        if (Wordle.verifyWord(guess)) {
            String attemptResult = wd.attempt(guess);
            if (attemptResult.equals(Wordle.WIN)) {
                // Handle win case
                for (int i = 0; i < 5; i++) {
                    boxesArray[row][i].setStyle("-fx-background-color: #79b851;" +
                            "-fx-text-fill: #fbfcff;" +
                            "-fx-font-size: 18");
                    boxesArray[row][i].setText(guess.substring(i, i + 1).toUpperCase());
                }
                logger.setText("Xin chúc mừng, Bạn đã thắng");
                return;
            } else {
                if (attemptResult.equals(Wordle.LOST)) {
                    // Handle lose case
                    for (int i = 0; i < 5; i++) {
                        boxesArray[row][i].setText(guess.substring(i, i + 1).toUpperCase());
                        if (wd.getKey().charAt(i) == guess.charAt(i)) {
                            boxesArray[row][i].setStyle("-fx-background-color: #79b851;" +
                                    "-fx-text-fill: #fbfcff;" +
                                    "-fx-font-size: 18");
                        } else {
                            if (wd.getKey().indexOf(guess.charAt(i)) != -1) {
                                boxesArray[row][i].setStyle("-fx-background-color: #f3c237;" +
                                        "-fx-text-fill: #fbfcff;" +
                                        "-fx-font-size: 18");
                            } else {
                                boxesArray[row][i].setStyle("-fx-background-color: #808080;" +
                                        "-fx-text-fill: #fbfcff;" +
                                        "-fx-font-size: 18");
                            }
                        }

                    }
                    String s = wd.getKey();
                    String log = String.format("Rất tiếc bạn đã thua , từ cần đoán là :  %s", s);
                    logger.setText(log);
                } else {
                    for (int i = 0; i < 5; i++) {
                        boxesArray[row][i].setText(guess.substring(i, i + 1).toUpperCase());
                        if (attemptResult.charAt(i) == 'n') {
                            boxesArray[row][i].setStyle("-fx-background-color: #808080;" +
                                    "-fx-text-fill: #fbfcff;" +
                                    "-fx-font-size: 18");
                        } else if (attemptResult.charAt(i) == 't') {
                            boxesArray[row][i].setStyle("-fx-background-color: #79b851;" +
                                    "-fx-text-fill: #fbfcff;" +
                                    "-fx-font-size: 18");
                        } else {
                            boxesArray[row][i].setStyle("-fx-background-color: #f3c237;" +
                                    "-fx-text-fill: #fbfcff;" +
                                    "-fx-font-size: 18");
                        }
                    }
                }
            }
        } else {
            // Show error in Textbox
            logger.setText("Bạn đã nhập một từ không hợp lệ, vui lòng nhập từ khác ");
        }
    }

    @FXML
    private void handleOnEnterPress(KeyEvent keyEvent) {
        if (this.wd.isPlaying()) {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                checkGuess();
                guessInput.setText("");
            }
        }
    }

    @FXML
    private void handleClickReset(MouseEvent mouseEvent) {
        wd = new Wordle();
        Label[][] boxesArray = {
                {box00, box01, box02, box03, box04},
                {box10, box11, box12, box13, box14},
                {box20, box21, box22, box23, box24},
                {box30, box31, box32, box33, box34},
                {box40, box41, box42, box43, box44},
                {box50, box51, box52, box53, box54}
        };
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                boxesArray[i][j].setText("");
                boxesArray[i][j].setStyle("-fx-background-color: #fbfcff;" +
                        "-fx-border-color: #000000;");
            }
        }
        guessInput.setText("");

        logger.setText("");
    }
}

