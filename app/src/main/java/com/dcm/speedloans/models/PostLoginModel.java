package com.dcm.speedloans.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class PostLoginModel {

    @SerializedName("id")
    private String id;

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("phonenumber")
    private String phonenumber;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("username")
    private String username;

    @SerializedName("secret_pin")
    @Nullable
    private String secret_pin;

    @SerializedName("last_login_datetime")
    private String last_login_datetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Nullable
    public String getSecret_pin() {
        return secret_pin;
    }

    public void setSecret_pin(@Nullable String secret_pin) {
        this.secret_pin = secret_pin;
    }

    public String getLast_login_datetime() {
        return last_login_datetime;
    }

    public void setLast_login_datetime(String last_login_datetime) {
        this.last_login_datetime = last_login_datetime;
    }

    public PostLoginModel(String id, String firstname, String lastname, String phonenumber,
                          String email, String password, String created_at, String username,
                          @Nullable String secret_pin, String last_login_datetime) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phonenumber = phonenumber;
        this.email = email;
        this.password = password;
        this.created_at = created_at;
        this.username = username;
        this.secret_pin = secret_pin;
        this.last_login_datetime = last_login_datetime;
    }
}
