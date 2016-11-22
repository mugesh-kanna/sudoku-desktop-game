package UI;

import static UI.gamePlay.*;
import static UI.global.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

public class mainMenu {

    // <editor-fold defaultstate="collapsed" desc="Main Panes">
    private GridPane mainMenuContainer;
    private BorderPane rightPartContainer;
    private BorderPane leftPartContainer;
    private GridPane gameModesContainer;
    private GridPane levelsContainer;
    private GridPane savedGamesContainer;
    private BorderPane pageHeaderContainer;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Labels">
    private Label logo;
    private Label logoText;
    private Label version;
    private Label headlineText;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Buttons">
    private Button newGameButton;
    private Button loadGameButton;
    private Button checkSudokuButton;
    private Button challangeComputerButton;
    private Button exitButton;
    private Button backButton;
    private Button easyButton;
    private Button mediumButton;
    private Button hardButton;
    // </editor-fold>

    /**
     * Initialize main menu elements
     *
     * @return mainMenuScene
     */
    public GridPane initialize() {
        //Main menu layout
        mainMenuContainer = new GridPane();

        //Creating two columns
        ColumnConstraints leftPart = new ColumnConstraints();
        leftPart.setPercentWidth(35);

        ColumnConstraints rightPart = new ColumnConstraints();
        rightPart.setPercentWidth(65);

        mainMenuContainer.getColumnConstraints().addAll(leftPart, rightPart);

        //Creating 100% height row
        RowConstraints fullHeight = new RowConstraints();
        fullHeight.setPercentHeight(100);

        mainMenuContainer.getRowConstraints().add(fullHeight);

        //Creating left part layout
        leftPartContainer = new BorderPane();
        leftPartContainer.getStyleClass().add("left-part");
        mainMenuContainer.setConstraints(leftPartContainer, 0, 0);
        mainMenuContainer.getChildren().add(leftPartContainer);

        //<editor-fold defaultstate="collapsed" desc="Logo">
        logo = new Label();
        logo.getStyleClass().add("logo");
        leftPartContainer.setTop(logo);
        leftPartContainer.setAlignment(logo, Pos.BOTTOM_CENTER);
        leftPartContainer.setMargin(logo, new Insets(175, 0, 30, 0));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Logo Label">
        logoText = new Label("Sudoku Game");
        logoText.getStyleClass().add("logo-text");
        leftPartContainer.setCenter(logoText);
        leftPartContainer.setAlignment(logoText, Pos.TOP_CENTER);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Version Label">
        version = new Label("Version 1.0");
        version.getStyleClass().add("version");
        leftPartContainer.setBottom(version);
        leftPartContainer.setAlignment(version, Pos.TOP_CENTER);
        leftPartContainer.setMargin(version, new Insets(0, 0, 30, 0));
        //</editor-fold>

        leftPartContainer.getChildren().addAll();

        //Right part layout
        rightPartContainer = new BorderPane();
        rightPartContainer.setPadding(new Insets(25));
        mainMenuContainer.setConstraints(rightPartContainer, 1, 0);
        mainMenuContainer.getChildren().add(rightPartContainer);

        //Initalize modes
        initializeGameModes();
        initializeLevelsMenu();
        rightPartContainer.setCenter(gameModesContainer);

        return mainMenuContainer;
    }

