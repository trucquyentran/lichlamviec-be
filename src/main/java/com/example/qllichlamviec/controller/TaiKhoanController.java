package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.modal.dto.TaiKhoanDangNhapDTO;
import com.example.qllichlamviec.modal.dto.TaiKhoanNguoiDungDTO;
import com.example.qllichlamviec.modal.system.DonViNameDTO;
import com.example.qllichlamviec.modal.system.Error;
import com.example.qllichlamviec.modal.system.NguoiDungDangNhapDTO;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/user")

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

            taiKhoanService.logoutToken(idtk, token);
            return new ResponseEntity<>("Đã đăng xuất khỏi tài khoản " + taiKhoan.getUsername(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi khi đăng xuất", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/edit-account")
    public ResponseEntity<Object> editAccount(HttpServletRequest httpServletRequest, @RequestBody TaiKhoanNguoiDungDTO taiKhoanNguoiDungDTO){
        try {
            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);

            taiKhoan.setHoTen(taiKhoanNguoiDungDTO.getHoTen());
            taiKhoan.setGioiTinh(taiKhoanNguoiDungDTO.getGioiTinh());
            taiKhoan.setSdt(taiKhoanNguoiDungDTO.getSdt());
            taiKhoan.setEmail(taiKhoanNguoiDungDTO.getEmail());
            taiKhoan.setNgaySinh(taiKhoanNguoiDungDTO.getNgaySinh());

            TaiKhoan tkCapNhat = taiKhoanService.update(taiKhoan);

            return new ResponseEntity<>(tkCapNhat, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Error("500", "Lỗi khi sửa thông tin tài khoản: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<Object> getQuyenTaiKhoan(HttpServletRequest httpServletRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        taiKhoanService.kiemTraUserAdmin(authentication);
//        thongBaoService.kiemTraVaGuiThongBao(httpServletRequest);
        return new ResponseEntity<>(""+authentication.getAuthorities(),HttpStatus.OK);
    }

    @GetMapping("/list-user")
    public List<TaiKhoan> ListUser(){
        return taiKhoanService.findAllUser();
    }

    @GetMapping("/list-user-donvi")
    public List<TaiKhoan> ListUserTrongDonVi(HttpServletRequest httpServletRequest){
        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);
        List<TaiKhoan> taiKhoanThuocDonViList = taiKhoanService.getByDonViID(taiKhoan.getDonVi().get_id());
        return taiKhoanThuocDonViList;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("add-user")
    @Transactional
    public ResponseEntity<Object> themNguoiDung(HttpServletRequest httpServletRequest, @RequestBody TaiKhoanNguoiDungDTO taiKhoanNguoiDungDTO) {
        try {
            // Lấy đơn vị từ cơ sở dữ liệu
            DonVi donVi = donViService.getById2(taiKhoanNguoiDungDTO.getDonVi());
            if (donVi == null) {
                return new ResponseEntity<>(new Error("404", "Không tìm thấy đơn vị với ID: " + taiKhoanNguoiDungDTO.getDonVi()), HttpStatus.NOT_FOUND);
            }

            // Lấy danh sách quyền từ cơ sở dữ liệu
            List<Quyen> quyenList = new ArrayList<>();
            for (String quyenId : taiKhoanNguoiDungDTO.getListQuyen()) {
                Quyen quyen = quyenService.getById(quyenId);
                if (quyen != null) {
                    quyenList.add(quyen);
                } else {
                    // Xử lý nếu quyền không tồn tại
                    return new ResponseEntity<>(new Error("404", "Không tìm thấy quyền với ID: " + quyenId), HttpStatus.NOT_FOUND);
                }
            }


            // Tạo tài khoản từ DTO
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setHoTen(taiKhoanNguoiDungDTO.getHoTen());
            taiKhoan.setGioiTinh(taiKhoanNguoiDungDTO.getGioiTinh());
            taiKhoan.setNgaySinh(taiKhoanNguoiDungDTO.getNgaySinh());
            taiKhoan.setEmail(taiKhoanNguoiDungDTO.getEmail());
            taiKhoan.setSdt(taiKhoanNguoiDungDTO.getSdt());

            taiKhoan.setUsername(taiKhoanNguoiDungDTO.getUsername());
            taiKhoan.setPassword(taiKhoanNguoiDungDTO.getPassword());
            taiKhoan.setNgayTao(LocalDateTime.now());
            taiKhoan.setDonVi(donVi);

//            Check username, email, sdt
            Error error = taiKhoanService.kiemTraTonTaiEmailHoacSdt(taiKhoan);
            if(error != null){
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

            }
//          Check password
            if (taiKhoanService.isStrongPassword(taiKhoan.getPassword())){
                return new ResponseEntity<>(new Error("400","Mật khẩu ít nhất 8 ký tự gồm chữ hoa, thường, số, đặc biệt"), HttpStatus.OK);
            }

            List<QuyenTaiKhoan> quyenTaiKhoanList = new ArrayList<>();
            for (Quyen quyen : quyenList) {
                quyenTaiKhoanList.add(new QuyenTaiKhoan(null, taiKhoan, quyen));
                taiKhoan.setQuyenTaiKhoanList(quyenTaiKhoanList);

            }
           taiKhoanService.khoiTaoTaiKhoan(taiKhoan);

            return new ResponseEntity<>(new Error("201", "Tài khoản đã được tạo thành công"), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new Error("500", "Lỗi khi tạo người dùng và tài khoản: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<Object> ListTaiKhoan(){
        try {
            List<TaiKhoan> taiKhoanList = taiKhoanService.getAll();
            return new ResponseEntity<>(taiKhoanList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi hiển thị danh sách"+ e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/edit-quyen-donvi")
    @Transactional
    public ResponseEntity<Object> editQuyenOrDonVi(HttpServletRequest httpServletRequest, @RequestBody TaiKhoanNguoiDungDTO taiKhoanNguoiDungDTO, @RequestParam String id){
        try {
            TaiKhoan taiKhoan = taiKhoanService.getByID(id);

            // Lấy đơn vị từ cơ sở dữ liệu
            DonVi donVi = donViService.getById2(taiKhoanNguoiDungDTO.getDonVi());
            if (donVi == null) {
                return new ResponseEntity<>(new Error("404", "Không tìm thấy đơn vị với ID: " + taiKhoanNguoiDungDTO.getDonVi()), HttpStatus.NOT_FOUND);
            }

            // Lấy danh sách quyền từ cơ sở dữ liệu
            List<Quyen> quyenList = new ArrayList<>();
            for (String quyenId : taiKhoanNguoiDungDTO.getListQuyen()) {
                Quyen quyen = quyenService.getById(quyenId);
                if (quyen != null) {
                    quyenList.add(quyen);
                } else {
                    // Xử lý nếu quyền không tồn tại
                    return new ResponseEntity<>(new Error("404", "Không tìm thấy quyền với ID: " + quyenId), HttpStatus.NOT_FOUND);
                }
            }

            taiKhoan.setDonVi(donVi);

            List<QuyenTaiKhoan> quyenTaiKhoanList = new ArrayList<>();
            for (Quyen quyen : quyenList) {
                quyenTaiKhoanList.add(new QuyenTaiKhoan(null, taiKhoan, quyen));
                taiKhoan.setQuyenTaiKhoanList(quyenTaiKhoanList);

            }

            TaiKhoan capNhatUser = taiKhoanService.phanQuyenOrDonVi(taiKhoan);

            return new ResponseEntity<>("Cập nhật thành công",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi khi chỉnh sửa "+ e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


}
