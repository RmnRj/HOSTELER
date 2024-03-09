package com.hms.hosteler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SETUPProcess {
    @FXML
    private Label NewLunch_Password;

    @FXML
    private Label NewLunch_UserID;

    @FXML
    private AnchorPane anchorSETUP2;

    @FXML
    private Button btn_OK_setup3;


    @FXML
    void initialize() {
        NewLunch_Password.setText("Apple12");
        NewLunch_UserID.setText(new save().getTemp());
    }

    @FXML
    void toLogin() {
       String fxml ="Login.fxml";
       AnchorPane anr= anchorSETUP2;
       Button btn = btn_OK_setup3;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
            Parent P = fxmlLoader.load();// Load Dashboard in fxmlLoader
            int x = 650, y = 450;
            Stage Lunch_stage = new Stage();
            Lunch_stage.setScene(new Scene(P));

            Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("img/z1_SecondIcon.png")));
            Lunch_stage.getIcons().add(image);
            Lunch_stage.setTitle("HOSTELER");

            Lunch_stage.setMaxWidth(x);
            Lunch_stage.setMinWidth(x);
            Lunch_stage.setMinHeight(y);
            Lunch_stage.setMaxHeight(y);
            anr.getChildren().removeAll();
            Stage stage1 = (Stage) btn.getScene().getWindow();
            stage1.close(); // Close Login page

            Lunch_stage.show(); // show Dashboard
        }catch (IOException e){
            new MessageBox("System Error");
            System.out.println(e.getMessage());
        }
    }
}
