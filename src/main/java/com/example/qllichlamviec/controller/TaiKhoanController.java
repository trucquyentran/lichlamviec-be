package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.service.JwtService;
import com.example.qllichlamviec.service.TaiKhoanService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class TaiKhoanController {
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
//    @Autowired
//    private JwtService jwtService;

}
