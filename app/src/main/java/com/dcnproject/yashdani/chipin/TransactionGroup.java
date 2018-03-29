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

    public String getPayee() {
        return payee;
    }

    public void setPayee(String name) {
        this.payee = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String year) {
        this.amount = year;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String genre) {
        this.desc = genre;
    }
}