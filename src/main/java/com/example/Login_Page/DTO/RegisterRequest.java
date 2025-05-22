package com.example.Login_Page.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    private String fullName;
    private String email;
    private int age;
    private String password;
    private String confirmPassword;
}