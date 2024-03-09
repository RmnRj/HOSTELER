package com.hms.hosteler;
//
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Fun {
    int year, month, day, adYear, adMonth, adDay;

    static boolean x = false;

    public int get_Year() {
        return year;
    }

    public int get_Month() {
        return month;
    }

    public int get_Day() {
        return day;
    }

    /*
    *
    * Converts AD to BS
    * This function converts date from AD to BS. Sometimes, it converts incorrect day differences of 1 2 days.
    * @takes three arguments year, month, day of AD
     */
    public void convertADtoBS(int adYear, int adMonth, int adDay) {
        int[] a = {30, 32, 31, 32, 31, 30, 30, 30, 30, 29, 30, 30};
        int bsYear, bsMonth, bsDay;

        bsDay = adDay + 17;
        bsMonth = adMonth + 8;
        bsYear = adYear + 56;
        if (bsDay > a[bsMonth]) {
//            if (bsMonth == 10 && isLeapYear(adYear)) {
//                bsDay = bsDay - 30;
//            } else {
                bsDay = bsDay - a[bsMonth];
//            }
            bsMonth++;
        }

        if (bsMonth > 12) {
            bsMonth = bsMonth - 12;
            bsYear++;
        }
        bsDay++;
        year = bsYear;
        month = bsMonth;
        day = bsDay;
    }

    /*
    *
    * Get System Date (AD)
    * This function get system date in year, month and day
    * @takes zero argument
    */
    public void setDate() {
        Date date = new Date(); // Get current date and time
        // Extract specific date components:
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        adYear = calendar.get(Calendar.YEAR);
        adMonth = calendar.get(Calendar.MONTH) + 1; // Java month is 0-based, adjust to 1-based
        adDay = calendar.get(Calendar.DAY_OF_MONTH);
        convertADtoBS(adYear, adMonth, adDay);
    }

    /*
    *
    * isLeapYEar
    * This find the year is leap year or not. Leap years are normally divisible by 4
    * and they are mostly centuries. This returns true if the year is leap year.
    * @takes argument year
     */
    public boolean isLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    /*
    *
    * Calculate Age
    * This is method to calculate age for DOB and return age () integer value.
    * @takes one argument DOB(Date of Birth) in BS example "2059-11-16"
     */
    public int age(String DOB) {
        int age;
        if(DOB == null){
            age = 0;
        }
        else {
            int[] bs = dateSeperator(DOB);
            if (11 == bs[1] || 11 > bs[1]) {
                if (5 == bs[2] || 5 > bs[2]) {
                    age = 2080 - bs[0];
                } else {
                    age = 2080 - bs[0] - 1;
                }
            } else {
                age = 2080 - bs[0] - 1;
            }
        }
        return age;
    }

    public int[] dateSeperator(String DOB){
        int[] bs = new int[3];
        if(DOB!=null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = format.parse(DOB);
            } catch (ParseException e) {
                System.out.println(e.getMessage() + "\n\n");
            }

            // Extract year, month, and day from the Date object
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            bs[0] = calendar.get(Calendar.YEAR);
            bs[1] = calendar.get(Calendar.MONTH) + 1; // Java month is 0-based, adjust to 1-based
            bs[2] = calendar.get(Calendar.DAY_OF_MONTH);
        }else {
            bs[0] = 0;bs[1] = 0;
            bs[2] = 0;
        }
        return bs;
    }

    /*
    *
    * MAke first latter Capital
    * This make any string value's first latter upper case. It is used in Names, Address, etc.
    * Example:- Take "samir" and return "Samir"
    * @Takes a String and return a String
     */
    public String upp(String s){ //change "samir" to "Samir"
        if(s == null || s.isEmpty()){
            return null;
        }
        if(!(new Validation().isValidStringDigit(s.substring(0,1)))){
        s = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        }
        return s;
    }
}
