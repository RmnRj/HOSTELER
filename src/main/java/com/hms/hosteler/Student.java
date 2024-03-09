package com.hms.hosteler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class Student {

    static String _url = new save().getDbms_url();
    static String _user = new save().getDbms_user();
    static String _password = new save().getDbms_password();

    @FXML
    private AnchorPane anchorStudent;

    @FXML
    private Button BTN_search, BTN_update;

    @FXML
    protected void StudentDetails_onAction() {
        new sceneSwitchController(anchorStudent, "modules/Student/StudentDetails.fxml");
    }

    @FXML
    protected void StudentRegistration_onAction() {
        new sceneSwitchController(anchorStudent, "modules/Student/StudentRegistration.fxml");
    }

    @FXML
    protected void DeleteStudent_onAction() {
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to Delete Student.");
            return;
        }
        new sceneSwitchController(anchorStudent, "modules/Student/DeleteStudent.fxml");
    }

    private void changePane(Pane pane1, Pane pane2) //(nextPaneID, currentPaneID)
    {// Function to make visible Pane
        pane2.visibleProperty().set(false);
        pane1.visibleProperty().set(true);
    }

    boolean per;
    @FXML
    void initialize(){
        per = new Permission().permission();
    }
    //-----------------------------------------------------------------------------------
    //-------------------------- Student Registration -----------------------------------

    @FXML
    private Pane StudentReg_Step1;
    @FXML
    private Pane StudentReg_Step2;

    @FXML
    private TextField StudentAddress_District;

    @FXML
    private TextField StudentAddress_Municipalty;

    @FXML
    private TextField StudentAddress_State;

    @FXML
    private TextField StudentAddress_Tole;

    @FXML
    private TextField StudentAddress_WardNo;

    @FXML
    private TextField StudentCountry;

    @FXML
    private TextField StudentDOB_DD;

    @FXML
    private TextField StudentDOB_MM;

    @FXML
    private TextField StudentDOB_YYYY;

    @FXML
    private TextField StudentEmail;

    @FXML
    private TextField StudentFatherName;

    @FXML
    private TextField StudentFather_Ph;

    @FXML
    private TextField StudentFirstName;

    @FXML
    private TextField StudentID;

    @FXML
    private TextField StudentJoinDate_dd;

    @FXML
    private TextField StudentJoinDate_mm;

    @FXML
    private TextField StudentJoinDate_yyyy;

    @FXML
    private TextField StudentLastName;

    @FXML
    private TextField StudentLocalGuardian;

    @FXML
    private TextField StudentLocalGuardian_Ph;

    @FXML
    private TextField StudentMotherName;

    @FXML
    private TextField StudentMother_Ph;

    @FXML
    private TextField StudentPh_optional;

    @FXML
    private TextField StudentPh_primary;

    @FXML
    private RadioButton StudentSex_Female;

    @FXML
    private RadioButton StudentSex_Male;

    @FXML
    private RadioButton StudentSex_Other;

    @FXML
    private TextField StudentTempAddress_District;

    @FXML
    private TextField StudentTempAddress_Municipalty;

    @FXML
    private TextField StudentTempAddress_State;

    @FXML
    private TextField StudentTempAddress_Tole;

    @FXML
    private TextField StudentTempAddress_WardNo;

    @FXML
    private CheckBox Student_SameAsPermanentAdd;

    String S = null;

    /*
    *
    * Set Gender for form
     */
    @FXML
    public void genderActionEvent() {
        if (StudentSex_Male.isSelected()) {
            S = "Male";
        } else if (StudentSex_Female.isSelected()) {
            S = "Female";
        } else {
            S = "Other";
        }
    }

    String getSex() {

        return S;
    }


    @FXML
    public void submitBTN_Fun() {
        //  for sql connection
        genderActionEvent();
        String Sfirstname = new Fun().upp(StudentFirstName.getText());
        String Slastname = new Fun().upp(StudentLastName.getText());
        String Studentid = ("S" + StudentID.getText());

        // date of birth
        int dobYear = Integer.parseInt(StudentDOB_YYYY.getText());
        int dobMonth = Integer.parseInt(StudentJoinDate_mm.getText());
        int dobDay = Integer.parseInt(StudentDOB_DD.getText());
        String dobDate = String.format("%04d-%02d-%02d", dobYear, dobMonth, dobDay);

        String phone1 = ("+977" + StudentPh_primary.getText());
        String phoneOp = !(StudentPh_optional.getText().isEmpty()) ? ("+977" + StudentPh_optional.getText()) : null;
        String email = !(StudentEmail.getText().isEmpty()) ? StudentEmail.getText() : null;

        // for perment address
        String country = new Fun().upp(StudentCountry.getText());
        String Pstate = new Fun().upp(StudentAddress_State.getText());
        String Pdistrict = new Fun().upp(StudentAddress_District.getText());
        String Pmunicipality = new Fun().upp(StudentAddress_Municipalty.getText());
        String Pward = StudentAddress_WardNo.getText();
        String Ptole = StudentAddress_Tole.getText().isEmpty() ? null : StudentAddress_Tole.getText();
        // for temporary adddress
        String Tstate = new Fun().upp(StudentTempAddress_State.getText());
        String Tdistrict = new Fun().upp(StudentTempAddress_District.getText());
        String Tmunicipality = new Fun().upp(StudentTempAddress_Municipalty.getText());
        String Tward = StudentTempAddress_WardNo.getText();
        String Ttole = StudentTempAddress_Tole.getText().isEmpty() ? null : StudentTempAddress_Tole.getText();

        String Fathername = new Fun().upp(StudentFatherName.getText());
        String Mothername = new Fun().upp(StudentMotherName.getText());
        String Fatherphone = "+977" + StudentFather_Ph.getText();
        String Motherrphone = !(StudentMother_Ph.getText().isEmpty()) ? "+977" + StudentMother_Ph.getText() : null;
        String GuardianName = new Fun().upp(StudentLocalGuardian.getText());
        String GuardianPh = "+977" + StudentLocalGuardian_Ph.getText();

        // join date
        int joinYear = Integer.parseInt(StudentJoinDate_yyyy.getText());
        int joinMonth = Integer.parseInt(StudentJoinDate_mm.getText());
        int joinDay = Integer.parseInt(StudentJoinDate_dd.getText());
        String joinDate = String.format("%04d-%02d-%02d", joinYear, joinMonth, joinDay);

        if (isValid()) {
            try { // sql query
                Connection con = DriverManager.getConnection(_url, _user, _password);
                String query = "INSERT INTO student (StudentID, FName, LName, Gender, DOB, Phone1 ,Phone2, Email, Country ,Pprovince,Pdistrict, Pmunicipality, Pward, Ptole, Tprovince,Tdistrict, Tmunicipality, Tward ,Ttole , JoinDate,FatherName, FatherPhone, MotherName, MotherPhone,  GuardianName, GuardianPhone )VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1, Studentid);
                statement.setString(2, Sfirstname);
                statement.setString(3, Slastname);
                statement.setString(4, getSex());
                // Parse and set date values as integers
                // date
                statement.setString(5, dobDate);
                statement.setString(6, phone1);
                statement.setString(7, phoneOp);
                statement.setString(8, email);
                statement.setString(9, country);
                statement.setString(10, Pstate);
                statement.setString(11, Pdistrict);
                statement.setString(12, Pmunicipality);
                statement.setString(13, Pward);
                statement.setString(14, Ptole);
                statement.setString(15, Tstate);
                statement.setString(16, Tdistrict);
                statement.setString(17, Tmunicipality);
                statement.setString(18, Tward);
                statement.setString(19, Ttole);
                statement.setString(20, joinDate);
                statement.setString(21, Fathername);
                statement.setString(22, Fatherphone);
                statement.setString(23, Mothername);
                statement.setString(24, Motherrphone);
                statement.setString(25, GuardianName);
                statement.setString(26, GuardianPh);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {

                    new MessageBox("Successful", "Your form has been submitted.");
                    clearForm();
                } else {
                    new MessageBox("Fail", "Your form has not submitted.");

                }
                statement.close();
                con.close();

            } catch (SQLException e) {
                new MessageBox("Fail", "Please try different StudentID, Phone1 or email.\nMay be someone already used this data.");
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    void setDate() { // Set Current Date Function
        Fun d = new Fun();
        d.setDate(); //d.setDate() is public function for find current date
        int year = d.get_Year();
        int month = d.get_Month();
        int day = d.get_Day();
        StudentJoinDate_yyyy.setText(Integer.toString(year));
        StudentJoinDate_mm.setText(Integer.toString(month));
        StudentJoinDate_dd.setText(Integer.toString(day));
    }

    @FXML
    private void handleSameAsPermanentAddress() {
        if (Student_SameAsPermanentAdd.isSelected()) {
            // Copy permanent address to temporary address
            StudentTempAddress_State.setText(StudentAddress_State.getText());
            StudentTempAddress_District.setText(StudentAddress_District.getText());
            StudentTempAddress_Municipalty.setText(StudentAddress_Municipalty.getText());
            StudentTempAddress_WardNo.setText(StudentAddress_WardNo.getText());
            StudentTempAddress_Tole.setText(StudentAddress_Tole.getText());
        }
    }

    // clear the fields
    @FXML
    public void clearForm() {
        StudentID.clear();
        StudentFirstName.clear();
        StudentLastName.clear();
        StudentEmail.clear();
        StudentDOB_YYYY.clear();
        StudentDOB_MM.clear();
        StudentDOB_DD.clear();
        StudentPh_primary.clear();
        StudentPh_optional.clear();
        StudentAddress_State.clear();
        StudentAddress_District.clear();
        StudentAddress_Municipalty.clear();
        StudentAddress_WardNo.clear();
        StudentAddress_Tole.clear();
        StudentTempAddress_State.clear();
        StudentTempAddress_District.clear();
        StudentTempAddress_Municipalty.clear();
        StudentTempAddress_WardNo.clear();
        StudentTempAddress_Tole.clear();
        StudentJoinDate_yyyy.clear();
        StudentJoinDate_mm.clear();
        StudentJoinDate_dd.clear();
        StudentFirstName.clear();
        StudentFather_Ph.clear();
        StudentMotherName.clear();
        StudentMother_Ph.clear();
        StudentLocalGuardian.clear();
        StudentLocalGuardian_Ph.clear();
        StudentFatherName.clear();
        StudentCountry.clear();
    }


    @FXML //Switch step 1 to step2 | onAction(Step1(Pane).Next(button))
    private void SetStep2() {
        genderActionEvent();
        if (StudentID.getText().isEmpty() || StudentFirstName.getText().isEmpty() || StudentLastName.getText().isEmpty() || StudentDOB_YYYY.getText().isEmpty() || StudentDOB_MM.getText().isEmpty() || StudentDOB_DD.getText().isEmpty() || getSex().isEmpty() || StudentPh_primary.getText().isEmpty() || StudentCountry.getText().isEmpty() || StudentAddress_State.getText().isEmpty() || StudentAddress_District.getText().isEmpty() || StudentAddress_Municipalty.getText().isEmpty() || StudentAddress_WardNo.getText().isEmpty() || StudentTempAddress_State.getText().isEmpty() || StudentTempAddress_District.getText().isEmpty() || StudentTempAddress_Municipalty.getText().isEmpty() || StudentTempAddress_WardNo.getText().isEmpty() || StudentJoinDate_yyyy.getText().isEmpty() || StudentJoinDate_mm.getText().isEmpty() || StudentJoinDate_dd.getText().isEmpty()) {
            new MessageBox("Fill all mandatory boxes.");
            return;
        }
        changePane(StudentReg_Step2, StudentReg_Step1);
    }

    @FXML //Switch step 2 to step1 | onAction(Step2(Pane).Back(button))
    private void SetStep1() {
        changePane(StudentReg_Step1, StudentReg_Step2);
    }

    //-------------------------- Student Registration -----------------------------------
    //-----------------------------------------------------------------------------------

    //---------------------------------------------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //-----------------------------------------------------------------------------------
    //-------------------------- Student search ----------------------------------------

    /*
    * [ Warden Only ]
    * Searching Student for different task (update)
     */
    @FXML
    void SearchFun() {
        if(!per){
            new MessageBox("Access denied.");
            return;
        }
        try { // sql query
            Connection con = DriverManager.getConnection(_url, _user, _password);
            String query = "select FName, LName, Gender, DOB, Phone1 ,Phone2, Email, Country ,Pprovince,Pdistrict, Pmunicipality, Pward, Ptole, Tprovince,Tdistrict, Tmunicipality, Tward ,Ttole , JoinDate,FatherName, FatherPhone, MotherName, MotherPhone,  GuardianName, GuardianPhone from student where StudentID = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, ("S" + StudentID.getText()));
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                StudentFirstName.setText(rs.getString(1));
                StudentLastName.setText(rs.getString(2));

                if (rs.getString(3).equals("Female")) {
                    StudentSex_Female.setSelected(true);
                } else if (rs.getString(3).equals("Male")) {
                    StudentSex_Male.setSelected(true);
                } else {
                    StudentSex_Other.setSelected(true);
                }

                int[] bs = new int[3];
                if (!rs.getString(4).isEmpty()) {
                    bs = new Fun().dateSeperator(rs.getString(4));
                } else {
                    bs[0] = 0;
                    bs[1] = 0;
                    bs[2] = 0;
                }
                StudentDOB_YYYY.setText(Integer.toString(bs[0]));
                StudentDOB_MM.setText(Integer.toString(bs[1]));
                StudentDOB_DD.setText(Integer.toString(bs[2]));

                StudentPh_primary.setText(rs.getString(5).substring(4));
                StudentPh_optional.setText(rs.getString(6).substring(4));
                StudentEmail.setText(rs.getString(7));
                StudentCountry.setText(rs.getString(8));
                StudentAddress_State.setText(rs.getString(9));
                StudentAddress_District.setText(rs.getString(10));
                StudentAddress_Municipalty.setText(rs.getString(11));
                StudentAddress_WardNo.setText(rs.getString(12));
                StudentAddress_Tole.setText(rs.getString(13));
                StudentTempAddress_State.setText(rs.getString(14));
                StudentTempAddress_District.setText(rs.getString(15));
                StudentTempAddress_Municipalty.setText(rs.getString(16));
                StudentTempAddress_WardNo.setText(rs.getString(17));
                StudentTempAddress_Tole.setText(rs.getString(18));

                if (!rs.getString(19).isEmpty()) {
                    bs = new Fun().dateSeperator(rs.getString(19));
                } else {
                    bs[0] = 0;
                    bs[1] = 0;
                    bs[2] = 0;
                }
                StudentJoinDate_yyyy.setText(Integer.toString(bs[0]));
                StudentJoinDate_mm.setText(Integer.toString(bs[1]));
                StudentJoinDate_dd.setText(Integer.toString(bs[2]));

                StudentFatherName.setText(rs.getString(20));
                StudentFather_Ph.setText(rs.getString(21).substring(4));
                StudentMotherName.setText(rs.getString(22));
                StudentMother_Ph.setText(rs.getString(23).substring(4));
                StudentLocalGuardian.setText(rs.getString(24));
                StudentLocalGuardian_Ph.setText(rs.getString(25).substring(4));

            } else {
                new MessageBox("Fail", "Fail Search process");

            }
            rs.close();
            statement.close();
            con.close();

        } catch (SQLException e) {
            new MessageBox("Fail", "StudentID is not found.");
            System.out.println(e.getMessage());
        }
    }

    //-------------------------- Student search ---------------------------------------
    //----------------------------------------------------------------------------------
    //---------------------------------------------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //-----------------------------------------------------------------------------------
    //-------------------------- Student update -------------------------------------

    /*
    *
    *  [ Warden Only ]
    *  To update Student
     */
    @FXML
    void updateFun() {
        if(!per){
            new MessageBox("Access denied.");
            return;
        }

        genderActionEvent();
        genderActionEvent();
        // Get user input from text fields
        String studentID = "S" + StudentID.getText();
        String firstName = new Fun().upp(StudentFirstName.getText());
        String lastName = new Fun().upp(StudentLastName.getText());
        String dob = StudentDOB_YYYY.getText() + "-" + StudentDOB_MM.getText() + "-" + StudentDOB_DD.getText(); // Combine date parts
        String phone1 = "+977" + StudentPh_primary.getText(); // Assuming country code is already added, otherwise modify
        String phone2 = "+977" + StudentPh_optional.getText(); // Assuming country code is already added, otherwise modify
        String email = StudentEmail.getText();
        String country = new Fun().upp(StudentCountry.getText());
        String pState = new Fun().upp(StudentAddress_State.getText());
        String pDistrict = new Fun().upp(StudentAddress_District.getText());
        String pMunicipality = new Fun().upp(StudentAddress_Municipalty.getText());
        String pWard = StudentAddress_WardNo.getText();
        String pTole = StudentAddress_Tole.getText();
        String tState = new Fun().upp(StudentTempAddress_State.getText());
        String tDistrict = new Fun().upp(StudentTempAddress_District.getText());
        String tMunicipality = new Fun().upp(StudentTempAddress_Municipalty.getText());
        String tWard = StudentTempAddress_WardNo.getText();
        String tTole = StudentTempAddress_Tole.getText();
        String joinDate = StudentJoinDate_yyyy.getText() + "-" + StudentJoinDate_mm.getText() + "-" + StudentJoinDate_dd.getText(); // Combine date parts
        String fatherName = new Fun().upp(StudentFatherName.getText());
        String fatherPhone = "+977" + StudentFather_Ph.getText();
        String motherName = new Fun().upp(StudentMotherName.getText());
        String motherPhone = "+977" + StudentMother_Ph.getText();
        String guardianName = new Fun().upp(StudentLocalGuardian.getText());
        String guardianPhone = "+977" + StudentLocalGuardian_Ph.getText();

        // Updating Student Details
        try {
            Connection con = DriverManager.getConnection(_url, _user, _password);

            // Update query with placeholders for field values
            String query = "UPDATE student SET FName = ?, LName = ?, DOB = ?, Phone1 = ?, Phone2 = ?, Email = ?, Country = ?, Pprovince = ?, Pdistrict = ?, Pmunicipality = ?, Pward = ?, Ptole = ?, Tprovince = ?, Tdistrict = ?, Tmunicipality = ?, Tward = ?, Ttole = ?, JoinDate = ?, FatherName = ?, FatherPhone = ?, MotherName = ?, MotherPhone = ?, GuardianName = ?, GuardianPhone = ?, Gender = ? WHERE StudentID = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, dob);
            statement.setString(4, phone1);
            statement.setString(5, phone2);
            statement.setString(6, email);
            statement.setString(7, country);
            statement.setString(8, pState);
            statement.setString(9, pDistrict);
            statement.setString(10, pMunicipality);
            statement.setString(11, pWard);
            statement.setString(12, pTole);
            statement.setString(13, tState);
            statement.setString(14, tDistrict);
            statement.setString(15, tMunicipality);
            statement.setString(16, tWard);
            statement.setString(17, tTole);
            statement.setString(18, joinDate);
            statement.setString(19, fatherName);
            statement.setString(20, fatherPhone);
            statement.setString(21, motherName);
            statement.setString(22, motherPhone);
            statement.setString(23, guardianName);
            statement.setString(24, guardianPhone);
            statement.setString(25, getSex());
            statement.setString(26, studentID); // Set StudentID as last parameter

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                new MessageBox("Successful", "Student data has been updated.");
                clearForm();
            } else {
                new MessageBox("Fail", "Failed to update student data.");
            }

            statement.close();
            con.close();
        } catch (SQLException e) {
            new MessageBox("Check all information mainly studentid, email and phone1");
            System.out.println(e.getMessage());
        }
    }

    //-------------------------- Student update -------------------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // -------------------------- Delete Student ----------------------

    @FXML
    private Label DE_STDName;

    @FXML
    private Label DE_label_found;

    @FXML
    private TextField DS_SID;

    @FXML
    private TextField DS_dd;

    @FXML
    private TextField DS_mm;

    @FXML
    private TextField DS_yyyy;

    /*
    *
    * [ Warden only ]
    * Delete Function to delete Student
     */
    @FXML
    void DeleteFun() {
        if (DS_SID.getText().isEmpty() || DS_yyyy.getText().isEmpty() || DS_mm.getText().isEmpty() || DS_dd.getText().isEmpty()) {
            new MessageBox("Fill Boxes to do Job");
            return;
        }
        String sid, leave;
        sid = ("S" + DS_SID.getText());
        int joinYear = Integer.parseInt(DS_yyyy.getText());
        int joinMonth = Integer.parseInt(DS_mm.getText());
        int joinDay = Integer.parseInt(DS_dd.getText());
        leave = String.format("%04d-%02d-%02d", joinYear, joinMonth, joinDay);

        try {
            Connection conn = DriverManager.getConnection(_url, _user, _password);

            // Check if student exists
            PreparedStatement stmtStudent = conn.prepareStatement("SELECT * FROM student WHERE StudentID = ?");
            stmtStudent.setString(1, sid);
            ResultSet rs = stmtStudent.executeQuery();

            if (!rs.next()) {
                DE_label_found.setText("Student not found!");
                rs.close();
                stmtStudent.close();
                conn.close();
                return;
            }

            conn = DriverManager.getConnection(_url, _user, _password);
            // Move student data to ex_student table
            PreparedStatement stmtExStudent = conn.prepareStatement("INSERT INTO ex_student (StudentID, FName, LName, DOB, Gender, Phone1, Phone2, Email, Country, Pprovince, Pdistrict, Pmunicipality, Pward, Ptole, Tprovince, Tdistrict, Tmunicipality, Tward, Ttole, JoinDate, LeaveDate, FatherName, FatherPhone, MotherName, MotherPhone, GuardianName, GuardianPhone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmtExStudent.setString(1, sid);
//            stmtExStudent.setString(1, studentID);
            stmtExStudent.setString(2, rs.getString("FName"));
            stmtExStudent.setString(3, rs.getString("LName"));
            stmtExStudent.setString(4, rs.getString("DOB"));
            stmtExStudent.setString(5, rs.getString("Gender"));
            stmtExStudent.setString(6, rs.getString("Phone1"));
            stmtExStudent.setString(7, rs.getString("Phone2"));
            stmtExStudent.setString(8, rs.getString("Email"));
            stmtExStudent.setString(9, rs.getString("Country"));
            stmtExStudent.setString(10, rs.getString("Pprovince"));
            stmtExStudent.setString(11, rs.getString("Pdistrict"));
            stmtExStudent.setString(12, rs.getString("Pmunicipality"));
            stmtExStudent.setString(13, rs.getString("Pward"));
            stmtExStudent.setString(14, rs.getString("Ptole"));
            stmtExStudent.setString(15, rs.getString("Tprovince"));
            stmtExStudent.setString(16, rs.getString("Tdistrict"));
            stmtExStudent.setString(17, rs.getString("Tmunicipality"));
            stmtExStudent.setString(18, rs.getString("Tward"));
            stmtExStudent.setString(19, rs.getString("Ttole"));
            stmtExStudent.setString(20, rs.getString("JoinDate"));
            stmtExStudent.setString(21, leave);
            stmtExStudent.setString(22, rs.getString("FatherName"));
            stmtExStudent.setString(23, rs.getString("FatherPhone"));
            stmtExStudent.setString(24, rs.getString("MotherName"));
            stmtExStudent.setString(25, rs.getString("MotherPhone"));
            stmtExStudent.setString(26, rs.getString("GuardianName"));
            stmtExStudent.setString(27, rs.getString("GuardianPhone"));

            int xx = stmtExStudent.executeUpdate();

            // Delete student from "student" table
            stmtStudent = conn.prepareStatement("DELETE FROM student WHERE StudentID = ?");
            stmtStudent.setString(1, sid);
            int yy = stmtStudent.executeUpdate();
            stmtStudent.close();
            rs.close();
            stmtExStudent.close();
            conn.close();

            if (xx > 0 && yy > 0) {
                new MessageBox("Student Delete Successfully");
            } else {
                new MessageBox("Process is failed");
            }

//            DE_label_found.setText("Student deleted successfully!");
        } catch (SQLException e) {
            new MessageBox("Please, check student is assign in Room or not.\nIf assign then remove him/her.");
            System.out.println(e.getMessage());
            // Handle exceptions gracefully
        }

    }

    /*
    *
    * [ Automatically run ]
    * Search Student Name using StudentID and set value
    * in Delete Student Form
     */
    @FXML
    void searchSTD() {
        try {
            Connection connection = DriverManager.getConnection(_url, _user, _password);
            String query = "Select FName, LName from student where StudentID = ?";
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setString(1, ("S" + DS_SID.getText()));
            ResultSet rs = stm.executeQuery();
            rs.next();
            DE_label_found.setText("Found");
            DE_STDName.setText((rs.getString(1) + " " + rs.getString(2)));
            rs.close();
            stm.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            DE_STDName.setText("");
            DE_label_found.setText("Not Found");
        }
    }

    /*
    *
    * Set current date in Form
     */
    @FXML

    void SET_DATE() {
        Fun d = new Fun();

        //d.setDate() is public function for find current date
        d.setDate();

        int year = d.get_Year();
        int month = d.get_Month();
        int day = d.get_Day();

        DS_yyyy.setText(Integer.toString(year));
        DS_mm.setText(Integer.toString(month));
        DS_dd.setText(Integer.toString(day));
    }

    @FXML
    private TextField SearchSTDname;


    /*
    * [ Warden ]
    * For the search of Student
     */
    @FXML
    void SearchFun2() {
        if(!(new Permission().permission())){
            new MessageBox("Only Warden have permission");
            return;
        }
        if (SearchSTDname.getText().isEmpty()) {
            new MessageBox("Empty Search Field");
            return;
        }

        // Call showDetails() to show the information
        Student.showDetails(SearchSTDname.getText());
    }

    //----------------------------Delete Student -----------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //--------------------------------- Search Student --------------------------
    @FXML
    private Label age;

    @FXML
    private Label country;

    @FXML
    private Label district;

    @FXML
    private Label dob;

    @FXML
    private Label email;

    @FXML
    private Label fathername;

    @FXML
    private Label fatherph;

    @FXML
    private Label guardianname;

    @FXML
    private Label guardianph;

    @FXML
    private Label joindate;

    @FXML
    private Label leavedate;

    @FXML
    private Label mother;

    @FXML
    private Label motherph;

    @FXML
    private Label municipality;

    @FXML
    private Label phone1;

    @FXML
    private Label phone2;

    @FXML
    private Label province;

    @FXML
    private Label sex;

    @FXML
    private Label student;

    @FXML
    private Label studentid;

    @FXML
    private Label tdistrict;

    @FXML
    private Label tmunicipality;

    @FXML
    private Label tole;

    @FXML
    private Label tprovince;

    @FXML
    private Label ttole;

    @FXML
    private Label tward;

    @FXML
    private Label wardno;

    /*
    *
    * [ Warden ]
    * This method is called for showing StudentProfile
    * Called in Home, Student [above somewhere], studentDetails
     */
    public static void showDetails(String studentId) {
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection(_url, _user, _password);

            // Prepare SQL statement
            String sql = "SELECT * FROM student WHERE StudentID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, studentId);

            // Execute query
            ResultSet resultSet = statement.executeQuery();

            // Check if student exists
            if (resultSet.next()) {
                // If student exists, load the detail page with retrieved data
                FXMLLoader loader = new FXMLLoader(Student.class.getResource("modules/Student/studentprofile.fxml"));
                Parent root = loader.load();

                Student controller = loader.getController();
                controller.initData(resultSet);

                Scene scene = new Scene(root, 864, 600);
                Stage stage = new Stage();
                Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("img/z1_SecondIcon.png")));
                stage.getIcons().add(image);
                stage.setTitle("HOSTELER / Student - Profile");
                stage.setMinWidth(864);
                stage.setMinHeight(600);
                stage.setScene(scene);
                stage.show();
            } else {
                // If student does not exist, display an error message
                new MessageBox("Student not found.");
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

    /*
    *
    * [ Warden Only ]
    * This function is called when I want to show ExStudent profile
    * Called in StudentDetails.java
     */
    public static void showexstudentDetails(String studentId) {
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection(_url, _user, _password);

            // Prepare SQL statement
            String sql = "SELECT * FROM ex_student WHERE StudentID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, studentId);

            // Execute query
            ResultSet resultSet = statement.executeQuery();

            // Check if student exists
            if (resultSet.next()) {
                // If student exists, load the detail page with retrieved data
                FXMLLoader loader = new FXMLLoader(Student.class.getResource("modules/Student/studentprofile.fxml"));
                Parent root = loader.load();

                Student controller = loader.getController();

                //initdata() called with ResultSet [location down]
                controller.initdata(resultSet);

                Scene scene = new Scene(root, 864, 600);
                Stage stage = new Stage();
                stage.setMinWidth(864);
                stage.setMinHeight(600);
                stage.setScene(scene);
                stage.show();
            } else {
                new MessageBox("Ex-Student not found.");
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

    /*
     *
     * Set value in Profile of Student
     * Called in showDetails()
     */
    @FXML
    public void initData(ResultSet resultSet) throws SQLException {
        // Populate labels with retrieved student details
        // S_name.setText(resultSet.getString("Sfname+Slname"));
        student.setText(resultSet.getString("FName") + " " + resultSet.getString("LName"));

        studentid.setText(resultSet.getString("StudentID"));
        //studentid.setText(resultSet.getString("age"));
        // age wala banau hai
        age.setText(String.valueOf(!(resultSet.getString("DOB").isEmpty()) ? (new Fun().age(resultSet.getString("DOB"))) : 0));
        // Set other student details...
        dob.setText(resultSet.getString("DOB"));

        sex.setText(resultSet.getString("Gender"));
        phone1.setText(resultSet.getString("Phone1"));
        phone2.setText(resultSet.getString("Phone2"));
        email.setText(resultSet.getString("Email"));
        country.setText(resultSet.getString("Country"));
        province.setText(resultSet.getString("Pprovince"));

        municipality.setText(resultSet.getString("Pmunicipality"));
        district.setText(resultSet.getString("Pdistrict"));
        wardno.setText(resultSet.getString("Pward"));
        tole.setText(resultSet.getString("Ptole"));
        tprovince.setText(resultSet.getString("Tprovince"));
        tdistrict.setText(resultSet.getString("Tdistrict"));
        tmunicipality.setText(resultSet.getString("Tmunicipality"));
        tward.setText(resultSet.getString("Tward"));
        ttole.setText(resultSet.getString("Ttole"));
        joindate.setText(resultSet.getString("JoinDate"));
//        leavedate.setText((resultSet.getString("LeaveDate").isEmpty())?"yyyy-mm-dd" : resultSet.getString("LeaveDate"));
        fathername.setText(resultSet.getString("FatherName"));
        fatherph.setText(resultSet.getString("FatherPhone"));
        mother.setText(resultSet.getString("MotherName"));
        motherph.setText(resultSet.getString("MotherPhone"));
        guardianname.setText(resultSet.getString("GuardianName"));
        guardianph.setText(resultSet.getString("GuardianPhone"));
        resultSet.close();
    }


    /*
    *
    * Set value in Profile of ExStudent
    * Called in showexstudentDetails()
     */
    @FXML
    public void initdata(ResultSet resultSet) throws SQLException {
        // Populate labels with retrieved student details
        // S_name.setText(resultSet.getString("Sfname+Slname"));
        student.setText(resultSet.getString("FName") + " " + resultSet.getString("LName"));

        studentid.setText(resultSet.getString("StudentID"));
        age.setText(String.valueOf(!(resultSet.getString("DOB").isEmpty()) ? (new Fun().age(resultSet.getString("DOB"))) : 0));
        // other student details...
        dob.setText(resultSet.getString("DOB"));

        sex.setText(resultSet.getString("Gender"));
        phone1.setText(resultSet.getString("Phone1"));
        phone2.setText(resultSet.getString("Phone2"));
        email.setText(resultSet.getString("Email"));
        country.setText(resultSet.getString("Country"));
        province.setText(resultSet.getString("Pprovince"));

        municipality.setText(resultSet.getString("Pmunicipality"));
        district.setText(resultSet.getString("Pdistrict"));
        wardno.setText(resultSet.getString("Pward"));
        tole.setText(resultSet.getString("Ptole"));
        tprovince.setText(resultSet.getString("Tprovince"));
        tdistrict.setText(resultSet.getString("Tdistrict"));
        tmunicipality.setText(resultSet.getString("Tmunicipality"));
        tward.setText(resultSet.getString("Tward"));
        ttole.setText(resultSet.getString("Ttole"));
        joindate.setText(resultSet.getString("JoinDate"));
        leavedate.setText(resultSet.getString("LeaveDate"));
        fathername.setText(resultSet.getString("FatherName"));
        fatherph.setText(resultSet.getString("FatherPhone"));
        mother.setText(resultSet.getString("MotherName"));
        motherph.setText(resultSet.getString("MotherPhone"));
        guardianname.setText(resultSet.getString("GuardianName"));
        guardianph.setText(resultSet.getString("GuardianPhone"));
        resultSet.close();
    }
    //--------------------------------- Search Student --------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\
    //------------------------------------- Validation ---------------------------------

    /*
    *
    * Check all validation of Student Reg form
    * return boolean value
     */
    boolean isValid() {
        Validation v = new Validation();
        if (!v.isValidName(StudentFirstName.getText()+" "+ StudentLastName.getText())) {
            new MessageBox("Invalid both name");
            return false;
        }else if (!v.isValidBSDate(Integer.parseInt(StudentDOB_YYYY.getText()), Integer.parseInt(StudentDOB_MM.getText()), Integer.parseInt(StudentDOB_DD.getText()))) {
            new MessageBox("Date of Birth is in valid");
            return false;
        }else if (!v.isValidPhone("+977" + StudentPh_primary.getText())) {
            new MessageBox("Invalid Phone 01");
            return false;
        }else if (!(StudentPh_optional.getText().isEmpty())) {
            if (!v.isValidPhone("+977" + StudentPh_optional.getText())) {
                new MessageBox("Invalid Phone 02");
                return false;
            }
        }else if (!(StudentEmail.getText().isEmpty())) { //Email khali xaina. if khali xa then skip grxa.
            if (!v.isValidEmail(StudentEmail.getText())) {//Optional case
                new MessageBox("Invalid email");
                return false;
            }
        }else if (!v.isValidBSDate(Integer.parseInt(StudentJoinDate_yyyy.getText()), Integer.parseInt(StudentJoinDate_mm.getText()), Integer.parseInt(StudentJoinDate_dd.getText()))) {
            new MessageBox("Join date is invalid");
            return false;
        }else if(!v.isValidName(StudentFirstName.getText())){
            new MessageBox("Invalid Father's Name");
            return false;
        }else if (!v.isValidPhone("+977" + StudentFather_Ph.getText())) {
            new MessageBox(" Invalid Father's Phone number");
            return false;
        }else if (!v.isValidName(StudentMotherName.getText())) {
            new MessageBox("Invalid Mother's Name");
            return false;
        }else if(!(StudentMother_Ph.getText().isEmpty())) {
            if (!v.isValidPhone("+977" + StudentMother_Ph.getText())) {
                new MessageBox("Invalid Mother's Phone Number");
                return false;
            }
        }else if (!v.isValidName(StudentLocalGuardian.getText())) {
            new MessageBox("Invalid Guardian's Name");
            return false;
        }else if (!v.isValidPhone("+977" + StudentLocalGuardian_Ph.getText())) {
            new MessageBox("Invalid Guardian's Phone Number");
            return false;
        }
        return true;
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}


