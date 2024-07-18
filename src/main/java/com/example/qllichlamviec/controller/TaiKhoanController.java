package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.modal.dto.TaiKhoanDangNhapDTO;
import com.example.qllichlamviec.modal.dto.TaiKhoanDTO;
import com.example.qllichlamviec.modal.system.Error;
import com.example.qllichlamviec.modal.system.NguoiDungDangNhapDTO;
import com.example.qllichlamviec.modal.system.TaiKhoanNguoiDungDTO;
import com.example.qllichlamviec.service.*;
import com.example.qllichlamviec.util.*;
import com.example.qllichlamviec.util.pojo.Session;
import lombok.extern.slf4j.Slf4j;
import com.example.qllichlamviec.modal.system.Token;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ql-lich/user")

@Slf4j
public class TaiKhoanController {
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private DonViService donViService;
    @Autowired
    private QuyenService quyenService;
    @Autowired
    private QuyenTaiKhoanService quyenTaiKhoanService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ThongBaoService thongBaoService;

    @PostMapping("/dang-nhap")
    public ResponseEntity<Object> dangnhap(@RequestBody TaiKhoanDangNhapDTO taiKhoan){
        TaiKhoan taiKhoan1 = taiKhoanService.getByUsername(taiKhoan.getUsername());
        if (taiKhoan1 != null){
            if (taiKhoan1.getTrangThai() == 0){
                return new ResponseEntity<>(new Error("400","Tài khoản của bạn đã bị chặn"), HttpStatus.OK);
            }
            if (passwordEncoder.matches(taiKhoan.getPassword(), taiKhoan1.getPassword())){
                Token token = new Token();
                token.setTaiKhoan(modelMapper.map((taiKhoan1), new TypeToken<NguoiDungDangNhapDTO>(){
                }.getType()));

                token.setAccess_token(jwtService.generateTokenLogin(taiKhoan1.get_id().toHexString(), taiKhoan1.getTrangThai()));
                token.setRefresh_token(jwtService.generateRefreshToken(taiKhoan1.get_id().toHexString(),taiKhoan1.getTrangThai()));

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
            NguoiDungDangNhapDTO nguoiDungDangNhapDTO = modelMapper.map(taiKhoan, NguoiDungDangNhapDTO.class);
            nguoiDungDangNhapDTO.setListQuyen(new ArrayList<>());
            for (QuyenTaiKhoan qtk : taiKhoan.getQuyenTaiKhoanList()){
                nguoiDungDangNhapDTO.getListQuyen().add(qtk.getQuyen().getTenQuyen());
            }
            return new ResponseEntity<>(nguoiDungDangNhapDTO, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("doi-mk")
    public ResponseEntity<Object> doiMatKhau(HttpServletRequest httpRequest, @RequestBody TaiKhoanDangNhapDTO taiKhoanDTO){
        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpRequest);
        if (passwordEncoder.matches(taiKhoanDTO.getOldPassword(), taiKhoan.getPassword())){
            if (taiKhoanService.isStrongPassword(taiKhoanDTO.getNewPassword())){
                return new ResponseEntity<>(new Error("400","Mật khẩu ít nhất 8 ký tự gồm chữ hoa, thường, số, đặc biệt"), HttpStatus.OK);
            }
            taiKhoan.setPassword(taiKhoanDTO.getNewPassword());
            taiKhoanService.doiMatKhau(taiKhoan);
            return new ResponseEntity<>("Đổi mật khẩu thành công", HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(new Error("400","Mật khẩu cũ không chính xác"), HttpStatus.OK);
        }
    }

    @GetMapping("/dang-xuat")
    public ResponseEntity<Object> dangXuat(HttpServletRequest httpRequest){
        try {
            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpRequest);
            String token = jwtService.getToken(httpRequest);
            String idtk = taiKhoan.get_id().toHexString();

            return ResponseEntity.ok(taiKhoanService.logoutToken(idtk, token));
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi khi đăng xuất", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/edit-account")
    public ResponseEntity<Object> suaThongTinAccount(HttpServletRequest httpServletRequest, @RequestBody TaiKhoanDTO taiKhoanDTO){
        try {
            TaiKhoan tkCapNhat = taiKhoanService.editMyAccount(taiKhoanDTO, httpServletRequest);
            return new ResponseEntity<>(tkCapNhat, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Error("500", "Lỗi khi sửa thông tin tài khoản: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<Object> getQuyenTaiKhoan(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(""+authentication.getAuthorities(),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list-user")
    public List<TaiKhoanNguoiDungDTO> ListUser(){
        return taiKhoanService.findAllUser();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list-user-donvi")
    public List<TaiKhoanNguoiDungDTO> ListUserTrongDonVi(HttpServletRequest httpServletRequest){
        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);
        List<TaiKhoan> taiKhoanThuocDonViList = taiKhoanService.getByDonViID(taiKhoan.getDonVi().get_id());

        List<TaiKhoanNguoiDungDTO> taiKhoanDTOList = new ArrayList<>();
        for (TaiKhoan tk: taiKhoanThuocDonViList){
            TaiKhoanNguoiDungDTO taiKhoanDTO = taiKhoanService.mapToTaiKhoanNguoiDungDTO(tk);
            taiKhoanDTOList.add(taiKhoanDTO);
        }
        return taiKhoanDTOList;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("add-user")
    @Transactional
    public ResponseEntity<Object> themNguoiDung(HttpServletRequest httpServletRequest, @RequestBody TaiKhoanDTO taiKhoanDTO) {
        try {
           return taiKhoanService.themTaiKhoan(taiKhoanDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(new Error("500", "Lỗi khi tạo người dùng và tài khoản: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getall")
    public ResponseEntity<Object> ListTaiKhoan(){
        try {
            List<TaiKhoan> taiKhoanList = taiKhoanService.getAll();
            return new ResponseEntity<>(taiKhoanList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi hiển thị danh sách"+ e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/edit-tai-khoan")
    @Transactional
    public ResponseEntity<Object> editTaiKhoan(HttpServletRequest httpServletRequest, @RequestBody TaiKhoanDTO taiKhoanDTO, @RequestParam String id){
        try {
            return ResponseEntity.ok(taiKhoanService.editTaiKhoan(taiKhoanDTO, httpServletRequest, id));
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi khi chỉnh sửa! "+ e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete-tai-khoan")
    public ResponseEntity<Object> deleteTaiKhoan(@RequestParam String id){
        try {
            taiKhoanService.deleteByID(id);
            return new ResponseEntity<>("Xoá tài khoản thành công!",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Error("400","Lỗi khi xoá tài khoản! "+ e.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }


}
