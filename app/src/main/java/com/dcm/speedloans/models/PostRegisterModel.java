package com.dcm.speedloans.models;

import com.google.gson.annotations.SerializedName;

public class PostRegisterModel {
    @SerializedName("success")
    private String success;

    public PostRegisterModel(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
