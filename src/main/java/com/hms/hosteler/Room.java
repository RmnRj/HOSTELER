package com.hms.hosteler;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.*;

public class Room {

    String _url = new save().getDbms_url();
    String _user = new save().getDbms_user();
    String _password = new save().getDbms_password();
    @FXML
    private AnchorPane anchorRoom;
    @FXML
    void AllocateRoom_onAction()  {
        new sceneSwitchController(anchorRoom, "modules/Room/AllocateRoom.fxml");
    }

    @FXML
    void AllRoomsDetail_onAction()  {
        new sceneSwitchController(anchorRoom, "modules/Room/AllRoomDetails.fxml");
    }
    @FXML
    void RoomDetail_onAction()  {
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to visit Room Details.");
            return;
        }
        new sceneSwitchController(anchorRoom, "modules/Room/RoomDetails.fxml");
    }

    //-----------------------------------------------------------------------------------
    //-------------------------- Allocate Room -------------------------------------------

    @FXML
    private Label AR_StudentName, AR_label1, AR_label2;

    @FXML
    private TextField AR_RoomID;

    @FXML
    private TextField AR_StudentID;



    @FXML
    private TextField AR_dd;


    @FXML
    private TextField AR_mm;

    @FXML
    private TextField AR_yyyy;

    boolean f1,f2;

    int A;
    @FXML
    void AllocateRoomFun() throws SQLException {
        if(AR_mm.getText().isEmpty() || AR_dd.getText().isEmpty() || AR_yyyy.getText().isEmpty() || AR_RoomID.getText().isEmpty() || AR_StudentID.getText().isEmpty() || AR_StudentName.getText().isEmpty()){
            new MessageBox("All boxes/fields are mandatory.");
            return;
        }
        if(checkAvailability() && f2 && f1) {
            int yyyy = Integer.parseInt(AR_yyyy.getText());
            int mm = Integer.parseInt(AR_mm.getText());
            int dd = Integer.parseInt(AR_dd.getText());
            String date = String.format("%04d-%02d-%02d", yyyy, mm, dd);
            try {
                Connection con = DriverManager.getConnection(_url, _user, _password);
                String query1 = "insert into student_room values (?,?,?)";
                PreparedStatement statement = con.prepareStatement(query1);
                statement.setString(1, ("S" + AR_StudentID.getText()));
                statement.setString(2, ("R" + AR_RoomID.getText()));
                statement.setString(3, date);
                int rowsAffected = statement.executeUpdate();
                statement.close();

                String query2 = "update room set Availability = ? where RoomID = ?";
                PreparedStatement statement1 = con.prepareStatement(query2);
                statement1.setInt(1, (A - 1));
                statement1.setString(2, ("R" + AR_RoomID.getText()));
                int rowsAffected2 = statement1.executeUpdate();
                statement1.close();
                con.close();
                if (rowsAffected > 0 && rowsAffected2 > 0) {
                    AR_RoomID.clear();
                    AR_dd.clear();
                    AR_yyyy.clear();
                    AR_mm.clear();
                    AR_StudentName.setText("");
                    AR_StudentID.clear();
                    new MessageBox("Create save Successfully");
                }
            }catch (SQLException e){
                new MessageBox("Check StudentID, if he/she already assigned or not.");
            }
        }
    }
    boolean checkAvailability() throws SQLException{
        Connection con = DriverManager.getConnection(_url, _user, _password);
        String query = "select count(*) from room where RoomID = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, ("R"+AR_RoomID.getText()));
        ResultSet r = statement.executeQuery();
        r.next();
        int a = r.getInt(1);
        r.close();
        statement.close();
        con.close();
        if (a != 1){
            return false;
        }

        Connection con1 = DriverManager.getConnection(_url, _user, _password);
        String query1 = "select Availability from room where RoomID = ?";
        PreparedStatement statement2 = con1.prepareStatement(query1);
        statement2.setString(1, ("R"+AR_RoomID.getText()));
        ResultSet rr = statement2.executeQuery();
        rr.next();
        int aa = rr.getInt(1);
        rr.close();
        statement2.close();
        con1.close();
        if(aa != 0){
            A = aa;
            return true;
        }
        return false;
    }

    @FXML
    void setDateBS(){
        Fun d = new Fun();
        d.setDate();
        int year = d.get_Year();
        int month = d.get_Month();
        int day = d.get_Day();
        AR_yyyy.setText(Integer.toString(year));
        AR_mm.setText(Integer.toString(month));
        AR_dd.setText(Integer.toString(day));
    }

    @FXML
    void AR_RoomIDChecker() { // called in set on key released
        try {
            Connection con = DriverManager.getConnection(_url, _user, _password);
            String query1 = "select count(RoomID) from room where RoomID = ?";
            PreparedStatement statement1 = con.prepareStatement(query1);
            statement1.setString(1, ("R" + AR_RoomID.getText()));
            ResultSet result = statement1.executeQuery();
            result.next();
            int nn = result.getInt(1);
            result.close();
            statement1.close();
            con.close();
            if (nn == 1) {
                f1 = true;
                AR_label1.setText("Found.");
            }else {
                f1 = false;
                AR_label1.setText("Not Found.");
            }
        }catch (SQLException ex){
            new MessageBox("Error");
        }
    }

    @FXML
    void checkStudentID() { // called in set on key released
        try {
            Connection con = DriverManager.getConnection(_url, _user, _password);
            String query1 = "select FName, LName from student where StudentID = ?";
            PreparedStatement statement1 = con.prepareStatement(query1);
            statement1.setString(1, ("S" + AR_StudentID.getText()));
            ResultSet result = statement1.executeQuery();
            result.next();
            String FN = result.getString(1);
            String LN = result.getString(2);
//            System.out.println(FN+" "+LN);
            result.close();
            statement1.close();
            con.close();
            f2 = true;
            AR_StudentName.setText((FN+" "+LN));
            AR_label2.setText("Found.");
        }catch (SQLException ex){
//            ex.printStackTrace();
            f2 = false;
            AR_label2.setText("Student not found.");
        }
    }
    //-------------------------- Allocate Room ------------------------------------------
    //-----------------------------------------------------------------------------------
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //-----------------------------------------------------------------------------------
    //-------------------------- Room Deallocate -------------------------------------------

    @FXML
    void DeAlloctae(){
        boolean Deallocationflag = false;
        if(AR_StudentID.getText().isEmpty()){
            new MessageBox("Enter RoomID and StudentID to De_Allocate");
            return;
        }
        try{
            Connection con = DriverManager.getConnection(_url,_user,_password);
            String q = "delete from student_room where RoomID = ? and StudentID = ? limit 1";
            PreparedStatement ss = con.prepareStatement(q);
            ss.setString(1,"R"+AR_RoomID.getText());
            ss.setString(2,"S"+AR_StudentID.getText());
            if(ss.executeUpdate() > 0){
                Deallocationflag = true;
                System.out.println("Deallocate Successful");
            }else {
                Deallocationflag = false;
                new MessageBox("Unsuccessful Process");
            }
            ss.close();
            con.close();

        }catch (SQLException e){
            Deallocationflag = false;
            System.out.println(e.getMessage());
        }
        int aa = 0;
        if(Deallocationflag){
            boolean flag2 = false;
            try{
                Connection con = DriverManager.getConnection(_url,_user,_password);
                String q = "select Availability from room where RoomID = ?";
                PreparedStatement ss = con.prepareStatement(q);
                ss.setString(1,"R"+AR_RoomID.getText());
                ResultSet rs = ss.executeQuery();
                if(rs.next()){
                    aa = rs.getInt(1) + 1;
                    flag2 = true;
                }
                rs.close();
                ss.close();
                con.close();
                if(flag2) {
                    con = DriverManager.getConnection(_url, _user, _password);
                    q = "update room set Availability = ? where RoomID = ?";
                    ss = con.prepareStatement(q);
                    ss.setInt(1, aa);
                    ss.setString(2, "R" + AR_RoomID.getText());
                    if (ss.executeUpdate() > 0) {
                        new MessageBox("Deallocate Successful");
                        System.out.println("Deallocate Successful");
                    } else {
                        new MessageBox("Unsuccessful Process");
                        System.out.println("Deallocate UnSuccessful");
                    }
                    ss.close();
                    con.close();
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }

    }

    //-------------------------- Room Deallocate -------------------------------------------
    //-----------------------------------------------------------------------------------



}
