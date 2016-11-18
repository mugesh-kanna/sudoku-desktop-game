package UI;

import static UI.global.FADE_IN;
import static UI.global.fade;
import static UI.global.switchPanes;
import static UI.global.CLEAR_SUDOKU;
import static UI.global.READ_SUDOKU;
import static UI.global.TRANSPARENT_BG;
import static UI.global.computerSolution;
import static UI.global.initButtonStyle;
import static UI.global.windowLayout;
import static UI.global.mainMenuContainer;
import static UI.global.markSolution;
import static UI.global.playingMode;
import static UI.global.userSudoku;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sudoku.checker;
import sudoku.sudoku;

public class gamePlay {

    BorderPane gamePlayContainer;
    private static TextField[][] sudokuCells = new TextField[9][9];
    static Label levelLabel;

    /**
     * Initialize game play elements
     * @author Muhammad Tarek
     * @return gamePlayLayout
     */
    public BorderPane initialize() {
        //Main layout
        gamePlayContainer = new BorderPane();

        //Toolbar layout
        BorderPane toolbarLayout = new BorderPane();
        toolbarLayout.getStyleClass().add("toolbar");

        toolbarLayout.setPrefHeight(75);

        //Headline + saveButton button layout
        BorderPane headlineAndSaveLayout = new BorderPane();

        //Toolbar objects
        Label headline = new Label("Check your Sudoku");
        headline.setId("headline");

        headline.setMaxWidth(Double.MAX_VALUE);
        headline.setAlignment(Pos.CENTER);
        headlineAndSaveLayout.setCenter(headline);
        headlineAndSaveLayout.setAlignment(headline, Pos.TOP_CENTER);
        toolbarLayout.setCenter(headlineAndSaveLayout);
        headlineAndSaveLayout.setMargin(headline, new Insets(0, 0, 0, 80));

        //Back Button
        Button backButton = new Button("");
        backButton.getStyleClass().add("iconButton");
        backButton.setId("backButton");
        toolbarLayout.setLeft(backButton);

        backButton.setOnAction(e -> {
            switchPanes(windowLayout, gamePlayContainer, mainMenuContainer);
            sudokuOperation(CLEAR_SUDOKU);
        });

        //Save Button
        Button saveButton = new Button("");
        saveButton.getStyleClass().add("iconButton");
        saveButton.setId("saveButton");
        headlineAndSaveLayout.setRight(saveButton);
        headlineAndSaveLayout.setMargin(saveButton, new Insets(0, 15, 0, 0));

        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("iconButton");
        submitButton.setId("submitButton");
        toolbarLayout.setRight(submitButton);

        initSudokuBlock();
        saveButton.setOnAction(e -> {
            showPopup("Game is saved successfuly", 1);
        });

        //Adding the toolbar in the top of the window
        gamePlayContainer.setTop(toolbarLayout);

        submitButton.setOnAction((event) -> {
            try {
                sudokuOperation(READ_SUDOKU);
                checkSudoku();
            } catch (InterruptedException ex) {
                Logger.getLogger(gamePlay.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        //Left panel
        BorderPane leftPanelLayout = new BorderPane();
        leftPanelLayout.setPadding(new Insets(30, 0, 30, 10));
        gamePlayContainer.setLeft(leftPanelLayout);
        
        GridPane gameDetailsLayout = new GridPane();
        leftPanelLayout.setTop(gameDetailsLayout);
        
        GridPane gameControlsLayout = new GridPane();
        gameControlsLayout.setVgap(10);
        leftPanelLayout.setBottom(gameControlsLayout);
        
        Label levelHeadline = new Label("Level");
        levelHeadline.getStyleClass().add("text");
        levelHeadline.getStyleClass().add("text--headline");
        gameDetailsLayout.setConstraints(levelHeadline, 0, 0);
        
        levelLabel = new Label();
        levelLabel.getStyleClass().add("text");
        levelLabel.getStyleClass().add("text--normal");
        gameDetailsLayout.setConstraints(levelLabel, 0, 1);
        
        
        Label timerHeadline = new Label("Time");
        timerHeadline.getStyleClass().add("text");
        timerHeadline.getStyleClass().add("text--headline");
        gameDetailsLayout.setConstraints(timerHeadline, 0, 2);
        
        Label timerLabel = new Label();
        timerLabel.getStyleClass().add("text");
        timerLabel.getStyleClass().add("text--normal");
        gameDetailsLayout.setConstraints(timerLabel, 0, 4);
        
        gameDetailsLayout.getChildren().addAll(levelHeadline, levelLabel, timerHeadline, timerLabel);
        
        //Pause Button
        Image pauseButtonIcon = new Image(getClass().getResourceAsStream("/icons/pause.png"));
        ImageView pauseButtonIconView = new ImageView(pauseButtonIcon);
        Button pauseGameButton = new Button("       Pause", pauseButtonIconView);
        initButtonStyle(pauseGameButton, gameControlsLayout, 0, pauseButtonIconView, TRANSPARENT_BG);
        
        pauseGameButton.setOnAction(e -> {
            
        });
        
        //Hint Button
        Image hintButtonIcon = new Image(getClass().getResourceAsStream("/icons/hint.png"));
        ImageView hintButtonIconView = new ImageView(hintButtonIcon);
        Button hintButton = new Button("       Hint", hintButtonIconView);
        initButtonStyle(hintButton, gameControlsLayout, 1, hintButtonIconView, TRANSPARENT_BG);
        
        //Solve Button
        Image solveButtonIcon = new Image(getClass().getResourceAsStream("/icons/challenge-computer.png"));
        ImageView solveButtonIconView = new ImageView(solveButtonIcon);
        Button solveGameButton = new Button("       Solve", solveButtonIconView);
        initButtonStyle(solveGameButton, gameControlsLayout, 2, solveButtonIconView, TRANSPARENT_BG);

        return gamePlayContainer;
    }

    /**
     * Create Sudoku cells
     * @author Muhammad Tarek
     */
    private void initSudokuBlock() {
        //Sudoku card layout
        BorderPane cardBg = new BorderPane();
        cardBg.getStyleClass().add("card");
        cardBg.setPadding(new Insets(7));

        cardBg.setMaxHeight(475);
        cardBg.setMaxWidth(475);

        //Cells container layout
        GridPane cellsLayout = new GridPane();
        cellsLayout.getStyleClass().add("cells-container");
        cardBg.setCenter(cellsLayout);

        int rowCounter, columnCounter;

        //Creating Sudoku cells
        for (rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (columnCounter = 0; columnCounter < 9; columnCounter++) {
                //Create cells and positioning hem
                sudokuCells[rowCounter][columnCounter] = new TextField();
                cellsLayout.setConstraints(sudokuCells[rowCounter][columnCounter], columnCounter, rowCounter);
                cellsLayout.getChildren().add(sudokuCells[rowCounter][columnCounter]);

                sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell");

                //If the cell is No.2 or No.5 on any column it will have right border
                if (columnCounter == 2 || columnCounter == 5) {
                    sudokuCells[rowCounter][columnCounter].getStyleClass().add("border-right");
                }

                //If the cell is No.3 or No.6 on any row it will have top border
                if (rowCounter == 3 || rowCounter == 6) {
                    sudokuCells[rowCounter][columnCounter].getStyleClass().add("border-top");
                    //Because the previus line of code override the right border
                    if (columnCounter == 2 || columnCounter == 5) {
                        sudokuCells[rowCounter][columnCounter].getStyleClass().add("border-top-right");
                    }
                }
            }
        }

        gamePlayContainer.setCenter(cardBg);
        gamePlayContainer.setMargin(cardBg, new Insets(0,0,0, - 150));
        gamePlayContainer.setAlignment(cardBg, Pos.CENTER);
        gamePlayContainer.getChildren().addAll();
    }

    /**
     * @author Muhammad Tarek
     * @param message
     * @param alertType
     */
    private void showPopup(String message, int alertType) {
        //Alert message layout
        GridPane alertLayout = new GridPane();
        alertLayout.setHgap(10);

        gamePlayContainer.setBottom(alertLayout);
        gamePlayContainer.setAlignment(alertLayout, Pos.CENTER);

        //Alert message
        Label alertMessage = new Label(message);
        alertMessage.getStyleClass().add("alert-message");

        alertMessage.getStyleClass().add(alertType == 1 ? "alert-message-success" : "alert-message-danger");

        alertLayout.setConstraints(alertMessage, 1, 0);
        alertLayout.setMargin(alertMessage, new Insets(10, 0, 0, 0));

        //Alert icon
        Label alertIcon = new Label();
        alertIcon.getStyleClass().add("alert-icon");

        alertIcon.getStyleClass().add(alertType == 1 ? "alert-icon-success" : "alert-icon-danger");

        alertLayout.setConstraints(alertIcon, 0, 0);

        //Adding the alert in gameScene
        alertLayout.getChildren().addAll(alertIcon, alertMessage);
        alertLayout.setAlignment(Pos.CENTER);

        //Fading animation
        fade(alertLayout, 1000, 0, FADE_IN);

        //Auto hide the alert
        Timeline countDown = new Timeline(new KeyFrame(
                Duration.millis(3000),
                ae -> {
                    fade(alertLayout, 0, 1000, 1);
                    gamePlayContainer.setBottom(null);
                }
        ));
        countDown.play();
    }

    private void saveButtonCurrentGame() {
        //TODO

        /*
         1. Read the Sudoku from the screen
         2. Show pop-up telling the user that the game is saveButtond
         */
    }

    /**
     * @author 
     * @throws InterruptedException
     */
    private void checkSudoku() throws InterruptedException {
        sudoku Sudoku = new sudoku();
        Sudoku.setSudoku(userSudoku);

        Sudoku.initSudokuWrongCells();

        if (playingMode != 4) {
            checker Checker = new checker();

            Checker.check();
            markSolution = Sudoku.getsudokuWrongCells();
        }

        Boolean isSudoku = true;

        for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                if (markSolution[rowCounter][columnCounter]) {
                    sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell-danger");
                    isSudoku = false;
                }
            }
        }

        if (isSudoku) {
            for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
                for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                    sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell-success");
                }
            }
        }
    }

