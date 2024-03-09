package com.hms.hosteler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


import java.sql.*;
import java.util.Random;

public class Setting {
    String _url = new save().getDbms_url();
    String _user = new save().getDbms_user();
    String _password = new save().getDbms_password();
    static boolean set_module_vis = false; //This is for to check any setting module is visible or not.

    private static Pane visiblePane; // to store visible pane

    save user = new save();
    Validation validation = new Validation();
    String ID = user.getHOSTELER_ID(); // ID used to store existing Hosteler_id
    String userid = user.getUSER_ID();

    @FXML
    private Pane CreateUser, DeleteUser, ChangePh, ChangePassword, ChangeEmail, AddRoom, DeleteRoom, UpdateOccupancy;

    public Setting() throws SQLException {
    }
    // ModulesId or PaneID of modules


    //------------------FUNCTIONS-----------------------------------------
    private void changePane(Pane pane1) //(current_pane_ID, nextPaneID)
    {// Function to make visible Pane
        if (set_module_vis) {
            visiblePane.visibleProperty().set(false);
            pane1.visibleProperty().set(true);
            visiblePane = pane1;
        } else {
            pane1.visibleProperty().set(true);
            visiblePane = pane1;
            set_module_vis = true;
        }
    }

    private void Cancels() {
        visiblePane.visibleProperty().set(false);
        set_module_vis = false;
        checkerCreateUser = false;
    }

    //----------------------------FUNCTIONS---------------------------

    //----------------------------ActionEvents-------------------------
    //--------ActionEvents for Modules---------------------

