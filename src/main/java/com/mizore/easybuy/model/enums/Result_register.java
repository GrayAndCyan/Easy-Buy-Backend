package com.mizore.easybuy.model.enums;

public class Result_register {
    public int status;
    public String description;

    public Result_register(){
    }

    public Result_register( int status, String description){
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