    /**
     * @author Muhammad Tarek, Mustafa Magdy
     * @param opType
     */
    static void sudokuOperation(int opType) {
        for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (int columnCounter = 0; columnCounter < 9; columnCounter++) {

                switch (opType) {
                    //Read Sudoku
                    case 1:
                        userSudoku[rowCounter][columnCounter] = Integer.parseInt(sudokuCells[rowCounter][columnCounter].getText());
                        break;
                    //Print Sudoku
                    case 2:
                        if (computerSolution[rowCounter][columnCounter] != 0) {
                            sudokuCells[rowCounter][columnCounter].setText(computerSolution[rowCounter][columnCounter] + "");
                            sudokuCells[rowCounter][columnCounter].setDisable(true);
                        }
                        break;
                    //Clear Sudoku fields and array
                    case 3:
                        sudokuCells[rowCounter][columnCounter].setText("");
                        sudokuCells[rowCounter][columnCounter].setDisable(false);
                        sudokuCells[rowCounter][columnCounter].getStyleClass().remove("cell-danger");
                        sudokuCells[rowCounter][columnCounter].getStyleClass().remove("cell-success");

                        userSudoku[rowCounter][columnCounter] = 0;
                        computerSolution[rowCounter][columnCounter] = 0;
                        markSolution[rowCounter][columnCounter] = Boolean.FALSE;
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