    private void initializeGameModes() {
        gameModesContainer = new GridPane();
        gameModesContainer.setAlignment(Pos.CENTER);

        //Creating custom rows
        RowConstraints rowNo[] = new RowConstraints[5];
        for (int counter = 0; counter < 5; counter++) {
            rowNo[counter] = new RowConstraints();
            rowNo[counter].setPercentHeight(13);
            gameModesContainer.getRowConstraints().add(rowNo[counter]);
        }

        //<editor-fold defaultstate="collapsed" desc="New Game Button">
        Image newGameButtonIcon = new Image(getClass().getResourceAsStream("/icons/new-game.png"));
        ImageView newGameButtonIconView = new ImageView(newGameButtonIcon);
        newGameButton = new Button("       New Game", newGameButtonIconView);
        initButtonStyle(newGameButton, gameModesContainer, 0, newGameButtonIconView, WHITE_BG);

        newGameButton.setOnAction(e -> {
            switchPanes(rightPartContainer, gameModesContainer, levelsContainer);
            playingMode = 1;
            gamePlayContainer.setLeft(gameLeftPanelContainer);

            saveButton.setVisible(true);
            saveButton.setDisable(false);
            submitButton.setText("Submit");
            headlineLabel.setText("New Game");
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Load Game Button">
        Image loadGameButtonIcon = new Image(getClass().getResourceAsStream("/icons/load-game.png"));
        ImageView laodGameIconView = new ImageView(loadGameButtonIcon);
        loadGameButton = new Button("       Load last game", laodGameIconView);
        initButtonStyle(loadGameButton, gameModesContainer, 1, laodGameIconView, WHITE_BG);

        loadGameButton.setOnAction(e -> {
            initializeSavedGames();
            switchPanes(rightPartContainer, gameModesContainer, savedGamesContainer);
            playingMode = 2;
            gamePlayContainer.setLeft(gameLeftPanelContainer);

            saveButton.setVisible(true);
            saveButton.setDisable(false);
            submitButton.setText("Submit");
            headlineLabel.setText("Loaded Game");
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Check Sudoku Button">
        Image checkSudokuIcon = new Image(getClass().getResourceAsStream("/icons/check-sudoku.png"));
        ImageView checkSudokuIconView = new ImageView(checkSudokuIcon);
        checkSudokuButton = new Button("       Check your Sudoku", checkSudokuIconView);
        initButtonStyle(checkSudokuButton, gameModesContainer, 2, checkSudokuIconView, WHITE_BG);

        checkSudokuButton.setOnAction(e -> {
            switchPanes(screenContainer, mainMenuContainer, gamePlayContainer);
            playingMode = 3;
            gamePlayContainer.setLeft(null);

            saveButton.setVisible(false);
            saveButton.setDisable(true);
            submitButton.setText("Check");
            headlineLabel.setText("Check your Sudoku");
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Challenge Computer">
        Image challengeComputerIcon = new Image(getClass().getResourceAsStream("/icons/challenge-computer.png"));
        ImageView challengeComputerIconView = new ImageView(challengeComputerIcon);
        challangeComputerButton = new Button("       Challenge Computer", challengeComputerIconView);
        initButtonStyle(challangeComputerButton, gameModesContainer, 3, challengeComputerIconView, WHITE_BG);

        challangeComputerButton.setOnAction(e -> {
            switchPanes(screenContainer, mainMenuContainer, gamePlayContainer);
            playingMode = 4;
            gamePlayContainer.setLeft(null);

            saveButton.setVisible(false);
            saveButton.setDisable(true);
            submitButton.setText("Challenge");
            headlineLabel.setText("Challenge Computer");
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Exit Button">
        Image exitButtonIcon = new Image(getClass().getResourceAsStream("/icons/exit.png"));
        ImageView exitButtonIconView = new ImageView(exitButtonIcon);
        exitButton = new Button("       Exit", exitButtonIconView);
        initButtonStyle(exitButton, gameModesContainer, 4, exitButtonIconView, WHITE_BG);

        exitButton.setOnAction(e -> {
            System.exit(0);
        });
        //</editor-fold>
    }

    private void initializeLevelsMenu() {
        levelsContainer = new GridPane();
        levelsContainer.setAlignment(Pos.CENTER);

        //Creating custom rows
        RowConstraints rowNo[] = new RowConstraints[6];
        for (int counter = 0; counter < 6; counter++) {
            rowNo[counter] = new RowConstraints();

            if (counter == 0) {
                rowNo[counter].setPercentHeight(20);
            }

            rowNo[counter].setPercentHeight(13);
            levelsContainer.getRowConstraints().add(rowNo[counter]);
        }

        //BorderPane to hold text and back arrow
        pageHeaderContainer = new BorderPane();
        levelsContainer.setConstraints(pageHeaderContainer, 0, 0);
        levelsContainer.getChildren().add(pageHeaderContainer);
        levelsContainer.setHalignment(pageHeaderContainer, HPos.CENTER);

        //Back Button
        backButton = new Button("");
        backButton.getStyleClass().add("button-icon--dark");
        backButton.getStyleClass().add("back-icon--dark");
        pageHeaderContainer.setLeft(backButton);
        pageHeaderContainer.setAlignment(backButton, Pos.CENTER);
        pageHeaderContainer.setMargin(backButton, new Insets(0, -80, 0, -65));

        backButton.setOnAction(e -> switchPanes(rightPartContainer, levelsContainer, gameModesContainer));

        //Headline
        headlineText = new Label("Choose game level");
        headlineText.getStyleClass().add("headline-text");
        pageHeaderContainer.setCenter(headlineText);
        pageHeaderContainer.setAlignment(headlineText, Pos.CENTER);

        //<editor-fold defaultstate="collapsed" desc="Easy Level Button">
        easyButton = new Button("Easy");
        initButtonStyle(easyButton, levelsContainer, 1, null, WHITE_BG);

        easyButton.setOnAction(e -> {
            switchPanes(screenContainer, mainMenuContainer, gamePlayContainer);

            ArrayList<String> sudokuGame = null;
            try {
                sudokuGame = database.Select("Easy", 0);
                sudokuIdOriginal = sudokuGame.get(0).split(",")[1];

                levelLabel.setText(easyButton.getText());
                gameTime.setTimer(timerLabel, 0);
                gameTime.start();
            } catch (SQLException ex) {
                Logger.getLogger(mainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            assignSudoku(sudokuGame.get(0), null);
            sudokuOperation(PRINT_SUDOKU);
            switchPanes(rightPartContainer, levelsContainer, gameModesContainer);
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Medium Level Button">
        mediumButton = new Button("Medium");
        initButtonStyle(mediumButton, levelsContainer, 2, null, WHITE_BG);

        mediumButton.setOnAction(e -> {
            switchPanes(screenContainer, mainMenuContainer, gamePlayContainer);

            ArrayList<String> sudokuGame = null;
            try {
                sudokuGame = database.Select("Medium", 0);
                sudokuIdOriginal = sudokuGame.get(0).split(",")[1];

                levelLabel.setText(mediumButton.getText());
                gameTime.setTimer(timerLabel, 0);
                gameTime.start();
            } catch (SQLException ex) {
                Logger.getLogger(mainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            assignSudoku(sudokuGame.get(0), null);
            sudokuOperation(PRINT_SUDOKU);
            switchPanes(rightPartContainer, levelsContainer, gameModesContainer);
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Hard Level Button">
        hardButton = new Button("Hard");
        initButtonStyle(hardButton, levelsContainer, 3, null, WHITE_BG);

        hardButton.setOnAction(e -> {
            switchPanes(screenContainer, mainMenuContainer, gamePlayContainer);

            ArrayList<String> sudokuGame = null;
            try {
                sudokuGame = database.Select("Hard", 0);
                sudokuIdOriginal = sudokuGame.get(0).split(",")[1];

                gameTime.setTimer(timerLabel, 0);
                gameTime.start();
                levelLabel.setText(hardButton.getText());
            } catch (SQLException ex) {
                Logger.getLogger(mainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            assignSudoku(sudokuGame.get(0), null);
            sudokuOperation(PRINT_SUDOKU);
            switchPanes(rightPartContainer, levelsContainer, gameModesContainer);
        });
        //</editor-fold>
    }

    public void initializeSavedGames() {
        ArrayList<String> savedGames = null;

        try {
            savedGames = database.Select(null, 1);
        } catch (SQLException ex) {
            Logger.getLogger(mainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (savedGames == null) {
            savedGames = new ArrayList<>();
        }

        int gamesNumber = savedGames.size();

        savedGamesContainer = new GridPane();
        savedGamesContainer.setAlignment(Pos.CENTER);

        //Creating custom rows
        RowConstraints headlineRow = new RowConstraints();
        headlineRow.setPercentHeight(20);
        savedGamesContainer.getRowConstraints().add(headlineRow);

        RowConstraints gamesRow = new RowConstraints();
        gamesRow.setPercentHeight(65);
        savedGamesContainer.getRowConstraints().add(gamesRow);

        //BorderPane to hold text and back arrow
        pageHeaderContainer = new BorderPane();
        savedGamesContainer.setConstraints(pageHeaderContainer, 0, 0);
        savedGamesContainer.getChildren().add(pageHeaderContainer);
        savedGamesContainer.setHalignment(pageHeaderContainer, HPos.CENTER);

        //Creating elemens
        backButton = new Button("");
        backButton.getStyleClass().add("button-icon--dark");
        backButton.getStyleClass().add("back-icon--dark");
        pageHeaderContainer.setLeft(backButton);
        pageHeaderContainer.setAlignment(backButton, Pos.CENTER);
        pageHeaderContainer.setMargin(backButton, new Insets(0, -170, 0, -10));

        backButton.setOnAction(e -> {
            switchPanes(rightPartContainer, savedGamesContainer, gameModesContainer);
            savedGamesContainer.getChildren().clear();
        });

        //Headline
        headlineText = new Label("Choose a game");
        headlineText.getStyleClass().add("headline-text");
        pageHeaderContainer.setCenter(headlineText);
        pageHeaderContainer.setAlignment(headlineText, Pos.CENTER);

        //Container
        GridPane savedGamesLayout = new GridPane();
        savedGamesLayout.setVgap(20);

        //Scrollbar
        ScrollPane scrollPane = new ScrollPane(savedGamesLayout);
        scrollPane.getStyleClass().add("scroll-panel");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setMinWidth(360);
        savedGamesContainer.setMargin(scrollPane, new Insets(0, 0, 0, 60));

        savedGamesContainer.setConstraints(scrollPane, 0, 1);
        savedGamesContainer.getChildren().add(scrollPane);

        GridPane gameBlock[] = new GridPane[gamesNumber];

        for (int counter = 0; counter < gamesNumber; counter++) {
            String[] data = savedGames.get(counter).split(",");

            gameBlock[counter] = new GridPane();
            gameBlock[counter].getStyleClass().add("game-block");
            gameBlock[counter].setAlignment(Pos.CENTER_LEFT);
            gameBlock[counter].setId(data[0]); //Change to game ID

            //Container
            BorderPane savedGameBlockContainer = new BorderPane();
            gameBlock[counter].setConstraints(savedGameBlockContainer, 0, 0);
            gameBlock[counter].getChildren().add(savedGameBlockContainer);

            //Details container
            GridPane savedGameDetailsContainer = new GridPane();
            savedGameBlockContainer.setLeft(savedGameDetailsContainer);
            savedGameBlockContainer.setMargin(savedGameDetailsContainer, new Insets(0, 130, 0, 0));

            //Custom row constraints
            RowConstraints firstRow = new RowConstraints();
            firstRow.setPercentHeight(50);
            savedGameDetailsContainer.getRowConstraints().add(firstRow);

            RowConstraints secondRow = new RowConstraints();
            secondRow.setPercentHeight(50);
            savedGameDetailsContainer.getRowConstraints().add(secondRow);

            //<editor-fold defaultstate="collapsed" desc="Saved Game Title">
            Button savedGameTitle = new Button(data[4]);
            savedGameTitle.getStyleClass().add("button-transparent--dark");
            savedGameDetailsContainer.setConstraints(savedGameTitle, 0, 0);
            savedGameDetailsContainer.getChildren().add(savedGameTitle);
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Saved Game Lavel Label">
            Label savedGameLevelLabel = new Label(data[3]);
            savedGameLevelLabel.getStyleClass().add("game-level");
            savedGameDetailsContainer.setConstraints(savedGameLevelLabel, 0, 1);
            savedGameDetailsContainer.getChildren().add(savedGameLevelLabel);
            savedGameDetailsContainer.setValignment(savedGameLevelLabel, VPos.BOTTOM);
            //</editor-fold>

            //Formating the time
            String time = data[2];
            SimpleDateFormat sdf = new SimpleDateFormat("ss");
            Date dateObj = null;
            try {
                dateObj = sdf.parse(time);
            } catch (ParseException ex) {
                Logger.getLogger(mainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            //<editor-fold defaultstate="collapsed" desc="Saved Game Time Label">
            Label savedGameTimeLabel = new Label(dateObj.getMinutes() + ":" + dateObj.getSeconds());
            savedGameTimeLabel.getStyleClass().add("game-time");
            savedGameDetailsContainer.setConstraints(savedGameTimeLabel, 0, 1);
            savedGameDetailsContainer.getChildren().add(savedGameTimeLabel);
            savedGameDetailsContainer.setValignment(savedGameTimeLabel, VPos.BOTTOM);
            savedGameDetailsContainer.setMargin(savedGameTimeLabel, new Insets(0, 0, 0, 70));
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Saved Game Delete Button">
            Button savedGameDeleteButton = new Button();
            savedGameDeleteButton.getStyleClass().add("button-icon--dark");
            savedGameDeleteButton.getStyleClass().add("delete-icon");
            savedGameBlockContainer.setRight(savedGameDeleteButton);
            savedGameBlockContainer.setMargin(savedGameDeleteButton, new Insets(4, 0, 0, 0));
            //</editor-fold>

            //Adding blocks to the container
            savedGamesLayout.setConstraints(gameBlock[counter], 0, counter);
            savedGamesLayout.getChildren().add(gameBlock[counter]);

            //Switching scenes and printing the Sudoku
            savedGameTitle.setOnAction(e -> {
                //Printing Sudoku and saving Sudoku ID
                sudokuId = data[0];
                sudokuIdOriginal = data[6];
                assignSudoku(data[1], null);
                assignSudoku(data[5], markSolution);
                sudokuOperation(PRINT_SUDOKU);

                switchPanes(screenContainer, mainMenuContainer, gamePlayContainer);

                levelLabel.setText(data[3]);
                timerLabel.setText(savedGameTimeLabel.getText());

                gameTime.setTimer(timerLabel, Integer.parseInt(data[2]));
                gameTime.start();

                //Clear container
                savedGamesContainer.getChildren().clear();
                switchPanes(rightPartContainer, savedGamesContainer, gameModesContainer);
            });

            //Deleting the game
            savedGameDeleteButton.setOnAction(e -> {
                String gameID = savedGameDeleteButton.getParent().getParent().getId();
                Object gameBlockObject = savedGameDeleteButton.getParent().getParent();
                int gameBlockNumber = savedGamesLayout.getRowIndex((Node) gameBlockObject);
                fade(gameBlockObject, 200, 0, FADE_OUT);

                for (int blockCounter = gameBlockNumber; blockCounter < gamesNumber - 1; blockCounter++) {
                    //Creating timeline animation
                    Timeline updateGameTimeline = new Timeline();

                    KeyValue fromKeyValue = new KeyValue(gameBlock[blockCounter + 1].translateYProperty(), gameBlock[blockCounter + 1].getTranslateY());
                    KeyValue toKeyValue = new KeyValue(gameBlock[blockCounter + 1].translateYProperty(), gameBlock[blockCounter + 1].getTranslateY() - 20 - gameBlock[blockCounter + 1].getHeight());

                    KeyFrame startMove = new KeyFrame(Duration.ZERO, fromKeyValue);
                    KeyFrame finishMove = new KeyFrame(Duration.millis(300), toKeyValue);

                    updateGameTimeline.getKeyFrames().addAll(startMove, finishMove);
                    updateGameTimeline.play();
                }

                try {
                    database.deleteGame(Integer.parseInt(gameID.replace("#", "")));
                } catch (SQLException ex) {

                }
            });
        }
    }

    /**
     *
     * @param Sudoku
     */
    private void assignSudoku(String Sudoku, Boolean[][] originalSudoku) {
        int charptr = 0;

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (originalSudoku != null) {
                    originalSudoku[row][column] = Integer.parseInt(Sudoku.charAt(charptr) + "") == 0 ? Boolean.FALSE : Boolean.TRUE;
                } else {
                    computerSolution[row][column] = Integer.parseInt(Sudoku.charAt(charptr) + "");
                }

                charptr++;
            }
        }
    }
}
