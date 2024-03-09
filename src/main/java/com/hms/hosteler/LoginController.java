package com.hms.hosteler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class LoginController {

    @FXML
    private TextField PasswordText, UserID;
    @FXML
    private PasswordField PasswordPassword;
    @FXML
    ImageView EyeIcon;
    @FXML
    private AnchorPane anchorL;
    @FXML
    private Button btn_login;
    @FXML
    private Label label;
    // ID of outermost Anchor pane ID;
    String _url = new save().getDbms_url();
    String _user = new save().getDbms_user();
    String _password = new save().getDbms_password();

    @FXML
    void switch2signup() {
        if(checkInitializer){
            new MessageBox("Initiate the setup","It seems like its your first time in HOSTELER. Lets\nSETUP begin.");
            NEW_LUNCH();
            return;
        }
        new sceneSwitchController(anchorL, "Signup.fxml");
    }

    @FXML
    void switch2forgot() {
        if(checkInitializer){
            new MessageBox("Initiate the setup","It seems like its your first time in HOSTELER. Lets\nSETUP begin.");
            NEW_LUNCH();
            return;
        }
        new sceneSwitchController(anchorL, "forgetPass.fxml");
    }

    boolean checkInitializer;

    @FXML
    void initialize(){
        checkUsers();
        checkInitializer = PropertiesReader.getUserExist().equals("false");
//        if(PropertiesReader.getUserExist().equals("false")){

    }

    void checkUsers(){
        try{
            Connection con = DriverManager.getConnection(_url,_user,_password);
            PreparedStatement pp = con.prepareStatement("select count(*) from login");
            ResultSet rs = pp.executeQuery();
            if(rs.next()) {
                int count = rs.getInt(1);
                if (count == 0) {
                    PreparedStatement pp2 = con.prepareStatement("truncate table employee");
                    pp2.executeUpdate();
                    PropertiesReader.setUserExist("false");
                    PropertiesReader.setBillNo("1000");
                }
            }rs.close();
            pp.close();
            con.close();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //---------------------------- Load Dashboard --------------------------------
    @FXML
    void ShowDashboard() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent P = fxmlLoader.load();// Load Dashboard in fxmlLoader

        FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("modules/Home/home.fxml"));
        Parent homePane = homeLoader.load(); // load home.fxml

        DashboardController dashboardController = fxmlLoader.getController(); //getController

        dashboardController.Loader(homePane);//call loader
        Stage DashboardStage = new Stage();
        DashboardStage.setScene(new Scene(P));

        Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("img/z1_SecondIcon.png")));
        DashboardStage.getIcons().add(image);
        DashboardStage.setTitle("HOSTELER");

        DashboardStage.setMinWidth(1000);
        DashboardStage.setMinHeight(500);
        DashboardStage.setMaximized(true);

        anchorL.getChildren().removeAll();
        Stage stage1 = (Stage) btn_login.getScene().getWindow();
        stage1.close(); // Close Login page

        DashboardStage.show(); // show Dashboard

    }

    String userid = null;
    String password = null;

    @FXML
    private void loginFun(){

            new setPasswordVisible().textSetter(PasswordPassword,PasswordText);
            userid = UserID.getText();
            password = PasswordPassword.getText();

        if(checkInitializer){
            new MessageBox("Initiate the setup","It seems like its your first time in HOSTELER. Lets\nSETUP begin.");
            NEW_LUNCH();
            return;
        }

            if (userid.isEmpty() || password.isEmpty()) {
                label.setText("Please enter both UserID and Password.");
                return;
            }

            if (checker()) {
                return;
            }
            try {
                Connection con = DriverManager.getConnection(_url, _user, _password);
                String query2 = "SELECT COUNT(*) FROM login WHERE UserId=? AND Password=?";
                PreparedStatement statement1 = con.prepareStatement(query2);
                statement1.setString(1, userid); // put userID in first '?' in userid = ?
                statement1.setString(2, password); // same as UserID
                ResultSet resultSet = statement1.executeQuery();
                resultSet.next();
                int x = resultSet.getInt(1);
                resultSet.close();
                statement1.close();
                con.close();

                if (x == 1) {  // Successful login
                    // Getting HostelerId
                    Connection con2 = DriverManager.getConnection(_url, _user, _password);
                    String query3 = "SELECT HostelerId,Post FROM employee natural join login WHERE UserId=?";
                    PreparedStatement statement2 = con2.prepareStatement(query3);
                    statement2.setString(1, userid);
                    ResultSet resultSet1 = statement2.executeQuery();
                    resultSet1.next();
                    String hosteler_id = resultSet1.getString("HostelerId");
                    String post = resultSet1.getString("Post");
                    if (post.isEmpty()) {
                        post = "Employee";
                    }
                    new save().setUSER_ID(userid);
                    new save().setHOSTELER_ID(hosteler_id);
                    new save().setUserPost(post);
                    resultSet1.close();
                    statement2.close();
                    con2.close();
                    PropertiesReader.setUserExist("true");
                    ShowDashboard();  // Calling to dashboard loading method
                }
                password = null;
                userid = null;
                // Invalid credentials
                label.setText("Invalid Credentials");
            } catch (SQLException | IOException e) {
                label.setText("System Error");
                System.out.println(e.getMessage());
            }
    }

    boolean checker() { // this is for checking login with default password.
        try {
            Connection con2 = DriverManager.getConnection(_url, _user, _password);
            String query3 = "SELECT UserID, HostelerId FROM login WHERE UserId=?";
            PreparedStatement statement2 = con2.prepareStatement(query3);
            statement2.setString(1, userid);
            ResultSet resultSet1 = statement2.executeQuery();
            resultSet1.next();
            String u = resultSet1.getString(1);
            String h = resultSet1.getString(2);
            resultSet1.close();
            statement2.close();
            con2.close();
            if (u.equals(h)) {
                new MessageBox("Change Default password first from Signup then login.");
                return true;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    //---------------------------- Load Dashboard -----------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------------------------Show Hide Password ------------------------
    @FXML
    private void LoginPassword() {
        new setPasswordVisible(PasswordPassword, PasswordText, EyeIcon);
    }

    //------------------------Show Hide Password -----------------------
    //------------------------ click "Enter" button for Login -----------------------
    @FXML
    void LoginHandel2nd(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            loginFun();
        }
    }
    // ------------------------ click "Enter" button for Login -----------------------

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------------------------- Set up Hosteler if this first time ---------------
    //------------------------------ New Lunch ---------------------------------------


    @FXML
    void NEW_LUNCH() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SETUPProcess.fxml"));
            Parent P = fxmlLoader.load();// Load SETUP in fxmlLoader

            int x = 620, y = 450;
//        DashboardController dashboardController = fxmlLoader.getController(); //getController

            Stage SETUP_stage = new Stage();
            SETUP_stage.setScene(new Scene(P));

            Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("img/z1_SecondIcon.png")));
            SETUP_stage.getIcons().add(image);
            SETUP_stage.setTitle("HOSTELER / Setup-1 ");
            SETUP_stage.setMaxWidth(x);
            SETUP_stage.setMaxHeight(y);
            SETUP_stage.setMinWidth(x);
            SETUP_stage.setMinHeight(y);

            anchorL.getChildren().removeAll();
            Stage stage1 = (Stage) btn_login.getScene().getWindow();
            stage1.close(); // Close Login page

            SETUP_stage.show(); // show Dashboard

        } catch (IOException e) {
            new MessageBox("Error in SETUP Process");
            System.out.println(e.getMessage());
        }
    }
    //------------------------------ New Lunch ---------------------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //-------------------------------- New Set UP ------------------------------------------
    @FXML
    private AnchorPane anchorSETUP;

    @FXML
    private Pane setup1;

    @FXML
    private Pane setup2;


    @FXML // For exit Button
    void CLOSE(){
        Platform.exit();
    }
    private void changeScene(Pane pane1, Pane pane2) {//(currentPaneID, nextPaneID)
        if (pane1 != null && pane2 != null) {
            pane1.visibleProperty().set(false);
            pane1.setDisable(true);
            pane2.setDisable(false);
            pane2.visibleProperty().set(true);
        } else {
            System.out.println("Error: One or both panes are null.");
        }
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //----------------------- Page 1 ---------------------------------------------
    @FXML
    void toPage2(){
        changeScene(setup1,setup2);
    }
    //----------------------- Page 1 ---------------------------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //----------------------- Page 2 ---------------------------------------------
    @FXML
    private Button btn_Next_setup2;

    @FXML
    void toPage1(){
        changeScene(setup2,setup1);
    }


    @FXML
    void toAddEmp(){
        try {
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("modules/Employee/AddEmployee.fxml"));
            Parent root = loader.load();

            int x = 870;
            int y  = 600;
            Employee controller = loader.getController();


            new save().setSetup();
            Stage Stage_AddEmp = new Stage();
            Scene scene = new Scene(root);
            Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("img/z1_SecondIcon.png")));
            Stage_AddEmp.getIcons().add(image);
            Stage_AddEmp.setTitle("HOSTELER / Setup-1 / Add-Employee");
            Stage_AddEmp.setMinWidth(x);
            Stage_AddEmp.setMinHeight(y);
            Stage_AddEmp.setScene(scene);
            Stage_AddEmp.show();

            //Closing Window
            anchorSETUP.getChildren().removeAll();
            Stage stage1 = (Stage) btn_Next_setup2.getScene().getWindow();
            stage1.close();

        } catch (IOException e) {
            new MessageBox("SetUp Error", "Please, restart this software.");
            System.out.println("SetUp Error" + e.getMessage());
        }
    }
    //----------------------- Page 1 ---------------------------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //-----------------------  page 3  ---------------------------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}