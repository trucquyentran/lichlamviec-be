package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.modal.system.Error;
import com.example.qllichlamviec.service.JwtService;
import com.example.qllichlamviec.service.QuyenService;
import com.example.qllichlamviec.service.QuyenTaiKhoanService;
import com.example.qllichlamviec.service.TaiKhoanService;
import com.example.qllichlamviec.util.Quyen;
import com.example.qllichlamviec.util.TaiKhoan;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/admin/quyen")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class QuyenController {
    @Autowired
    private QuyenService quyenService;
    @Autowired
//    private ModuleLayer moduleLayer;
//    @Autowired
    private JwtService jwtService;
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private QuyenTaiKhoanService quyenTaiKhoanService;

    @GetMapping("/getall")
    public List<Quyen> getAll(){
        return quyenService.findAll();
    }

    @GetMapping
    public ResponseEntity<Object> searchQuyen(@RequestParam(required = false) String search, HttpServletRequest httpServletRequest){
        try {
            if (Pattern.compile("^[0-9a-fA-F]{24}$").matcher(search).matches() == true){
                List<Quyen> q = new ArrayList<>();
                return new ResponseEntity<>(quyenService.getById(search), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>(new Error("400", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody Quyen quyen, HttpServletRequest httpRequest){
        try {
            return new ResponseEntity<>(quyenService.save(quyen), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Error("400", e.getMessage()), HttpStatus.BAD_REQUEST);

        }
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody Quyen quyen, HttpServletRequest httpRequest){
        try {
            return new ResponseEntity<Object>(quyenService.upadate(quyen), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Error("400", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object>delete(@PathVariable String id, HttpServletRequest httpRequest){
        try {
            quyenService.deleteByID(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Error("400", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Object>deleteQuyenTK(@PathVariable("id") ObjectId id){
//        try {
//            quyenTaiKhoanService.deleteByTaiKhoan(id);
//
//            return new ResponseEntity<>("Xoá thành công các quyền của user có ID: "+id, HttpStatus.OK);
//        }catch (Exception e){
//            return new ResponseEntity<>(new Error("400", e.getMessage()), HttpStatus.BAD_REQUEST);
//        }
//    }

}
