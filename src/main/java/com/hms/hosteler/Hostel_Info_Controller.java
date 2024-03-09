package com.hms.hosteler;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Hostel_Info_Controller {

    @FXML
    private AnchorPane anchorHostelInfo;

    @FXML
    private TextArea Address;

    @FXML
    private TextField Contact_1;

    @FXML
    private TextField Contact_2;

    @FXML
    private TextField Email;

    @FXML
    private TextField Hostel_Name;

    @FXML
    private TextField WebSite;

    String HostelName, HostelContact1, HostelContact2, HostelEmail, HostelWebsite, HostelAddress;
    private static final String HOSTEL_FILE = new save().getHostelerInfoPath();// Your properties file name
    private Properties properties = new Properties();

    @FXML
    void initialize() {
        get_data();
    }

    @FXML
    void save_onAction() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Save Hostel Information/Details?");
        confirmationAlert.setContentText("Are you sure you want to save the hostel details?");

        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeTwo = new ButtonType("No");
        confirmationAlert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        if (confirmationAlert.showAndWait().get() == buttonTypeOne) {
            // User clicked Yes, proceed with saving data
            System.out.println("Saving data...");
            saveData(); // Replace with your actual data saving logic
        } else {
            System.out.println("Data saving cancelled.");
        }
    }

    void saveData() {
        HostelName = Hostel_Name.getText();
        HostelContact1 = Contact_1.getText();
        HostelAddress = Address.getText();
        // Validate input fields
        if (HostelName.isEmpty() || HostelContact1.isEmpty() || HostelAddress.isEmpty()) {
            new MessageBox("Hostel name, contact<1> and Address is mandatory.");
            return;
        }
        HostelContact2 = (Contact_2.getText().isEmpty()) ? null :Contact_2.getText();
        HostelEmail = (Email.getText().isEmpty()) ? null : Email.getText();
        HostelWebsite = (WebSite.getText().isEmpty()) ? null: WebSite.getText();

        properties.setProperty("HostelName", HostelName);
        properties.setProperty("HostelContact1", HostelContact1);
        properties.setProperty("HostelContact2", HostelContact2);
        properties.setProperty("HostelEmail", HostelEmail);
        properties.setProperty("HostelWebsite", HostelWebsite);
        properties.setProperty("HostelAddress", HostelAddress);

        // Save data to the properties file
        try (FileOutputStream out = new FileOutputStream(HOSTEL_FILE)) {
            properties.store(out, "Hostel information"); // Add comment for clarity
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public void get_data() {
        // Load data from the properties file
        try (FileInputStream in = new FileInputStream(HOSTEL_FILE)) {
            properties.load(in);
        } catch (IOException e) {
//            new MessageBox("Hostel Information","Please set hostel name and other details which you still don not set in hosteler.");
            System.out.println(e.getMessage());
            return;
        }

        Hostel_Name.setText(properties.getProperty("HostelName"));
        Contact_1.setText(properties.getProperty("HostelContact1"));
        Contact_2.setText(properties.getProperty("HostelContact2"));
        Email.setText(properties.getProperty("HostelEmail"));
        WebSite.setText(properties.getProperty("HostelWebsite"));
        Address.setText(properties.getProperty("HostelAddress"));
    }

    @FXML
    void backTOHome() {
        new sceneSwitchController(anchorHostelInfo, "modules/Home/home.fxml");
    }
}


