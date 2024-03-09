package com.hms.hosteler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class ProfileController {

    static String _url = new save().getDbms_url();
    static String _user = new save().getDbms_user();
    static String _password = new save().getDbms_password();

    @FXML
    private Label Label_age;

    @FXML
    private Label Label_citizenship;

    @FXML
    private Label Label_country;

    @FXML
    private Label Label_dob;

    @FXML
    private Label Label_email;

    @FXML
    private Label Label_hid;

    @FXML
    private Label Label_join;

    @FXML
    private Label Label_leave;

    @FXML
    private Label Label_name;

    @FXML
    private Label Label_pd;

    @FXML
    private Label Label_phone1;

    @FXML
    private Label Label_phone2;

    @FXML
    private Label Label_pm;

    @FXML
    private Label Label_post;

    @FXML
    private Label Label_pp;

    @FXML
    private Label Label_pt;

    @FXML
    private Label Label_pw;

    @FXML
    private Label Label_sex;

    @FXML
    private Label Label_td;

    @FXML
    private Label Label_tm;

    @FXML
    private Label Label_tp;

    @FXML
    private Label Label_tt;

    @FXML
    private Label Label_tw;

    @FXML
    private Label Label_uid;

    private int age;
//    private static String hid,Name, uid, post, dob, gender, phone1, phone2, email, country, citizenship, pProvince, pDistrict, pMunicipal, pWard, pTole, tProvince, tDistrict, tMunicipal, tWard, tTole, join, leave;

    static String uid;

    public static String getUid() {
        return uid;
    }

    public static void setUid(String u) {
        uid = u;
    }

    @FXML
    void initialize(){
        showProfile(new save().getHOSTELER_ID());
    }

    void showProfile(String hostelerid){
        try {

            Connection connection = DriverManager.getConnection(_url, _user, _password);
            String ss = "select UserID from login where HostelerId = ?";
            PreparedStatement statement1 = connection.prepareStatement(ss);
            statement1.setString(1, hostelerid);
            ResultSet r = statement1.executeQuery();
            if (r.next()) {
                setUid (r.getString(1) + "\t[User]");

            } else {
                setUid("\t\t[Not User]");
            }

            r.close();
            statement1.close();
            connection.close();
            // Establish database connection
            connection = DriverManager.getConnection(_url,_user,_password);

            // Prepare SQL statement
            String sql = "SELECT * FROM employee WHERE HostelerId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, hostelerid);

            // Execute query
            ResultSet resultSet = statement.executeQuery();

            // Check if student exists
            if (resultSet.next()) {
                initData(resultSet);
            } else {
                new MessageBox("Employee not found.");
                // If student does not exist, display an error message
                System.out.println("Student not found.");
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showDetails(String hostelerid) {
        try {
            Connection connection = DriverManager.getConnection(_url, _user, _password);
            String s = "select UserID from login where HostelerId = ?";
            PreparedStatement ss = connection.prepareStatement(s);
            ss.setString(1, hostelerid);
            ResultSet rr = ss.executeQuery();
            if (rr.next()) {
                setUid (rr.getString(1) + "\t[User]");

            } else {
                setUid("\t\t[Not User]");
            }

            rr.close();
            ss.close();
            connection.close();
            // Establish database connection
            connection = DriverManager.getConnection(_url,_user,_password);
            String sss = "select UserID from login where HostelerId = ?";
            PreparedStatement statement1 = connection.prepareStatement(sss);
            statement1.setString(1, hostelerid);
            ResultSet r = statement1.executeQuery();
            if (r.next()) {
                setUid (r.getString(1) + "[User]");

            } else {
                setUid("        [Not User]");
            }

            r.close();
            statement1.close();
            connection.close();
            // Establish database connection
            connection = DriverManager.getConnection(_url,_user,_password);

            // Prepare SQL statement
            String sql = "SELECT * FROM employee WHERE HostelerId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, hostelerid);

            // Execute query
            ResultSet resultSet = statement.executeQuery();

            // Check if student exists
            if (resultSet.next()) {
                // If student exists, load the detail page with retrieved data
                FXMLLoader loader = new FXMLLoader(ProfileController.class.getResource("modules/Employee/Profile.fxml"));
                Parent root = loader.load();

                ProfileController controller = loader.getController();
                controller.initData(resultSet);

                Scene scene = new Scene(root,864,600);
                Stage stage = new Stage();
                Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("img/z1_SecondIcon.png")));
                stage.getIcons().add(image);
                stage.setTitle("HOSTELER / Profile");
                stage.setMinWidth(864);
                stage.setMinHeight(600);
                stage.setScene(scene);
                stage.show();
            } else {
                new MessageBox("Employee not found.");
                // If student does not exist, display an error message
                System.out.println("Student not found.");
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void initData(ResultSet resultSet) throws SQLException {
        // Populate labels with retrieved student details
        // S_name.setText(resultSet.getString("Sfname+Slname"));
        Label_name.setText(resultSet.getString("FName") + " " + resultSet.getString("LName"));
        Label_post.setText(resultSet.getString("Post"));
         Label_uid.setText(getUid());
        Label_hid.setText(resultSet.getString("HostelerId"));
        Label_dob.setText(resultSet.getString("DOB"));

        // age wala banau hai 😊😊🥱
        Label_age.setText (Integer.toString((resultSet.getString("DOB").isEmpty() ? 0 : new Fun().age(resultSet.getString("DOB")))));
        Label_sex.setText(resultSet.getString("Gender"));

        //Setting other employee  details...
        Label_phone1.setText(resultSet.getString("Phone1"));
        Label_phone2.setText(resultSet.getString("Phone2"));
        Label_email.setText(resultSet.getString("Email"));
        Label_country.setText(resultSet.getString("Country"));
        Label_citizenship.setText(resultSet.getString("CitizenshipNo"));
        Label_pp.setText(resultSet.getString("PProvince"));
        Label_pd.setText(resultSet.getString("PDistrict"));
        Label_pm.setText(resultSet.getString("PMunicipality"));
        Label_pw.setText(resultSet.getString("PWard"));
        Label_pt.setText(resultSet.getString("PTole"));
        Label_tp.setText(resultSet.getString("TProvince"));
        Label_td.setText(resultSet.getString("TDistrict"));
        Label_tm.setText(resultSet.getString("TMunicipality"));
        Label_tw.setText(resultSet.getString("TWard"));
        Label_tt.setText(resultSet.getString("TTole"));
        Label_join.setText(resultSet.getString("JoinDate"));

    }
    // for ex employee details
    public static void showexempDetails(String hostelerid) {
        try {
            setUid(" - - - - - ");
            // Establish database connection
            Connection connection = DriverManager.getConnection(_url,_user,_password);

            // Prepare SQL statement
            String sql = "SELECT * FROM ex_employee WHERE HostelerId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, hostelerid);

            // Execute query
            ResultSet resultSet = statement.executeQuery();

            // Check if student exists
            if (resultSet.next()) {
                // If student exists, load the detail page with retrieved data
                FXMLLoader loader = new FXMLLoader(ProfileController.class.getResource("modules/Employee/Profile.fxml"));
                Parent root = loader.load();

                ProfileController controller = loader.getController();

                controller.initdata(resultSet); //calling

                Scene scene = new Scene(root,864,600);
                Stage stage = new Stage();
                stage.setMinWidth(864);
                stage.setMinHeight(600);
                stage.setScene(scene);
                stage.show();
            } else {
                new MessageBox("Ex-Employee not found");
                // If student does not exist, display an error message
//                System.out.println("Emp not ");
            }
            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }


    }
    public void initdata (ResultSet resultSet) throws SQLException {
        // Populate labels with retrieved student details
        // S_name.setText(resultSet.getString("Sfname+Slname"));
        Label_name.setText(resultSet.getString("FName") + " " + resultSet.getString("LName"));
        Label_post.setText(resultSet.getString("ExPost"));
         Label_uid.setText(getUid());
        Label_hid.setText(resultSet.getString("HostelerId"));
        Label_dob.setText(resultSet.getString("DOB"));

        // age wala banau hai😊😊🥱
        Label_age.setText (Integer.toString((resultSet.getString("DOB").isEmpty() ? 0 : new Fun().age(resultSet.getString("DOB")))));
        Label_sex.setText(resultSet.getString("Gender"));
        Label_uid.setText(getUid());
        //Setting other employee  details...
        Label_phone1.setText(resultSet.getString("Phone1"));
        Label_phone2.setText(resultSet.getString("Phone2"));
        Label_email.setText(resultSet.getString("Email"));
        Label_country.setText(resultSet.getString("Country"));
        Label_citizenship.setText(resultSet.getString("CitizenshipNo"));
        Label_pp.setText(resultSet.getString("PProvince"));
        Label_pd.setText(resultSet.getString("PDistrict"));
        Label_pm.setText(resultSet.getString("PMunicipality"));
        Label_pw.setText(resultSet.getString("PWard"));
        Label_pt.setText(resultSet.getString("PTole"));
        Label_tp.setText(resultSet.getString("TProvince"));
        Label_td.setText(resultSet.getString("TDistrict"));
        Label_tm.setText(resultSet.getString("TMunicipality"));
        Label_tw.setText(resultSet.getString("TWard"));
        Label_tt.setText(resultSet.getString("TTole"));
        Label_join.setText(resultSet.getString("JoinDate"));
        Label_leave.setText(resultSet.getString("LeaveDate"));
    }

}
