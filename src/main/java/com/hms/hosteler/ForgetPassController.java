package com.hms.hosteler;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Random;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
//import path.to.your.OTPManager;

public class ForgetPassController {
    String _url = new save().getDbms_url();
    String _user = new save().getDbms_user();
    String _password = new save().getDbms_password();

    boolean zz = false;

    @FXML
    private  Label FP_label_sending;

    @FXML
    private AnchorPane anchorF;

    @FXML
    private TextField FP_ConfirmOTP;

    @FXML
    private TextField FP_ConfirmPassword;

    @FXML
    private TextField FP_Email;

    @FXML
    private TextField FP_UserID;

    @FXML
    private Label FP_InvalidOTP_label,Label_Error;

    @FXML
    private TextField FP_NewPassword;

    @FXML
    private Label FP_PasswordNotMatch;

    @FXML
    private Pane step1FP, step2FP, step3FP; // confirmEmail, OTP, ChangePassword;

    private String userid = null;

    private void changePane(Pane pane1, Pane pane2) //(currentPaneID, nextPaneID)
    {// Function to make visible Pane
        pane1.visibleProperty().set(false);
        pane1.setDisable(true);
        pane2.setDisable(false);
        pane2.visibleProperty().set(true);
    }
    //-----------------------ActionEvents----------------------------------
    @FXML
    void switch2login() throws IOException {new sceneSwitchController(anchorF, "login.fxml");}
    @FXML
    void emailtootp() {
        changePane(step1FP, step2FP);
    }
    @FXML
    void otp2email() {
        changePane(step2FP, step1FP);
    }

    @FXML
    void otp2pass() {
        changePane(step2FP,step3FP);
    }

    @FXML
    void pass2otp(){
        changePane(step3FP, step2FP);
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------------------- userId and email confirmation -----------------------

    @FXML // This is called  in onAction of email conformation page
    void EmailChecker() {
        userid = FP_UserID.getText();
        String email = FP_Email.getText();
        try {
            Connection con = DriverManager.getConnection(_url, _user, _password);
            //Checking userID from login
            String query1 = "select Email from employee natural join login where UserID=?";
            PreparedStatement statement1 = con.prepareStatement(query1);
            statement1.setString(1, userid);
            ResultSet resultSet1 = statement1.executeQuery();

            if(resultSet1.next()) {
                String tempEmail = resultSet1.getString("Email");
                System.out.println(tempEmail+"\n"+ email);

                if (email.equals(tempEmail)) {
                    Label_Error.setText("");
                    FP_label_sending.setText("Wait a moment...");
                    OTPSending();
                    if (zz) {
                        new MessageBox("OTP sent", "Your are OTP is sent successfully in your email.\nConfirm it in next page and change your password.\nOPT is valid for only 2 minutes.");
                        zz = false;
                        emailtootp();
                    }
                }else {
                    Label_Error.setText("Invalid Enter");
                    System.out.println("Invalid Email");
                }
            }else {
                Label_Error.setText("Invalid Enter");
                System.out.println("Invalid UserID");
            }
            resultSet1.close();
            statement1.close();
            con.close();
        }catch (SQLException e){
            Label_Error.setText("Invalid Enter");
            System.out.println(e.getMessage() + "SQLException");
        }
    }
    //------------------- userId and email confirmation -------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------------------- sent OTP --------------------------
    @FXML
    protected void OTPSending() {
        // Generate a random 6-digit OTP
        String otp1 = generateRandomOTP();
        String otp2 = generateRandomOTP();
        saveOTP(userid, (otp1 + otp2));
        sendOTP(otp1, otp2);
        zz = true;
    }

    private String generateRandomOTP() {
        StringBuilder otp = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            otp.append(random.nextInt(10)); // Generate a random digit (0-9)
        }
        return otp.toString();
    }

    private void sendOTP(String otp1, String otp2) {
        String to = FP_Email.getText();
        String  HostelName = PropertiesReader.getHostelName();
        String name = getName();

        String from = "srrr4286@gmail.com";
        String pass = "wvjtvocaploibirn";
        //"wvjtvocaploibirn" SMTP password

        String messageBody = "Hi " + name +" ("+userid+"),\n" +
                "\n" +
                "You recently requested a password reset for your "+ HostelName +" account. Your one-time password (OTP) is:\n" +
                "\n" +
                "[ "+otp1+" "+otp2+" ]\n" +
                "\n" +
                "Please enter this code on the password reset page to create a new password. This OTP is valid for 2 minutes only.\n" +
                "\n" +
                "If you did not request a password reset, please disregard this email and contact the hostel administration immediately.\n" +
                "\n" +
                "Thank you,\n" +
                "\n" +
                "The HOSTELER Team";

        // Set up properties for SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Create a Session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });

        try {
            // Create a MimeMessage using the Session
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("HOSTELER - Password Reset");
            message.setText(messageBody);

            // Send the message
            Transport.send(message);
        }catch (Exception e){
            new MessageBox("Email is not sent. Please, check your connectivity.");
        }
    }

