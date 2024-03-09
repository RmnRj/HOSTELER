package com.hms.hosteler;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.Optional;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;

import static javafx.fxml.FXMLLoader.load;
//import javafx.fxml.Initializable;


public class DashboardController {
    String _url = new save().getDbms_url();
    String _user = new save().getDbms_user();
    String _password = new save().getDbms_password();

    @FXML
    private BorderPane innerBoderpane;


    Node file;

    //----------------------------------------Functions----------------------------------------------
//    @FXML
//    protected void Passer(URL url) throws IOException {
//        AnchorPane A = FXMLLoader.<AnchorPane>load(url);
//        Loader(A);
//
    @FXML
    public void Loader(Node A){
        innerBoderpane.setCenter(A);
    }
    //-----------------------------------------Functions--------------------------------------------
    //----------------------------------------ActionEvents-------------------------------------------
    //--------------------------Main modules-------------------------------
    @FXML
    private void toHome() {
        try {
            file = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("modules/Home/home.fxml")));
            Loader(file);
        }catch (IOException | NullPointerException e) {
            System.out.println("Showing toHome"+e.getMessage());
            new MessageBox("Error");
        }
    }
    @FXML
    private void toProfile(){
        try {
            file = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("modules/Employee/Profile.fxml")));
            Loader(file);
        } catch (IOException e) {
            System.out.println("Showing toProfile"+e.getMessage());
            new MessageBox("Error");
        }
    }
    @FXML
    private void toSetting()  {
        try {
            file = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("modules/Setting/Setting.fxml")));
            Loader(file);
        }catch (IOException | NullPointerException e) {
            System.out.println("toSetting" + e.getMessage());
            new MessageBox("Error");
        }
    }
    @FXML
    private void toStudent() {
        try {
            AnchorPane file = load(Objects.requireNonNull(getClass().getResource("modules/Student/Student.fxml")));
            Loader(file);
        }catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error");
        }
    }
    @FXML
    private void toEmployee()  {
        try {
            file = load(Objects.requireNonNull(getClass().getResource("modules/Employee/Employee.fxml")));
            Loader(file);
        }catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error");
        }
    }
    @FXML
    private void toRoom()  {
        try {
            file = load(Objects.requireNonNull(getClass().getResource("modules/Room/Room.fxml")));
            Loader(file);
        }catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error");
        }
    }
    @FXML
    private void toVisitor()  {
        try {
            file = load(Objects.requireNonNull(getClass().getResource("modules/Visitor/visitor.fxml")));
            Loader(file);
        }catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error" + e.getMessage());
        }
    }
    @FXML
    private void toBilling() {
        if(!(new Permission().permission()))
        {
            new MessageBox("Invalid Access","You do not have permission to do billing.");
            return;
        }
        try {
            file = load(Objects.requireNonNull(getClass().getResource("modules/Billing/billing.fxml")));
            Loader(file);
        }catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error");
        }
    }

    @FXML
    private void toAbout() {
        try {
            file = load(Objects.requireNonNull(getClass().getResource("modules/About/About.fxml")));
            Loader(file);
        }catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error");
        }
    }

    @FXML
    private void actionLogout()  {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(alert.getOwner());
        alert.setTitle("LOGOUT");
        alert.setContentText("You're about to logout. Click \"Ok\" to logout successfully\nor click \"Cancel\" to cancel this process.\n");

        ButtonType Ok = new ButtonType("Ok", ButtonBar.ButtonData.YES);
        ButtonType Cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(Ok, Cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == Ok) {
                Platform.exit(); // Exit main program
            }
        }
    }
    //LogOUT-----------------------------
    //--------------------------Main modules-------------------------------
    //----------------------------------------------------------------------------------------------------
    //--------------------------Fast Action Sub Modules-------------------------------

    @FXML
    void studentReg() throws IOException{
//        try {
            file = load(Objects.requireNonNull(getClass().getResource("modules/Student/StudentRegistration.fxml")));
            Loader(file);
//        }catch (IOException e) {
//            System.out.println(e.getMessage());
//            new MessageBox("Error");
//        }
    }
    @FXML
    void studentDetails(){
        try {
            file = load(Objects.requireNonNull(getClass().getResource("modules/Student/StudentDetails.fxml")));
            Loader(file);
        }catch (IOException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error");
        }
    }
    @FXML
    void DeleteStudent() {
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to Delete Student.");
            return;
        }
        try {
            file = load(Objects.requireNonNull(getClass().getResource("modules/Student/DeleteStudent.fxml")));
            Loader(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error");
        }
    }

    @FXML
    void addEmp() {
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to Add Student.");
            return;
        }
        try {
            file = load(Objects.requireNonNull(getClass().getResource("modules/Employee/AddEmployee.fxml")));
            Loader(file);
        }catch (IOException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error");
        }
    }
    @FXML
    void EmpDetails() {
        try {
            file = load(Objects.requireNonNull(getClass().getResource("modules/Employee/EmployeeDetails.fxml")));
            Loader(file);
        }catch (IOException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error");
        }
    }

    @FXML
    void DeleteEmp() {
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to Delete Employee.");
            return;
        }
        try {
            file = load(Objects.requireNonNull(getClass().getResource("modules/Employee/DeleteEmp.fxml")));
            Loader(file);
        }catch (IOException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error");
        }
    }

    @FXML
    void RoomDetail() {
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to visit Room Details.");
            return;
        }
        try{
        file = load(Objects.requireNonNull(getClass().getResource("modules/Room/RoomDetails.fxml")));
        Loader(file);
        }catch (IOException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error");
        }
    }
    @FXML
    void allRoomsDetails(){
        try {
            file = load(Objects.requireNonNull(getClass().getResource("modules/Room/AllRoomDetails.fxml")));
            Loader(file);
        }catch (IOException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error");
        }
    }
    @FXML
    void AllocateRoom(){
        try {
            file = load(Objects.requireNonNull(getClass().getResource("modules/Room/AllocateRoom.fxml")));
            Loader(file);
        }catch (IOException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error");
        }
    }
    @FXML
    void VisitorForm() {
        try {
            file = load(Objects.requireNonNull(getClass().getResource("modules/Visitor/visitorform.fxml")));
            Loader(file);
        }catch (IOException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error");
        }
    }
    @FXML
    void VisitorDetails() {
        try {
            file = load(Objects.requireNonNull(getClass().getResource("modules/Visitor/VisitorDetails.fxml")));
            Loader(file);
        }catch (IOException e) {
            System.out.println(e.getMessage());
            new MessageBox("Error");
        }
    }

    //--------------------------Fast Action Sub Modules-------------------------------
    //----------------------------------------ActionEvents-------------------------------------------
}
