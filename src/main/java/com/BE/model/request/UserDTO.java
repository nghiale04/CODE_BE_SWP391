package com.BE.model.request;

import lombok.Data;

@Data
public class UserDTO {
    private String fullName;
    private String userName;
    private String newPassword;
    private String confirmPassword;
    private String phoneNumber;
    private String address;
}
