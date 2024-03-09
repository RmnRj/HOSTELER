package com.hms.hosteler;

//import javafx.fxml.FXML;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /*
    *
    * Validation for the digit only
    * It check string have any character or not IF found any character return false
    * IF not found return true. (This is mainly dates, where character are not allowed )
    * @Take String argument
     */
    public boolean isValidStringDigit(String text) {
        // Using Character.isDigit()
        for (char c : text.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }


    /*
    *
    * BS Date validation
    * Checking wrong condition and return false IF cannot find wrong
    * condition means date is correct and return true
    * @Take three integer arguments
    */
    public boolean isValidBSDate(int year, int month, int day) {
        Fun d1 = new Fun();
        d1.setDate();
        int y = d1.get_Year(); // current year BS
        int m = d1.get_Month(); // current month
        int d = d1.get_Day(); // current day

        //Checking wrong date conditions and return false
        if (year < 2000 || year > y || month < 1 || month > 12 || day < 1 || day > 32) {
            return false;
        }
        if (year == y && ((month == m && day > d) || (month > m))) {
            return false;
        }
//           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        return true;
    }


    public boolean isValidPost(String post){
         return (post.equalsIgnoreCase("warden") || post.equalsIgnoreCase("employee") || post.equalsIgnoreCase("cook") || post.equalsIgnoreCase("manager") || post.equalsIgnoreCase("receptionist") || post.equalsIgnoreCase("admin"));
    }

    /*
    *
    * Name Validation
    * This check name is valid or not
    * @Take capability to take full name at once like "Raman Raj"
     */
    public boolean isValidName(String name) {
        return name.matches("^[a-zA-Z\\s]+$");
    }

    /*
    *
    * Validation for email
    * check email is in pattern of email or not. Example: xxxxxxx@example.com
    * @take string argument
     */
    public static boolean isValidEmail(String email) {
        // Regular expression for email validation
        String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    /*
    *
    * Validation for Phone Number
    * It check (for nepal) phone number have +97798xx... or +97797xx... pattern or not
    * @take string argument
     */
    public boolean isValidPhone(String phone) {
//        System.out.println("Debug: Phone number - " + phone);
        if(phone.matches("^\\+9779[7-8]\\d{8}$")){
            System.out.println("Debug: Phone number - " + phone);
        }
        return phone.matches("^\\+9779[7-8]\\d{8}$");
    }

    /*
    *
    * Validation for Password
    * Initialize x = 0
    * condition 1: (Mandatory)password must have at least minimum 6 character (normally min char is 8)
    *              { x++ }     [For basic]
    * condition 2: it must have at least one LowerCase  { x++ }     [For weaker]
    * condition 3: it must have at least one UpperCase  { x++ }     [For weak]
    * condition 4: it must have at least one digit of any numbers { x++ }   [For medium]
    * condition 5: (Optional) it must have at least one special character (i.e. '@', '#',
    *              '$', '&', '!', etc.)  { x++ }    [For strong]
    * to pass password validation Password must score 3 or more than this function return true value
    * otherwise it returns false
    * @take String arguments example "Apple12"
     */
    boolean isPasswordValid(String password) {
        int numConditionsMet = 0;
        if (password.length() <= 5) {
            return false; // Fail if password is less than or equal to 5 characters
        }
        numConditionsMet++;
        // Check for lowercase
        if (password.matches("^[a-z]+$") || password.matches(".*[a-z].*")) {
            numConditionsMet++;
        }

        // Check for uppercase
        if (password.matches("^[A-Z]+$") || password.matches(".*[A-Z].*")) {
            numConditionsMet++;
        }

        // Check for digit
        if (password.matches(".*\\d.*")) {
            numConditionsMet++;
        }

        // Check for special character
        if (password.matches(".*[@#$&!].*")) {
            numConditionsMet++;
        }

        return numConditionsMet >= 3;
    }

    /*
    *
    * This function only check first 5 character
     */
    public  boolean isValidTime(String timeStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime.parse(timeStr, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}


