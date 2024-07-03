package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.service.DonViService;
import com.example.qllichlamviec.service.JwtService;
import com.example.qllichlamviec.service.TaiKhoanService;
import com.example.qllichlamviec.util.DonVi;
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
@RequestMapping("/admin/don-vi")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class DonViController {
    @Autowired
    private DonViService donViService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping(value = "/getall")
    public List<DonVi> getAlls(){
         return donViService.findAll();
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestParam(required = false) String search, HttpServletRequest httpRequest){
        try {
            if (Pattern.compile("^[0-9a-fA-F]{24}$").matcher(search).matches()==true){
                List<DonVi> dv = new ArrayList<>();
                return new ResponseEntity<>(donViService.getById(search), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody DonVi donVi, HttpServletRequest httpRequest){
        try {
            return new ResponseEntity<>(donViService.save(donVi), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Error("400"), HttpStatus.BAD_REQUEST);

        }
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody DonVi donVi, HttpServletRequest httpRequest){
        try {
            return new ResponseEntity<Object>(donViService.update(donVi), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Error("400"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object>delete(@PathVariable String id, HttpServletRequest httpRequest){
        try {
            donViService.deleteByID(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Error("400"), HttpStatus.BAD_REQUEST);
        }
    }
}
