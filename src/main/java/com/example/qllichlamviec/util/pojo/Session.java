package com.example.qllichlamviec.util.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    private String accessToken;
    private String refreshToken;
}
