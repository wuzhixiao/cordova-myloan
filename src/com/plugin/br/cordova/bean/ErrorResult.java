package com.plugin.br.cordova.bean;

/**
 * Created by Administrator on 2018/9/14 0014.
 */

public class ErrorResult {
    private int errorCode;
    private String date;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ErrorResult{" +
                "errorCode=" + errorCode +
                ", date='" + date + '\'' +
                '}';
    }
}
