package com.dcnproject.yashdani.chipin;

/**
 * Created by Yash Dani on 30-03-2018.
 */

public class TransactionProfileCards {
    private String profilePayee, profileAmount, profileDesc;

    public TransactionProfileCards() {
    }

    public TransactionProfileCards(String profilePayee, String profileDesc, String profileAmount) {
        this.profilePayee = profilePayee;
        this.profileDesc = profileDesc;
        this.profileAmount = profileAmount;
    }

    public String getPayee() {
        return profilePayee;
    }

    public void setPayee(String name) {
        this.profilePayee = name;
    }

    public String getAmount() {
        return profileAmount;
    }

    public void setAmount(String year) {
        this.profileAmount = year;
    }

    public String getDesc() {
        return profileDesc;
    }

    public void setDesc(String genre) {
        this.profileDesc = genre;
    }
}