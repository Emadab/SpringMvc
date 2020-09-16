package com.example.demo.Model.ResponseModel;

import java.util.Date;

public class ResponseModel {
    private int status;

    private String message;

    private Date timestamp = new Date();

    private Object result;

    public ResponseModel() {
    }

    public ResponseModel(int status, String message, Object result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public ResponseModel setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseModel setMessage(String message) {
        this.message = message;
        return this;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public ResponseModel setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public ResponseModel setResult(Object result) {
        this.result = result;
        return this;
    }
}
