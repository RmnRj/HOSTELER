package com.hms.hosteler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class StudentDetails {

    String _url = new save().getDbms_url();
    String _user = new save().getDbms_user();
    String _password = new save().getDbms_password();
    @FXML
    private TableColumn<STD, Integer> SD_age;

    @FXML
    private TableColumn<STD, String> SD_joinDate;

    @FXML
    private TableColumn<STD, String> SD_leave;

    @FXML
    private TableColumn<STD, String> SD_localG_ph;

    @FXML
    private TableColumn<STD, String> SD_name;

    @FXML
    private TableColumn<STD, String> SD_ph;

    @FXML
    private TableColumn<STD, String> SD_sex;

    @FXML
    private TableColumn<STD, String> SD_sid;

    @FXML
    private TableView<STD> SD_table;

    @FXML
    private TextField SearchText;

    private final ObservableList<STD> studentData = FXCollections.observableArrayList();

    @FXML
    private Label lbl_count,Label_std_exStd; // to count total no of student/ex-student

    private boolean flag = true;

    @FXML
    void initialize(){
        SD_sid.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        SD_name.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        SD_age.setCellValueFactory(new PropertyValueFactory<>("age"));
        SD_sex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        SD_ph.setCellValueFactory(new PropertyValueFactory<>("phone"));
        SD_localG_ph.setCellValueFactory(new PropertyValueFactory<>("localguardian_ph"));
        SD_joinDate.setCellValueFactory(new PropertyValueFactory<>("join"));
        SD_leave.setCellValueFactory(new PropertyValueFactory<>("leave"));

        ShowStudentData();
    }

    @FXML
    void ShowEXStudentData() {
        Label_std_exStd.setText("No of Ex-Student :- ");
        try {
            int xx = 0;
            studentData.clear();
            String studentID, fullname, sex, phone, localguardian_ph, join, leave;
            int age;
            Connection con = DriverManager.getConnection(_url, _user, _password);
            String str = "select StudentID, FName, LName, DOB,Gender,Phone1,GuardianPhone,JoinDate,LeaveDate from ex_student";
            PreparedStatement stm = con.prepareStatement(str);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                do {
                    studentID = rs.getString(1);
                    fullname = rs.getString(2) + " " + rs.getString(3);
                    age = !(rs.getString(4).isEmpty()) ? new Fun().age(rs.getString(4)) : 0;
                    sex = rs.getString(5);
                    phone = rs.getString(6);
                    localguardian_ph = rs.getString(7);
                    join = rs.getString(8);
                    leave = rs.getString(9);

                    STD s = new STD(studentID, fullname, age, sex, phone, localguardian_ph, join, leave);
                    studentData.add(s);
                    xx++;
                } while (rs.next());
            } else {

                new MessageBox("Empty Table");
                STD s = new STD("null", "null", 0, "null", "null", "null", "null", "null");
                studentData.add(s);
            }
            rs.close();
            stm.close();
            con.close();

            SD_table.setItems(studentData);
            lbl_count.setText(Integer.toString(xx));
        } catch (SQLException e) {
            System.out.println("This is from ShowExStudentData\n" + e.getMessage());
        }

        flag = false;
    }

    @FXML
    void ShowStudentData() {
        Label_std_exStd.setText("No of Ex-Student :- ");
        try{
            int xx = 0;
            studentData.clear();
            String studentID, fullname,sex,phone,localguardian_ph,join,leave;
            int age;
            Connection con = DriverManager.getConnection(_url,_user,_password);
            String str = "select StudentID, FName, LName, DOB,Gender,Phone1,GuardianPhone,JoinDate from student";
            PreparedStatement stm= con.prepareStatement(str);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                do{
                   studentID = rs.getString(1);
                   fullname = rs.getString(2) + " " + rs.getString(3);
                    age = !(rs.getString(4).isEmpty()) ? new Fun().age(rs.getString(4)) : 0;
                   sex = rs.getString(5);
                   phone = rs.getString(6);
                   localguardian_ph = rs.getString(7);
                   join = rs.getString(8);
                   leave = "yyyy-mm-dd";

                    STD s = new STD(studentID, fullname,age,sex,phone,localguardian_ph,join,leave);
                    studentData.add(s);
                    xx++;
                }while (rs.next());
            }
            else {
                new MessageBox("Empty Table");
                STD s = new STD("null", "null",0,"null","null","null","null","null");
                studentData.add(s);
            }
            rs.close();
            stm.close();
            con.close();

            SD_table.setItems(studentData);
            lbl_count.setText(Integer.toString(xx));
        }catch (SQLException e){
            System.out.println("This is from ShowStudentData\n"+e.getMessage());
        }
        flag = true;
    }

    public static class STD{
        private final String studentID, fullname,sex,phone,localguardian_ph,join,leave;
        private final int age;

        public STD(String studentID, String fullname,int age, String sex, String phone, String localguardian_ph, String join, String leave) {
            this.studentID = studentID;
            this.fullname = fullname;
            this.sex = sex;
            this.phone = phone;
            this.localguardian_ph = localguardian_ph;
//            this.address = address;
            this.join = join;
            this.leave = leave;
            this.age = age;
        }

        public String getStudentID() {
            return studentID;
        }

        public String getFullname() {
            return fullname;
        }

        public String getSex() {
            return sex;
        }

        public String getPhone() {
            return phone;
        }

        public String getLocalguardian_ph() {
            return localguardian_ph;
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

    @FXML
    void SearchFun1() {
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to Search.");
            return;
        }
        if(SearchText.getText().isEmpty()){
            new MessageBox("Empty Search Box");
            return;
        }
        if(flag){
            Student.showDetails(SearchText.getText());

        }else {
            Student.showexstudentDetails(SearchText.getText());
        }
    }
}