    @FXML
    private void toCreateUser() {
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to Create User.");
            return;
        }
        changePane(CreateUser);
    }

    @FXML
    private void toDeleteUser() {
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to Delete User.");
            return;
        }changePane(DeleteUser);
    }

    @FXML
    private void toChangePh() {
        ch_init();
        changePane(ChangePh);
    }

    @FXML
    private void toChangePassword() {
        changePane(ChangePassword);
    }

    @FXML
    private void toChangeEmail() {
        changePane(ChangeEmail);
    }

    @FXML
    private void toAddRoom() {
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to Add Room.");
            return;
        }
        changePane(AddRoom);
    }

    @FXML
    void toDeleteRoom() {
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to Delete Room.");
            return;
        }changePane(DeleteRoom);
    }

    @FXML
    private void toUpdateOccupancy() {
        if(!(new Permission().permission())){
            new MessageBox("Only warden have permission to update Occupancy.");
            return;
        }changePane(UpdateOccupancy);}

    boolean check_UserPassword(String password) {
        try {
            Connection con = DriverManager.getConnection(_url, _user, _password);
            String query2 = "SELECT COUNT(*) FROM login WHERE UserId=? AND Password=?";
            PreparedStatement statement1 = con.prepareStatement(query2);
            statement1.setString(1, userid); // put userID in first '?' in userid = ?
            statement1.setString(2, password); // same as UserID
            ResultSet resultSet = statement1.executeQuery();
            resultSet.next();
            int x = resultSet.getInt(1);
            resultSet.close();
            statement1.close();
            con.close();
            if (x == 1) {
                return true;
            }
        } catch (SQLException e) {
            return  false;
        }
        return false;
    }

    //--------ActionEvents for Modules---------------------
    //-------------------------------------------------------------------------------------------------
    //--------ActionEvents for Buttons---------------------
    public void cancelPane() {
        Cancels();
    }


    //--------ActionEvents for Buttons--------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //----------------------- Create user --------------------------------
    @FXML
    private TextField createUser_DPassword;

    @FXML
    private TextField createUser_ID;

    @FXML
    private Label label_InvalidHostelerID,CU_label1;

    boolean checkerCreateUser = false;


    @FXML
    void CreateUser_Fun1() throws SQLException {

        Connection con = DriverManager.getConnection(_url, _user, _password);

        String query1 = "SELECT COUNT(*) FROM employee WHERE HostelerId=?";
        String query2 = "SELECT COUNT(*) FROM login WHERE HostelerId=?";

        PreparedStatement statement1 = con.prepareStatement(query2);
        statement1.setString(1, createUser_ID.getText());
        ResultSet resultSet = statement1.executeQuery();
        resultSet.next();
        int x = resultSet.getInt(1);

        if (x == 1) {
            label_InvalidHostelerID.setText("Already login access.");
        } else {
            PreparedStatement statement2 = con.prepareStatement(query1);
            statement2.setString(1, createUser_ID.getText());
            ResultSet resultSet2 = statement2.executeQuery();
            resultSet2.next();
            int y = resultSet2.getInt(1);

            if (y == 1) {
                String Default = Generate_DefaultPassword();
                createUser_DPassword.setText(Default);
                checkerCreateUser = true;
                label_InvalidHostelerID.setText("Found.");
            } else {
                label_InvalidHostelerID.setText("Not Found.");
            }
            resultSet2.close();
            statement2.close();
        }
        resultSet.close();
        statement1.close();
        con.close();
    }


    public static String Generate_DefaultPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*?";
        int length = 8; // Adjust password length as needed
        Random random = new Random();
        StringBuilder passwordBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            passwordBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return passwordBuilder.toString();
    }

    @FXML
    void CreateUser_Fun2(){ // Create save onAction
            if (checkerCreateUser) {
                try {
                    Connection con1 = DriverManager.getConnection(_url, _user, _password);
                    String query1 = "insert into login values (?,?,?)";
                    PreparedStatement statement2 = con1.prepareStatement(query1);
                    statement2.setString(1, createUser_ID.getText());
                    statement2.setString(2, createUser_DPassword.getText());
                    statement2.setString(3, createUser_ID.getText());
                    int rowsAffected = statement2.executeUpdate();
                    statement2.close();
                    con1.close();

                    if (rowsAffected > 0) {
                        new MessageBox("Create save Successfully");
                        createUser_ID.clear();
                        createUser_DPassword.clear();
                        label_InvalidHostelerID.setText("");
                        checkerCreateUser = false;
                    } else {
                        new MessageBox("Unsuccessful Process. Try again after some time");
                    }
                }catch (SQLException e){
                    new MessageBox("Error in Creating");
                }
            } else {
                new MessageBox("Please, complete above process first. Then\n click this button.");
            }
    }

    //------------------- create user -----------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------------------- Delete user -----------------------------
    @FXML
    private TextField DU_HostelerId;

    @FXML
    private TextArea DU_deleteReason;

    @FXML
    private Label DE_label1;

    boolean DeleteUserFlag = false;

    @FXML
    void UserChecker_DeleteUSer() {
        if (DU_HostelerId.getText().isEmpty()) {
            return;
        }
        try {
            DeleteUserFlag = false;
            Connection con = DriverManager.getConnection(_url, _user, _password);
            String qq = "Select HostelerId from login natural join employee where HostelerId = ?";
            PreparedStatement statement = con.prepareStatement(qq);
            statement.setString(1, DU_HostelerId.getText());
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                DeleteUserFlag = true;
                DE_label1.setText("Found");
            } else {
                DeleteUserFlag = false;
                DE_label1.setText("");
            }
            rs.close();
            statement.close();
            con.close();
        }catch (SQLException e){
            DeleteUserFlag = false;
            new MessageBox("System Error");
        }
    }
    @FXML
    void DeleteUserFun() throws SQLException {
        if (DU_HostelerId.getText().isEmpty() || DU_deleteReason.getText().isEmpty()) {
            new MessageBox("Both HostelerId and Reason for delete is mandatory.");
            return;
        }
        if (DU_HostelerId.getText().equals(new save().getHOSTELER_ID())) {
            new MessageBox("Unusual Process", "Sorry, you cannot delete yourself.");
            return;
        }
        if(!DeleteUserFlag){
            return;
        }
        try {
            Connection con = DriverManager.getConnection(_url, _user, _password);
            String qq = "DELETE FROM login WHERE HostelerId = ? LIMIT 1 ";
            PreparedStatement statement = con.prepareStatement(qq);
            statement.setString(1, DU_HostelerId.getText());
            int rowAffected = statement.executeUpdate();
            statement.close();
            con.close();
            if(rowAffected > 0){
//                DE_label1.setText("Found");
//                new MessageBox("Delete Successful");
                System.out.println("Delete Successful from login");
            }else {
                DeleteUserFlag = false;
                new MessageBox("Delete Unsuccessful");
            }
        }catch (SQLException e){
            new MessageBox("System Error");
        }
        if (DeleteUserFlag) {
            Connection conn = DriverManager.getConnection(_url, _user, _password);
            String q2 = "INSERT INTO deleted_user values (?,?)";
            PreparedStatement statement1 = conn.prepareStatement(q2);
            statement1.setString(1, DU_HostelerId.getText());
            statement1.setString(2, DU_deleteReason.getText());
            int ff = statement1.executeUpdate();
            statement1.close();
            conn.close();
            if (ff > 0) {
                DU_HostelerId.clear();
                DeleteUserFlag = false;
                DE_label1.setText("");
                DU_deleteReason.clear();
                new MessageBox("Delete save successfully.");
                System.out.println("Data added in deleted_user");
            }else {
                new MessageBox("User is not found");
            }
        }

    }

    //------------------- Delete user -----------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------------------- ChangePhone no -----------------------------
    @FXML
    private TextField CPN_NewPh;

    @FXML
    private PasswordField CPN_Pass;

    String item1;

    String item2;
    private final ObservableList<String> list = FXCollections.observableArrayList(item1, item2);
    void refreshPhone() { // Refresh phone no after update
        try {
            item1 = getPhone();
        } catch (SQLException e) {
            item1 = "Not Found";
        }
        try {
            item2 = getPhone2();
        } catch (SQLException e) {
            item2 = "";
        }
        list.clear();
        list.add(item1);
        list.add(item2);
    }
    @FXML
    private ChoiceBox<String> CPN_ph;

    void ch_init() {
        refreshPhone();
        CPN_ph.setValue("Choose phone");
        CPN_ph.setItems(list);
    }

    String p;

    String getPhone() throws SQLException { // to get Primary Phone number
        Connection con1 = DriverManager.getConnection(_url, _user, _password);
        String ph = "select phone1 from employee  where HostelerId = ?";
        PreparedStatement statement = con1.prepareStatement(ph);
        statement.setString(1, ID);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        p = resultSet.getString("Phone1");
        resultSet.close();
        statement.close();
        con1.close();
        return p;
    }

    String getPhone2() throws SQLException { // to get Secondary Primary Phone number
        Connection con1 = DriverManager.getConnection(_url, _user, _password);
        String ph2 = "select phone2 from employee  where HostelerId = ?";
        PreparedStatement statement = con1.prepareStatement(ph2);
        statement.setString(1, ID);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        p = resultSet.getString("Phone2");
        resultSet.close();
        statement.close();
        con1.close();
        return p;
    }

    @FXML
    void setChangePh() throws SQLException {

        String newPhone = CPN_NewPh.getText();
        String pass = CPN_Pass.getText();


        if (check_UserPassword(pass)) {// successful
            if(!(new Validation().isValidPhone("+977"+newPhone))){
                new MessageBox("Invalid Phone Number");
                return;
            }
            String q1 = "UPDATE employee SET phone1 = ? where HostelerId = ?";
            String q2 = "UPDATE employee SET phone2 = ? where HostelerId = ?";
            int r;
            String selected = CPN_ph.getValue();
            if (item1.equals(selected)) { //item1.equals(CPN_ph.getValue()) this gives true if selected phone no. is primary number.
                Connection con2 = DriverManager.getConnection(_url, _user, _password);
                PreparedStatement statement2 = con2.prepareStatement(q1);
                statement2.setString(1, ("+977" + newPhone));
                statement2.setString(2, ID);
                r = statement2.executeUpdate();
                statement2.close();
                con2.close();
            } else if (item2.equals(selected)) {
                Connection con3 = DriverManager.getConnection(_url, _user, _password);
                PreparedStatement statement3 = con3.prepareStatement(q2);
                statement3.setString(1, ("+977" + newPhone));
                statement3.setString(2, ID);
                r = statement3.executeUpdate();
                statement3.close();
                con3.close();
            } else {
                new MessageBox("Please, select your phone number to change.");
                return;
            }
            if (r > 0) {
                CPN_NewPh.clear();
                CPN_Pass.clear();
                ch_init();
                new MessageBox("Phone number is successfully changed.");
            }
        } else {
            new MessageBox("Wrong Password");
        }
    }

    //------------------- ChangePhone no -----------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------------------- ChangePassword no -----------------------------
    @FXML
    private PasswordField CP_ConfirmPass_Pass;

    @FXML
    private TextField CP_ConfirmPass_Text;

    @FXML
    private PasswordField CP_NewPass_Pass;

    @FXML
    private TextField CP_NewPass_Text;

    @FXML
    private PasswordField CP_OldPass_Pass;

    @FXML
    private TextField CP_OldPass_Text;

    @FXML
    private ImageView CP_eye1;

    @FXML
    private ImageView CP_eye2;

    @FXML
    private ImageView CP_eye3;

    @FXML
    private Label CP_label_Invalid;

    @FXML
    void changePasswordFun() throws SQLException {
        new setPasswordVisible().textSetter(CP_OldPass_Pass, CP_OldPass_Text);
        new setPasswordVisible().textSetter(CP_NewPass_Pass, CP_NewPass_Text);
        new setPasswordVisible().textSetter(CP_ConfirmPass_Pass, CP_ConfirmPass_Text);
        String pass = CP_OldPass_Pass.getText();
        String NewPass = CP_NewPass_Pass.getText();
        String ConfirmPass = CP_ConfirmPass_Pass.getText();

        if (pass.isEmpty() || NewPass.isEmpty() || ConfirmPass.isEmpty()) {
            new MessageBox("All field are mandatory to update");
            return;
        }
        if (!(NewPass.equals(ConfirmPass))) {
            CP_label_Invalid.setText("New password and retype password are not match.");
            return;
        }

        if (check_UserPassword(pass)) {
            if(!(new Validation().isPasswordValid(NewPass))){
                new MessageBox("Password is too weak like you. Please create\nStrong Password.");
                return;
            }
            Connection conn = DriverManager.getConnection(_url, _user, _password);
            String query = "UPDATE login SET Password=? WHERE UserID=?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, NewPass); // put userID in first '?' in userid = ?
            st.setString(2, userid); // same as UserID
            int y = st.executeUpdate();
            st.close();
            conn.close();
            if (y > 0) {
                new MessageBox("Your password is change successfully.");
                CP_OldPass_Pass.clear();
                CP_OldPass_Text.clear();
                //CP_OldPass_Text.visibleProperty().set(false);
                CP_NewPass_Pass.clear();
                CP_NewPass_Text.clear();
                //CP_NewPass_Text.visibleProperty().set(false);
                CP_ConfirmPass_Pass.clear();
                CP_ConfirmPass_Text.clear();
                //CP_ConfirmPass_Text.visibleProperty().set(false);
                CP_label_Invalid.setText(null);
            }
        } else {
            new MessageBox("Current Password is incorrect.");
        }
    }

    @FXML
    void eye1Fun() {
        new setPasswordVisible(CP_OldPass_Pass, CP_OldPass_Text, CP_eye1);
    }

    @FXML
    void eye2Fun() {
        new setPasswordVisible(CP_NewPass_Pass, CP_NewPass_Text, CP_eye2);
    }

    @FXML
    void eye3Fun() {
        new setPasswordVisible(CP_ConfirmPass_Pass, CP_ConfirmPass_Text, CP_eye3);
    }

    //------------------------- ChangePassword no -----------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //-------------------------------- Change Email -----------------------------
    @FXML
    private PasswordField CEA_Password;

    @FXML
    private TextField CEA_Password_Text;

    @FXML
    private TextField CEA_email;

    @FXML
    private ImageView CEA_eye;

    @FXML
    void ChangeEmailFun() throws SQLException {
        new setPasswordVisible().textSetter(CEA_Password, CEA_Password_Text);
        String pass = CEA_Password.getText();
        String email = CEA_email.getText();

        if (check_UserPassword(pass)) {
            if(!Validation.isValidEmail(email)){
                new MessageBox("Invalid Email");
                return;
            }
            Connection con = DriverManager.getConnection(_url, _user, _password);
            String query2 = "SELECT COUNT(Email) FROM employee WHERE Email = ?";
            PreparedStatement statement1 = con.prepareStatement(query2);
            statement1.setString(1, email);
            ResultSet resultSet = statement1.executeQuery();
            resultSet.next();
            int x = resultSet.getInt(1);
            resultSet.close();
            statement1.close();
            con.close();
            if (x <= 0) //if x = 1 email is already used if x = 0 email is not used yet.
            {
                Connection conn = DriverManager.getConnection(_url, _user, _password);
                String query = "update employee set Email = ? where HostelerId = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, ID);
                int y = statement.executeUpdate();
                statement.close();
                conn.close();
                if (y > 0) {
                    new MessageBox("Your Email is change successfully.");
                    CEA_email.clear();
                    CEA_Password.clear();
                    CEA_Password_Text.clear();
                }
            }
        } else {
            new MessageBox("Password is not match.");
        }
    }

    @FXML
    void CEA_eye_fun() {
        new setPasswordVisible(CEA_Password, CEA_Password_Text, CEA_eye);
    }

    //------------------- Change Email -----------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------------------- Add Room -----------------------------

    boolean rID, rOCC;

    @FXML
    private TextField AR_Occupancy;
    @FXML
    private void AR_Occupancy_Valid() {  // validation for occupancy
        String text = AR_Occupancy.getText();
        rOCC = validation.isValidStringDigit(text);
    }
    @FXML
    private TextField AR_RoomID;
    @FXML
    void AR_RoomID_Valid() { // assign in onKeyRelease action
        String text = AR_RoomID.getText();
        rID = validation.isValidStringDigit(text);
    }

    @FXML
    private Label AR_label;


    String R;

    int occ;

    boolean FirstStep() throws SQLException {
        R = AR_RoomID.getText();
        occ = Integer.parseInt(AR_Occupancy.getText());
        if (R.isEmpty() || AR_Occupancy.getText().isEmpty() || Integer.parseInt(AR_Occupancy.getText()) == 0) {
            new MessageBox("You must fill all boxes.");
            return false;
        }
        if (rOCC && rID) {
            Connection con = DriverManager.getConnection(_url, _user, _password);
            String q1 = "select count(*) from room where RoomID = ?";
            PreparedStatement stm = con.prepareStatement(q1);
            stm.setString(1, ("R" + R));
            ResultSet r = stm.executeQuery();
            r.next();
            int roomID_Check = r.getInt(1);
            r.close();
            stm.close();
            con.close();
            if (roomID_Check == 0) { // RoomID is not assign yet.
                return true;
            } else {
                AR_label.setText("RoomId already exist.");
            }
        }
        return false;
    }

    @FXML
    void AddRoomFun() {
        try {
            if (FirstStep()) {
                Connection cc = DriverManager.getConnection(_url, _user, _password);
                String q = "insert into room values (?,?,?)";
                PreparedStatement s = cc.prepareStatement(q);
                s.setString(1, ("R" + R));
                s.setInt(2, occ);
                s.setInt(3, occ);
                int xx = s.executeUpdate();

                if (xx > 0) { // successful
                    AR_RoomID.clear();
                    AR_Occupancy.clear();
                    AR_label.setText("");
                    new MessageBox("Add Room", "Room or Rooms are added successfully in the system.");
                }
                s.close();
                cc.close();
            } else {
                new MessageBox("Invalid enter");
            }
        } catch (SQLException ex) {
            if (Integer.parseInt(AR_RoomID.getText()) > 99999) {
                new MessageBox("RoomID must be in 5 digit.");
            } else {
                new MessageBox("Error");
            }
        }
    }
    //------------------- Add Room -----------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------------------- Delete Room -----------------------------
    @FXML
    private TextField DR_RoomId;

    @FXML
    private Label DR_label_stdno;

    @FXML
    private Label DR_label_stdno1;

    @FXML
    private Label DR_label_found;

    boolean checkDeletable1,checkDeletable2;


    @FXML
    private void DR_RoomID_check_deletable() throws SQLException{
        checkDeletable1 = true;
        checkDeletable2 = true;
        String roomID = ("R"+DR_RoomId.getText());
        Connection con = DriverManager.getConnection(_url, _user, _password);
        String query = "select count(RoomID) from room where RoomID = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1,roomID);
        ResultSet R = statement.executeQuery();
        R.next();
        int yy = R.getInt(1);
        if (yy == 1){ // check RoomID exist or not.
            DR_label_found.setText("Found");
        }else if(yy == 0){
            DR_label_found.setText("Not Found");
            checkDeletable1 = false;
        }
        String query2 = "select count(StudentID) from student_room where RoomID = ?";
        PreparedStatement statement2 = con.prepareStatement(query2);
        statement2.setString(1, roomID);
        ResultSet resultSet = statement2.executeQuery();
        resultSet.next();
        int zz = resultSet.getInt(1);

        if (zz != 0){ // Students are assigned on this room
             checkDeletable2 = false;

        }
        DR_label_stdno.setText(Integer.toString(zz)); // Print no of RD_students.
        if (zz == 1 || zz == 0){
            DR_label_stdno1.setText("Student");
        }else if(zz > 1){
            DR_label_stdno1.setText("Students");
        }
        R.close();
        statement.close();
        resultSet.close();
        statement2.close();
        con.close();
    }

    @FXML
    void DeleteRoom_Fun() throws SQLException {
        if (checkDeletable1) {
            if (checkDeletable2) {
                Connection con = DriverManager.getConnection(_url, _user, _password);
                String query = "delete from room where RoomID = ? limit 1";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1, ("R" + DR_RoomId.getText()));
                int rowAffected = statement.executeUpdate();
                statement.close();
                con.close();
                if (rowAffected > 0) {
                    DR_RoomId.clear();
                    DR_label_found.setText("");
                    new MessageBox("Room is deleted Successfully");
                }
            } else {
                new MessageBox("Please transfer RD_students from this room\nthen delete it. For transformation Deallocate\n room then assign student to another room.");
            }
        }
    }
    //------------------- Delete Room -----------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //------------------- Update Occupancy -----------------------------
    boolean pp = true; // for the room id exist or not.
    @FXML
    private TextField UO_Occ;

    @FXML
    private TextField UO_RoomID;

    @FXML
    private Label UP_label;

    int oo;

        @FXML
        void UO_Occ_setter() throws SQLException { // called in set on key released
            Connection con = DriverManager.getConnection(_url, _user, _password);
            String query1 = "select count(RoomID) from room where RoomID = ?";
            PreparedStatement statement1 = con.prepareStatement(query1);
            statement1.setString(1, ("R" + UO_RoomID.getText()));
            ResultSet result = statement1.executeQuery();
            result.next();
            int nn = result.getInt(1);
            result.close();
            statement1.close();
            con.close();
            if (nn == 0) {
                pp = false;
                UP_label.setText("Not Found.");
                UO_Occ.clear();

            } else {
            pp = true;
            UP_label.setText("Found.");
            Connection c = DriverManager.getConnection(_url, _user, _password);
            String query = "select Occupancy from room where RoomID = ?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1, ("R" + UO_RoomID.getText()));
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            oo = resultSet.getInt(1);
            UO_Occ.setText(Integer.toString(oo));
            resultSet.close();
            statement.close();
            c.close();
        }
    }

    @FXML
    void UpdateOccupancyFun() throws SQLException{
        oo = Integer.parseInt(UO_Occ.getText());
        if(new Permission().permission()) {
            if (pp) {
                Connection c = DriverManager.getConnection(_url, _user, _password);
                String query = "UPDATE room SET Occupancy = ? WHERE RoomID = ?";
                PreparedStatement statement = c.prepareStatement(query);

                statement.setInt(1, oo);
                statement.setString(2, ("R"+UO_RoomID.getText()));
                int yyy = statement.executeUpdate();
                statement.close();
                c.close();

                System.out.println(yyy);
                if (yyy > 0) {
                    new MessageBox("Update occupancy is successful.");
                    UO_Occ.clear();
                    UO_RoomID.clear();
                    UP_label.setText("");
                }
            }
        }else {
            new MessageBox("You do not access to update occupancy.");
        }
    }
    //------------------- Update Occupancy -----------------------------
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}