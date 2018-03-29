package com.dcnproject.yashdani.chipin;

import android.widget.ImageView;

/**
 * Created by Yash Dani on 29-03-2018.
 */
public class ContactCards {
    private String name,email;
    private int display;

    public ContactCards() {
    }

    public ContactCards(String name, String email, int display) {
        this.name = name;
        this.email = email;
        this.display = display;
        }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDisplay() {
        return display;
    }
    /*public ImageView getDisplay() {
        return display;
    }
    public void setDisplay(ImageView display){
        this.display = display;
    }*/

    public String getEmail() {
        return email;
    }

    public void setEmail(String genre) {
        this.email = genre;
    }
}

