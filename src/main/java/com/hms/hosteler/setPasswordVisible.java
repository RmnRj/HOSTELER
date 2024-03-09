package com.hms.hosteler;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class setPasswordVisible {
    private final Image OpenEye = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/hide.png")));
    private final Image CloseEye = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/show.png")));
    public setPasswordVisible(PasswordField HiddenPassword, TextField TextPassword, ImageView EyeIcon) {
        boolean isPasswordVisible = HiddenPassword.isVisible(); // return true if PasswordField is visible.
        HiddenPassword.setVisible(!isPasswordVisible);
//        HiddenPassword.setDisable(isPasswordVisible); // making disable
//        TextPassword.setDisable(!isPasswordVisible); //making enable
        TextPassword.setVisible(isPasswordVisible);
        EyeIcon.setImage(isPasswordVisible ? CloseEye : OpenEye);
        if(isPasswordVisible) {
            TextPassword.setText(HiddenPassword.getText());
        }else {
            HiddenPassword.setText(TextPassword.getText());
        }
    }
    public setPasswordVisible(){}
    public void textSetter(PasswordField HiddenPassword, TextField TextPassword){
        if (TextPassword.isVisible()){
            HiddenPassword.setText(TextPassword.getText());
        }
        if (HiddenPassword.isVisible()){
            TextPassword.setText(HiddenPassword.getText());
        }
    }
}