package com.dcnproject.yashdani.chipin;

/**
 * Created by Yash Dani on 29-03-2018.
 */

public class Movie {
    private String name;
    public Movie() {}
    public Movie(String title) {
        this.name = title;
        }
    public String getTitle() {
        return name;
    }
    public void setTitle(String name) {
        this.name = name;
    }
}
