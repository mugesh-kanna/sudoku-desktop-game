package UI;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Class to initialize all global variables and methods
 *
 * @author Muhammad
 */
public class global {

    static BorderPane windowLayout;
    static BorderPane gamePlayContainer;
    static GridPane mainMenuContainer;
    static GridPane scorePageContainer;

    //<editor-fold defaultstate="collapsed" desc="Sudoku Arrays">
    static Integer[][] userSudoku = new Integer[9][9]; //Reads the Sudoku from the user
    static Integer[][] computerSolution = new Integer[9][9]; //Where computer returns the wrong cells
    static Boolean[][] markSolution = new Boolean[9][9]; //Where computer returns the wrong cells
//</editor-fold>

    public static final int READ_SUDOKU = 1;
    public static final int PRINT_SUDOKU = 2;
    public static final int CLEAR_SUDOKU = 3;

    public static final int FADE_IN = 1;
    public static final int FADE_OUT = 2;
    
    public static final int TRANSPARENT_BG = 1;
    public static final int WHITE_BG = 2;

    static int playingMode;

    /**
     *
     * @param parent
     * @param fromChild
     * @param toChild
     */
    public static void switchPanes(BorderPane parent, Pane fromChild, Pane toChild) {
        Timeline translateAnimation = new Timeline();

        //Creating all key values for the animation
        KeyValue fromChildOpacityStart = new KeyValue(fromChild.opacityProperty(), 1);
        KeyValue fromChildOpacityEnd = new KeyValue(fromChild.opacityProperty(), 0);

        KeyValue toChildOpacityStart = new KeyValue(fromChild.opacityProperty(), 0);
        KeyValue toChildOpacityEnd = new KeyValue(fromChild.opacityProperty(), 1);

        KeyValue fromChildtranslateStart = new KeyValue(toChild.translateYProperty(), 0);
        KeyValue fromChildtranslateEnd = new KeyValue(toChild.translateYProperty(), 20);

        KeyValue toChildtranslateStart = new KeyValue(toChild.translateYProperty(), 20);
        KeyValue toChildtranslateEnd = new KeyValue(toChild.translateYProperty(), 0);

        //Creating the timeline keyframes
        //Hiding and moving fromChild
        KeyFrame startMoveOut = new KeyFrame(Duration.ZERO, fromChildtranslateStart);
        KeyFrame startFadeOut = new KeyFrame(Duration.millis(50), fromChildOpacityStart);
        KeyFrame finishFadeOut = new KeyFrame(Duration.millis(150), fromChildOpacityEnd);
        KeyFrame fnishMoveOut = new KeyFrame(Duration.millis(150), fromChildtranslateEnd);

        //Clearing the setCenter
        KeyFrame clear = new KeyFrame(Duration.millis(151), e -> {
            parent.setCenter(null);
        });

        //Showing and moving toChild
        KeyFrame startFadeIn = new KeyFrame(Duration.millis(151), toChildOpacityStart);
        KeyFrame startMoveIn = new KeyFrame(Duration.millis(151), toChildtranslateStart);
        KeyFrame addingToCenter = new KeyFrame(Duration.millis(151), e -> {
            parent.setCenter(toChild);
        });
        KeyFrame finishFadeIn = new KeyFrame(Duration.millis(250), toChildOpacityEnd);
        KeyFrame finishMoveIn = new KeyFrame(Duration.millis(300), toChildtranslateEnd);

        translateAnimation.getKeyFrames().addAll(startFadeOut, startMoveOut, finishFadeOut, fnishMoveOut, clear, startFadeIn, startMoveIn, addingToCenter, finishFadeIn, finishMoveIn);
        translateAnimation.play();

    }

    /**
     * 
     * @param node
     * @param duration
     * @param delay
     * @param fadeType 
     */
    public static void fade(Object node, int duration, int delay, int fadeType) {
        FadeTransition fadeAnimation = new FadeTransition(Duration.millis(duration), (Node) node);

        if (delay != 0)
            fadeAnimation.setDelay(Duration.millis(delay));
        
        fadeAnimation.setFromValue(fadeType == 1 ? 0 : 1);
        fadeAnimation.setToValue(fadeType == 1 ? 1 : 0);

        fadeAnimation.play();
    }
    
       /**
     * Initialize button styles, icons sizes Muhammad Tarek
     *
     * @since 6, November
     * @param button
     * @param layout
     * @param position
     * @param icon
     */
    static void initButtonStyle(Button button, GridPane layout, int position, ImageView icon, int bgColor) {
        button.setOpacity(0);
        button.getStyleClass().add("icon-text-button");

        if (icon != null) {
            icon.setFitHeight(bgColor == 1 ? 20 : 24);
            icon.setFitWidth(bgColor == 1 ? 20 : 24);
        }
        
        if (bgColor == 1)
            button.getStyleClass().add("transparent-button");
        else
            button.getStyleClass().add("white-button");

        layout.setConstraints(button, 0, position);
        layout.setHalignment(button, HPos.CENTER);
        layout.setValignment(button, VPos.CENTER);
        layout.getChildren().add(button);
        
        fade(button, 300, 150 * (position + 1), FADE_IN);
    }
}