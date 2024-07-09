package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.modal.dto.LichLanViecDTO;
import com.example.qllichlamviec.modal.dto.TaiKhoanDangNhapDTO;
import com.example.qllichlamviec.modal.dto.TaiKhoanNguoiDungDTO;
import com.example.qllichlamviec.modal.dto.ThongBaoDTO;
import com.example.qllichlamviec.modal.system.Error;
import com.example.qllichlamviec.modal.system.NguoiDungDangNhapDTO;
import com.example.qllichlamviec.service.*;
import com.example.qllichlamviec.util.*;
import org.apache.catalina.security.SecurityUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/admin/lich-lam-viec")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class LichLamViecController {
    @Autowired
    private LichLamViecService lichLamViecService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private DonViService donViService;
//    @Autowired
//    private ThongBaoService thongBaoService;

    @GetMapping(value = "/getall")
    public List<LichLamViec> getAlls(){
        return lichLamViecService.findAll();
    }

    @GetMapping
    public ResponseEntity<Object> search(@RequestParam(required = false) String search, HttpServletRequest httpRequest){
        try {
            if (Pattern.compile("^[0-9a-fA-F]{24}$").matcher(search).matches()==true){
                return new ResponseEntity<>(lichLamViecService.getById(search), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/lich-don-vi/{idDonVi}")
    public ResponseEntity<List<LichLamViec>> getLichByDonVi(@PathVariable ObjectId idDonVi){
        try {
                List<LichLamViec> llv = lichLamViecService.getByIdDonVi(idDonVi);
                return new ResponseEntity<>(llv, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/lich-user/{NguoiDungId}")
    public ResponseEntity<List<LichLamViec>> getLichByNguoiDung(@PathVariable ObjectId NguoiDungId){
        try {
            List<LichLamViec> llv = lichLamViecService.getByNguoiDungID(NguoiDungId);
            return new ResponseEntity<>(llv, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/lich-tai-khoan")
    public ResponseEntity<Object> getLichTaiKhoan(HttpServletRequest httpServletRequest) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);

            ObjectId nguoiDungId = new ObjectId(String.valueOf(taiKhoan.get_id())); // Chuyển đổi chuỗi ID thành ObjectId
            List<LichLamViec> llv = lichLamViecService.getByNguoiDungID(nguoiDungId);

            return new ResponseEntity<>(llv, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @PutMapping
    public ResponseEntity<Object> update(@RequestBody LichLamViec lichLamViec, HttpServletRequest httpRequest){
        try {
            return new ResponseEntity<Object>(lichLamViecService.update(lichLamViec), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Error("400", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

//    -----------------------

    @PostMapping("/them")
    public ResponseEntity<Object> themLichLamViec(@RequestBody LichLanViecDTO lichLamViecDTO, HttpServletRequest httpRequest) {
        try {

            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpRequest);

            if (taiKhoan == null) {
                return new ResponseEntity<>("Không tìm thấy thông tin người dùng với ID: "+taiKhoan, HttpStatus.UNAUTHORIZED);
            }

            LichLamViec lichLamViec = new LichLamViec();
            lichLamViec.setThoiGianBD(lichLamViecDTO.getThoiGianBD());
            lichLamViec.setThoiGianKT(lichLamViecDTO.getThoiGianKT());
            lichLamViec.setDiaDiem(lichLamViecDTO.getDiaDiem());
            lichLamViec.setNoiDung(lichLamViecDTO.getNoiDung());
            lichLamViec.setTieuDe(lichLamViecDTO.getTieuDe());
            lichLamViec.setGhiChu(lichLamViecDTO.getGhiChu());


//            lichLamViec.setNguoiDung(taiKhoan.getNguoiDung());
//            lichLamViec.setDonVi(taiKhoan.getNguoiDung().getDonVi());

            DonVi donVi = donViService.getById(lichLamViecDTO.getDonVi().toHexString());
            if (donVi == null) {
                return new ResponseEntity<>("Không tìm thấy thông tin đơn vị với ID: " +donVi, HttpStatus.UNAUTHORIZED);
            }
            lichLamViec.setDonVi(donVi);

//            lichLamViec.setNguoiDung(taiKhoan.getNguoiDung());

            LichLamViec lichLamViecDaTao = lichLamViecService.save(lichLamViec);


            ThongBao thongBao = new ThongBao();
            thongBao.setThoiGian(LocalDateTime.now());
//            thongBao.setUser();


//            thongBaoService.save(thongBao);

            return new ResponseEntity<>(lichLamViecDaTao, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi tạo lịch làm việc: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/them-lich-user")
    public ResponseEntity<Object> themLicUser(@RequestBody LichLanViecDTO lichLamViecDTO, HttpServletRequest httpRequest) {
        try {

            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpRequest);

            if (taiKhoan == null) {
                return new ResponseEntity<>("Không tìm thấy thông tin người dùng", HttpStatus.UNAUTHORIZED);
            }

            LichLamViec lichLamViec = new LichLamViec();
            lichLamViec.setThoiGianBD(lichLamViecDTO.getThoiGianBD());
            lichLamViec.setThoiGianKT(lichLamViecDTO.getThoiGianKT());
            lichLamViec.setDiaDiem(lichLamViecDTO.getDiaDiem());
            lichLamViec.setNoiDung(lichLamViecDTO.getNoiDung());
            lichLamViec.setTieuDe(lichLamViecDTO.getTieuDe());
            lichLamViec.setGhiChu(lichLamViecDTO.getGhiChu());

            lichLamViec.setTaiKhoan(taiKhoan);

            LichLamViec lichLamViecDaTao = lichLamViecService.save(lichLamViec);

            return new ResponseEntity<>(lichLamViecDaTao, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi tạo lịch làm việc: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object>deleteByAdmin(@PathVariable String id, HttpServletRequest httpRequest){
        try {
            lichLamViecService.deleteByID(id);
            return new ResponseEntity<>("Xoá thành công lịch làm việc với ID: "+id, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Error("400", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteByUser(@PathVariable String id, HttpServletRequest httpRequest) {
        try {
            // Lấy thông tin tài khoản từ httpRequest
            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpRequest);

            // Lấy thông tin lịch làm việc cần xoá từ id
            LichLamViec lichLamViec = lichLamViecService.getById(id);

            // Kiểm tra xem lịch làm việc có thuộc về tài khoản đang đăng nhập không
            if (!lichLamViec.getTaiKhoan().get_id().equals(taiKhoan.get_id())) {
                return new ResponseEntity<>("Bạn không có quyền xoá lịch làm việc này.", HttpStatus.FORBIDDEN);
            }

            // Xoá lịch làm việc
            lichLamViecService.deleteByID(id);

            return new ResponseEntity<>("Xoá thành công lịch làm việc với ID: " + id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Error("400", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }





}
