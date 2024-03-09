package com.hms.hosteler;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class sceneSwitchController {
    public sceneSwitchController(AnchorPane currentAnchorpane, String fxml){
//        try {
//            AnchorPane nextAnchorpane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(fxml)));
//            currentAnchorpane.getChildren().removeAll();
//            currentAnchorpane.getChildren().setAll(nextAnchorpane);
//        }catch (IOException e){
//            System.out.println("sceneSwitchController error" + e.getMessage());
//        }
        try {
            URL resourceUrl = Main.class.getResource(fxml);
            if (resourceUrl != null) {
                AnchorPane nextAnchorpane = FXMLLoader.load(resourceUrl);
                currentAnchorpane.getChildren().removeAll();
                currentAnchorpane.getChildren().setAll(nextAnchorpane);
            } else {
                System.out.println("FXML file not found: " + fxml);
            }
        } catch (IOException e) {
            System.out.println("sceneSwitchController error: " + e.getMessage());
        }
    }
}