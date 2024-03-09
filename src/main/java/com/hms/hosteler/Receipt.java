package com.hms.hosteler;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Optional;

public class Receipt {

    @FXML
    private TableColumn<Billing.Bills, Integer> Amount;

    @FXML
    private Label BAmtWord;

    @FXML
    private Label BBillno;

    @FXML
    private Label BDate;

    @FXML
    private Label BMFrom;

    @FXML
    private Label BMTo;

    @FXML
    private Label BTotalamt;

    @FXML
    private Label BUser;

    @FXML
    private Label BstdID;

    @FXML
    private Label BstdName;

    @FXML
    private Label HAddress;

    @FXML
    private Label HContacts;

    @FXML
    private Label HName;

    @FXML
    private Button btn_Print;

    @FXML
    private TableColumn<Billing.Bills, String> Partic;

    @FXML
    private Pane PrintPane;

    @FXML
    private TableView<Billing.Bills> Table;

    @FXML
    private Label due_extra;

    @FXML
    private Label due_extra_Rs;

    @FXML
    private Label due_extra_amt;

    @FXML
    private Label paindamt;

    @FXML
    private Label website;

    @FXML
    void init(){
        HName.setText(PropertiesReader.getHostelName());
        HAddress.setText(PropertiesReader.getHostelAdd());
        HContacts.setText(PropertiesReader.getHostelContact1() + ", " + PropertiesReader.getHostelContact2());
        website.setText(PropertiesReader.getHostelWeb()); //Hostel information

        BBillno.setText(Billing.getBills());
        BDate.setText(Billing.get_Date());
        BstdName.setText(Billing.getStudentName());
        BstdID.setText(Billing.getStudentId());

        BMFrom.setText(Billing.getFMonth());
        BMTo.setText(Billing.getTMonth());
        int T = Billing.getT();
        BTotalamt.setText(Integer.toString(T));
        int P = Billing.getP();
        paindamt.setText(Integer.toString(P));
        DUE_EXTRA(T,P);

        BUser.setText(Billing.getReceiver());

        BAmtWord.setText(convertToWords(P)+ " only.");
    }

    void DUE_EXTRA(int Total, int Paid){
        if(Total == 0){
            return;
        }
        if(Paid == Total){
            due_extra.setText("");
            due_extra_Rs.setText("");
            due_extra_amt.setText("");
        }else if(Paid > Total){
            due_extra.setText("Extra: ");
            due_extra_Rs.setText("Rs");
            due_extra_amt.setText(Integer.toString(Paid - Total));
        }else if(Paid < Total){
            due_extra.setText("Due: ");
            due_extra_Rs.setText("Rs");
            due_extra_amt.setText(Integer.toString(Total - Paid));
        }
    }

    private ObservableList<Billing.Bills> b = FXCollections.observableArrayList();
    @FXML
    void initialize(){
        init();
        b = Billing.getBILL();
        Partic.setCellValueFactory(new PropertyValueFactory<>("particular"));
        Amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        Table.setItems(b);
    }

    @FXML
    private AnchorPane recAnchor;
    @FXML
    void GoBack() {
        recAnchor.getChildren().removeAll();
        Stage stage1 = (Stage) btn_Print.getScene().getWindow();
        stage1.close();
    }

    @FXML
    void PrintJob(){
        // Example: Print the content of the AnchorPane along with labels
        printContent(PrintPane);
        PropertiesReader.setBillNo(Integer.toString(Integer.parseInt(Billing.getBills())+1));
    }

    private void printContent(Pane content) {
        if (content == null) {
            System.out.println("Content is null");
            return;
        }

        PrinterJob printerJob = PrinterJob.createPrinterJob();

        if (printerJob != null) {
            // Print the content
            boolean success = printerJob.printPage(content);

            if (success) {
                printerJob.endJob();
            } else {
                System.out.println("Failed to print");
            }
        } else {
            System.out.println("Could not create a printer job.");
        }
    }

    private static final String[] ones = {
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"
    };

    private static final String[] teens = {
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
    };

    private static final String[] tens = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };

    private static final String[] thousands = {"", "Thousand", "Million", "Billion"};

    public static String convertToWords(int num) {
        if (num == 0) {
            return "Zero";
        }

        int i = 0;
        String words = "";

        while (num > 0) {
            if (num % 1000 != 0) {
                words = helper(num % 1000) + thousands[i] + " " + words;
            }
            num /= 1000;
            i++;
        }

        return words.trim();
    }

    private static String helper(int num) {
        if (num == 0) {
            return "";
        } else if (num < 10) {
            return ones[num] + " ";
        } else if (num < 20) {
            return teens[num - 10] + " ";
        } else if (num < 100) {
            return tens[num / 10] + " " + helper(num % 10);
        } else {
            return ones[num / 100] + " Hundred " + helper(num % 100);
        }
    }
}
