package com.bashirli.notebook.model;

import java.io.Serializable;

public class DataModel implements Serializable {
    private String email;
    private String main;
    private String date;

    public DataModel(String email, String main, String date) {
        this.email = email;
        this.main = main;
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public String getMain() {
        return main;
    }

    public String getDate() {
        return date;
    }
}
