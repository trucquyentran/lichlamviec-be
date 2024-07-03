package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.modal.dto.TaiKhoanDangNhapDTO;
import com.example.qllichlamviec.modal.system.Error;
import com.example.qllichlamviec.modal.system.NguoiDungDangNhapDTO;
import com.example.qllichlamviec.service.JwtService;
import com.example.qllichlamviec.service.TaiKhoanService;
import com.example.qllichlamviec.util.QuyenTaiKhoan;
import com.example.qllichlamviec.util.pojo.Session;
import com.example.qllichlamviec.util.TaiKhoan;
import lombok.extern.slf4j.Slf4j;
import com.example.qllichlamviec.modal.system.Token;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@RequestMapping("/admin/user")
@Slf4j
public class TaiKhoanController {
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/dang-nhap")
    public ResponseEntity<Object> dangnhap(@RequestBody TaiKhoanDangNhapDTO taiKhoan){
        TaiKhoan taiKhoan1 = taiKhoanService.getByUsername(taiKhoan.getUsername());
        if (taiKhoan1 != null){
            if (taiKhoan1.getTrangThai() == 0){
                return new ResponseEntity<>(new Error("400","Tài khoản của bạn đã bị chặn"), HttpStatus.OK);
            }
            if (passwordEncoder.matches(taiKhoan.getPassword(), taiKhoan1.getPassword())){
                Token token = new Token();
                token.setNguoiDung(modelMapper.map((taiKhoan1.getNguoiDung()), new TypeToken<NguoiDungDangNhapDTO>(){
                }.getType()));
                token.setAccess_token(jwtService.generateTokenLogin(taiKhoan1.get_id().toHexString(),taiKhoan1.getNguoiDung().get_id().toHexString(), taiKhoan1.getTrangThai()));
                token.setRefresh_token(jwtService.generateRefreshToken(taiKhoan1.get_id().toHexString(),taiKhoan1.getNguoiDung().get_id().toHexString(),taiKhoan1.getTrangThai()));

           if (taiKhoan1.getListSession() == null){
               taiKhoan1.setListSession(new ArrayList<>());
           }
           taiKhoan1.getListSession().add(new Session(token.getAccess_token(), token.getRefresh_token()));
           taiKhoanService.update(taiKhoan1);
           return new ResponseEntity<>(token, HttpStatus.OK);
        }else {
                taiKhoanService.update(taiKhoan1);
                return new ResponseEntity<>(new Error("400", "Sai thông tin tài khoản hoặc mật khẩu."), HttpStatus.OK);
            }
        }else return new ResponseEntity<>(new Error("400", "Sai thông tin tài khoản hoặc mật khẩu"), HttpStatus.OK);
    }

    @GetMapping("/account")
    public ResponseEntity<Object> getThongTin(@RequestParam(required = false) Long id, HttpServletRequest httpRequest){
        try {
            String forwardedForHeader = httpRequest.getHeader("X-Forwarded-For");
            String ip = (forwardedForHeader != null && !forwardedForHeader.isEmpty())?forwardedForHeader.split("\\s*")[0]:null;
            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpRequest);
            NguoiDungDangNhapDTO nguoiDungDangNhapDTO = modelMapper.map(taiKhoan.getNguoiDung(), NguoiDungDangNhapDTO.class);
            nguoiDungDangNhapDTO.setListQuyen(new ArrayList<>());
            for (QuyenTaiKhoan qtk : taiKhoan.getQuyenTaiKhoanList()){
                nguoiDungDangNhapDTO.getListQuyen().add(qtk.getQuyen().getTenQuyen());
            }
            return new ResponseEntity<>(nguoiDungDangNhapDTO, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
