package com.fifteen.squares.views;

import com.fifteen.squares.FifteenSquaresApplication;
import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecondaryPresenter {

    @FXML
    private View secondary;
    @FXML
    private Node oneSquare;

    @FXML
    private Node twoSquare;

    @FXML
    private Node threeSquare;

    @FXML
    private Node fourSquare;

    @FXML
    private Node fifeSquare;

    @FXML
    private Node sixSquare;

    @FXML
    private Node sevenSquare;

    @FXML
    private Node eightSquare;

    @FXML
    private Node nineSquare;

    @FXML
    private Node tenSquare;

    @FXML
    private Node elevenSquare;

    @FXML
    private Node twelveSquare;

    @FXML
    private Node thirteenSquare;

    @FXML
    private Node fourteenSquare;

    @FXML
    private Node fifteenSquare;
    @FXML
    private Node emptySquare;
    @FXML
    private Label valueTimer;
    @FXML
    private VBox squareBox;
    private Timeline timeline;
    private long currentNumberSeconds = 0;

    public void initialize() {
        secondary.setShowTransitionFactory(BounceInRightTransition::new);

        initTimeLine();

        FloatingActionButton playButton = new FloatingActionButton(MaterialDesignIcon.PLAY_ARROW.text,
                e -> {
                    Button source = (Button) e.getSource();
                    if (source.getText().equals(MaterialDesignIcon.PLAY_ARROW.text)) {
                        squareBox.setDisable(false);
                        timeline.playFromStart();
                        System.out.println("Play");
                    }

                });

        FloatingActionButton pauseButton = new FloatingActionButton(MaterialDesignIcon.PAUSE.text,
                e -> {
                    squareBox.setDisable(true);
                    timeline.stop();
                    System.out.println("Pause");
                });
        playButton.attachTo(pauseButton, Side.LEFT);

        secondary.getLayers().add(pauseButton.getLayer());

        secondary.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        MobileApplication.getInstance().showLayer(FifteenSquaresApplication.MENU_LAYER)));
                appBar.setTitleText("Game");
                appBar.getActionItems().add(MaterialDesignIcon.FAVORITE.button(e ->
                        System.out.println("Favorite")));
            }
        });
    }

    @FXML
    void onMousePressed(MouseEvent event) {
        System.out.println("Pressed square");
        Node square = ((Node) event.getSource());
//        square.setTranslateX(55);
        GridPane parent = (GridPane) square.getParent();
        PositionSquare emptySqPosition = new PositionSquare(GridPane.getRowIndex(emptySquare), GridPane.getColumnIndex(emptySquare));
        PositionSquare childSqPosition = new PositionSquare(GridPane.getRowIndex(square), GridPane.getColumnIndex(square));
        System.out.println("GridPane: " + parent.getRowConstraints().size() + "-" + parent.getColumnConstraints().size());
        System.out.println("Square layout: " + GridPane.getRowIndex(square) + "-" + GridPane.getColumnIndex(square));
//        emptySquare.setTranslateX(-55);
        if (isMaybeMove(parent.getRowConstraints().size(), parent.getRowConstraints().size(), childSqPosition, emptySqPosition)) {
            GridPane.setColumnIndex(square, emptySqPosition.columnNumber);
            GridPane.setRowIndex(square, emptySqPosition.rowNumber);
            GridPane.setColumnIndex(emptySquare, childSqPosition.columnNumber);
            GridPane.setRowIndex(emptySquare, childSqPosition.rowNumber);
        }


    }

    private void initTimeLine() {
        timeline = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                valueTimer.setText(LocalTime.ofSecondOfDay(++currentNumberSeconds).toString());
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
    }

    private boolean isMaybeMove(int gridRowNumber, int gridColumnNumber, PositionSquare childSqPosition, PositionSquare emptySqPosition) {
        if (childSqPosition.rowNumber + 1 <= gridRowNumber
                && emptySqPosition.rowNumber == childSqPosition.rowNumber + 1
                && emptySqPosition.columnNumber == childSqPosition.columnNumber) {
            return true;
        } else if (childSqPosition.rowNumber - 1 >= 0
                && emptySqPosition.rowNumber == childSqPosition.rowNumber - 1
                && emptySqPosition.columnNumber == childSqPosition.columnNumber) {
            return true;
        } else if (childSqPosition.columnNumber + 1 <= gridColumnNumber
                && emptySqPosition.columnNumber == childSqPosition.columnNumber + 1
                && emptySqPosition.rowNumber == childSqPosition.rowNumber) {
            return true;
        } else if (childSqPosition.columnNumber - 1 >= 0
                && emptySqPosition.columnNumber == childSqPosition.columnNumber - 1
                && emptySqPosition.rowNumber == childSqPosition.rowNumber) {
            return true;
        }
        return false;
    }

    private void setСomplexity(int complexity) {
        List<CourseMovement> courses = new ArrayList<>();
        PositionSquare emptySqPosition = new PositionSquare(GridPane.getRowIndex(emptySquare), GridPane.getColumnIndex(emptySquare));
        GridPane parent = (GridPane) emptySquare.getParent();
        if (emptySqPosition.columnNumber - 1 >= 0)
            courses.add(CourseMovement.LEFT);
        if (emptySqPosition.columnNumber + 1 <= parent.getColumnConstraints().size() - 1)
            courses.add(CourseMovement.RIGHT);
        if (emptySqPosition.rowNumber - 1 >= 0)
            courses.add(CourseMovement.TOP);
        if (emptySqPosition.rowNumber + 1 <= parent.getRowConstraints().size() - 1)
            courses.add(CourseMovement.BOTTOM);

        CourseMovement courseMovement = randomCourse(courses);
        if (courseMovement!=null){
            switch (courseMovement){
                case LEFT:
//                    GridPane.setColumnIndex(square, emptySqPosition.columnNumber);
//                    GridPane.setRowIndex(square, emptySqPosition.rowNumber);
//                    GridPane.setColumnIndex(emptySquare, childSqPosition.columnNumber);
//                    GridPane.setRowIndex(emptySquare, childSqPosition.rowNumber);
            }

//            PositionSquare childSqPosition = new PositionSquare(GridPane.getRowIndex(square), GridPane.getColumnIndex(square));
        }


    }

    private CourseMovement randomCourse(List<CourseMovement> courses){
        if (courses.isEmpty())
            return null;
        else
          return   courses.get(new  Random().nextInt(courses.size()));

    }

    class PositionSquare {
        public PositionSquare(int rowNumber, int columnNumber) {
            this.rowNumber = rowNumber;
            this.columnNumber = columnNumber;
        }

        private int rowNumber;
        private int columnNumber;

        public int getRowNumber() {
            return rowNumber;
        }

        public int getColumnNumber() {
            return columnNumber;
        }

        public void setRowNumber(int rowNumber) {
            this.rowNumber = rowNumber;
        }

        public void setColumnNumber(int columnNumber) {
            this.columnNumber = columnNumber;
        }
    }
}
