package com.fifteen.squares.views;

import com.fifteen.squares.FifteenSquaresApplication;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PrimaryPresenter {

    @FXML
    private View primary;

    @FXML
    private Label label;

    public void initialize() {
        primary.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setSpacing(20);
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        MobileApplication.getInstance().showLayer(FifteenSquaresApplication.MENU_LAYER)));
                appBar.setTitleText("Menu");
                appBar.getActionItems().add(MaterialDesignIcon.DESCRIPTION.button(e ->
                        System.out.println("About")));
                appBar.getActionItems().add(MaterialDesignIcon.GAMEPAD.button(e ->
                        System.out.println("Game")));
                appBar.getActionItems().add(MaterialDesignIcon.EXIT_TO_APP.button(e ->
                        System.out.println("Exit")));
//                initButtons(appBar);
            }
        });
    }
    
    @FXML
    void buttonClick() {
        label.setText("Hello JavaFX Universe!");
    }

    private void initButtons(AppBar appBar){
        FloatingActionButton aboutButton =new FloatingActionButton(MaterialDesignIcon.DESCRIPTION.text,
                e -> {
                        System.out.println("About");

                });
        FloatingActionButton gameButton =new FloatingActionButton(MaterialDesignIcon.GAMEPAD.text,
                e -> {
                    System.out.println("Game");

                });
        FloatingActionButton exitButton =new FloatingActionButton(MaterialDesignIcon.EXIT_TO_APP.text,
                e -> {
                    System.out.println("Exit");

                });
        aboutButton.attachTo(gameButton, Side.LEFT);
        gameButton.attachTo(exitButton, Side.RIGHT);
        appBar.getActionItems().add(exitButton.getLayer());
    }
    
}
