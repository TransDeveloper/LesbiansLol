package com.finnacorp.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private String application;
    public Logger(String application){
        this.application = application;
    }
    public void Log(String message){
        System.out.println(String.format(
            "%s [%s] %s",
            getLocalTime(), application, message
        ));
    }

    public static String getLocalTime(){
        Date d = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }
}
