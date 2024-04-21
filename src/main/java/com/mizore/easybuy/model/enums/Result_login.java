package com.mizore.easybuy.model.enums;

public class Result_login {
    public int status;
    public long id;
    public int role;
    public String description;
    public String token;

    public Result_login(){
    }

    public Result_login(int status, long id, int role, String description ){
        this.status=status;
        this.id = id;
        this.role = role;
        this.description = description; }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
