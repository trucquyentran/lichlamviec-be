package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.modal.dto.ThongKeNhanVienDvDTO;
import com.example.qllichlamviec.service.ThongKeService;
import com.example.qllichlamviec.util.pojo.FormatTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ql-lich/thongke")

public class ThongKeController {
    @Autowired
    private ThongKeService thongKeService;
    @GetMapping
    public Object soLuongLichTrongNgay(@RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start, @RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end){


        return thongKeService.soLuongLich(start,end);
    }
    @GetMapping("/countByDonVi")
    public List<ThongKeNhanVienDvDTO> countByDonVi() {
        return thongKeService.countByDonVi();
    }
    @GetMapping("/slnguoidung")
    public long countNguoiDung(){
        return thongKeService.countUser();
    }

    @GetMapping("/sllich")
    public long countLich(){
        return thongKeService.countEvent();
    }

    @GetMapping("/count-event-of-user")
    public ResponseEntity<Object> countEventOfUser(){
        try {
            return ResponseEntity.ok(thongKeService.countLichByUser());
        }
        catch (Exception e){
            return  new ResponseEntity<Object>("lỗi"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}