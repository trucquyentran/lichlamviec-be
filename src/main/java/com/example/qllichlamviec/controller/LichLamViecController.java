package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.service.JwtService;
import com.example.qllichlamviec.service.LichLamViecService;
import com.example.qllichlamviec.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/lich-lam-viec")
@PreAuthorize("role('ROLE_ADMIN')")
public class LichLamViecController {
    @Autowired
    private LichLamViecService lichLamViecService;
//    @Autowired
    private ModuleLayer moduleLayer;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TaiKhoanService taiKhoanService;

}
