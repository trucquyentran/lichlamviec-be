package com.example.qllichlamviec.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanDangNhapDTO {
    private Long id;
    private String username;
    private String password;
    private String oldPassdword;
    private String newPassword;
    private String newPassword2;
    private String avatar;

}
