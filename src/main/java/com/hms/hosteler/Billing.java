package com.hms.hosteler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Billing {

    String _url = new save().getDbms_url();
    String _user = new save().getDbms_user();
    String _password = new save().getDbms_password();
    private final ObservableList<String> Npmonths = FXCollections.observableArrayList("Baishakh", "Jestha", "Asar", "Shrawan", "Bhadau", "Aswin", "Kartik", "Mansir", "Poush", "Magh", "Falgun", "Chaitra");


    private int Total = 0, m1;
    boolean ch1 = false, ch2 = true; //to know monthly fee add or not

    @FXML
    private ChoiceBox<?> MMfrom;

    @FXML
    private ChoiceBox<?> MMto;

    @FXML
    private TextField OtherField;

    @FXML
    private TextField OtherFieldsRat;

    @FXML
    private TextField Paid;

    @FXML
    private TextField STDname;

    @FXML
    private TextField billNo;

    @FXML
    private CheckBox checkMonthly;

    @FXML
    private TextField dd;

    @FXML
    private Label InvalidMessage,message;

    @FXML
    private Label due_extra, due_extra_rs, due_extra_amt;

    @FXML
    private TextField mm;

    @FXML
    private TextField stdID;

    @FXML
    private Label totalAmount;

    @FXML
    private TextField yyyy;

    private boolean flag1 = false;

    @FXML
    void CheckOtherRate(){
        if(!(OtherFieldsRat.getText().isEmpty())){
            if(new Validation().isValidStringDigit(OtherFieldsRat.getText())){
                InvalidMessage.setText("");
                flag1 = true;
            }else {
                InvalidMessage.setText("Invalid");
                flag1 = false;
            }
        }
    }


    @FXML
    void initialize() {
        Month(MMfrom);
        Month(MMto);
        billNo.setText(PropertiesReader.getBillNo());
    }

    @FXML
    void Month(ChoiceBox A) {
        A.setValue("Months");
        A.setItems(Npmonths);
    }

    private int NoOfMonth(String MF, String MT) { // For Monthly fee
        int N , mf, mt;
        mf = NM(MF);
        mt = NM(MT);

        if (mt == 0 && mf != 0)
            N = 1;
        else {
            if (mf < mt) {
                N = mt - mf;
                N += 1;
            } else {
                N = mf - mt;
                N = 13 - N;
            }
        }
        return N;
    }

    private int NM(String M) {
        return switch (M) {
            case "Baishakh" -> 1;
            case "Jestha" -> 2;
            case "Asar" -> 3;
            case "Shrawan" -> 4;
            case "Bhadau" -> 5;
            case "Aswin" -> 6;
            case "Kartik" -> 7;
            case "Mansir" -> 8;
            case "Poush" -> 9;
            case "Magh" -> 10;
            case "Falgun" -> 11;
            case "Chaitra" -> 12;
            case null, default -> 0;
        };
    }

    //Total Amount Buttom -------------------------------
    @FXML
    private void toTable() {
        // Validation(); // this is missing
        int X = 0;
        if (MMfrom.getSelectionModel().isEmpty() && MMto.getSelectionModel().isEmpty()) {
            new MessageBox("Select Month", "Selection of month is mandatory.\nExample:-\n\t1 month:- From: Asar\n\t3 months:- From: Baishakh   To: Asar\n ");
        } else {
            X = NoOfMonth((String) MMfrom.getValue(), (String) MMto.getValue()); // No of month
        }
        if (X > 0 && X < 25) {  // month (0<X<25).
            if (checkMonthly.isSelected()) { //Monthly fee checkbox is selected or not
                ch1 = true;  // ch1 = True for one time add
                if (ch2) {
                    m1 = X; // store month in m1
                    TOTALAMOUNT("Monthly fee", 8000 * m1, true, false); //(T,F) for one time add
                }
                ch2 = false;
            } else {
                if (ch1) {
                    TOTALAMOUNT("Monthly fee ", 8000 * m1, false, true); //(F,T) for one time subtract
                    ch2 = true; // reset ch2 in initial condition
                    ch1 = false; // reset ch1
                }
            }
            Extras(X);
        } else if (X > 24) {
            new MessageBox("Large Calculation Error", "This system calculate fees upto 24 months. If you have to calculate more than 24 months. Make multiple bills.\nSorry for our limitation of calculation.");
        }
    }

    private void Extras(int x) {
        if(flag1) {
            if (!OtherField.getText().trim().isEmpty() && !OtherFieldsRat.getText().trim().isEmpty()) {
                // Both are not empty
                TOTALAMOUNT(OtherField.getText(), (Integer.parseInt(OtherFieldsRat.getText())) * x, true, true);
                OtherField.clear();
                OtherFieldsRat.clear();
            } else if ((OtherField.getText().trim().isEmpty() && !OtherFieldsRat.getText().trim().isEmpty()) || (!OtherField.getText().trim().isEmpty() && OtherFieldsRat.getText().trim().isEmpty())) {
                //For one is empty another is not
                new MessageBox("OtherFields", "Fill both OtherField and its rate or do not fill any one from two.");
            }
        }
    }

    @FXML
    private void AddonAction() {
        int x = NoOfMonth((String) MMfrom.getValue(), (String) MMto.getValue());
        if (x > 0 && x < 25) { // 0 month
            Extras(x);
        } else if (x > 24) {
            new MessageBox("Large Calculation Error", "This system calculate fees upto 24 months. If you have to calculate more than 24 months. Make multiple bills.\nSorry for our limitation of calculation.");
        } else { // For zero month
            new MessageBox("Select Month", "Selection of month is mandatory.\nExample:-\n\t1 month:- From: Asar\n\t3 months:- From: Baishakh To: Asar\n ");
        }
    }

    private void TOTALAMOUNT(String particular, int amt, boolean F1, boolean F2) {
        if (F1 && F2) { // T,T
            Total = Total + (amt);
            this.amt[i] = amt;
            par[i] = particular;
            i++;
        } else if (F1 && !F2) { // T,F
            Total = Total + amt;// other is only store monthly fee 1 time
            BILL.add(new Bills(particular, amt));
        } else if (!F1 && F2) { // F,T
            Total = Total - amt;
            BILL.clear();
        }
        totalAmount.setText(Total + ".00");// total is the label to see in window
        DUE_EXTRA2();
    }

    int i = 0;
    int[] amt = new int[10];
    String[] par = new String[10];
    //---------------------clear -------
    @FXML
    void clearForm() {
        yyyy.clear();
        mm.clear();
        dd.clear();
        stdID.clear(); // studentID
        STDname.clear();//Student Name
        Month(MMfrom); //Month
        Month(MMto);// Month Choicebox
        OtherField.clear();
        OtherFieldsRat.clear();
        totalAmount.setText("00.00");
        Paid.clear();
        checkMonthly.setSelected(false); //Monthly fee checkbox
    }

    @FXML
    private boolean FormChecker() {
        if ((STDname.getText() == null || stdID.getText() == null) && (yyyy.getText() == null || dd.getText() == null || mm.getText() == null)) {
            new MessageBox("Error", "Fill all Student information field.\nAnd Fill Date which is in top right corner.");
            return false;
        }
        Validation v = new Validation();
        if(!v.isValidStringDigit(billNo.getText())){
            new MessageBox("Invalid Bill No.");
            return false;
        }
        if(!v.isValidBSDate(Integer.parseInt(yyyy.getText()),Integer.parseInt(mm.getText()),Integer.parseInt(dd.getText())) ){
            new MessageBox("Invalid Date");
            return false;
        }
        if(!v.isValidStringDigit(OtherFieldsRat.getText())){
            new MessageBox("Invalid OtherField Rate.");
            return false;
        }

        return true;
    }

    @FXML
    void SET_DATE() {
        Fun d = new Fun();
        d.setDate(); //d.setDate() is public function for find current date
        int year = d.get_Year();
        int month = d.get_Month();
        int day = d.get_Day();
        yyyy.setText(Integer.toString(year));
        mm.setText(Integer.toString(month));
        dd.setText(Integer.toString(day));
    }

    @FXML
    void StudentFinder() {
        try {
            Connection con2 = DriverManager.getConnection(_url, _user, _password);
            PreparedStatement preparedStatement = con2.prepareStatement("select FName, LName from student where StudentID=?");
            preparedStatement.setString(1, stdID.getText());
            ResultSet set = preparedStatement.executeQuery();
            if(set.next()){
                STDname.setText(set.getString(1) + " " + set.getString(2));
            }
            else {
                STDname.setText("Student is Not Found");
            }
            set.close();
            preparedStatement.close();
            con2.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            STDname.setText("Not Found");
        }
    }
    @FXML// To find Due or Extra
    void DUE_EXTRA() {
        if (!(new Validation().isValidStringDigit(Paid.getText()))) {
            message.setText("Character not allowed in Paid Amount.");
            return;
        }
        String paidText = Paid.getText().trim(); // Trim whitespace
        if (paidText.isEmpty()) {
            // Handle empty input case
            // For example, display an error message or set a default value
            message.setText("Paid amount cannot be empty.");
            return;
        }
        if (!(new Validation().isValidStringDigit(paidText))) {
            message.setText("Character not allowed in Paid Amount.");
            return;
        }
        DUE_EXTRA2();
    }

    @FXML
    void DUE_EXTRA2(){
        if (Total == 0) {
            return;
        }
        if (!(new Validation().isValidStringDigit(Paid.getText()))) {
            return;
        }
        String paidText = Paid.getText().trim(); // Trim whitespace
        if (paidText.isEmpty()) {
            return;
        }
        if (!(new Validation().isValidStringDigit(paidText))) {
            return;
        }
        if (Integer.parseInt(Paid.getText()) == Total) {
            due_extra.setText("");
            due_extra_rs.setText("");
            due_extra_amt.setText("");
        } else if (Integer.parseInt(Paid.getText()) > Total) {
            due_extra.setText("Extra: ");
            due_extra_rs.setText("Rs");
            due_extra_amt.setText(Integer.toString(Integer.parseInt(Paid.getText()) - Total));
        } else if (Integer.parseInt(Paid.getText()) < Total) {
            due_extra.setText("Due: ");
            due_extra_rs.setText("Rs");
            due_extra_amt.setText(Integer.toString(Total - Integer.parseInt(Paid.getText())));
        }
    }

    @FXML
    String getUserNAme() {
        String s;
        try {

            Connection con2 = DriverManager.getConnection(_url, _user, _password);
            PreparedStatement preparedStatement = con2.prepareStatement("select FName, LName from employee where HostelerId=?");
            preparedStatement.setString(1, new save().getHOSTELER_ID());
            ResultSet set = preparedStatement.executeQuery();
            if(set.next()){
                 s = (set.getString(1) + " " + set.getString(2));
            }
            else {
                s = "Employee";
            }
            set.close();
            preparedStatement.close();
            con2.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            s = "Not Found";
        }
        return s;
    }
    private static String studentName,studentId,date,bills,FMonth,TMonth,Receiver;
    private static int t,p;


    public static String getStudentName() {
        return studentName;
    }

    public static String getStudentId() {
        return studentId;
    }

    public static String get_Date() {
        return date;
    }

    public static String getBills() {
        return bills;
    }

    public static String getFMonth() {
        return FMonth;
    }

    public static String getTMonth() {
        return TMonth;
    }

    public static String getReceiver() {
        return Receiver;
    }

    public static int getT() {
        return t;
    }

    public static int getP() {
        return p;
    }

    void particularsSetter(){
        for (int j = 0; j < i; j++){
            BILL.add(new Bills(par[j],amt[j]));
        }
    }

    public static ObservableList<Bills> getBILL() {
        return Billing.BILL;
    }

    static Stage stage = new Stage();


    @FXML
    void ReceiptFunction(){
        if(!FormChecker()){
            return;
        }
        bills = billNo.getText(); // bill No
        studentId = stdID.getText(); // StudentID
        studentName =STDname.getText(); // StudentName
        t = Total; // TotalAmount
       p =  Integer.parseInt(Paid.getText());
        // For Month From to
        if( MMto.getValue() == null || MMto.getValue().equals("Months") || MMto.getValue() == "") {
            TMonth = "";
        }else {
            TMonth =(String) MMto.getValue();
        }
        FMonth = (String) MMfrom.getValue();

        Receiver =getUserNAme();
        int joinYear = Integer.parseInt(yyyy.getText());
        int joinMonth = Integer.parseInt(mm.getText());
        int joinDay = Integer.parseInt(dd.getText());
        date = String.format("%04d-%02d-%02d", joinYear, joinMonth, joinDay);

        particularsSetter();
        //This set data on the ObserableList

        try {
            FXMLLoader loader = new FXMLLoader(ProfileController.class.getResource("modules/Billing/receipt.fxml"));
            Parent root = loader.load();

            // Get the controller instance and set the stage reference
//            Receipt controller = loader.getController();

            int x = 860;
            int y = 570;
            Scene scene = new Scene(root);
            stage.setMinWidth(x);
            stage.setMinHeight(y);
            stage.setMaxWidth(x);
            stage.setMaxHeight(y);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Calling Receipt : " + e.getMessage());
        }

    }

//    @FXML
//    public static void closeStage(){
//        stage.close();
//    }

    private static final ObservableList<Bills> BILL = FXCollections.observableArrayList();

    public static class Bills{
        String particular;
        int amount;

        public Bills(String particular, int amount) {
            this.particular = particular;
            this.amount = amount;
        }

        public String getParticular() {
            return particular;
        }

        public int getAmount() {
            return amount;
        }
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}