    String getName()  {
        try {
            String Fname = "Sir/Madam", Lname = "";
            Connection con = DriverManager.getConnection(_url, _user, _password);
            String qq = "select FName,LName from employee as e where e.HostelerId=(select l.HostelerId from login as l where UserID = ?)";
            PreparedStatement statement = con.prepareStatement(qq);
            statement.setString(1, userid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Fname = resultSet.getString("FName");
                Lname = resultSet.getString("LName");
            }
            statement.close();
            resultSet.close();
            con.close();
            return (Fname + " " + Lname);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return "User";
        }
    }
    //----------------------- sent OTP ----------------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------------------confirm OTP------------------
    private static final int OTP_EXPIRY_TIME = 2 * 60 * 1000; // 2 minutes in milliseconds
    private static final Map<String, String> otpMap = new HashMap<>();
    @FXML
    public void OTPHandle() {  // This is the function action on OTP next button
        String enteredOTP = FP_ConfirmOTP.getText();
        String userID = userid; // Get the UserID from your application context
        if (!otpMap.containsKey(userID)) {
            // OTP has expired
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("OTP Expired");
            alert.setHeaderText("OTP has expired. Please request a new one.");
            alert.showAndWait();
            return; // Exit the function
        }
        if (validateOTP(userID, enteredOTP)) {
            otp2pass();
        } else {
            FP_InvalidOTP_label.setText("Invalid OTP.");
        }
    }
        private static void saveOTP(String userID, String otp) {
            otpMap.put(userID, otp);

            // Schedule automatic removal after 2 minutes
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    otpMap.remove(userID);
                }
            }, OTP_EXPIRY_TIME);
        }

        public static boolean validateOTP(String userID, String otp) {
            String savedOTP = otpMap.get(userID);
            return savedOTP != null && savedOTP.equals(otp);
        }

    //------------------confirm otp------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //----------------------- change Password ----------------------------------
    @FXML
    void changePasswordMethod() {
            String NP = FP_NewPassword.getText(); // New Password
            String CP = FP_ConfirmPassword.getText(); // Confirm Password

            if (!NP.equals(CP)){
                FP_PasswordNotMatch.setText("Password not match.");
            }else {
                if(new Validation().isPasswordValid(NP)) {
                    try {
                        Connection can = DriverManager.getConnection(_url, _user, _password);
                        String qu = "UPDATE login SET Password = ? where UserID = ?";
                        PreparedStatement statement = can.prepareStatement(qu);
                        statement.setString(1, NP);
                        statement.setString(2, userid);
                        int rowAffected = statement.executeUpdate();
                        if (rowAffected > 0) {
                            new MessageBox("Password Changed", "Congratulation you change password successfully. Now\nlogin with new password.");
                            switch2login();
                        }
                        statement.close();
                        can.close();
                    }catch (SQLException | IOException e){
                        new MessageBox("Sorry Message", "Due to some reason your password is not updated\n in the system.Sorry for that.\nTry again or restart or reload the system.");
                    }
                }else {
                    new MessageBox("Create strong password");
                }
            }
    }
    //----------------------- change Password ----------------------------------

    //----------------------- OTP Resend ----------------------------------
    @FXML
    void OTPResendFun() {
        OTPSending();
        if (zz) {

            new MessageBox("OTP Resent", "OTP is resent successfully in your email. Confirm it in next page and change your password.\n" +
                    "OPT is valid for only 2 minutes.");
            zz = false;
        }
    }
    //----------------------- OTP Resend ----------------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @FXML
    void EnterHandle1st(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            EmailChecker();
        }
    }

    @FXML
    void EnterHandle2nd(KeyEvent event)  {
        if (event.getCode() == KeyCode.ENTER) {
            OTPHandle();
        }
    }

    @FXML
    void EnterHandle3rd(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            changePasswordMethod();
        }
    }
    //----------------------- ActionEvents ----------------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
