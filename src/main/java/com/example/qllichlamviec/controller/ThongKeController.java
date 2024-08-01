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
    @GetMapping("/slnhanviencuadonvi")
    public List<ThongKeNhanVienDvDTO> countByDonVi() {
        return thongKeService.countEmpByDonVi();
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


    @GetMapping("/slnguoidung")
    public long countNguoiDung(){
        return thongKeService.countUser();
    }

    @GetMapping("/sllich")
    public long countLich(){
        return thongKeService.countEvent();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @GetMapping("/count-event-of-user")
    public ResponseEntity<Object> countEventOfUser(){
        try {
            return ResponseEntity.ok(thongKeService.countLichByUser());
        }
        catch (Exception e){
            return  new ResponseEntity<Object>("lỗi"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count-event-of-user/{id}")
    public ResponseEntity<Object> countCateEventOfUser(@PathVariable ObjectId id){
        try {
            return ResponseEntity.ok(thongKeService.countCateEventOfUser(id));
        }
        catch (Exception e){
            return  new ResponseEntity<Object>("Lỗi" +e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/event-by-date/{id}")
    public ResponseEntity<Object> eventByDate(@PathVariable ObjectId id){
        try {
            return ResponseEntity.ok(thongKeService.lichByDate(id));
        }
        catch (Exception e){
            return  new ResponseEntity<Object>("Lỗi" +e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/dslichtheongay")
    public ResponseEntity<Object> listEventOfDate(@RequestParam Date start, Date end){
        try {
            return ResponseEntity.ok(thongKeService.listEventByDate(start, end));
        }
        catch (Exception e){
            return  new ResponseEntity<Object>("Lỗi"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/slnhanvientheoquyen")
    public ResponseEntity<Object> countEmpOfRole(){
        try {
            return ResponseEntity.ok(thongKeService.countEmpByRole());
        }
        catch (Exception e){
            return new ResponseEntity<Object>("lỗi" +e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}