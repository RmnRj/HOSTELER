package com.hms.hosteler;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.*;
import java.util.Properties;


public class Home {
    String _url = new save().getDbms_url();
    String _user = new save().getDbms_user();
    String _password = new save().getDbms_password();

    @FXML
    private Label address,TotalStudent,vacantRoom;

    @FXML
    private AnchorPane anchorHome;

    @FXML
    private Label contact1;

    @FXML
    private Label contact2;

    @FXML
    private Label email, name;

    @FXML
    private Label website;

    private Properties properties = new Properties();

    @FXML
    void ToHostelInfo()  {
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to Update Hostel information.");
            return;
        }
        new sceneSwitchController(anchorHome,"Hostel_Info.fxml");
    }

    @FXML
    void toNewReg(){
        new sceneSwitchController(anchorHome, "modules/Student/StudentRegistration.fxml");
    }

    @FXML
    void toAllRoomDetails()  {
        new sceneSwitchController(anchorHome,"modules/Room/AllRoomDetails.fxml");
    }

    @FXML
    void initialize(){
//        name = PropertiesReader.getHostelName();
        name.setText(PropertiesReader.getHostelName());
        contact1.setText(PropertiesReader.getHostelContact1());
        contact2.setText(PropertiesReader.getHostelContact2());
        email.setText(PropertiesReader.getHostelEmail());
        website.setText(PropertiesReader.getHostelWeb());
        address.setText(PropertiesReader.getHostelAdd());

        TotalStudent.setText(Integer.toString(function1()));
        vacantRoom.setText(Integer.toString(function2()));
    }

    private int function1() {
        int count ;
        try { // sql query
            Connection con = DriverManager.getConnection(_url, _user, _password);

            String query = "select count(*) from student";
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            rs.next();
            count = rs.getInt(1);
            rs.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            count = 0;
        }
        return count;
    }

    private int function2() {
        int count ;
        try { // sql query
            Connection con = DriverManager.getConnection(_url, _user, _password);

            String query = "select count(*) from room where availability != 0";
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            rs.next();
            count = rs.getInt(1);
            rs.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            count = 0;
        }
        return count;
    }

    @FXML
    private TextField Home_search;

    @FXML
    void searchAction_Home(){
        if(!(new Permission().permission())){
            new MessageBox("Only Warden have permission");
            return;
        }
        if(Home_search.getText().isEmpty()){
            new MessageBox("Enter HostelerID to search");
            return;
        }

        String Sub = Home_search.getText().substring(0,1).toUpperCase() + Home_search.getText().substring(1);
        if(Sub.substring(0,1).equals("H")){
            ProfileController.showDetails(Home_search.getText());
        }else if(Sub.substring(0,1).equals("S")){
            Student.showDetails(Home_search.getText());
        }
    }
}
