package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.modal.system.Error;
import com.example.qllichlamviec.service.DonViService;
import com.example.qllichlamviec.service.JwtService;
import com.example.qllichlamviec.service.TaiKhoanService;
import com.example.qllichlamviec.util.DonVi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/ql-lich/don-vi")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class DonViController {
    @Autowired
    private DonViService donViService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping(value = "/getall")
    public List<DonVi> getAlls(@RequestParam int page, int size){
         return donViService.findAll(page,size);
    }

    @GetMapping
    public ResponseEntity<Object> chiTietDonVi(@RequestParam(required = false) String dv, HttpServletRequest httpRequest){
        try {
            if (Pattern.compile("^[0-9a-fA-F]{24}$").matcher(dv).matches()==true){

                return new ResponseEntity<>(donViService.getById(dv), HttpStatus.OK);
            }else {

                return new ResponseEntity<>(taiKhoanService.getTaiKhoanDonVi(dv),HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/tim-kiem")
    public ResponseEntity<Object> searchDonVi(@RequestParam String keyword){
        try {
            return new ResponseEntity<>(donViService.search(keyword),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Error("500","Lỗi khi tìm kiếm: "+e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody DonVi donVi, HttpServletRequest httpRequest){
        try {
            donViService.save(donVi);
            return new ResponseEntity<>("Thêm đơn vị thành công", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Error("400", e.getMessage()), HttpStatus.BAD_REQUEST);

        }
    }

    @PutMapping
    public ResponseEntity<Object> update(@Valid @RequestBody DonVi donVi, HttpServletRequest httpRequest){
        try {
            return new ResponseEntity<Object>(donViService.update(donVi), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Error("400", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object>delete(@PathVariable String id, HttpServletRequest httpRequest){
        try {
            donViService.deleteByID(id);
            return new ResponseEntity<>("Xoá thành công đơn vị có ID: "+id, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Error("400", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/select/donvi-tructhuoc")
    public ResponseEntity<Object> selectDonViTrucThuoc(HttpServletRequest httpRequest) {
        return new ResponseEntity<>(donViService.getSelectDonViTrucThuoc(taiKhoanService.getTaiKhoanFromRequest(httpRequest)), HttpStatus.OK);
    }

    @GetMapping("/select/donvi-cha")
    public ResponseEntity<Object> selectDonViCha() {
        return new ResponseEntity<>(donViService.getSelectDonViCha(), HttpStatus.OK);
    }

    @GetMapping("/select/toan-bo-donvi")
    public ResponseEntity<Object> selectToanBoDonVi() {
        return new ResponseEntity<>(donViService.getSelectToanBoDonVi(), HttpStatus.OK);
    }
}
