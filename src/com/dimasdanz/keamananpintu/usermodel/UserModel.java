package com.dimasdanz.keamananpintu.usermodel;

public class UserModel {
	private String user_name = null;
    private String user_id = null;
    private String user_password = null;

    public UserModel(String userid, String username, String password) {
    	this.user_id = userid;
        this.user_name = username;
        this.user_password = password;
    }
    
    public String getUserID() {
	    return user_id;
    }

    public String getName() {
	    return user_name;
    }
    
    public String getPassword() {
    	return user_password;
    }
}
