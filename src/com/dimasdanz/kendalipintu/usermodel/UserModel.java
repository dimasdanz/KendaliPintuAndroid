/*******************************************************************************
 * Copyright (c) 2014 Dimas Rullyan Danu
 * 
 * Kendali Pintu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Kendali Pintu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Kendali Pintu. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.dimasdanz.kendalipintu.usermodel;

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
