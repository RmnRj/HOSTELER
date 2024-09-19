package com.hms.hosteler;

public class save {
    private static String USER_ID;
    private static String HOSTELER_ID;

    private static String USER_POST;

    private static String temp = ".......[Your HostelerID]";

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        save.temp = temp;
    }

    private static boolean setup = false;

    public boolean isSetup() {
        return setup;
    }

    public void setSetup() {
        save.setup = true;
    }

    public void makeSetup_Disable(){
        save.setup = false;
    }

    public String getUserPost() {
        return USER_POST;
    }

    public void setUserPost(String userPost) {
        USER_POST = userPost;
//        System.out.println(userPost);
    }

    String hostelerInfoPath = "HOSTELER/src/main/java/com/hms/hosteler/hostel_info.properties";

    String BillNo = "HOSTELER/src/main/java/com/hms/hosteler/Login_billNo.properties";

    public String getLoginBillNo() {
        return BillNo;
    }

    private final String dbms_url = "jdbc:mysql://**your_dbms_url";

    private final String dbms_user = "_user";
    private final String dbms_password = "_password";

    public String getDbms_url() {
        return dbms_url;
    }

    public String getHostelerInfoPath() {
        return hostelerInfoPath;
    }

    public String getDbms_user() {
        return dbms_user;
    }

    public String getDbms_password() {
        return dbms_password;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getHOSTELER_ID() {
        return HOSTELER_ID;
    }

    public void setHOSTELER_ID(String HOSTELER_ID) {
        this.HOSTELER_ID = HOSTELER_ID;
    }


}




