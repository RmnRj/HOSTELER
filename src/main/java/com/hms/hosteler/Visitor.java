package com.hms.hosteler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.*;
import java.time.LocalTime;

public class Visitor {

    boolean studentFlag = false;

    String _url = new save().getDbms_url();
    String _user = new save().getDbms_user();
    String _password = new save().getDbms_password();

    @FXML
    private AnchorPane anchorVisitor;


    @FXML
    void VisitorsDetails_onAction() {
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to see Visitor Details.");
            return;
        }
        new sceneSwitchController(anchorVisitor, "modules/Visitor/VisitorDetails.fxml");
    }
    @FXML
    void VisitorForm_onAction() {
        new sceneSwitchController(anchorVisitor, "modules/Visitor/visitorform.fxml");
    }

    String currentTime(){
        // Get current time
        LocalTime currentTime = LocalTime.now();
        // Extract and format hours and minutes
        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        return String.format("%02d:%02d", hour, minute);
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //---------------------------------- class_Visitor Form ------------------------------------------------
    @FXML
    private TextField vDate_dd;

    @FXML
    private TextField vDate_mm;

    @FXML
    private TextField vDate_yyyy;

    @FXML
    private TextField vDistrict;

    @FXML
    private TextField vINTime;

    @FXML
    private TextField vOUTTime;

    @FXML
    private TextField vMunicipal;

    @FXML
    private TextField vNAme;

    @FXML
    private TextField vPhone;


    @FXML
    private TextField vRelation;

    @FXML
    private TextField vState;

    @FXML
    private Label vStdName;

    @FXML
    private TextField vStudentID;


    @FXML
    private TextField vTole;

    @FXML
    private TextField vWard;

    @FXML
    private RadioButton femalebtn;

    @FXML
    private RadioButton malebtn;

    @FXML
    private RadioButton otherbtn;

    @FXML
            private TextField VSNO;

    String S=null;

    public void genderActionEvent() {
        if (femalebtn.isSelected()) {
            S = "Female";
        } else if (malebtn.isSelected()) {
            S = "Male";
        } else {
            S = "Other";
        }
    }

    String getSex() {
        return S;
    }

    Connection con;

    @FXML
    public void submit() {

        genderActionEvent();
        int sno = Integer.parseInt(VSNO.getText());
        String name = vNAme.getText();
        String phone = ("+977"+vPhone.getText());
        String relation =  vRelation.getText();
        String stdID =  vStudentID.getText();

        int yyyy = Integer.parseInt( vDate_yyyy.getText());
        int mm = Integer.parseInt(vDate_mm.getText());
        int dd = Integer.parseInt(vDate_dd.getText());
        String vDate = String.format("%04d-%02d-%02d", yyyy, mm, dd);

        String state =  vState.getText();
        String district =  vDistrict.getText() ;
        String municipality = vMunicipal.getText();
        String ward = vWard.getText();
        String tole = !(vTole.getText().isEmpty()) ? vTole.getText() : null;
        String inTime = vINTime.getText();
        String outTime = !(vOUTTime.getText().isEmpty()) ? vOUTTime.getText() : null;
        try {
            if (name.isEmpty() || getSex().isEmpty() || vStudentID.getText().isEmpty() || vDate_yyyy.getText().isEmpty() || vDate_dd.getText().isEmpty() || vDate_mm.getText().isEmpty() || vPhone.getText().isEmpty() || relation.isEmpty() || state.isEmpty() || district.isEmpty() || municipality.isEmpty() || ward.isEmpty()  || inTime.isEmpty()) {
                new MessageBox("All boxes are  mandatory");
                return;
            }
        }catch (NullPointerException e){
            System.out.println(e.getMessage());
            return;
        }

        boolean flag = true;

        if(isValid()) {
            if (studentFlag) {
                try {
                    con = DriverManager.getConnection(_url, _user, _password);
                    String ss = "select count(*) from visitor where Vsno = ?";
                    PreparedStatement stm = con.prepareStatement(ss);
                    stm.setInt(1, Integer.parseInt(VSNO.getText()));
                    ResultSet rs = stm.executeQuery();
                    if (rs.next()) {
                        if (rs.getInt(1) > 0) {
                            flag = false;
                        }
                    }
                    rs.close();
                    stm.close();
                    con.close();
                    if (flag) {

                        // First Insertion into class_Visitor
                        con = DriverManager.getConnection(_url, _user, _password);
                        String query = "INSERT INTO visitor (Vsno, Vname ,Vgender ,Vphone, Vprovince,  Vdistrict ,Vmunicipality ,Vward ,Vtole) VALUES ( ?, ?, ?, ?, ?,?,?,?,?)";
                        PreparedStatement statement = con.prepareStatement(query);
                        statement.setInt(1, sno);
                        statement.setString(2, name);
                        statement.setString(3, getSex());
//            statement.setString(3,vDate);
                        statement.setString(4, phone);
//                    statement.setString(5, relation);
                        statement.setString(5, state);
                        statement.setString(6, district);
                        statement.setString(7, municipality);
                        statement.setString(8, ward);
                        statement.setString(9, tole);
//            statement.setString(11,inTime);

                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
//                            new MessageBox("Success", "Your form has been submitted");
                            System.out.println("Visitor is visited.");
                            clearForm();
                        } else {
                            new MessageBox("Unsuccessful", "Your form is not submitted!");
                        }
                        statement.close();
                        con.close();
                    }

                    // Second Insertion into visitor_time
                    con = DriverManager.getConnection(_url, _user, _password);
                    String query = "INSERT INTO visitor_time VALUES (?,?, ?, ?, ?,?)";
                    PreparedStatement statement = con.prepareStatement(query);
                    statement.setInt(1, sno);
                    statement.setString(2, vDate);
                    statement.setString(3, inTime);
                    statement.setString(4, outTime);
                    statement.setString(5, stdID);
                    statement.setString(6, relation);

                    if (statement.executeUpdate() > 0) {
                        new MessageBox("class_Visitor is successfully submitted.");
                        System.out.println("Visitor is Visited");
                        clearForm();
                    } else {
                        new MessageBox("Check all data and try again.");
                    }
                    statement.close();
                    con.close();

                } catch (SQLException e) {
                    new MessageBox("Unsuccessful process. Check every thing and try again.");
                    System.out.println(e.getMessage());
                }
            } else {
                new MessageBox("StudentID is invalid");
            }
        }

    }

    @FXML
    void clearForm(){
        VSNO.clear();
        vNAme.clear();
        vDate_yyyy.clear();
        vPhone.clear();
        vDate_mm.clear();
        vDate_dd.clear();
        vTole.clear();
        vWard.clear();
        vMunicipal.clear();
        vDistrict.clear();
        vState.clear();
        vINTime.clear();
        femalebtn.setSelected(false);
        malebtn.setSelected(false);
        otherbtn.setSelected(false);
        vStudentID.clear();
        vStdName.setText("");
        vOUTTime.clear();
        vRelation.clear();
    }

    @FXML
    void setINTime(){
        vINTime.setText(currentTime());
    }

    @FXML
    void setOUTTime(){
        vOUTTime.setText(currentTime());
    }

    @FXML
    void setDate(){
        Fun d = new Fun();
        d.setDate(); //d.setDate() is public function for find current date
        int year = d.get_Year();
        int month = d.get_Month();
        int day = d.get_Day();
        vDate_yyyy.setText(Integer.toString(year));
        vDate_mm.setText(Integer.toString(month));
        vDate_dd.setText(Integer.toString(day));
    }

    @FXML
    void searchBy(){
        boolean flag;
        if(VSNO.getText().isEmpty()){
            new MessageBox("Empty SID");
            return;
        }

        try{
            Connection con = DriverManager.getConnection(_url,_user, _password);
            PreparedStatement preparedStatement = con.prepareStatement("select * from visitor where Vsno = ?");
            preparedStatement.setInt(1,Integer.parseInt(VSNO.getText()));
            ResultSet s = preparedStatement.executeQuery();
            if(s.next()){
                System.out.println("Visitor Search Successfully");
                flag = true; // Found
                vNAme.setText(s.getString(2));
                setVsid(VSNO.getText());
                if(s.getString(3).equals("Female")) {
                    femalebtn.setSelected(true);
                }else if(s.getString(3).equals("Male")){
                    malebtn.setSelected(true);
                }else{
                    otherbtn.setSelected(true);
                }
                vPhone.setText(s.getString(4).substring(4));
//                vRelation.setText(s.getString(5));
                vState.setText(s.getString(5));
                vDistrict.setText(s.getString(6));
                vMunicipal.setText(s.getString(7));
                vWard.setText(s.getString(8));
                vTole.setText(s.getString(9));
            }else {
                flag = false;
                new MessageBox("This class_Visitor is not found.");
            }
            s.close();
            preparedStatement.close();
            con.close();

        }catch (SQLException e){
            new MessageBox("Enter correct SNo. of class_Visitor.");
            flag = false;
            System.out.println(e.getMessage());
        }


        if(flag) {
            try {
                Connection con2 = DriverManager.getConnection(_url, _user, _password);
                PreparedStatement preparedStatement = con2.prepareStatement("select OUT_time, IN_time, Vdate, StudentID,Relation from visitor_time where Vsno=?");
                preparedStatement.setInt(1, Integer.parseInt(VSNO.getText()));
                ResultSet s = preparedStatement.executeQuery();
                while (s.next()) {
                    String value = s.getString(1);
                    if (s.wasNull()) {
                        vINTime.setText(s.getString(2));
                        int[] bs = new int[3];
                        if (!s.getString(3).isEmpty()) {
                            bs = new Fun().dateSeperator(s.getString(3));
                        } else {
                            bs[0] = 0;
                            bs[1] = 0;
                            bs[2] = 0;
                        }
                        vDate_yyyy.setText(Integer.toString(bs[0]));
                        vDate_mm.setText(Integer.toString(bs[1]));
                        vDate_dd.setText(Integer.toString(bs[2]));
                        vStudentID.setText(s.getString(4));
                        vRelation.setText(s.getString(5));
                    }
                }
                s.close();
                preparedStatement.close();
                con2.close();
            } catch (SQLException e) {
                new MessageBox("Enter correct SNo. of class_Visitor.");
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    void UpdateData() {
        if (!checkVsid(VSNO.getText())) {
            new MessageBox("Invalid", "You may be change the Sno. Your old\nSno is '" + getVsid() + "'.");
            return;
        }
        genderActionEvent();
        String stdID = (vStudentID.getText());
        int yyyy = Integer.parseInt( vDate_yyyy.getText());
        int mm = Integer.parseInt(vDate_mm.getText());
        int dd = Integer.parseInt(vDate_dd.getText());
        String vDate = String.format("%04d-%02d-%02d", yyyy, mm, dd);
        String inTime = vINTime.getText();
        String outTime = vOUTTime.getText();
        String relation =  vRelation.getText();

        try {
            if ( vDate_yyyy.getText().isEmpty() || vDate_dd.getText().isEmpty() || vDate_mm.getText().isEmpty() || inTime.isEmpty() || outTime == null) {
                new MessageBox("All boxes are  mandatory");
                return;
            }
        }catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return;
        }
        if(isValid()) {
            try {
                con = DriverManager.getConnection(_url, _user, _password);
                String query2 = "update visitor_time set Vdate = ?, IN_time = ?, OUT_time = ?, StudentID = ?,Relation = ? where Vsno = ?";
                PreparedStatement statement1 = con.prepareStatement(query2);

                statement1.setString(1, vDate);
                statement1.setString(2, inTime);
                statement1.setString(3, outTime);
                statement1.setString(4, stdID);
                statement1.setString(5, relation);
                statement1.setInt(6, Integer.parseInt(VSNO.getText()));
                if (statement1.executeUpdate() > 0) {
//                    new MessageBox("class_Visitor is successfully submitted.");
                    System.out.println("Visitor update Successfully");
                    clearForm();
                } else {
                    new MessageBox("Error");
                }
                statement1.close();
                con.close();

            } catch (SQLException e) {
                new MessageBox("Unsuccessful process. Check every thing and try again.");
                System.out.println(e.getMessage());
            }
        }

    }

    @FXML
    void StudentFinder() {
        try {
            Connection con2 = DriverManager.getConnection(_url, _user, _password);
            PreparedStatement preparedStatement = con2.prepareStatement("select FName, LName from student where StudentID=?");
            preparedStatement.setString(1, vStudentID.getText());
            ResultSet set = preparedStatement.executeQuery();
            if(set.next()){
                vStdName.setText(set.getString(1) + " " + set.getString(2));
                studentFlag = true;
            }
            else {
                vStdName.setText("Not Found");
            }
            set.close();
            preparedStatement.close();
            con2.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            vStdName.setText("Not Found");
        }
    }

    private static String vsid = null;
    void setVsid(String s){
        vsid = s;
    }
    boolean checkVsid(String s){
        return vsid.equals(s);
    }
    String getVsid(){
        return vsid;
    }

    boolean isValid() {
        Validation v = new Validation();
        if (!v.isValidName(vNAme.getText())) {
            new MessageBox("Invalid class_Visitor name");
            return false;
        }else if (!v.isValidBSDate(Integer.parseInt(vDate_yyyy.getText()), Integer.parseInt(vDate_mm.getText()), Integer.parseInt(vDate_dd.getText()))) {
            new MessageBox("Date of Birth is in valid");
            return false;
        }else if (!v.isValidPhone("+977" + vPhone.getText())) {
            new MessageBox("Invalid Phone ");
            return false;
        }else if(!v.isValidTime(vINTime.getText().substring(0,5))){
            new MessageBox("Invalid IN Time ");
            return false;
        }else if(!vOUTTime.getText().isEmpty()){
            if(!v.isValidTime(vOUTTime.getText().substring(0,5))) {
                new MessageBox("Invalid OUT Time ");
                return false;
            }
        }
        return true;
    }

    //---------------------------------- class_Visitor Form ------------------------------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //---------------------------------- class_Visitor Table without OutTime ------------------------------------------------
    @FXML
    private TableColumn<class_Visitor, String> V_InTime;

    @FXML
    private TableColumn<class_Visitor, String> V_Name;

    @FXML
    private TableColumn<class_Visitor, Integer> V_Sno;

    @FXML
    private TableColumn<class_Visitor, String> V_StudentNAme;

    @FXML
    private TableView<class_Visitor> V_table;

    private final ObservableList<class_Visitor> VisData = FXCollections.observableArrayList();

    @FXML
    void showVisiting(){
        V_Sno.setCellValueFactory(new PropertyValueFactory<>("sno"));
        V_Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        V_StudentNAme.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        V_InTime.setCellValueFactory(new PropertyValueFactory<>("inTime"));

        showTable();

    }

    @FXML
    void showTable(){
        VisData.clear();
        try {
            Connection connection = DriverManager.getConnection(_url, _user, _password);
            String sql = "SELECT v.Vsno, v.Vname, s.FName, s.LName, vt.IN_time FROM visitor v JOIN visitor_time vt ON v.Vsno = vt.Vsno JOIN student s ON vt.StudentID = s.StudentID WHERE vt.OUT_time IS NULL";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
            do {
                int sno = resultSet.getInt("Vsno");
                String name = resultSet.getString("Vname");
                String studentName = resultSet.getString("FName") + " " + resultSet.getString("LName");
                String inTime = resultSet.getString("IN_time");
                class_Visitor V = new class_Visitor(sno, name, studentName, inTime);
                VisData.add(V);
            }while (resultSet.next());
            }else {
                new MessageBox("Empty Table");
                VisData.add(new class_Visitor(0, "null", "null", "empty"));
            }

            V_table.setItems(VisData);
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            // Handle database errors appropriately
        }
    }


    public static class class_Visitor {
        int sno;
        String name;
        String inTime;
        String studentName;

        public int getSno() {
            return sno;
        }
        public String getName() {
            return name;
        }
        public String getStudentName() {
            return studentName;
        }
        public String getInTime() {
            return inTime;
        }
        public class_Visitor(int sno, String name, String studentName, String inTime) {
            this.sno = sno;
            this.name = name;
            this.studentName = studentName;
            this.inTime = inTime;
        }
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
