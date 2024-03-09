package com.hms.hosteler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class Employee {


    @FXML
    AnchorPane anchorEmployee;

    @FXML
    private AnchorPane anchorAddEmp;

    @FXML
    private Button btn_submit;
    String _url = new save().getDbms_url();
    String _user = new save().getDbms_user();
    String _password = new save().getDbms_password();

    @FXML
    private TextField EmpSearch;


    //-----------------------------------------ActionEvents--------------------------------------------

    @FXML
    void AddEmp() {

        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to Add Employee.");
            return;
        }new sceneSwitchController(anchorEmployee, "modules/Employee/AddEmployee.fxml");
    }

    @FXML
    void EmpDetails()  {
        new sceneSwitchController(anchorEmployee, "modules/Employee/EmployeeDetails.fxml");
    }

    @FXML
    void toDeleteEmp(){
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to Delete Employee.");
            return;
        }
        new sceneSwitchController(anchorEmployee, "modules/Employee/DeleteEmp.fxml");}



    // -----------------------------------------ActionEvents-------------------------------------------
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //-----------------------------------------------------------------------------------
    //-------------------------- Add Emp -------------------------------------------

    @FXML
    private TextField E_Post;

    @FXML
    private TextField E_Citizenship;

    @FXML
    private TextField E_Country;

    @FXML
    private TextField E_DOB_dd;

    @FXML
    private TextField E_DOB_mm;

    @FXML
    private TextField E_DOB_yyyy;

    @FXML
    private TextField E_Email;

    @FXML
    private TextField E_FName, E_LName;

    @FXML
    private TextField E_HostelerID;

    @FXML
    private TextField E_Join_dd;

    @FXML
    private TextField E_Join_mm;

    @FXML
    private TextField E_Join_yyyy;
    // for address
    @FXML
    private TextField E_PAddress_District;

    @FXML
    private TextField E_PAddress_State;

    @FXML
    private TextField E_PAddress_munici;

    @FXML
    private TextField E_PAddress_tole_optional;

    @FXML
    private TextField E_PAddress_ward;

    @FXML
    private TextField E_Phone;

    @FXML
    private TextField E_Phone_Optional;

    @FXML
    private CheckBox E_Same_as_PAddress;

    @FXML
    private RadioButton E_Sex_Female;

    @FXML
    private RadioButton E_Sex_Other;

    @FXML
    private RadioButton E_Sex_male;


    @FXML
    private TextField E_TAddress_District, E_TAddress_Munici, E_TAddress_State, E_TAddress_Tole_optional, E_TAddress_Ward;


    String getSex() {
        String S = null;
        if (E_Sex_male.isSelected()) {
            S = "Male";
        } else if (E_Sex_Female.isSelected()) {
            S = "Female";
        } else if(E_Sex_Other.isSelected()){
            S = "Other";
        }
        return S;
    }

    Connection con;

    Fun f = new Fun();

    String firstname, lastname,hostelerId,post, dob, join, phone1, phone2, email, country, citizenship, PState, PDistrict, PMunicipality, PWard, PTole, TState, TDistrict, TMunicipality, TWard, TTole;

    @FXML
    public void saveButton(){
        firstname = f.upp(E_FName.getText());
        lastname = f.upp(E_LName.getText());
        hostelerId = ("H" + E_HostelerID.getText());
        post = new Fun().upp(E_Post.getText());
        if(new save().isSetup()){
            if(!(post.toLowerCase().equals("warden"))){
                new MessageBox("Error","Make post = Warden.\n");
                return;
            }
        }
        // date of birth
        int dobYear = Integer.parseInt( E_DOB_yyyy.getText());
        int dobMonth = Integer.parseInt(E_DOB_mm.getText());
        int dobDay = Integer.parseInt(E_DOB_dd.getText());
        dob = String.format("%04d-%02d-%02d", dobYear, dobMonth, dobDay);

        phone1 = "+977"+E_Phone.getText();
        phone2 = !(E_Phone_Optional.getText().isEmpty())? "+977"+E_Phone_Optional.getText() : "";
        email =  E_Email.getText();
        country = f.upp(E_Country.getText());
        citizenship = E_Citizenship.getText();
        // for permanent address
        PState = f.upp(E_PAddress_State.getText());
        PDistrict =  f.upp(E_PAddress_District.getText());
        PMunicipality = f.upp(E_PAddress_munici.getText());
        PWard = (E_PAddress_ward.getText());
        if (E_PAddress_tole_optional.getText() != null) { // Check for null before calling isEmpty()
            PTole = f.upp(E_PAddress_tole_optional.getText());
        } else {
            PTole = null;
        }
        // for temporary address
        TState = f.upp(E_TAddress_State.getText());
        TDistrict = f.upp(E_TAddress_District.getText());
        TMunicipality = f.upp(E_TAddress_Munici.getText());
        TWard = E_TAddress_Ward.getText();
        if (E_TAddress_Tole_optional.getText() != null) {
            TTole = f.upp(E_TAddress_Tole_optional.getText());
        } else {
            TTole = null; // Assign null if empty
        }

        // for join date
        int joinYear =  Integer.parseInt(E_Join_yyyy.getText());
        int joinMonth = Integer.parseInt(E_Join_mm.getText());
        int joinDay = Integer.parseInt(E_Join_dd.getText());
        join = String.format("%04d-%02d-%02d", joinYear, joinMonth, joinDay);
        if(checkEmptyBox()){
            new MessageBox("Incomplete Form","Fill mandatory boxes with correct information");
            return;
        }

        if(isValid()) {
            // for database to insert data
            try {
                con = DriverManager.getConnection(_url, _user, _password);
                String query = "INSERT INTO employee (FName, LName, HostelerId, Post, DOB, Gender, Phone1, Phone2, Email, Country, CitizenshipNo , PProvince, PDistrict,  PMunicipality , PWard, PTole, TProvince , TDistrict, TMunicipality ,TWard, TTole, JoinDate)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1, firstname);
                statement.setString(2, lastname);
                statement.setString(3, hostelerId);
                statement.setString(4, post);
                statement.setString(5, dob);
                statement.setString(6, getSex());
                statement.setString(7, phone1);
                statement.setString(8, phone2);
                statement.setString(9, email);
                statement.setString(10, country);
                statement.setString(11, citizenship);
                statement.setString(12, PState);
                statement.setString(13, PDistrict);
                statement.setString(14, PMunicipality);
                statement.setString(15, PWard);
                statement.setString(16, PTole);
                statement.setString(17, TState);
                statement.setString(18, TDistrict);
                statement.setString(19, TMunicipality);
                statement.setString(20, TWard);
                statement.setString(21, TTole);
                statement.setString(22, join);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    new MessageBox("Successful", "Employee is added successfully");
                    if(new save().isSetup()){
                        SETUP(hostelerId);
                    }
                    clearAddEmp();

                }else {
                    new MessageBox("Unsuccessful", "Employee is not added.");
                }
                statement.close();
                con.close();
            } catch (SQLException | NumberFormatException e) {
                System.out.println(e.getMessage());
                new MessageBox("Fail", "Your form has not submit.\nMay be HostelerId, Phone no, email, Citizenship\nare already registered.");
            }
        }else {
            new MessageBox("Email is not vaild");
        }
    }

    @FXML
    void clearAddEmp() {
        E_FName.clear();
        E_LName.clear();
        E_HostelerID.clear();
        E_DOB_yyyy.clear();
        E_DOB_mm.clear();
        E_DOB_dd.clear();
        E_Phone.clear();
        E_Phone_Optional.clear();
        E_Email.clear();
        E_Country.clear();
        E_Citizenship.clear();
        E_PAddress_State.clear();
        E_PAddress_District.clear();
        E_PAddress_munici.clear();
        E_PAddress_ward.clear();
        E_PAddress_tole_optional.clear();
        E_TAddress_State.clear();
        E_TAddress_District.clear();
        E_TAddress_Munici.clear();
        E_TAddress_Ward.clear();
        E_TAddress_Tole_optional.clear();
        E_Join_yyyy.clear();
        E_Join_mm.clear();
        E_Join_dd.clear();
        E_Sex_Other.setSelected(false);
        E_Sex_Female.setSelected(false);
        E_Sex_male.setSelected(false);
    }

    @FXML
    void setDate() { // Set Current Date Function
        Fun d = new Fun();
        d.setDate(); //d.setDate() is public function for find current date
        int year = d.get_Year();
        int month = d.get_Month();
        int day = d.get_Day();
        E_Join_yyyy.setText(Integer.toString(year));
        E_Join_mm.setText(Integer.toString(month));
        E_Join_dd.setText(Integer.toString(day));
    }


    @FXML // Same as Permanent Address in Temporary Address
    void PerToTempAdd(){
        if(E_Same_as_PAddress.isSelected()){
            E_TAddress_State.setText(E_PAddress_State.getText());
            E_TAddress_District.setText(E_PAddress_District.getText());
            E_TAddress_Munici.setText(E_PAddress_munici.getText());
            E_TAddress_Ward.setText(E_PAddress_ward.getText());
            E_TAddress_Tole_optional.setText(E_PAddress_tole_optional.getText());
        }
    }

    boolean isValid() {
        Validation v = new Validation();
        if (!v.isValidName(E_FName.getText() + " " + E_LName.getText())) {
            new MessageBox("Invalid  Name");
            return false;
        }else if(!v.isValidPost(E_Post.getText())){
            new MessageBox("Invalid Post.\nAvailable Post:-\nWarden,Employee,Manager,Receptionist,Cook");
            return false;
        }else if (!v.isValidBSDate(Integer.parseInt(E_DOB_yyyy.getText()), Integer.parseInt(E_DOB_mm.getText()), Integer.parseInt(E_DOB_dd.getText()))) {
            new MessageBox("Date of Birth is in valid");
            return false;
        }else if (!v.isValidPhone("+977" + E_Phone.getText())) {
            new MessageBox("Invalid Phone 01");
            return false;
        }else if(!(E_Phone_Optional.getText().isEmpty())) {
            if (!v.isValidPhone("+977" + E_Phone_Optional.getText())) {
                new MessageBox("Invalid Phone 02");
                return false;
            }
        }else if (!(E_Email.getText().isEmpty())) { //Email khali xaina. if khali xa then skip grxa.
            if (!v.isValidEmail(E_Email.getText())) { //Optional case
                new MessageBox("Invalid Email");
                return false;
            }
        }else if (!v.isValidBSDate(Integer.parseInt(E_Join_yyyy.getText()), Integer.parseInt(E_Join_mm.getText()), Integer.parseInt(E_Join_dd.getText()))) {
            new MessageBox("Join date is invalid");
            return false;
        }
        return true;
    }
    //-------------------------- Add Emp -------------------------------------------
    //-----------------------------------------------------------------------------------
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //-----------------------------------------------------------------------------------
    //-------------------------- Search / Update -------------------------------------------
     @FXML
    void SearchBy() {
        if(E_HostelerID.getText().isEmpty()){
            new MessageBox("Enter HostelerId to search.");
        }
        try{
            Connection con = DriverManager.getConnection(_url, _user, _password);
            String sqr = "Select FName, LName, Post, DOB, Gender, Phone1, Phone2, Email, Country, CitizenshipNo , PProvince, PDistrict,  PMunicipality , PWard, PTole, TProvince , TDistrict, TMunicipality ,TWard, TTole, JoinDate from employee where HostelerId = ?";
            PreparedStatement statement = con.prepareStatement(sqr);
            statement.setString(1, ("H"+E_HostelerID.getText()));
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            E_FName.setText(resultSet.getString(1));
            E_LName.setText(resultSet.getString(2));
            E_Post.setText(resultSet.getString(3));

            int[] bs = new int[3];
            if(!resultSet.getString(4).isEmpty()){
             bs = new Fun().dateSeperator(resultSet.getString(4));
            }else {
                bs[0] = 0;
                bs[1] = 0;
                bs[2] = 0;
            }
            E_DOB_yyyy.setText(Integer.toString(bs[0]));
            E_DOB_mm.setText(Integer.toString(bs[1]));
            E_DOB_dd.setText(Integer.toString(bs[2]));

             if(resultSet.getString(5).equals("Female")) {
                 E_Sex_Female.setSelected(true);
             }else if(resultSet.getString(5).equals("Male")){
                 E_Sex_male.setSelected(true);
             }else{
                 E_Sex_Other.setSelected(true);
             }

            E_Phone.setText(resultSet.getString(6));
            E_Phone_Optional.setText(resultSet.getString(7));
            E_Email.setText(resultSet.getString(8));
            E_Country.setText(resultSet.getString(9));
            E_Citizenship.setText( resultSet.getString(10));
            E_PAddress_State.setText( resultSet.getString(11));
            E_PAddress_District.setText( resultSet.getString(12));
            E_PAddress_munici.setText( resultSet.getString(13));
            E_PAddress_ward.setText( resultSet.getString(14));
            E_PAddress_tole_optional.setText( resultSet.getString(15));
            E_TAddress_State.setText( resultSet.getString(16));
            E_TAddress_District.setText( resultSet.getString(17));
            E_TAddress_Munici.setText( resultSet.getString(18));
            E_TAddress_Ward.setText( resultSet.getString(19));
            E_PAddress_tole_optional.setText( resultSet.getString(20));

            if(!resultSet.getString(21).isEmpty()){
                bs = new Fun().dateSeperator(resultSet.getString(21));
            }else {
                bs[0] = 0;
                bs[1] = 0;
                bs[2] = 0;
            }
            E_Join_yyyy.setText(Integer.toString(bs[0]));
            E_Join_mm.setText(Integer.toString(bs[1]));
            E_Join_dd.setText(Integer.toString(bs[2]));

            resultSet.close();
            statement.close();
            con.close();

        }catch (SQLException e){
            new MessageBox("Not Found");
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void UpdateInfo() {
        firstname = f.upp(E_FName.getText());
        lastname = f.upp(E_LName.getText());
        hostelerId = ("H" + E_HostelerID.getText());
        post = E_Post.getText();
        // date of birth
        int dobYear = Integer.parseInt( E_DOB_yyyy.getText());
        int dobMonth = Integer.parseInt(E_DOB_mm.getText());
        int dobDay = Integer.parseInt(E_DOB_dd.getText());
        dob = String.format("%04d-%02d-%02d", dobYear, dobMonth, dobDay);

        phone1 = E_Phone.getText();
        phone2 = !(E_Phone_Optional.getText().isEmpty())? E_Phone_Optional.getText() : "";
        email =  E_Email.getText();
        country = f.upp(E_Country.getText());
        citizenship = E_Citizenship.getText();
        // for permanent address
        PState = f.upp(E_PAddress_State.getText());
        PDistrict =  f.upp(E_PAddress_District.getText());
        PMunicipality = f.upp(E_PAddress_munici.getText());
        PWard = (E_PAddress_ward.getText());
        if (E_PAddress_tole_optional.getText() != null) { // Check for null before calling isEmpty()
            PTole = f.upp(E_PAddress_tole_optional.getText());
        } else {
            PTole = null;
        }
        // for temporary address
        TState = f.upp(E_TAddress_State.getText());
        TDistrict = f.upp(E_TAddress_District.getText());
        TMunicipality = f.upp(E_TAddress_Munici.getText());
        TWard = E_TAddress_Ward.getText();
        if (E_TAddress_Tole_optional.getText() != null) {
            TTole = f.upp(E_TAddress_Tole_optional.getText());
        } else {
            TTole = null; // Assign null if empty
        }

        // for join date
        int joinYear =  Integer.parseInt(E_Join_yyyy.getText());
        int joinMonth = Integer.parseInt(E_Join_mm.getText());
        int joinDay = Integer.parseInt(E_Join_dd.getText());
        join = String.format("%04d-%02d-%02d", joinYear, joinMonth, joinDay);
        if(checkEmptyBox()){
            new MessageBox("Fill All Mandatory Boxes with data");
            return;
        }
        try{
            Connection con = DriverManager.getConnection(_url,_user,_password);
            String str = "update employee set FName = ?, LName = ?, Post = ?, DOB = ?, Gender = ?, Phone1 = ?, Phone2 = ?, Email = ?, Country = ?, CitizenshipNo = ? , PProvince = ?, PDistrict = ?,  PMunicipality = ? , PWard = ?, PTole = ?, TProvince = ?, TDistrict = ?, TMunicipality = ?,TWard = ?, TTole = ?, JoinDate = ? where HostelerId = ?";
            PreparedStatement statement = con.prepareStatement(str);
            statement.setString(1, firstname);
            statement.setString(2, lastname);
            statement.setString(3, post);
            statement.setString(4, dob);
            statement.setString(5, getSex());
            statement.setString(6, phone1);
            statement.setString(7, phone2);
            statement.setString(8, email);
            statement.setString(9, country);
            statement.setString(10, citizenship);
            statement.setString(11, PState);
            statement.setString(12, PDistrict);
            statement.setString(13, PMunicipality);
            statement.setString(14, PWard);
            statement.setString(15, PTole);
            statement.setString(16, TState);
            statement.setString(17, TDistrict);
            statement.setString(18, TMunicipality);
            statement.setString(19, TWard);
            statement.setString(20, TTole);
            statement.setString(21, join);
            statement.setString(22, hostelerId);
            int rowsUpdated = statement.executeUpdate();

            // Handle successful update and potential errors
            if (rowsUpdated > 0) {
                new MessageBox("Update successful");
                clearAddEmp(); // Clear form or handle as needed
            } else {
                new MessageBox("Update failed");
                // Provide specific error message if possible
            }
            statement.close();
            con.close();
        }catch (SQLException e){
            new MessageBox("Error in Update");
            System.out.println(e.getMessage());
        }
    }

    boolean checkEmptyBox(){
        //            new MessageBox("Incomplete Form","Fill mandatory boxes with correct information");
        return firstname.isEmpty() || lastname.isEmpty() || hostelerId.isEmpty() || post.isEmpty() || dob.isEmpty() || getSex().isEmpty() || phone1.isEmpty() || email.isEmpty() || country.isEmpty() || citizenship.isEmpty() || PState.isEmpty() || PDistrict.isEmpty() || PMunicipality.isEmpty() || PWard.isEmpty() || TState.isEmpty() || TDistrict.isEmpty() || TMunicipality.isEmpty() || TWard.isEmpty() || join.isEmpty();
    }
    //-------------------------- Ex Emp -------------------------------------------
    //-----------------------------------------------------------------------------------
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //-----------------------------------------------------------------------------------
    //-------------------------- Emp Delete -------------------------------------------

    @FXML
    private Label DE_EMPName;

    @FXML
    private TextField DE_HID;

    @FXML
    private TextField DE_dd;

    @FXML
    private Label DE_label_found;

    @FXML
    private TextField DE_mm;

    @FXML
    private TextField DE_yyyy;

    @FXML
    void DeleteEmpFun() {
        Connection con;
        if (DE_HID.getText().isEmpty() || DE_yyyy.getText().isEmpty() || DE_mm.getText().isEmpty() || DE_dd.getText().isEmpty()){
            new MessageBox("Fill Boxes to do Job");
            return;
        }
        String hid,fName, lName, post, dob, gender, phone1, phone2, email, country, citizenship, pProvince, pDistrict, pMunicipal, pWard, pTole, tProvince, tDistrict, tMunicipal, tWard, tTole, join, leave;
        hid = ("H" + DE_HID.getText());
        int joinYear =  Integer.parseInt(DE_yyyy.getText());
        int joinMonth = Integer.parseInt(DE_mm.getText());
        int joinDay = Integer.parseInt(DE_dd.getText());
        leave = String.format("%04d-%02d-%02d", joinYear, joinMonth, joinDay);

        try{
            con = DriverManager.getConnection(_url,_user,_password);
            String qry = "select count(HostelerId) from login where HostelerId = ?";
            PreparedStatement sss = con.prepareStatement(qry);
            sss.setString(1, hid);
            ResultSet result = sss.executeQuery();
            result.next();
            int xxx = result.getInt(1);
            result.close();
            sss.close();
            con.close();
            if (xxx > 0)
            {
                new MessageBox("This employee has User Account. Please,\ndelete first his/her userID or User account from\nsetting then delete employee.");
                return;
            }
            con = DriverManager.getConnection(_url,_user,_password);
            String sqr = "Select FName, LName, Post, DOB, Gender, Phone1, Phone2, Email, Country, CitizenshipNo , PProvince, PDistrict,  PMunicipality , PWard, PTole, TProvince , TDistrict, TMunicipality ,TWard, TTole, JoinDate from employee where HostelerId = ?";
            PreparedStatement statement = con.prepareStatement(sqr);
            statement.setString(1, hid);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            fName = resultSet.getString(1);
            lName =  resultSet.getString(2);
            post = resultSet.getString(3);
            dob = resultSet.getString(4);
            gender = resultSet.getString(5);
            phone1 = resultSet.getString(6);
            phone2 = resultSet.getString(7);
            email = resultSet.getString(8);
            country = resultSet.getString(9);
            citizenship = resultSet.getString(10);
            pProvince = resultSet.getString(11);
            pDistrict = resultSet.getString(12);
            pMunicipal = resultSet.getString(13);
            pWard = resultSet.getString(14);
            pTole = resultSet.getString(15);
            tProvince = resultSet.getString(16);
            tDistrict = resultSet.getString(17);
            tMunicipal = resultSet.getString(18);
            tWard = resultSet.getString(19);
            tTole = resultSet.getString(20);
            join = resultSet.getString(21);


            resultSet.close();
            statement.close();

            String qry2 = "delete from employee where HostelerId = ? limit 1";
            PreparedStatement statement1 = con.prepareStatement(qry2);
            statement1.setString(1, hid);
            int rowAffected = statement1.executeUpdate();
            statement1.close();
            con.close();

            // Condition for deleted
            if(rowAffected > 0){
                con = DriverManager.getConnection(_url,_user,_password);
                String query = "INSERT INTO ex_employee (FName, LName, HostelerId, ExPost, DOB, Gender, Phone1, Phone2, Email, Country, CitizenshipNo , PProvince, PDistrict,  PMunicipality , PWard, PTole, TProvince , TDistrict, TMunicipality ,TWard, TTole, JoinDate, LeaveDate) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement stm = con.prepareStatement(query);
                stm.setString(1, fName);
                stm.setString(2, lName);
                stm.setString(3, hid);
                stm.setString(4, post);
                stm.setString(5, dob);
                stm.setString(6, gender);
                stm.setString(7, phone1);
                stm.setString(8, phone2);
                stm.setString(9, email);
                stm.setString(10, country);
                stm.setString(11, citizenship);
                stm.setString(12, pProvince);
                stm.setString(13, pDistrict);
                stm.setString(14, pMunicipal);
                stm.setString(15, pWard);
                stm.setString(16, pTole);
                stm.setString(17, tProvince);
                stm.setString(18, tDistrict);
                stm.setString(19, tMunicipal);
                stm.setString(20, tWard);
                stm.setString(21, tTole);
                stm.setString(22, join);
                stm.setString(23,leave);
                int ff = stm.executeUpdate();
                stm.close();
                con.close();

                if (ff > 0){
                    new MessageBox("Employee Delete Successful");
                }else {
                    new MessageBox("Unsuccessful insert into ExEmployee");
                }
            }else {
                new MessageBox("Unsuccessful process");
            }
        }catch (SQLException e){
            new MessageBox("Error");
            DE_EMPName.setText("");
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void setDate_OnDeleteEmp() { // Set Current Date Function
        Fun d = new Fun();
        d.setDate(); //d.setDate() is public function for find current date
        int year = d.get_Year();
        int month = d.get_Month();
        int day = d.get_Day();
        DE_yyyy.setText(Integer.toString(year));
        DE_mm.setText(Integer.toString(month));
        DE_dd.setText(Integer.toString(day));
    }

    @FXML
    void EmpName(){ // Search if found
        try{
            Connection connection = DriverManager.getConnection(_url,_user,_password);
            String query = "Select FName, LName from employee where HostelerId = ?";
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setString(1,("H"+DE_HID.getText()));
            ResultSet rs = stm.executeQuery();
            rs.next();
            DE_label_found.setText("Found");
            DE_EMPName.setText((rs.getString(1) + " " + rs.getString(2)));
            rs.close();
            stm.close();
            connection.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            DE_EMPName.setText("");
            DE_label_found.setText("Not Found");
        }
    }


    @FXML
    void SearchFun() {
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to search.");
            return;
        }
        if(EmpSearch.getText().isEmpty()){
            new MessageBox("Fill HostelerId to search.");
            return;
        }
        ProfileController.showDetails(EmpSearch.getText());
    }

    //-------------------------- Emp Delete -------------------------------------------
    //-----------------------------------------------------------------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//------------------------------------------- SET UP HOSTELER ----------------------

    void SETUP(String hid){
        String pass = "Apple12";

        try {
            Connection con1 = DriverManager.getConnection(_url, _user, _password);
            String query1 = "insert into login values (?,?,?)";
            PreparedStatement statement2 = con1.prepareStatement(query1);
            statement2.setString(1, hid);
            statement2.setString(2, pass);
            statement2.setString(3, hid);
            int rowsAffected = statement2.executeUpdate();
            statement2.close();
            con1.close();

            if (rowsAffected > 0) {

                new save().makeSetup_Disable();
                PropertiesReader.setUserExist("true");

                new save().setTemp(hid);
                LunchLastProcess();

            } else {
                new MessageBox("Error","Restart your system.");
            }
        }catch (SQLException e){
           System.out.println("Error in Adding Employee [Set UP]" + e);
        }
    }

    @FXML
    void LunchLastProcess(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SETUPProcess2.fxml"));
            Parent P = fxmlLoader.load();//
            int x = 650, y = 450;
            Stage Lunch_stage = new Stage();
            Lunch_stage.setScene(new Scene(P));

            Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("img/z1_SecondIcon.png")));
            Lunch_stage.getIcons().add(image);
            Lunch_stage.setTitle("HOSTELER / Setup-2");

            Lunch_stage.setMaxWidth(x);
            Lunch_stage.setMinWidth(x);
            Lunch_stage.setMinHeight(y);
            Lunch_stage.setMaxHeight(y);

            Lunch_stage.show();

        }catch (IOException e){
            new MessageBox("System Error");
            System.out.println(e.getMessage());
        }
        anchorAddEmp.getChildren().removeAll();
        Stage stage1 = (Stage) btn_submit.getScene().getWindow();
        stage1.close();
    }
    //------------------------------------------- SET UP HOSTELER ----------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
