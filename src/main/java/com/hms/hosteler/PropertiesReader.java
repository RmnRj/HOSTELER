package com.hms.hosteler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    private static final String PROPERTIES_FILE = new save().getHostelerInfoPath();

    private static final String PROPERTIES_FILE_LOGIN_Bill = new save().getLoginBillNo();

    public static String getHostelName() {
        try {
            return getProperty1("HostelName");
        }catch (IOException e){
            System.out.println("HNAME " + e.getMessage());
        }
        return "Hostel Name";
    }

    public static String getHostelContact1()  {
        try {
        return getProperty1("HostelContact1");
        }catch (IOException e){
            System.out.println("HContact1 " + e.getMessage());
        }
        return "+97798xxxxxxxx";
    }

    public static String getHostelContact2()  {
        try{
        return getProperty1("HostelContact2");
        }catch (IOException e){
            System.out.println("HContact2 " + e.getMessage());
        }
        return "+97798xxxxxxxx";
    }

    public static String getHostelEmail() {
        try {
            return getProperty1("HostelEmail");
        } catch (IOException e) {
            System.out.println("HostelEmail " + e.getMessage());
        }
        return "xxxxxxxxxx@example.com";
    }

    public static String getHostelWeb() {
        try {
            return getProperty1("HostelWebsite");
        } catch (IOException e) {
            System.out.println("HostelWebsite " + e.getMessage());
        }
        return "https://www.xxxxxxxxxx.com";
    }

    public static String getHostelAdd() {
        try {
            return getProperty1("HostelAddress");
        } catch (IOException e) {
            System.out.println("HostelAdd " + e.getMessage());
        }
        return "Nepal";
    }

//    public static void setHostelAdd(String website) {
//        try {
//            setProperty1("HostelAddress", website);
//        } catch (IOException e) {
//            System.out.println("HostelAdd " + e.getMessage());
//        }
//    }

    public static String getUserExist() {
        try {
            return getProperty2("UserExist");
        } catch (IOException e) {
            System.out.println("User " + e.getMessage());
        }
        return "false";
    }

    public static void setUserExist(String s) {
        try {
            setProperty2("UserExist", s);
        } catch (IOException e) {
            System.out.println("User " + e.getMessage());
        }
    }

    public static String getBillNo() {
        try {
            return getProperty2("BillNo");
        } catch (IOException e) {
            System.out.println("User " + e.getMessage());
        }
        return "1000";
    }

    public static void setBillNo(String s) {
        try {
            setProperty2("BillNo", s);
        } catch (IOException e) {
            System.out.println("User " + e.getMessage());
        }
    }


    private static String getProperty1(String key) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(input);
            return properties.getProperty(key);
        }
    }

    private static String getProperty2(String key) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE_LOGIN_Bill)) {
            properties.load(input);
            return properties.getProperty(key);
        }
    }

    private static void setProperty2(String key, String value) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE_LOGIN_Bill)) {
            properties.load(input);
        }
        try (FileOutputStream output = new FileOutputStream(PROPERTIES_FILE_LOGIN_Bill)) {
            properties.setProperty(key, value);
            properties.store(output, null);
        }
    }
}