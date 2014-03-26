package com.dimasdanz.keamananpintu.usermodel;

public class UserModel {
	private String user_name = null;
    private String user_id = null;

    public UserModel(String userid, String username) {
    	this.user_id = userid;
        this.user_name = username;
    }
    
    public String getUserID() {
	    return user_id;
    }

    public String getName() {
	    return user_name;
    }
}
