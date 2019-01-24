package io.github.utshaw.myhealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("status")
    private String status;
    @SerializedName("token")
    private String token;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public String toString() {
        return "Login{" +
                "status='" + status + '\'' +
                ", token='" + token + '\'' +
                '}';
    }


}