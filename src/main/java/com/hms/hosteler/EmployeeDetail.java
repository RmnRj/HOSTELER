package com.hms.hosteler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class EmployeeDetail {

    String _url = new save().getDbms_url();
    String _user = new save().getDbms_user();
    String _password = new save().getDbms_password();


    @FXML
    private TextField EDSearchBy_ID_NAME;

    @FXML
    private TableColumn<Emp, String> ED_Address;

    @FXML
    private TableColumn<Emp,Integer> ED_Age;

    @FXML
    private TableColumn<Emp, String> ED_HID;

    @FXML
    private TableColumn<Emp,String> ED_JoinDate;

    @FXML
    private TableColumn<Emp, String> ED_Leave_Date;

    @FXML
    private TableColumn<Emp, String> ED_Name;

    @FXML
    private TableColumn<Emp, String> ED_Post;

    @FXML
    private ImageView ED_Search_Icon;

    @FXML
    private TableColumn<Emp, String> ED_Sex;

    @FXML
    private TableView<Emp> ED_Table;

    @FXML
    private Button ED_btn_ExEmp;

    @FXML
    private Button ED_btn_ViewProfile;

    @FXML
    private Label Lbl_count,Lbl_Emp_ExEmp;

    String hid, name, sex, post, address, join, leave;
    int age;

    boolean checker;

    private final ObservableList<Emp> EmpData = FXCollections.observableArrayList();
     private final ObservableList<Emp> ExEmpData = FXCollections.observableArrayList();

    boolean b;
    @FXML
    void initialize() {
        // if user is not warden then some feature will be disabled.
         b = new Permission().permission();

        ED_HID.setCellValueFactory(new PropertyValueFactory<>("hid"));
        ED_Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        ED_Age.setCellValueFactory(new PropertyValueFactory<>("age"));
        ED_Sex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        ED_Post.setCellValueFactory(new PropertyValueFactory<>("post"));
        ED_Address.setCellValueFactory(new PropertyValueFactory<>("address"));
        ED_JoinDate.setCellValueFactory(new PropertyValueFactory<>("join"));
        ED_Leave_Date.setCellValueFactory(new PropertyValueFactory<>("leave"));
        Lbl_Emp_ExEmp.setText("No of Employee :- ");
        EmpFun();
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------------------------------ Emp Table -----------------------------------------
    @FXML
    void EmpFun(){
        Lbl_Emp_ExEmp.setText("No of Employee :- ");
        int x = 0;
        EmpData.clear();
        try{
            Connection con = DriverManager.getConnection(_url, _user, _password);
            String qry = "select HostelerId,FName, LName, DOB, Gender, Post, PMunicipality,JoinDate from employee";
            PreparedStatement statement = con.prepareStatement(qry);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                do {
                    hid = resultSet.getString(1);
                    name = (resultSet.getString(2) + " " + resultSet.getString(3));
                    age = !(resultSet.getString(4).isEmpty()) ? (new Fun().age(resultSet.getString(4))) : 0;
//                System.out.println(resultSet.getString(4)+ "\n\n\n");
                    sex = resultSet.getString(5);
                    post = resultSet.getString(6);
                    address = resultSet.getString(7);
                    join = resultSet.getString(8);
                    leave = "yyyy-mm-dd";

                    // Making object of Emp
                    Emp E = new Emp(hid, name, age, sex, post, address, join, leave);

                    // Add data of E object in ObservableList
                    EmpData.add(E);
                    x++;
                    checker = true;
                } while (resultSet.next());
            }else {
                new MessageBox("Empty table");
                Emp E = new Emp("hid", "name", 0, "sex", "post", "address", "join", "leave");

                // Add data of E object in ObservableList
                EmpData.add(E);
                Lbl_count.setText(Integer.toString(x));
            }
            resultSet.close();
            statement.close();
            con.close();
            // Add data in table
            ED_Table.setItems(EmpData);
            Lbl_count.setText(Integer.toString(x));
        }catch (SQLException e){
            new MessageBox("Error");
            System.out.println(e.getMessage());
        }
    }
    //------------------------------ Emp Table -----------------------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------------------------------ Ex Employee Table -----------------------------------------
    @FXML
    void ExEmpFun(){

        if(!b){
            new MessageBox("Access denied.");
            return;
        }
        Lbl_Emp_ExEmp.setText("No of Ex-Employee :- ");
        ExEmpData.clear();
        int x =0;
        try{
            Connection con = DriverManager.getConnection(_url, _user, _password);
            String qry = "select HostelerId, FName, LName, DOB, Gender, PMunicipality,JoinDate, LeaveDate from ex_employee";
            PreparedStatement statement = con.prepareStatement(qry);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                do {

                    hid = resultSet.getString(1);
                    name = (resultSet.getString(2) + " " + resultSet.getString(3));
                    age = !(resultSet.getString(4).isEmpty()) ? new Fun().age(resultSet.getString(4)) : 0;
                    sex = resultSet.getString(5);
                    post = "  -  ";
                    address = resultSet.getString(6);
                    join = resultSet.getString(7);
                    leave = resultSet.getString(8);

                    Emp E = new Emp(hid, name, age, sex, post, address, join, leave);

                    ExEmpData.add(E);
                    x++;
                    checker = false;
                }while (resultSet.next());
            }else {
                new MessageBox("Empty table");
                Emp E = new Emp("hid", "name", 0, "sex", "post", "address", "join", "leave");

                ExEmpData.add(E);
            }
            resultSet.close();
            statement.close();
            con.close();
            ED_Table.setItems(ExEmpData);
            Lbl_count.setText(Integer.toString(x));

        }catch (SQLException e){
            new MessageBox("Error");
            System.out.println(e.getMessage());
        }
    }
    //------------------------------ Ex Employee Table -----------------------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------------------------------ Emp Class -----------------------------------------
    public static class Emp {
        private final String hid, name, sex, post, address, join, leave;
        private final int age;

        public Emp(String hid, String name,int age, String sex, String post, String address, String join, String leave) {
            this.hid = hid;
            this.name = name;
            this.sex = sex;
            this.post = post;
            this.address = address;
            this.join = join;
            this.leave = leave;
            this.age = age;
        }

        public String getHid() {
            return hid;
        }
        public String getName() {
            return name;
        }
        public String getSex() {
            return sex;
        }
        public String getPost() {
            return post;
        }
        public String getAddress() {
            return address;
        }
        public String getJoin() {
            return join;
        }
        public String getLeave() {
            return leave;
        }
        public int getAge() {
            return age;
        }
    }

    //------------------------------ Emp Class -----------------------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------------------------------- Search ProfileController ------------------------

//    String fName, lName, uid, dob, gender, phone1, phone2, email, country, citizenship, pProvince, pDistrict, pMunicipal, pWard, pTole, tProvince, tDistrict, tMunicipal, tWard, tTole;

    @FXML
    void SearchFun(){

        if(!b){
            new MessageBox("Access denied.");
        }
        if(EDSearchBy_ID_NAME.getText().isEmpty()){
            new MessageBox("Enter HostelerID to search");
            return;
        }
            if(checker){
                ProfileController.showDetails(EDSearchBy_ID_NAME.getText());
            }else {
               ProfileController.showexempDetails(EDSearchBy_ID_NAME.getText());
            }
    }

    @FXML
    void EnterFun_onTextField(KeyEvent event)  {
        if (event.getCode() == KeyCode.ENTER) {
            SearchFun();
        }
    }


    //------------------------------- Search ProfileController ------------------------

}
