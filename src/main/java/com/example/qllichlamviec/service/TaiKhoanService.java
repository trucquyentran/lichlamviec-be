package com.example.qllichlamviec.service;

import com.example.qllichlamviec.reponsitory.DonViReponsitory;
import com.example.qllichlamviec.reponsitory.NguoiDungReponsitory;
import com.example.qllichlamviec.reponsitory.TaiKhoanReponsitory;
import com.example.qllichlamviec.util.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
//    @Autowired
    private ModuleLayer moduleLayer;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<QuyenTaiKhoan> getListQuyenByID(String idTaiKhoan) {
        return quyenTaiKhoanService.getByTaiKhoanID(idTaiKhoan);
    }

    public TaiKhoan getByID(String idTaiKhoan) {
        return taiKhoanReponsitory.getByID(idTaiKhoan);
    }

    public TaiKhoan findById(ObjectId id) {
        return taiKhoanReponsitory.findById(id).orElse(null);
    }

    public TaiKhoan getByUsername(String username) {

        return taiKhoanReponsitory.getByUsername(username);
    }

    public TaiKhoan getByIDNguoiDung(String idNguoiDung) {

        return taiKhoanReponsitory.getByIDNguoiDung(idNguoiDung);
    }

    @Transactional
    public NguoiDung khoiTaoNguoiDung(NguoiDung nguoiDung) {
        return nguoiDungReponsitory.save(nguoiDung);
    }

    @Transactional
    public TaiKhoan khoiTaoNguoiDungKemTaiKhoan(TaiKhoan taiKhoan) {
        taiKhoan.setTrangThai(1);
        NguoiDung ngDungRS = nguoiDungReponsitory.save(taiKhoan.getNguoiDung());
        taiKhoan.setNguoiDung(ngDungRS);
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

    public static boolean isStrongPassword(String password) {
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        if (Pattern.compile(PASSWORD_PATTERN).matcher(password).matches()) {
            return false;
        } else return true;
    }

    public Error kiemTraNguoiDungTonTaiEmailHoacSoDienThoai(NguoiDung nguoiDung) {
        NguoiDung ngDungEmail = nguoiDungReponsitory.getByEmail(nguoiDung.getEmail());
        if (ngDungEmail != null) {
            return new Error("Đã tồn tại email");
        } else {
            NguoiDung ngDungSDT = nguoiDungReponsitory.getBySdt(nguoiDung.getSdt());
            if (ngDungEmail != null) {
                return new Error("Đã tồn tại số điện thoại");
            } else {
                return null;
            }
        }
    }

    public TaiKhoan save(TaiKhoan taiKhoan) {

        return taiKhoanReponsitory.save(taiKhoan);
    }


    public TaiKhoan update(TaiKhoan taiKhoan) {

        return taiKhoanReponsitory.save(taiKhoan);
    }

    public TaiKhoan doiMatKhau(TaiKhoan taiKhoan) {
        taiKhoan.setPassword(passwordEncoder.encode(taiKhoan.getPassword()));
        return taiKhoanReponsitory.save(taiKhoan);
    }

    public Error kiemTraDinhDangMatKhau(String password) {
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        if (Pattern.compile(PASSWORD_PATTERN).matcher(password).matches()) {
            return null;
        } else
            return new Error("Mật khẩu có ít nhất 8 ký tự , phải có ít nhất 1 ký tự hoa, thường , số, đặc biệt.");
    }




    public TaiKhoan themNguoiDungMoi(NguoiDung nguoiDung) {

        TaiKhoan tk = new TaiKhoan();
        tk.setUsername(nguoiDung.getSdt());
        tk.setPassword("Vnpt#042024123");
        tk.setNguoiDung(nguoiDung);
        tk.setTrangThai(1);
        List<QuyenTaiKhoan> quyenTaiKhoanList = new ArrayList<>();
        quyenTaiKhoanList.add(new QuyenTaiKhoan(null, tk, new Quyen(new ObjectId("66431ee6950a6be77897e99b"))));
        tk.setQuyenTaiKhoanList(quyenTaiKhoanList);
        return  khoiTaoNguoiDungKemTaiKhoan(tk);
    }

    public TaiKhoan getTaiKhoanFromRequest(HttpServletRequest httpRequest) {
        return taiKhoanReponsitory.getByID(jwtService.getIDTaiKhoanFromToken(jwtService.getToken(httpRequest)));
    }


}
