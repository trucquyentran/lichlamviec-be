package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.modal.dto.*;
import com.example.qllichlamviec.modal.system.Error;
import com.example.qllichlamviec.service.*;
import com.example.qllichlamviec.util.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ql-lich/lich-lam-viec")
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


    @GetMapping(value = "/getall")
    public List<LichLamViec> getAlls(){
        return lichLamViecService.findAll();
    }

    @GetMapping("/tim-kiem")
    public ResponseEntity<?> timKiemLichLamViec(@RequestParam String tuKhoa) {
        try {
            List<LichLamViecHienThiDTO> lichLamViecList = lichLamViecService.search(tuKhoa);
            if (lichLamViecList.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lichLamViecList);
        } catch (Exception e) {
            // Xử lý ngoại lệ và trả về thông báo lỗi phù hợp
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi tìm kiếm lịch làm việc: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<Object> chiTietLich(@RequestParam(required = false) String search, HttpServletRequest httpRequest){
        try {
            return ResponseEntity.ok(lichLamViecService.getChiTietLich(search,httpRequest));
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping ("/lich-don-vi")
    public ResponseEntity<List<LichLamViecDonViDTO>> LichCuaDonVi(HttpServletRequest httpServletRequest){
        try {

            return new ResponseEntity<>(lichLamViecService.getLichDonVi(httpServletRequest), HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/lich-tai-khoan-don-vi")
    public ResponseEntity <List<LichDonViTaiKhoanDTO>> LichTaiKhoanOfDonViQuanLy(HttpServletRequest httpServletRequest){
        try {
            return new ResponseEntity<>(lichLamViecService.getLichTaiKhoanOfDonViQuanLy(httpServletRequest),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping ("/lich-user")
    public ResponseEntity<Object> lichByIdNguoiDung(@RequestParam ObjectId tk){
        try {
            return ResponseEntity.ok(lichLamViecService.getLichByNguoiDung(tk));

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/lich-tai-khoan")
    public ResponseEntity<Object> getLichTaiKhoan(HttpServletRequest httpServletRequest) {
        try {
            return new ResponseEntity<>(lichLamViecService.getLichTaiKhoan(httpServletRequest), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PostMapping("/them-lich-donvi")
    public ResponseEntity<Object> themLichDonVi(@Valid @RequestBody LichLamViecDTO lichLamViecDTO, HttpServletRequest httpRequest) {
        try {
            return ResponseEntity.ok(lichLamViecService.taoLich(lichLamViecDTO, httpRequest));

        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi tạo lịch làm việc: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PostMapping("/them-lich-nhan-vien")
    public ResponseEntity<Object> themLichNhanVien(@Valid @RequestBody LichLamViecDTO lichLamViecDTO, HttpServletRequest httpRequest) {
        try {
            return ResponseEntity.ok(lichLamViecService.taoLich(lichLamViecDTO, httpRequest));

        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi tạo lịch: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PutMapping("/edit-lich-donvi")
    public ResponseEntity<Object> editLichDonVi(@Valid @RequestBody LichLamViecHienThiDTO lichLamViecHienThiDTO, @RequestParam String idLich, HttpServletRequest httpRequest) {
        try {
            return ResponseEntity.ok(lichLamViecService.editLich(lichLamViecHienThiDTO, idLich, httpRequest));

        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi sửa lịch làm việc: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PutMapping("/edit-lich-nhan-vien")
    public ResponseEntity<Object> editLichNhanVien(@Valid @RequestBody LichLamViecHienThiDTO lichLamViecHienThiDTO, @RequestParam String idLich, HttpServletRequest httpRequest) {
        try {
            return ResponseEntity.ok(lichLamViecService.editLich(lichLamViecHienThiDTO, idLich, httpRequest));

        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi sửa lịch làm việc: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/them-lich-user")
    public ResponseEntity<Object> themLichUser(@Valid @RequestBody LichLamViecDTO lichLamViecDTO, HttpServletRequest httpRequest) {
        try {
            return ResponseEntity.ok(lichLamViecService.taoLich(lichLamViecDTO, httpRequest));

        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi tạo lịch làm việc: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/edit-lich-user")
    public ResponseEntity<Object> EditLichUser(@Valid @RequestBody LichLamViecHienThiDTO lichLamViecHienThiDTO, @RequestParam String idLich, HttpServletRequest httpRequest) {
        try {
            return  ResponseEntity.ok(lichLamViecService.editLichCaNhan(lichLamViecHienThiDTO, idLich, httpRequest));

        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi sửa lịch làm việc: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object>deleteByAdmin(@PathVariable String id, HttpServletRequest httpRequest){
        try {
            return ResponseEntity.ok(lichLamViecService.deleteLich(id, httpRequest));
        }catch (Exception e){
            return new ResponseEntity<>(new Error("400","Bạn không có quyền xóa lịch làm việc này ", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
