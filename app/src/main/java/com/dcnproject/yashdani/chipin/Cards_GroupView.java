package com.dcnproject.yashdani.chipin;

/**
 * Created by aman on 25/3/18.
 */

public class Cards_GroupView {


    private String name, image;

    public Cards_GroupView() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Cards_GroupView(String name,String image){
        this.name=name;
        this.image = image;

    }
}
