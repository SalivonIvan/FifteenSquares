package com.fifteen.squares.views;

import com.fifteen.squares.FifteenSquaresApplication;
import com.fifteen.squares.Setting;
import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.SettingsService;
import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecondaryPresenter {

    @FXML
    private View secondary;
    @FXML
    private Node emptySquare;
    @FXML
    private Label valueTimer;
    @FXML
    private GridPane squaresBox;
    @FXML
    private Label winLabel;
    private Timeline timeline;
    private long currentNumberSeconds = 0;
    private Node[][] winnerPositionSquares;
    private Node[][] targetPositionSquares;
    private boolean isStartGame = false;

    public void initialize() {
        secondary.setShowTransitionFactory(BounceInRightTransition::new);
        valueTimer.setText(LocalTime.ofSecondOfDay(currentNumberSeconds).toString());
        winLabel.visibleProperty().addListener((observable) -> {
        });

        initStartPositionSquares();

        initTimeLineValueTimer();
        initTimeLineWinLabel();

        FloatingActionButton playButton = new FloatingActionButton(MaterialDesignIcon.PLAY_ARROW.text,
                e -> {
                    Button source = (Button) e.getSource();
                    squaresBox.setDisable(false);
                    squaresBox.setOpacity(1);
                    timeline.playFromStart();
                    if (!isStartGame) {
                        valueTimer.setText(LocalTime.ofSecondOfDay(currentNumberSeconds).toString());
                        initStartPositionSquares();
                        setСomplexity(Integer.parseInt(Services.get(SettingsService.class)
                                .map(settingsService -> settingsService.retrieve(Setting.LEVEL_COMPLEXITY))
                                .orElse(Setting.DEFAULT_LEVEL_COMPLEXITY)));
                        isStartGame = true;
                        winLabel.setVisible(false);
                    }

                    System.out.println("Play");

                });

        FloatingActionButton pauseButton = new FloatingActionButton(MaterialDesignIcon.PAUSE.text,
                e -> {
                    squaresBox.setDisable(true);
                    squaresBox.setOpacity(0.6);
                    timeline.stop();
                    System.out.println("Pause");
                });
        playButton.attachTo(pauseButton, Side.LEFT);

        secondary.getLayers().

                add(pauseButton.getLayer());

        secondary.showingProperty().

                addListener((obs, oldValue, newValue) ->

                {
                    if (newValue) {
                        AppBar appBar = MobileApplication.getInstance().getAppBar();
                        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e
                                -> MobileApplication.getInstance().showLayer(FifteenSquaresApplication.MENU_LAYER)));
                        appBar.setTitleText("Game");
                        appBar.getActionItems().add(MaterialDesignIcon.FAVORITE.button(e
                                -> System.out.println("Favorite")));
                    }
                });

        createAnimateSnow();
    }

    private void createAnimateSnow() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Circle snow = new Circle(3, Color.WHITESMOKE);
                Random random = new Random();
                snow.relocate(random.nextInt((int) secondary.getWidth()), -10);
                secondary.getChildren().add(snow);
                PathTransition pt = new PathTransition(Duration.seconds(10), snow);
                Path path = new Path();
                path.getElements().add(new MoveTo(0f, 50f));
                path.getElements().add(new MoveTo(50, secondary.getHeight()));

                pt.setPath(path);
                TranslateTransition ttSnow = new TranslateTransition(Duration.seconds(10), snow);
                ttSnow.setByY(secondary.getHeight());
                ttSnow.setOnFinished(event1 -> {
//                    ball.setTranslateX(random.nextInt((int) secondary.getWidth()));
                    snow.setTranslateX(0);
                    snow.setTranslateY(0);
                    ttSnow.play();
                    System.out.println("Finish snow" + snow.getLayoutY());
                });
                ttSnow.play();
//                pt.play();
            }
        }));

        timeline.setCycleCount(200);
        timeline.play();

