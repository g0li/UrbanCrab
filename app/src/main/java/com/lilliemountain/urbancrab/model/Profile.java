package com.lilliemountain.urbancrab.model;

import android.net.Uri;

public class Profile {
    String name,vmodel,noplate,email;

    public Profile() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVmodel() {
        return vmodel;
    }

    public void setVmodel(String vmodel) {
        this.vmodel = vmodel;
    }

    public String getNoplate() {
        return noplate;
    }

    public void setNoplate(String noplate) {
        this.noplate = noplate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public Profile(String name, String vmodel, String noplate, String email) {
        this.name = name;
        this.vmodel = vmodel;
        this.noplate = noplate;
        this.email = email;
    }

}
