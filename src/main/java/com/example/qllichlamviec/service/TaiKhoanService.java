package com.example.qllichlamviec.service;

import com.example.qllichlamviec.modal.system.Error;
import com.example.qllichlamviec.reponsitory.DonViReponsitory;
import com.example.qllichlamviec.reponsitory.NguoiDungReponsitory;
import com.example.qllichlamviec.reponsitory.QuyenTaiKhoanReponsitory;
import com.example.qllichlamviec.reponsitory.TaiKhoanReponsitory;
import com.example.qllichlamviec.util.*;
import com.example.qllichlamviec.util.pojo.Session;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
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

    public List<TaiKhoan> findAllUser(){
        return taiKhoanReponsitory.findAll();
    }

    public TaiKhoan getByUsername(String username) {

        return taiKhoanReponsitory.getByUsername(username);
    }

//    public TaiKhoan getByIDNguoiDung(String idNguoiDung) {
//
//        return taiKhoanReponsitory.getByIDNguoiDung(idNguoiDung);
//    }

    @Transactional
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
    public TaiKhoan phanQuyenOrDonVi(TaiKhoan taiKhoan) {

        List<QuyenTaiKhoan> quyenTaiKhoanList = taiKhoan.getQuyenTaiKhoanList();

        taiKhoan.setQuyenTaiKhoanList(null);

        TaiKhoan taiKhoanRs = taiKhoanReponsitory.save(taiKhoan);

        // Xóa tất cả các quyền cũ liên quan đến tài khoản
        quyenTaiKhoanService.deleteByTaiKhoan(taiKhoanRs.get_id());

        for (QuyenTaiKhoan qtk : quyenTaiKhoanList) {
            qtk.setTaiKhoan(taiKhoanRs);
            quyenTaiKhoanService.update(qtk);
        }
        return taiKhoanRs;
    }

    public static boolean isStrongPassword(String password) {
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        if (Pattern.compile(PASSWORD_PATTERN).matcher(password).matches()) {
            return false;
        } else return true;
    }

    public Error kiemTraTonTaiEmailHoacSdt(TaiKhoan taiKhoan) {
        TaiKhoan tkUsername = taiKhoanReponsitory.getByUsername(taiKhoan.getUsername());
        if (tkUsername != null){
            return new Error("400","Đã tồn tại Username");
        }
        TaiKhoan tkEmail = taiKhoanReponsitory.getByEmail(taiKhoan.getEmail());
        if (tkEmail != null) {
            return new Error("400","Đã tồn tại email");
        } else {
            TaiKhoan tkSdt = taiKhoanReponsitory.getBySdt(taiKhoan.getSdt());
            if (tkSdt != null) {
                return new Error("400","Đã tồn tại số điện thoại");
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
            return new Error("400","Mật khẩu có ít nhất 8 ký tự , phải có ít nhất 1 ký tự hoa, thường , số, đặc biệt.");
    }




//    public TaiKhoan themNguoiDungMoi(NguoiDung nguoiDung) {
//
//        TaiKhoan tk = new TaiKhoan();
//        tk.setUsername(nguoiDung.getSdt());
//        tk.setPassword("Vnpt#042024123");
//        tk.setNguoiDung(nguoiDung);
//        tk.setTrangThai(1);
//        List<QuyenTaiKhoan> quyenTaiKhoanList = new ArrayList<>();
//        quyenTaiKhoanList.add(new QuyenTaiKhoan(null, tk, new Quyen(new ObjectId("66431ee6950a6be77897e99b"))));
//        tk.setQuyenTaiKhoanList(quyenTaiKhoanList);
//        return  khoiTaoNguoiDungKemTaiKhoan(tk);
//    }

    public TaiKhoan getTaiKhoanFromRequest(HttpServletRequest httpRequest) {
        return taiKhoanReponsitory.getByID(jwtService.getIDTaiKhoanFromToken(jwtService.getToken(httpRequest)));
    }

//    public TaiKhoan logoutToken(String taiKhoanID ,String token) {
//        TaiKhoan taiKhoan = getByID(taiKhoanID);
//        if(taiKhoan.getListSession() == null){
//            taiKhoan.setListSession(new ArrayList<>());
//        }
//        for (Session session :taiKhoan.getListSession()) {
//            if(session.getAccessToken().equals(token)){
//                taiKhoan.getListSession().remove(session);
//            }
//        }
//        return taiKhoanReponsitory.save(taiKhoan);
//    }
    public TaiKhoan logoutToken(String taiKhoanID, String token) {
    TaiKhoan taiKhoan = getByID(taiKhoanID);
    if (taiKhoan.getListSession() == null) {
        taiKhoan.setListSession(new ArrayList<>());
    }

    List<Session> sessionsToRemove = new ArrayList<>();
    for (Session session : taiKhoan.getListSession()) {
        if (session.getAccessToken().equals(token)) {
            sessionsToRemove.add(session); // Thêm session vào danh sách cần xoá
        }
    }

    taiKhoan.getListSession().removeAll(sessionsToRemove); // Xoá tất cả các session trong danh sách cần xoá

    // Lưu thay đổi vào cơ sở dữ liệu
    taiKhoan = taiKhoanReponsitory.save(taiKhoan);

    return taiKhoan;
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


    public List<TaiKhoan> getAll() {
        return taiKhoanReponsitory.findAll();
    }
}
