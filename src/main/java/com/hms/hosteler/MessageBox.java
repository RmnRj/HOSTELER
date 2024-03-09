package com.hms.hosteler;

import javafx.scene.control.Alert;

public class MessageBox {

    public MessageBox(String H, String C) {
        Alert A = new Alert(Alert.AlertType.INFORMATION);
        A.setTitle(H);
        A.setContentText(C);
        A.showAndWait();
    }

    public MessageBox(String C) {
        Alert A = new Alert(Alert.AlertType.INFORMATION);
        A.setContentText(C);
        A.showAndWait();
    }
}
