package com.hms.hosteler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    int x = 650, y = 450;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),x,y);


        Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("img/z1_SecondIcon.png")));
        stage.getIcons().add(image);
        stage.setTitle("HOSTELER");
        stage.setScene(scene);

        stage.setMaxWidth(x);
        stage.setMinWidth(x);
        stage.setMinHeight(y);
        stage.setMaxHeight(y);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}