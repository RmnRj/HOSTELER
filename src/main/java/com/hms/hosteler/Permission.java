package com.hms.hosteler;


public class Permission {
    public boolean permission(){
        String accessor = new save().getUserPost().toLowerCase();
//        System.out.println(accessor);
        return accessor.equals("warden") || accessor.equals("receptionist") || accessor.equals("admin");
    }

}
