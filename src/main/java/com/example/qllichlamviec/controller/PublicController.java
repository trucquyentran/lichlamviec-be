package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.modal.dto.QuyenTaiKhoanDTO;
import com.example.qllichlamviec.modal.dto.TaiKhoanDTO;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ql-lich/public")
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

    @GetMapping("/khoitaouser")
    @Transactional
    public ResponseEntity<Object> khoiTaoAdmin() {

        DonVi dvGoc = donViService.save(new DonVi(null,"VNPT Long An",null));
        DonVi dv = donViService.save(new DonVi(null,"Trung tâm CNTT",dvGoc));
        Quyen q = quyenService.save(new Quyen(null,"Admin"));
        TaiKhoan tk = new TaiKhoan();
        tk.setUsername("admin");
        tk.setPassword("Admin@123");
        tk.setHoTen("Admin");
        tk.setEmail("Admin@vnpt.vn");
        tk.setSdt("0123456789");
        tk.setGioiTinh(true);
        tk.setNgaySinh(null);
        tk.setNgayTao(LocalDateTime.now());
        tk.setDonVi(dv);
        List<QuyenTaiKhoan> quyenTaiKhoanList = new ArrayList<>();
        quyenTaiKhoanList.add(new QuyenTaiKhoan(null,tk,q));
        tk.setQuyenTaiKhoanList(quyenTaiKhoanList);
        taiKhoanService.khoiTaoTaiKhoan(tk);
        return new ResponseEntity<>(new Error("201","OK"), HttpStatus.OK);
    }


}
