package com.hms.hosteler;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


import java.io.IOException;
import java.sql.*;

public class SignupController {

    String _url = new save().getDbms_url();
    String _user = new save().getDbms_user();
    String _password = new save().getDbms_password();
    @FXML
    private Label label_message, label_message1;

    @FXML
    private TextField NewUserID;

    @FXML
    private PasswordField DefaultPassword_PasswordField;

    @FXML
    private TextField DefaultPassword_TextField;

    @FXML
    private TextField HostelerID_Signup;

    @FXML
    private PasswordField NewPassConfirm_PasswordField;

    @FXML
    private TextField NewPassConfirm_TextField;

    @FXML
    private TextField NewPassword_TextField;

    @FXML
    private ImageView EyeIcon1;

    @FXML
    private ImageView EyeIcon2;

    @FXML
    private PasswordField NewPassword_PasswordField;

    @FXML
    private Pane PaneCreateNewPass;

    @FXML
    private Pane PaneDefaultPass;

    @FXML
    private AnchorPane anchorS;

    @FXML
    void switch2login() throws IOException { //  Login
        new sceneSwitchController(anchorS, "Login.fxml");
    }

    private void changePane(Pane pane1, Pane pane2) //(currentPaneID, nextPaneID)
    {// Function to make visible Pane
        pane1.visibleProperty().set(false);
        pane1.setDisable(true);
        pane2.setDisable(false);
        pane2.visibleProperty().set(true);
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //----------------------- password visibility -----------------------------
    @FXML
    private void NewPassword()  {
        new setPasswordVisible(NewPassword_PasswordField, NewPassword_TextField, EyeIcon1);
    }

    @FXML
    private void NewConfirmPassword()  { new setPasswordVisible(NewPassConfirm_PasswordField, NewPassConfirm_TextField, EyeIcon2);}

    @FXML
    private ImageView EyeIcon; // this is ID for eyeIcon in Default Password;
    @FXML
    private void DefPassword()  {
        new setPasswordVisible(DefaultPassword_PasswordField, DefaultPassword_TextField, EyeIcon);
    }
    //----------------------- password visibility -----------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    @FXML // this show create password pane
    void ShowCreatePass_Pane() {
        changePane(PaneDefaultPass, PaneCreateNewPass);
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------- Functions -----------------------
    @FXML
    void AfterDefaultPassFun() {
        new setPasswordVisible().textSetter(DefaultPassword_PasswordField,DefaultPassword_TextField);
       try {
           Connection con = DriverManager.getConnection(_url, _user, _password);
           String query1 = "SELECT COUNT(*) FROM login WHERE HostelerId=? AND Password=?";
           PreparedStatement statement1 = con.prepareStatement(query1);
           statement1.setString(1, HostelerID_Signup.getText());
           statement1.setString(2, DefaultPassword_PasswordField.getText());
           ResultSet resultSet = statement1.executeQuery();

           if (resultSet.next()) {
               int count = resultSet.getInt(1);
               if (count == 1) {
                   ShowCreatePass_Pane(); // Switch toLogin
               } else {
                   new MessageBox("Error", "Invalid HostelerId or DefaultPassword");
               }
           }
           resultSet.close();
           statement1.close();
           con.close();
       }catch (SQLException e){
           // Handle the case where no rows are returned
           new MessageBox("Error", "No matching user found");
           System.out.println(e.getMessage());
       }
    }




    @FXML
    void AfterNewPassFun() {
        new setPasswordVisible().textSetter(NewPassConfirm_PasswordField,NewPassConfirm_TextField);
        new setPasswordVisible().textSetter(NewPassword_PasswordField,NewPassword_TextField);
        try {
            // Establish database connection
            Connection con = DriverManager.getConnection(_url, _user, _password);
            // Check if UserId already exists
            String qq = "select count(*) from login where UserId = ?";  // Use PreparedStatement for safer queries
            PreparedStatement stm = con.prepareStatement(qq);
            stm.setString(1, NewUserID.getText());
            ResultSet result = stm.executeQuery();
            result.next();
            int x = result.getInt(1);
            result.close();
            stm.close();
            con.close();

            // Validate new password and confirm password
            String newpass = NewPassword_PasswordField.getText();
            String newpassconforim = NewPassConfirm_PasswordField.getText();
            String newUser = NewUserID.getText();

            if (x == 1) {
                label_message1.setText("UserID is already exist. Try another.");
                NewUserID.setText("");
            }

            // Password Validation
            if(!(new Validation().isPasswordValid(newpass))) {
                new MessageBox("Weak Password", "Please create strong password.\nPoint to be noted while creating password:\n1. Minimum 6 digit\n2. Atleast One uppercase, one lowercase character\n3. Use atleast one digit character\n4. One special character like @,$,&,#,etc.\n");
                return;
            }
                if (!(newpass.equals(newpassconforim))) {
                    label_message.setText("Confirm password is not match.");
                }// Exit if validation fails
                else {
                    // Update the login table with new UserId and password
                    Connection conn = DriverManager.getConnection(_url, _user, _password);
                    String query1 = "UPDATE login SET UserId = ?, Password = ? where HostelerId = ?"; // Set values separately
                    PreparedStatement statement1 = conn.prepareStatement(query1);
                    statement1.setString(1, newUser);
                    statement1.setString(2, newpass);
                    statement1.setString(3, HostelerID_Signup.getText());
                    int rowsUpdated = statement1.executeUpdate();
                    statement1.close();
                    conn.close();

                    // Handle successful update
                    if (rowsUpdated > 0) {
                        new MessageBox("SignUp Successful", "Congratulation to being new user of this system.");
                        switch2login(); // Redirect to login page
                    } else {
                        new MessageBox("Error", "Invalid HostelerId or DefaultPassword"); // Display error message
                    }
                }

        }catch (SQLException | IOException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void EnterHandle1st(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            AfterDefaultPassFun();
        }
    }
    @FXML
    void EnterHandle2nd(KeyEvent event)  {
        if (event.getCode() == KeyCode.ENTER) {
            AfterNewPassFun();
        }
    }
    //------- Functions -----------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
