package com.example.qllichlamviec.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanDangNhapDTO {
    private Long id;
    @Size(max = 50, message = "Username không hợp lệ, không được vượt quá 50 ký tự")
    private String username;
    private String password;
    private String oldPassword;
    private String newPassword;
    private String newPassword2;
    private String avatar;

}
