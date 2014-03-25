package com.dimasdanz.keamananpintu.logmodel;

public class LogModel {
	public String name = null;
    public String time = null;

    public LogModel( String name, String time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
	    return name;
    }

    public String getTime() {
	    return time;
    }
}