//        moveSnow.on
    }

    @FXML
    void onMousePressed(MouseEvent event) {
        System.out.println("Pressed square");
        Node childSquare = ((Node) event.getSource());
        moveSquares(childSquare);
        if (isWinnerPosition()) {
            winLabel.setVisible(true);
            isStartGame = false;
            squaresBox.setDisable(true);
            timeline.stop();
            currentNumberSeconds = 0;
        }

    }

    private void moveSquares(Node childSquare) {
        PositionSquare emptySqPosition = new PositionSquare(GridPane.getRowIndex(emptySquare), GridPane.getColumnIndex(emptySquare));
        PositionSquare childSqPosition = new PositionSquare(GridPane.getRowIndex(childSquare), GridPane.getColumnIndex(childSquare));
//        System.out.println("GridPane: " + squaresBox.getRowConstraints().size() + "-" + squaresBox.getColumnConstraints().size());
//        System.out.println("Square layout: " + GridPane.getRowIndex(childSquare) + "-" + GridPane.getColumnIndex(childSquare));

        if (isMaybeMove(squaresBox.getRowConstraints().size(), squaresBox.getRowConstraints().size(), childSqPosition, emptySqPosition)) {
            GridPane.setColumnIndex(childSquare, emptySqPosition.columnNumber);
            GridPane.setRowIndex(childSquare, emptySqPosition.rowNumber);
            GridPane.setColumnIndex(emptySquare, childSqPosition.columnNumber);
            GridPane.setRowIndex(emptySquare, childSqPosition.rowNumber);
        }
    }

    private void initTimeLineValueTimer() {
        timeline = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                valueTimer.setText(LocalTime.ofSecondOfDay(++currentNumberSeconds).toString());
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
    }

    private void initTimeLineWinLabel() {
        KeyValue kv1 = new KeyValue(winLabel.textFillProperty(), Color.RED);
        KeyValue kv2 = new KeyValue(winLabel.textFillProperty(), Color.GREEN);
        KeyFrame kf1 = new KeyFrame(Duration.ZERO, kv1);
        KeyFrame kf2 = new KeyFrame(Duration.millis(500), kv2);
        Timeline timeline = new Timeline(kf1, kf2);
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
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
        for (int i = 0; i < complexity; i++) {
            List<CourseMovement> courses = new ArrayList<>();
            PositionSquare emptySqPosition = new PositionSquare(GridPane.getRowIndex(emptySquare), GridPane.getColumnIndex(emptySquare));

            if (emptySqPosition.columnNumber - 1 >= 0) {
                courses.add(CourseMovement.LEFT);
            }
            if (emptySqPosition.columnNumber + 1 <= squaresBox.getColumnConstraints().size() - 1) {
                courses.add(CourseMovement.RIGHT);
            }
            if (emptySqPosition.rowNumber - 1 >= 0) {
                courses.add(CourseMovement.TOP);
            }
            if (emptySqPosition.rowNumber + 1 <= squaresBox.getRowConstraints().size() - 1) {
                courses.add(CourseMovement.BOTTOM);
            }

            CourseMovement courseMovement = randomCourse(courses);
            if (courseMovement != null) {
                Node childSquare = null;
                switch (courseMovement) {
                    case LEFT:
                        childSquare = targetPositionSquares[emptySqPosition.rowNumber][emptySqPosition.columnNumber - 1];
                        moveSquares(childSquare);
                        targetPositionSquares[emptySqPosition.rowNumber][emptySqPosition.columnNumber - 1] = emptySquare;
                        targetPositionSquares[emptySqPosition.rowNumber][emptySqPosition.columnNumber] = childSquare;
                        break;
                    case RIGHT:
                        childSquare = targetPositionSquares[emptySqPosition.rowNumber][emptySqPosition.columnNumber + 1];
                        moveSquares(childSquare);
                        targetPositionSquares[emptySqPosition.rowNumber][emptySqPosition.columnNumber + 1] = emptySquare;
                        targetPositionSquares[emptySqPosition.rowNumber][emptySqPosition.columnNumber] = childSquare;
                        break;
                    case TOP:
                        childSquare = targetPositionSquares[emptySqPosition.rowNumber - 1][emptySqPosition.columnNumber];
                        moveSquares(childSquare);
                        targetPositionSquares[emptySqPosition.rowNumber - 1][emptySqPosition.columnNumber] = emptySquare;
                        targetPositionSquares[emptySqPosition.rowNumber][emptySqPosition.columnNumber] = childSquare;
                        break;
                    case BOTTOM:
                        childSquare = targetPositionSquares[emptySqPosition.rowNumber + 1][emptySqPosition.columnNumber];
                        moveSquares(childSquare);
                        targetPositionSquares[emptySqPosition.rowNumber + 1][emptySqPosition.columnNumber] = emptySquare;
                        targetPositionSquares[emptySqPosition.rowNumber][emptySqPosition.columnNumber] = childSquare;
                        break;

                }
            }
        }

    }

    private CourseMovement randomCourse(List<CourseMovement> courses) {
        if (courses.isEmpty()) {
            return null;
        } else {
            return courses.get(new Random().nextInt(courses.size()));
        }

    }

    private void initStartPositionSquares() {
        int countRow = squaresBox.getRowConstraints().size();
        int countColumn = squaresBox.getColumnConstraints().size();
        targetPositionSquares = new Pane[countRow][countColumn];
        winnerPositionSquares = new Pane[countRow][countColumn];
        for (Node node : squaresBox.getChildren()) {
            int rowI = GridPane.getRowIndex(node);
            int columnI = GridPane.getColumnIndex(node);
            targetPositionSquares[rowI][columnI] = node;
            winnerPositionSquares[rowI][columnI] = node;
        }
    }

    private boolean isWinnerPosition() {
        for (int i = 0; i < squaresBox.getRowConstraints().size(); i++) {
            for (int j = 0; j < squaresBox.getColumnConstraints().size(); j++) {
                Node square = winnerPositionSquares[i][j];
                if (i != GridPane.getRowIndex(square) || j != GridPane.getColumnIndex(square)) {
                    return false;
                }
            }
        }
        return true;
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
