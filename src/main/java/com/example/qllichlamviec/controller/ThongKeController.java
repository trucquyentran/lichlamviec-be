package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.modal.dto.ThongKeNhanVienDvDTO;
import com.example.qllichlamviec.service.ThongKeService;
import com.example.qllichlamviec.util.pojo.FormatTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//    @GetMapping("/count-emp-have-event-today")
//    public ResponseEntity<Object> countEmpHaveEventToday() {
//        try {
//            return ResponseEntity.ok(thongKeService.countUserHaveEventToday());
//
//        } catch (Exception e) {
//            return new ResponseEntity<>("Lỗi khi sửa lịch làm việc: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
<<<<<<< HEAD

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
            return  new ResponseEntity<Object>("lỗi"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
=======
>>>>>>> 77c99bc9e3f8b6b8b88211c25b2d5817393905fc
}