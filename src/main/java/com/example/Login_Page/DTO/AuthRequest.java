package com.example.Login_Page.DTO;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}