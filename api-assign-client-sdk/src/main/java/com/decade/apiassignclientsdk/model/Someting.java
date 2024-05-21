package com.decade.apiassignclientsdk.model;

import lombok.Data;

@Data
public class Someting {
    private String des;
    private String myname;

    public Someting(String des, String myname) {
        this.des = des;
        this.myname = myname;
    }
}
