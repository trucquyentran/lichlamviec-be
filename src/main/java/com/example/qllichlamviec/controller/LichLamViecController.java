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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping ("/lich-don-vi")
    public ResponseEntity<List<LichLamViec>> getLichDonVi(HttpServletRequest httpServletRequest){
        try {
            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);
            DonVi donVi = donViService.getById(taiKhoan.getDonVi().get_id().toHexString());

            List<LichLamViec> llv = lichLamViecService.getByIdDonVi(donVi.get_id());
            return new ResponseEntity<>(llv, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }




    @GetMapping ("/lich-tai-khoan-don-vi")
    public ResponseEntity <List<LichDonViTaiKhoanDTO>> getLichTaiKhoanOfDonViQuanLy(HttpServletRequest httpServletRequest){
        try {

            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);
            DonVi donVi = donViService.getById(taiKhoan.getDonVi().get_id().toHexString());

            List<TaiKhoan> taiKhoanList = taiKhoanService.getByDonViID(donVi.get_id());

            // Lấy danh sách lịch làm việc của từng tài khoản trong đơn vị
            List<LichDonViTaiKhoanDTO> lichDonViTaiKhoanDTOList = new ArrayList<>();
            for (TaiKhoan tk : taiKhoanList) {

                List<LichLamViec> lichLamViecCuaTaiKhoan = lichLamViecService.getByTaiKhoanID(tk.get_id());

                LichDonViTaiKhoanDTO llv = new LichDonViTaiKhoanDTO();
                llv.setTaiKhoan(tk);
                llv.setLichLamViecList(lichLamViecCuaTaiKhoan);

                lichDonViTaiKhoanDTOList.add(llv);

            }

            return new ResponseEntity<>(lichDonViTaiKhoanDTOList,HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping ("/lich-user/{taiKhoanId}")
    public ResponseEntity<Object> getLichByNguoiDung(@PathVariable ObjectId taiKhoanId){
        try {
            List<LichLamViec> llv = lichLamViecService.getByTaiKhoanID(taiKhoanId);
            return new ResponseEntity<>(llv, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/lich-tai-khoan")
    public ResponseEntity<Object> getLichTaiKhoan(HttpServletRequest httpServletRequest) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);

            ObjectId taiKhoanId = new ObjectId(String.valueOf(taiKhoan.get_id())); // Chuyển đổi chuỗi ID thành ObjectId
            List<LichLamViec> llv = lichLamViecService.getByTaiKhoanID(taiKhoanId);

            return new ResponseEntity<>(llv, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

//
//
//    @PutMapping
//    public ResponseEntity<Object> update(@RequestBody LichLamViec lichLamViec, HttpServletRequest httpRequest){
//        try {
//            return new ResponseEntity<Object>(lichLamViecService.update(lichLamViec), HttpStatus.OK);
//        }catch (Exception e){
//            return new ResponseEntity<>(new Error("400", e.getMessage()), HttpStatus.BAD_REQUEST);
//        }
//    }

//    -----------------------

    @PostMapping("/them")
    public ResponseEntity<Object> themLichLamViec(@RequestBody LichLamViecDTO lichLamViecDTO, HttpServletRequest httpRequest) {
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

            // Check thời gian bắt đầu và kết thục lịch
            Error error = lichLamViecService.kiemTraThoiGianHopLe(lichLamViec);
            if (error != null){
                return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
            }

            DonVi donVi = donViService.getById(lichLamViecDTO.getDonVi().toHexString());
            if (donVi == null) {
                return new ResponseEntity<>("Không tìm thấy thông tin đơn vị với ID: " +donVi, HttpStatus.UNAUTHORIZED);
            }
            lichLamViec.setDonVi(donVi);

            LichLamViec lichLamViecDaTao = lichLamViecService.save(lichLamViec);

            return new ResponseEntity<>(lichLamViecDaTao, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi tạo lịch làm việc: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/edit-lich-donvi")
    public ResponseEntity<Object> EditLichDonVi(@RequestBody LichLamViecDTO lichLamViecDTO, @RequestParam String idLich, HttpServletRequest httpRequest) {
        try {

            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpRequest);
            if (taiKhoan == null) {
                return new ResponseEntity<>("Không tìm thấy thông tin người dùng", HttpStatus.UNAUTHORIZED);
            }

            LichLamViec lichLamViec = lichLamViecService.getById(idLich);
            if (lichLamViec == null) {
                return new ResponseEntity<>("Không tìm thấy lịch làm việc", HttpStatus.NOT_FOUND);
            }

            lichLamViec.setThoiGianBD(lichLamViecDTO.getThoiGianBD());
            lichLamViec.setThoiGianKT(lichLamViecDTO.getThoiGianKT());
            lichLamViec.setDiaDiem(lichLamViecDTO.getDiaDiem());
            lichLamViec.setNoiDung(lichLamViecDTO.getNoiDung());
            lichLamViec.setTieuDe(lichLamViecDTO.getTieuDe());
            lichLamViec.setGhiChu(lichLamViecDTO.getGhiChu());
            lichLamViec.setThoiGianTao(LocalDateTime.now());

            // Check thời gian bắt đầu và kết thục lịch
            Error error = lichLamViecService.kiemTraThoiGianHopLe(lichLamViec);
            if (error != null){
                return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
            }

            LichLamViec lichLamViecDaTao = lichLamViecService.save(lichLamViec);

            return new ResponseEntity<>(lichLamViecDaTao, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi sửa lịch làm việc: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/them-lich-user")
    public ResponseEntity<Object> themLichUser(@RequestBody LichCaNhanDTO lichCaNhanDTO, HttpServletRequest httpRequest) {
        try {

            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpRequest);

            if (taiKhoan == null) {
                return new ResponseEntity<>("Không tìm thấy thông tin người dùng", HttpStatus.UNAUTHORIZED);
            }

            LichLamViec lichLamViec = new LichLamViec();
            lichLamViec.setThoiGianBD(lichCaNhanDTO.getThoiGianBD());
            lichLamViec.setThoiGianKT(lichCaNhanDTO.getThoiGianKT());
            lichLamViec.setDiaDiem(lichCaNhanDTO.getDiaDiem());
            lichLamViec.setNoiDung(lichCaNhanDTO.getNoiDung());
            lichLamViec.setTieuDe(lichCaNhanDTO.getTieuDe());
            lichLamViec.setGhiChu(lichCaNhanDTO.getGhiChu());
            lichLamViec.setThoiGianTao(LocalDateTime.now());

            lichLamViec.setTaiKhoan(taiKhoan);

            // Check thời gian bắt đầu và kết thục lịch
            Error error = lichLamViecService.kiemTraThoiGianHopLe(lichLamViec);
            if (error != null){
                return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
            }

            LichLamViec lichLamViecDaTao = lichLamViecService.save(lichLamViec);

            return new ResponseEntity<>(lichLamViecDaTao, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi tạo lịch làm việc: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/edit-lich-user")
    public ResponseEntity<Object> EditLichUser(@RequestBody LichCaNhanDTO lichCaNhanDTO, @RequestParam String idLich, HttpServletRequest httpRequest) {
        try {

            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpRequest);
            if (taiKhoan == null) {
                return new ResponseEntity<>("Không tìm thấy thông tin người dùng", HttpStatus.UNAUTHORIZED);
            }

            LichLamViec lichLamViec = lichLamViecService.getById(idLich);
            if (lichLamViec == null) {
                return new ResponseEntity<>("Không tìm thấy lịch làm việc", HttpStatus.NOT_FOUND);
            }

            String tk = lichLamViec.getTaiKhoan().get_id().toHexString();
            String tk2 = taiKhoan.get_id().toHexString();

            if (tk2.equals(tk)) {
                lichLamViec.setThoiGianBD(lichCaNhanDTO.getThoiGianBD());
                lichLamViec.setThoiGianKT(lichCaNhanDTO.getThoiGianKT());
                lichLamViec.setDiaDiem(lichCaNhanDTO.getDiaDiem());
                lichLamViec.setNoiDung(lichCaNhanDTO.getNoiDung());
                lichLamViec.setTieuDe(lichCaNhanDTO.getTieuDe());
                lichLamViec.setGhiChu(lichCaNhanDTO.getGhiChu());

                // Check thời gian bắt đầu và kết thục lịch
                Error error = lichLamViecService.kiemTraThoiGianHopLe(lichLamViec);
                if (error != null){
                    return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
                }

                LichLamViec lichLamViecDaTao = lichLamViecService.save(lichLamViec);
                return new ResponseEntity<>(lichLamViecDaTao, HttpStatus.CREATED);
            }else {
                return new ResponseEntity<>("Đây là lịch cá nhân bạn không có quyền chỉnh sữa", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi sửa lịch làm việc: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object>deleteByAdmin(@PathVariable String id, HttpServletRequest httpRequest){
        try {
            TaiKhoan tk = taiKhoanService.getTaiKhoanFromRequest(httpRequest);
            LichLamViec lichLamViec = lichLamViecService.getById(id);

            // Lấy Authentication hiện tại từ SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            taiKhoanService.kiemTraUserAdmin(authentication);
            // Kiểm tra xem người dùng có phải là Admin hay không
            if (!taiKhoanService.kiemTraUserAdmin(authentication)) {
                // Kiểm tra xem lịch làm việc có thuộc về tài khoản đang đăng nhập không
                if (!lichLamViec.getTaiKhoan().get_id().equals(tk.get_id())) {
                    return new ResponseEntity<>("Bạn không có quyền xoá lịch làm việc này!", HttpStatus.FORBIDDEN);
                }
                // Xoá lịch làm việc
                lichLamViecService.deleteByID(id);
                return new ResponseEntity<>("Xoá thành công lịch làm việc với ID:"+id, HttpStatus.FORBIDDEN);
            }else {

                if (lichLamViec.getDonVi() != null || lichLamViec.getTaiKhoan().get_id().toString().equals(tk.get_id().toString())) {

                    lichLamViecService.deleteByID(id);
                }
                return new ResponseEntity<>("Xoá thành công lịch làm việc với ID: "+id, HttpStatus.OK);
            }

        }catch (Exception e){
            return new ResponseEntity<>(new Error("400","Bạn không có quyền xóa lịch làm việc này ", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }



//    @PreAuthorize("hasRole('ROLE_USER')")
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Object> deleteByUser(@PathVariable String id, HttpServletRequest httpRequest) {
//        try {
//            // Lấy thông tin tài khoản từ httpRequest
//            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpRequest);
//
//            // Lấy thông tin lịch làm việc cần xoá từ id
//            LichLamViec lichLamViec = lichLamViecService.getById(id);
//
//            // Kiểm tra xem lịch làm việc có thuộc về tài khoản đang đăng nhập không
//            if (!lichLamViec.getTaiKhoan().get_id().equals(taiKhoan.get_id())) {
//                return new ResponseEntity<>("Bạn không có quyền xoá lịch làm việc này.", HttpStatus.FORBIDDEN);
//            }
//
//            // Xoá lịch làm việc
//            lichLamViecService.deleteByID(id);
//
//            return new ResponseEntity<>("Xoá thành công lịch làm việc với ID: " + id, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(new Error("400", e.getMessage()), HttpStatus.BAD_REQUEST);
//        }
//    }
}
