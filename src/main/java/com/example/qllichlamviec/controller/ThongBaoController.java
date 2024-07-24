package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.service.LichLamViecService;
import com.example.qllichlamviec.service.TaiKhoanService;
import com.example.qllichlamviec.service.ThongBaoService;
import com.example.qllichlamviec.util.TaiKhoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/ql-lich/thong-bao")
public class ThongBaoController {
    @Autowired
    private ThongBaoService thongBaoService;
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private LichLamViecService lichLamViecService;

    @GetMapping("list-thong-bao")
    public ResponseEntity<Object> thongBaoLich(HttpServletRequest httpServletRequest){
        try {
            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);
            return ResponseEntity.ok(thongBaoService.getThongBaoByTaiKhoan(taiKhoan));
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
