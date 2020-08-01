package com.example.UserService.Model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    @NotBlank(message = "Mobile number is required!")
    private String mobile;

    @Column
    private Boolean is_kyc_done;

    public User(String email, String name, String mobile, Boolean is_kyc_done) {
        this.email = email;
        this.name = name;
        this.mobile = mobile;
        this.is_kyc_done = is_kyc_done;
    }

    public User(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getIs_kyc_done() {
        return is_kyc_done;
    }

    public void setIs_kyc_done(Boolean is_kyc_done) {
        this.is_kyc_done = is_kyc_done;
    }
}
