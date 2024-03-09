package com.hms.hosteler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;


public class VisitorDetails {

    String _url = new save().getDbms_url();
    String _user = new save().getDbms_user();
    String _password = new save().getDbms_password();
    @FXML
    private TableColumn<data, String> V_address;

    @FXML
    private TableColumn<data, String> V_date;

    @FXML
    private TableColumn<data, String> V_inTime;

    @FXML
    private TableColumn<data, String> V_name;

    @FXML
    private TableColumn<data, String> V_outTIme;

    @FXML
    private TableColumn<data, String> V_phone;

    @FXML
    private TableColumn<data, String> V_relation;

    @FXML
    private TableColumn<data, Integer> V_sno;

    @FXML
    private TableColumn<data, String> V_studentID;

    @FXML
    private TableColumn<data, String> V_studentNAme;

    @FXML
    private TableView<data> V_table;

    @FXML
    private Label lbl_count1;

    @FXML
    private Label lbl_count2;

    int c1, c2;

    boolean SearchFlag = false;

    private final ObservableList<data> DATA = FXCollections.observableArrayList();

    @FXML
    void initialize(){
        assert lbl_count1 != null : "fx:id=\"lbl_count1\" was not injected: check your FXML file 'VisitorDetails.fxml'.";
        assert lbl_count2 != null : "fx:id=\"lbl_count2\" was not injected: check your FXML file 'VisitorDetails.fxml'.";
        V_sno.setCellValueFactory(new PropertyValueFactory<>("sno"));
        V_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        V_studentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        V_studentNAme.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        V_relation.setCellValueFactory(new PropertyValueFactory<>("relation"));
        V_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        V_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        V_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        V_inTime.setCellValueFactory(new PropertyValueFactory<>("inTime"));
        V_outTIme.setCellValueFactory(new PropertyValueFactory<>("outTime"));

        showTable();

        setCount();
    }

    @FXML
    void showTable(){

        DATA.clear();
        try{
            Connection connection = DriverManager.getConnection(_url,_user,_password);
            String Qry;
            PreparedStatement ps = null;
            if(SearchFlag){
                Qry ="SELECT v.Vsno, v.Vname,s.StudentID, s.FName, s.LName,vt.Relation, v.Vphone, v.Vdistrict, vt.Vdate, vt.IN_time, vt.OUT_time FROM visitor v JOIN visitor_time vt ON v.Vsno = vt.Vsno JOIN student s ON vt.StudentID = s.StudentID where v.Vsno = ?";
                ps = connection.prepareStatement(Qry);
                ps.setInt(1,Integer.parseInt(VSN_SearchBar.getText()));

            }else {
            Qry ="SELECT v.Vsno, v.Vname,s.StudentID, s.FName, s.LName,vt.Relation, v.Vphone, v.Vdistrict, vt.Vdate, vt.IN_time, vt.OUT_time FROM visitor v JOIN visitor_time vt ON v.Vsno = vt.Vsno JOIN student s ON vt.StudentID = s.StudentID order by v.Vsno";
            ps = connection.prepareStatement(Qry);
            }

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                do{
                    int sno = rs.getInt(1);
                    String name = rs.getString(2);
                    String studentID = rs.getString(3);
                    String studentName = (rs.getString(4) + " " + rs.getString(5));
                    String relation = rs.getString(6);
                    String phone = rs.getString(7);
                    String address = rs.getString(8);
                    String date = rs.getString(9);
                    String inTime = rs.getString(10);
                    String outTime = rs.getString(11);

                    data d = new data(sno,name,studentID,studentName,relation,phone,address,date,inTime,outTime);
                    DATA.add(d);

                }while (rs.next());
            }else {
                new MessageBox("Empty Table");
                data d = new data(0,"name","studentID","studentName","relation","phone","address","date","inTime","outTime");
                DATA.add(d);
            }
            rs.close();
            ps.close();
            connection.close();

            V_table.setItems(DATA);
//            lbl_count2.setText(Integer.toString(c2));
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void setCount(){
        c1 = 0;
        try{
            Connection con = DriverManager.getConnection(_url,_user,_password);
            String qry = "select count(*) from class_Visitor";
            PreparedStatement pst = con.prepareStatement(qry);
            ResultSet resultSet = pst.executeQuery();
            if(resultSet.next()){
                c1 = resultSet.getInt(1);
            }
            resultSet.close();
            pst.close();
            con.close();
            lbl_count1.setText(Integer.toString(c1));

             con = DriverManager.getConnection(_url,_user,_password);
             qry = "select count(*) from visitor_time";
            PreparedStatement pst1 = con.prepareStatement(qry);
            ResultSet result = pst1.executeQuery();
            if(result.next()){
                c2 = result.getInt(1);
            }
            result.close();
            pst1.close();
            con.close();
            lbl_count2.setText(Integer.toString(c2));
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private TextField VSN_SearchBar;
    @FXML
    void VisitorSearchFun(){
        if(VSN_SearchBar.getText().isEmpty()){
            new MessageBox("Empty Search Bar");
            return;
        }
        if(!new Validation().isValidStringDigit(VSN_SearchBar.getText())){
            new MessageBox("Invalid Enter");
            return;
        }
        SearchFlag = true;
        showTable();
    }


    public static class data{
         private final int sno;
        private final String name,studentName,studentID,relation,phone,address,date,inTime,outTime;

        public data(int sno, String name, String studentID, String studentName, String relation, String phone, String address,String date, String inTime, String outTime) {
            this.sno = sno;
            this.name = name;
            this.studentName = studentName;
            this.studentID = studentID;
            this.relation = relation;
            this.phone = phone;
            this.address = address;
            this.date = date;
            this.inTime = inTime;
            this.outTime = outTime;
        }

        public int getSno() {
            return sno;
        }

        public String getDate() {
            return date;
        }

        public String getName() {
            return name;
        }

        public String getStudentName() {
            return studentName;
        }

        public String getStudentID() {
            return studentID;
        }

        public String getRelation() {
            return relation;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }

        public String getInTime() {
            return inTime;
        }

        public String getOutTime() {
            return outTime;
        }
    }
}
