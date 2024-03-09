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

public class RoomDetails {
    // Database connection parameters
    String _url = new save().getDbms_url();
    String _user = new save().getDbms_user();
    String _password = new save().getDbms_password();

    // FXML elements
    @FXML
    private Label RD_NoOfStudent, RD_label, RD_roomID;

    @FXML
    private TableView<RD_students> RD_Table;

    @FXML
    private TextField RD_search;

    @FXML
    private TableColumn<RD_students, String> RD_studentID;

    @FXML
    private TableColumn<RD_students, String> RD_students;

    // ObservableList for storing room details
    private final ObservableList<RD_students> Room_Details = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        // Initialize table columns
        RD_studentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        RD_students.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Initialize table with empty ObservableList
        RD_Table.setItems(Room_Details);
    }

    // Method to handle search functionality
    @FXML
    void searchFun() {
        String room = "R" + RD_search.getText();
        RD_roomID.setText(room);

        // Clear previous data
        Room_Details.clear();

        try {
            Connection con = DriverManager.getConnection(_url, _user, _password);

            // Query to check students assigned to the room
            String qry = "select Occupancy, Availability from room where RoomID = ?";
            PreparedStatement statement = con.prepareStatement(qry);
            statement.setString(1, room);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int o = resultSet.getInt(1);
            int a = resultSet.getInt(2);
            resultSet.close();
            statement.close();

            if (a == 0) {
                RD_NoOfStudent.setText("00");
                RD_label.setText("This room is empty.");
            } else {
                RD_NoOfStudent.setText(Integer.toString(o - a));

                // Query to retrieve student details
                String qry2 = "select StudentID from student_room where RoomID = ?";
                PreparedStatement statement1 = con.prepareStatement(qry2);
                statement1.setString(1, room);
                ResultSet resultSet1 = statement1.executeQuery();
                if(resultSet1.next()) {
                    do {
                        String id = resultSet1.getString(1);

                        // Query to get student name
                        String qry3 = "select FName, LName from student where StudentID = ?";
                        PreparedStatement statement2 = con.prepareStatement(qry3);
                        statement2.setString(1, id);
                        ResultSet resultSet2 = statement2.executeQuery();
                        resultSet2.next();
                        String n = resultSet2.getString(1) + " " + resultSet2.getString(2);
                        resultSet2.close();
                        statement2.close();
                        System.out.println(id + " " + n);
                        // Add student details to ObservableList
                        RD_students r = new RD_students(id, n);
                        Room_Details.add(r);
                    } while (resultSet1.next());
                }
                resultSet1.close();
                statement1.close();
            }
            con.close();
            RD_label.setText("");
            RD_Table.setItems(Room_Details);
        } catch (SQLException e) {
            System.out.println("Connection unsuccessful " + e.getMessage());
            RD_label.setText("Room is not found.");
            RD_roomID.setText("");
            RD_NoOfStudent.setText("0");
        }
    }

    // Inner class representing student details
    public static class RD_students {
        String studentID;
        String name;

        public String getStudentID() {
            return studentID;
        }

        public String getName() {
            return name;
        }

        public RD_students(String studentID, String name) {
            this.studentID = studentID;
            this.name = name;
        }
    }

}
