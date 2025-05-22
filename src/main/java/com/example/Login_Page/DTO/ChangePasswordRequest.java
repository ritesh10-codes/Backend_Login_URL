package com.example.Login_Page.DTO;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String email;
    private String newPassword;
    private String confirmPassword;
}
