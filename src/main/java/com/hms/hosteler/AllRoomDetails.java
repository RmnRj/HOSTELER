package com.hms.hosteler;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AllRoomDetails {


    @FXML
    private Label ARD_label;

    @FXML
    private TableView<RoomData> ARD_table;

    @FXML
    private TableColumn<RoomData, String> ARD_roomID;

    @FXML
    private TableColumn<RoomData, Integer> ARD_total;

    @FXML
    private TableColumn<RoomData, Integer> ARD_vacant;

    private final ObservableList<RoomData> allRoomData = FXCollections.observableArrayList();
    private final ObservableList<RoomData> vacantRoomData = FXCollections.observableArrayList();

    public void initialize() {
        ARD_roomID.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        ARD_vacant.setCellValueFactory(new PropertyValueFactory<>("availability"));
        ARD_total.setCellValueFactory(new PropertyValueFactory<>("occupancy"));

        loadRoomData();

       handleBTNAll();
    }

    @FXML
    private void handleBTNAll() {
        ARD_label.setText("All Rooms");
        ARD_table.setItems(allRoomData);
    }

    @FXML
    private void handleBTNVacant() {
        ARD_label.setText("Vacant Rooms");
        ARD_table.setItems(vacantRoomData);
    }

    private void loadRoomData() {
        String url = new save().getDbms_url();
        String user = new save().getDbms_user();
        String password = new save().getDbms_password();

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT RoomID, Availability, Occupancy FROM room")) {

            while (rs.next()) {
                RoomData roomData = new RoomData(
                        rs.getString("RoomID"),
                        rs.getInt("Availability"),
                        rs.getInt("Occupancy")
                );
                allRoomData.add(roomData);
                if (roomData.getAvailability() > 0) {
                    vacantRoomData.add(roomData);
                }
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            new MessageBox("Error" + ex.getMessage());
        }
    }

    public static class RoomData {
        private String roomID;
        private int availability;
        private int occupancy;

        public RoomData(String roomID, int availability, int occupancy) {
            this.roomID = roomID;
            this.availability = availability;
            this.occupancy = occupancy;
        }

        public String getRoomID() {
            return roomID;
        }

        public int getAvailability() {
            return availability;
        }

        public int getOccupancy() {
            return occupancy;
        }
    }
}
