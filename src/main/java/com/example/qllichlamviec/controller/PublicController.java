package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.modal.system.Error;
import com.example.qllichlamviec.service.*;
import com.example.qllichlamviec.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/public")
@Slf4j
public class PublicController {
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private DonViService donViService;
    @Autowired
    private QuyenService quyenService;
    @Autowired
    private QuyenTaiKhoanService quyenTaiKhoanService;
//    @Autowired
//    private ModuleLayer moduleLayer;
//    @Autowired
//    private JwtService jwtService;

    @GetMapping("/khoitaouser")
    @Transactional
    public ResponseEntity<Object> dangNhap() {
        DonVi dv = donViService.save(new DonVi(null,"Trung tâm CNTT"));
        Quyen q = quyenService.save(new Quyen(null,"Admin"));
        NguoiDung ngd= new NguoiDung();
        ngd.setHoTen("Admin");
        ngd.setEmail("Admin@vnpt.vn");
        ngd.setSdt("0123456789");
        ngd.setGioiTinh(true);
        ngd.setNgaySinh( LocalDate.now());
        ngd.setDonVi(dv);
        TaiKhoan tk = new TaiKhoan();
        tk.setUsername("admin");
        tk.setPassword("Admin@123");
        tk.setNguoiDung(ngd);
        List<QuyenTaiKhoan> quyenTaiKhoanList = new ArrayList<>();
        quyenTaiKhoanList.add(new QuyenTaiKhoan(null,tk,q));
        tk.setQuyenTaiKhoanList(quyenTaiKhoanList);
        taiKhoanService.khoiTaoNguoiDungKemTaiKhoan(tk);
        return new ResponseEntity<>(new Error("201","OK"), HttpStatus.OK);
    }


}
