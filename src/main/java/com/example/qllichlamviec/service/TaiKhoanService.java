package com.example.qllichlamviec.service;

import com.example.qllichlamviec.modal.dto.QuyenTaiKhoanDTO;
import com.example.qllichlamviec.modal.dto.TaiKhoanDTO;
import com.example.qllichlamviec.modal.dto.TaiKhoanDonViDTO;
import com.example.qllichlamviec.modal.system.Error;
import com.example.qllichlamviec.modal.system.TaiKhoanNguoiDungDTO;
import com.example.qllichlamviec.reponsitory.DonViReponsitory;
import com.example.qllichlamviec.reponsitory.NguoiDungReponsitory;
import com.example.qllichlamviec.reponsitory.QuyenTaiKhoanReponsitory;
import com.example.qllichlamviec.reponsitory.TaiKhoanReponsitory;
import com.example.qllichlamviec.util.*;
import com.example.qllichlamviec.util.pojo.Session;
import com.example.qllichlamviec.util.pojo.XuLyDauChuoiTimKiem;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class TaiKhoanService {
    @Autowired
    private TaiKhoanReponsitory taiKhoanReponsitory;
    @Autowired
    private NguoiDungReponsitory nguoiDungReponsitory;
    @Autowired
    private DonViReponsitory donViReponsitory;
    @Autowired
    private QuyenTaiKhoanService quyenTaiKhoanService;
    @Autowired
    private QuyenTaiKhoanReponsitory quyenTaiKhoanReponsitory;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DonViService donViService;
    @Autowired
    private QuyenService quyenService;

    public List<QuyenTaiKhoan> getListQuyenByID(String idTaiKhoan) {
        List<QuyenTaiKhoan> quyenTaiKhoanList = quyenTaiKhoanService.getByTaiKhoanID(idTaiKhoan);
        if (quyenTaiKhoanList == null){
            throw new RuntimeException("Không tìm thấy tài khoản nào thuộc nhóm quyền này.");
        }
        return quyenTaiKhoanList;

    }

    public TaiKhoan getByID(String idTaiKhoan) {
       TaiKhoan taiKhoan = taiKhoanReponsitory.getByID(idTaiKhoan);
        if(taiKhoan == null){
            throw new RuntimeException("Tài khoản không tồn tại trong dữ liệu hệ thống, vui lòng kiểm tra lại.");
        }
        return taiKhoan;
    }

    public TaiKhoan getByUsername(String username) {

        TaiKhoan taiKhoan = taiKhoanReponsitory.getByUsername(username);
        if(taiKhoan == null){
            throw new RuntimeException("Tài khoản không tồn tại trong dữ liệu hệ thống, vui lòng kiểm tra lại.");
        }
        return taiKhoan;
    }

    public List<TaiKhoan> getByDonViID(ObjectId id) {

        List<TaiKhoan> taiKhoanList = taiKhoanReponsitory.getByIdDonVi(id);
        if(taiKhoanList == null || taiKhoanList.isEmpty()){
            throw new RuntimeException("Không tìm thấy tài khoản nào thuộc đơn vị này.");
        }
        return taiKhoanList;
    }

    public TaiKhoan findById(ObjectId id) {
         TaiKhoan taiKhoan = taiKhoanReponsitory.findById(id).orElse(null);
        if (taiKhoan == null ) {
            throw new RuntimeException("Tài khoản không tồn tại trong dữ liệu hệ thống, vui lòng kiểm tra lại.");
        }
        return taiKhoan;

    }

    public List<TaiKhoan> getAll() {
        List<TaiKhoan> taiKhoanList = taiKhoanReponsitory.findAll();
        return taiKhoanList;
    }

    public void deleteByID(String id){
        getByID(id);
        taiKhoanReponsitory.deleteById(new ObjectId(id));
    }


    public List<TaiKhoanNguoiDungDTO> searchTaiKhoan(String tuKhoa){
        String regexKeyword = XuLyDauChuoiTimKiem.convertToRegex(tuKhoa);
        List<TaiKhoan> taiKhoanList = taiKhoanReponsitory.searchTaiKhoan(regexKeyword);
//        if (taiKhoanList == null || taiKhoanList.isEmpty()){
//            throw new RuntimeException("Không tìm thấy thông tin");
//        }

        List<TaiKhoanNguoiDungDTO> taiKhoanDTOList = new ArrayList<>();
        for (TaiKhoan tk: taiKhoanList){
            TaiKhoanNguoiDungDTO taiKhoanDTO = mapToTaiKhoanNguoiDungDTO(tk);
            taiKhoanDTOList.add(taiKhoanDTO);
        }
        return taiKhoanDTOList;
    }

    public TaiKhoanNguoiDungDTO mapToTaiKhoanNguoiDungDTO(TaiKhoan taiKhoan) {
        TaiKhoanNguoiDungDTO taiKhoanDTO = modelMapper.map(taiKhoan, TaiKhoanNguoiDungDTO.class);
//        taiKhoanDTO.setDonVi(taiKhoan.getDonVi().get_id());
        taiKhoanDTO.setListQuyen(new ArrayList<>());
        for (QuyenTaiKhoan qtk : taiKhoan.getQuyenTaiKhoanList()) {
            QuyenTaiKhoanDTO quyenTaiKhoanDTO = modelMapper.map(qtk, QuyenTaiKhoanDTO.class);
            taiKhoanDTO.getListQuyen().add(quyenTaiKhoanDTO);
        }
        return taiKhoanDTO;
    }

    public TaiKhoanDonViDTO getTaiKhoanDonVi(String donVi){
        DonVi dv = donViService.getById(donVi);
        List<TaiKhoan> taiKhoanCuaDVList = getByDonViID(dv.get_id());
        TaiKhoanDonViDTO taiKhoanDonViDTO = modelMapper.map(dv, TaiKhoanDonViDTO.class);
        taiKhoanDonViDTO.setTaiKhoanList(new ArrayList<>());
        for (TaiKhoan tk: taiKhoanCuaDVList){
            TaiKhoanNguoiDungDTO taiKhoanNguoiDungDTO = mapToTaiKhoanNguoiDungDTO(tk);
            taiKhoanDonViDTO.getTaiKhoanList().add(taiKhoanNguoiDungDTO);
        }
        return taiKhoanDonViDTO;

    }

    public List<TaiKhoanNguoiDungDTO> findAllUser(int pageNumber){
        // Tạo Pageable object để chỉ định số trang và kích thước trang
        Pageable pageable = PageRequest.of(pageNumber, 3); // 3 là số dòng trên mỗi trang
        if (pageNumber == 0){
            List<TaiKhoan> taiKhoanList = taiKhoanReponsitory.findAll();
            List<TaiKhoanNguoiDungDTO> taiKhoanDTOList = new ArrayList<>();
            for (TaiKhoan tk: taiKhoanList){
                TaiKhoanNguoiDungDTO taiKhoanDTO = mapToTaiKhoanNguoiDungDTO(tk);
                taiKhoanDTOList.add(taiKhoanDTO);
            }
            return taiKhoanDTOList;
        }else {
            Page<TaiKhoan> taiKhoanList = taiKhoanReponsitory.findAll(pageable);
            List<TaiKhoanNguoiDungDTO> taiKhoanDTOList = new ArrayList<>();
            for (TaiKhoan tk: taiKhoanList){
                TaiKhoanNguoiDungDTO taiKhoanDTO = mapToTaiKhoanNguoiDungDTO(tk);
                taiKhoanDTOList.add(taiKhoanDTO);
            }
            return taiKhoanDTOList;
        }
        // Lấy trang dữ liệu từ repository


    }

    public TaiKhoan khoiTaoTaiKhoan(TaiKhoan taiKhoan) {

        taiKhoan.setTrangThai(1);

        List<QuyenTaiKhoan> quyenTaiKhoanList = taiKhoan.getQuyenTaiKhoanList();
        taiKhoan.setQuyenTaiKhoanList(null);
        taiKhoan.setPassword(passwordEncoder.encode(taiKhoan.getPassword()));
        TaiKhoan taiKhoanRs = taiKhoanReponsitory.save(taiKhoan);

        for (QuyenTaiKhoan qtk : quyenTaiKhoanList) {
            qtk.setTaiKhoan(taiKhoanRs);
            quyenTaiKhoanService.save(qtk);
        }
        return taiKhoanRs;
    }

    @Transactional
    public ResponseEntity<Object> themTaiKhoan(TaiKhoanDTO taiKhoanDTO) {

        // Lấy đơn vị từ cơ sở dữ liệu
        DonVi donVi = donViService.getById2(taiKhoanDTO.getDonVi());

        // Lấy danh sách quyền từ cơ sở dữ liệu
        List<Quyen> quyenList = new ArrayList<>();
        for (String quyenId : taiKhoanDTO.getListQuyen()) {
            Quyen quyen = quyenService.getById(quyenId);
                quyenList.add(quyen);
        }

        // Tạo tài khoản từ DTO
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setHoTen(taiKhoanDTO.getHoTen());
        taiKhoan.setGioiTinh(taiKhoanDTO.getGioiTinh());
        taiKhoan.setNgaySinh(taiKhoanDTO.getNgaySinh());
        taiKhoan.setEmail(taiKhoanDTO.getEmail());
        taiKhoan.setSdt(taiKhoanDTO.getSdt());

        taiKhoan.setUsername(taiKhoanDTO.getUsername());
        taiKhoan.setPassword(passwordEncoder.encode(taiKhoanDTO.getPassword()));
        taiKhoan.setNgayTao(LocalDateTime.now());
        taiKhoan.setTrangThai(1);
        taiKhoan.setDonVi(donVi);

//            Check username, email, sdt
        kiemTraTonTaiEmailHoacSdt(taiKhoan);
//          Check password
        if (isStrongPassword(taiKhoan.getPassword())){
            return new ResponseEntity<>(new Error("400","Mật khẩu ít nhất 8 ký tự gồm chữ hoa, thường, số, đặc biệt"), HttpStatus.BAD_REQUEST);
        }

        List<QuyenTaiKhoan> quyenTaiKhoanList = new ArrayList<>();
        for (Quyen quyen : quyenList) {
            quyenTaiKhoanList.add(new QuyenTaiKhoan(null, taiKhoan, quyen));
            taiKhoan.setQuyenTaiKhoanList(quyenTaiKhoanList);

        }

//        khoiTaoTaiKhoan(taiKhoan);

        List<QuyenTaiKhoan> quyenTaiKhoanList2 = taiKhoan.getQuyenTaiKhoanList();
        taiKhoan.setQuyenTaiKhoanList(null);

        TaiKhoan taiKhoanRs = taiKhoanReponsitory.save(taiKhoan);

        for (QuyenTaiKhoan qtk : quyenTaiKhoanList2) {
            qtk.setTaiKhoan(taiKhoanRs);
            quyenTaiKhoanService.save(qtk);
        }

        return new  ResponseEntity<>("Tài khoản đã được tạo thành công",HttpStatus.OK);
    }

    @Transactional
    public TaiKhoan phanQuyenOrDonVi(TaiKhoan taiKhoan) {

        List<QuyenTaiKhoan> quyenTaiKhoanList = taiKhoan.getQuyenTaiKhoanList();

        taiKhoan.setQuyenTaiKhoanList(null);

        TaiKhoan tk = taiKhoanReponsitory.save(taiKhoan);

        // Xóa tất cả các quyền cũ liên quan đến tài khoản
        quyenTaiKhoanService.deleteByTaiKhoan(tk.get_id());

        for (QuyenTaiKhoan qtk : quyenTaiKhoanList) {
            qtk.setTaiKhoan(tk);
            quyenTaiKhoanService.update(qtk);
        }
        return tk;
    }

    public static boolean isStrongPassword(String password) {
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        if (Pattern.compile(PASSWORD_PATTERN).matcher(password).matches()) {
            return false;
        } else return true;
    }

    public void kiemTraTonTaiEmailHoacSdt(TaiKhoan taiKhoan) {
        try {
            taiKhoan.validate();
            TaiKhoan tkUsername = taiKhoanReponsitory.getByUsername(taiKhoan.getUsername());
            if (tkUsername != null){
                throw new RuntimeException("Đã tồn tại Username");
            }
            // Biểu thức chính quy không cho phép khoảng trắng và dấu
            String regex = "^[a-zA-Z0-9]+$";
            Pattern pattern = Pattern.compile(regex);

            // Kiểm tra username có hợp lệ không
            if (taiKhoan.getUsername() == null || !pattern.matcher(taiKhoan.getUsername()).matches()) {
                throw new RuntimeException("Username không được chứa khoảng trắng hoặc dấu.");
            }
            TaiKhoan tkEmail = taiKhoanReponsitory.getByEmail(taiKhoan.getEmail());
            if (tkEmail != null) {
                throw new RuntimeException("Đã tồn tại email");
            } else {
                TaiKhoan tkSdt = taiKhoanReponsitory.getBySdt(taiKhoan.getSdt());
                if (tkSdt != null) {
                    throw new RuntimeException("Đã tồn tại số điện thoại");
                } else {
//                return null;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public TaiKhoan save(TaiKhoan taiKhoan) {

        return taiKhoanReponsitory.save(taiKhoan);
    }


    public TaiKhoan update(TaiKhoan taiKhoan) {
        return taiKhoanReponsitory.save(taiKhoan);
    }

    public TaiKhoan editMyAccount(TaiKhoanDTO taiKhoanDTO, HttpServletRequest httpServletRequest) {
        TaiKhoan taiKhoan = getTaiKhoanFromRequest(httpServletRequest);

        taiKhoan.setHoTen(taiKhoanDTO.getHoTen());
        taiKhoan.setGioiTinh(taiKhoanDTO.getGioiTinh());
        taiKhoan.setSdt(taiKhoanDTO.getSdt());
        taiKhoan.setEmail(taiKhoanDTO.getEmail());
        taiKhoan.setNgaySinh(taiKhoanDTO.getNgaySinh());

        return taiKhoanReponsitory.save(taiKhoan);

    }

    public ResponseEntity<Object> editTaiKhoan(TaiKhoanDTO taiKhoanDTO, HttpServletRequest httpServletRequest, String id){

        TaiKhoan taiKhoan = getByID(id);

        taiKhoan.setHoTen(taiKhoanDTO.getHoTen());
        taiKhoan.setGioiTinh(taiKhoanDTO.getGioiTinh());
        taiKhoan.setSdt(taiKhoanDTO.getSdt());
        taiKhoan.setEmail(taiKhoanDTO.getEmail());
        taiKhoan.setNgaySinh(taiKhoanDTO.getNgaySinh());
        taiKhoanReponsitory.save(taiKhoan);

        // Lấy đơn vị từ cơ sở dữ liệu
        DonVi donVi = donViService.getById2(taiKhoanDTO.getDonVi());

        // Lấy danh sách quyền từ cơ sở dữ liệu
        List<Quyen> quyenList = new ArrayList<>();
        for (String quyenId : taiKhoanDTO.getListQuyen()) {
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

        TaiKhoan capNhatUser = phanQuyenOrDonVi(taiKhoan);

        return new ResponseEntity<>("Cập nhật thành công",HttpStatus.OK);
    }

    public TaiKhoan doiMatKhau(TaiKhoan taiKhoan) {
        taiKhoan.setPassword(passwordEncoder.encode(taiKhoan.getPassword()));
        return taiKhoanReponsitory.save(taiKhoan);
    }

    public Error kiemTraDinhDangMatKhau(String password) {
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        if (Pattern.compile(PASSWORD_PATTERN).matcher(password).matches()) {
            return null;
        } else{
            return new Error("400","Mật khẩu có ít nhất 8 ký tự , phải có ít nhất 1 ký tự hoa, thường , số, đặc biệt.");
        }
    }


    public TaiKhoan getTaiKhoanFromRequest(HttpServletRequest httpRequest) {
        return taiKhoanReponsitory.getByID(jwtService.getIDTaiKhoanFromToken(jwtService.getToken(httpRequest)));
    }

    public ResponseEntity<Object> logoutToken(String taiKhoanID, String token) {
    TaiKhoan taiKhoan = getByID(taiKhoanID);
    if (taiKhoan.getListSession() == null) {
        taiKhoan.setListSession(new ArrayList<>());
    }
    List<Session> sessionsToRemove = new ArrayList<>();
    for (Session session : taiKhoan.getListSession()) {
        String sessionToken = session.getAccessToken().trim();
        String trimmedToken = token.trim();
        if (sessionToken.equals(trimmedToken)) {
            sessionsToRemove.add(session); // Thêm session vào danh sách cần xoá
        }
    }

    taiKhoan.getListSession().removeAll(sessionsToRemove); // Xoá tất cả các session trong danh sách cần xoá

    // Lưu thay đổi vào cơ sở dữ liệu
    taiKhoan = taiKhoanReponsitory.save(taiKhoan);
    return new ResponseEntity<>("Đã đăng xuất khỏi tài khoản " + taiKhoan.getUsername(), HttpStatus.OK);

    }

    public boolean kiemTraUserAdmin(Authentication authentication) {
        try {
            List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
            List<String> roles = new ArrayList<>();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            throw e;
        }
    }



}
