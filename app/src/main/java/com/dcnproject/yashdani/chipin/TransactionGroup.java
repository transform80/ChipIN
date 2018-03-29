package com.dcnproject.yashdani.chipin;

/**
 * Created by Yash Dani on 29-03-2018.
 */

public class TransactionGroup {
    private String payee, amount, desc;

    public TransactionGroup() {
    }

    public TransactionGroup(String title, String genre, String year) {
        this.payee = title;
        this.desc = genre;
        this.amount = year;
    }

    public String getTitle() {
        return payee;
    }

    public void setTitle(String name) {
        this.payee = name;
    }

    public String getYear() {
        return amount;
    }

    public void setYear(String year) {
        this.amount = year;
    }

    public String getGenre() {
        return desc;
    }

    public void setGenre(String genre) {
        this.desc = genre;
    }
}