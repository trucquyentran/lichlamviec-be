package com.example.qllichlamviec.modal.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private String access_token;
    private String refresh_token;
    private NguoiDungDangNhapDTO nguoiDung;
}
