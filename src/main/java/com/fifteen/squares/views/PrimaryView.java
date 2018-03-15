package com.fifteen.squares.views;

import com.gluonhq.charm.glisten.mvc.View;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class PrimaryView {

    public static final String LEVEL_COMPLEXITY = "level.complexity";
    public static final String DEFAULT_LEVEL_COMPLEXITY = "10";

    private final String name;

    public PrimaryView(String name) {
        this.name = name;
    }
    
    public View getView() {
        try {
            View view = FXMLLoader.load(PrimaryView.class.getResource("primary.fxml"));
            view.setName(name);
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View(name);
        }
    }
}
