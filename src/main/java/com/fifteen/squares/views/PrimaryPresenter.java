package com.fifteen.squares.views;

import com.fifteen.squares.FifteenSquaresApplication;
import com.fifteen.squares.Setting;
import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.SettingsService;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PrimaryPresenter {

    @FXML
    private View primary;

    @FXML
    private TextField valueLevelComplexity;

    public void initialize() {
        primary.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setSpacing(20);
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        MobileApplication.getInstance().showLayer(FifteenSquaresApplication.MENU_LAYER)));
                appBar.setTitleText("Setting");
//                appBar.getActionItems().add(MaterialDesignIcon.DESCRIPTION.button(e ->
//                        System.out.println("Setting")));
//                appBar.getActionItems().add(MaterialDesignIcon.GAMEPAD.button(e ->
//                        System.out.println("Game")));
//                appBar.getActionItems().add(MaterialDesignIcon.EXIT_TO_APP.button(e ->
//                        System.out.println("Exit")));
                initValueLevelComplexity();
            }
        });
    }

    @FXML
    void buttonClick() {
        Services.get(SettingsService.class).ifPresent(service -> {
            if (valueLevelComplexity.getText() == null || valueLevelComplexity.getText().trim().equals(""))
                valueLevelComplexity.setText("0");
            service.store(Setting.LEVEL_COMPLEXITY, valueLevelComplexity.getText());
        });
    }

    private void initValueLevelComplexity() {
        Services.get(SettingsService.class).ifPresent(service -> {
            String level = service.retrieve(Setting.LEVEL_COMPLEXITY);
            if (level == null) {
                level = Setting.DEFAULT_LEVEL_COMPLEXITY;
                service.store(Setting.LEVEL_COMPLEXITY, level);
            }
            valueLevelComplexity.setText(level);
        });
    }

}
