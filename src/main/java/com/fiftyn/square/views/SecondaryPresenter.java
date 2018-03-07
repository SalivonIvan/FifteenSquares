package com.fiftyn.square.views;

import com.fiftyn.square.FiftynSquareApplication;
import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class SecondaryPresenter {

    @FXML
    private View secondary;
    @FXML
    private Node emptySquare;

    public void initialize() {
        secondary.setShowTransitionFactory(BounceInRightTransition::new);

        secondary.getLayers().add(new FloatingActionButton(MaterialDesignIcon.INFO.text,
                e -> System.out.println("Info")).getLayer());

        secondary.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        MobileApplication.getInstance().showLayer(FiftynSquareApplication.MENU_LAYER)));
                appBar.setTitleText("Secondary");
                appBar.getActionItems().add(MaterialDesignIcon.FAVORITE.button(e ->
                        System.out.println("Favorite")));
            }
        });
    }

    @FXML
    void onMousePressed(MouseEvent event) {
        System.out.println("Pressed schape");
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
