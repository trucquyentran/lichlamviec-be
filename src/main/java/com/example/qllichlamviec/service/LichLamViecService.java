package com.example.qllichlamviec.service;

import com.example.qllichlamviec.modal.dto.LichCaNhanDTO;
import com.example.qllichlamviec.modal.dto.LichDonViTaiKhoanDTO;
import com.example.qllichlamviec.modal.dto.LichLamViecDTO;
import com.example.qllichlamviec.modal.dto.LichLamViecDonViDTO;
import com.example.qllichlamviec.modal.system.Error;
import com.example.qllichlamviec.modal.system.NguoiDungDangNhapDTO;
import com.example.qllichlamviec.modal.system.TaiKhoanNguoiDungDTO;
import com.example.qllichlamviec.reponsitory.DonViReponsitory;
import com.example.qllichlamviec.reponsitory.LichLamViecReponsitory;
import com.example.qllichlamviec.reponsitory.NguoiDungReponsitory;
import com.example.qllichlamviec.util.*;
import com.example.qllichlamviec.util.pojo.RegexUtils;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpClient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LichLamViecService {
    @Autowired
    private LichLamViecReponsitory lichLamViecReponsitory;
    @Autowired
    private DonViReponsitory donViReponsitory;
    @Autowired
    private NguoiDungReponsitory nguoiDungReponsitory;
    @Autowired
    private ThongBaoService thongBaoService;
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private DonViService donViService;
    @Autowired
    private ModelMapper modelMapper;

    public LichLamViec save(LichLamViec lichLamViec){
        return lichLamViecReponsitory.save(lichLamViec);
    }
    public LichLamViec update(LichLamViec lichLamViec){
        return lichLamViecReponsitory.save(lichLamViec);
    }
    public List<LichLamViec> getByTaiKhoanID(ObjectId taiKhoan){

        List<LichLamViec> lichLamViecList = lichLamViecReponsitory.getByIDTaiKhoan(taiKhoan);
        if (lichLamViecList == null) {
            throw new RuntimeException("Hiện tại tài khoản này không có lịch làm việc nào trên hệ thống.");
        }
        return lichLamViecList;
    }

    public List<LichLamViec> getByIdDonVi(ObjectId donVi){

        List<LichLamViec> lichLamViecList = lichLamViecReponsitory.getByIdDonVi(donVi);
        if (lichLamViecList == null) {
            throw new RuntimeException("Hiện tại đơn vị này không có lịch làm việc nào trên hệ thống.");
        }
        return lichLamViecList;
    }


    public LichLamViec getById(String id){

        LichLamViec lichLamViec = lichLamViecReponsitory.getByID(id);
        if (lichLamViec == null) {
            throw new RuntimeException("Làm việc này không tồn tại trên hệ thống.");
        }
        return lichLamViec;
    }
    public List<LichLamViec>findAll(){

        List<LichLamViec> lichLamViecList = lichLamViecReponsitory.findAll();
        if (lichLamViecList == null) {
            throw new RuntimeException("Hiện tại không có lịch làm việc nào trên hệ thống.");
        }
        return lichLamViecList;
    }
    public void deleteByID(String id){
        getById(id);
        lichLamViecReponsitory.deleteById(new ObjectId(id));
    }
    public void deleteByNguoiDungID(ObjectId id){
        taiKhoanService.findById(id);
        lichLamViecReponsitory.deleteByNguoiDungID(id);
    }

    public List<LichLamViecDTO> search(String tuKhoa){
        String regexKeyword = RegexUtils.convertToRegex(tuKhoa);

        List<DonVi> donViList = donViService.search(tuKhoa);
        List<ObjectId> donViIds = donViList.stream().map(DonVi::get_id).collect(Collectors.toList());

        List<TaiKhoanNguoiDungDTO> taiKhoanNguoiDungDTOList = taiKhoanService.searchTaiKhoan(tuKhoa);
        List<ObjectId> taiKhoanIds = taiKhoanNguoiDungDTOList.stream().map(TaiKhoanNguoiDungDTO::get_id).collect(Collectors.toList());


        List<LichLamViec> lichLamViecList = lichLamViecReponsitory.searchLich(taiKhoanIds,donViIds,regexKeyword);
        List<LichLamViecDTO> llvList = new ArrayList<>();
        for (LichLamViec llv: lichLamViecList) {
            LichLamViecDTO lichLamViecDTOList = modelMapper.map(llv,LichLamViecDTO.class);
            if (llv.getTaiKhoan()!= null){
                lichLamViecDTOList.setTaiKhoan(llv.getTaiKhoan().get_id());
            }else {
                lichLamViecDTOList.setDonVi(llv.getDonVi().get_id());
            }
            llvList.add(lichLamViecDTOList);
        }
        return llvList;
    }

    public void kiemTraThoiGianHopLe(LichLamViec lichLamViec) {
        LocalDateTime thoiGianBD = lichLamViec.getThoiGianBD();
        LocalDateTime thoiGianKT = lichLamViec.getThoiGianKT();

        LocalDateTime now = LocalDateTime.now();

        if (thoiGianBD.isBefore(now)) {
            throw new RuntimeException("Thời gian bắt đầu không được nằm trong quá khứ.");
        } else if (thoiGianBD.isAfter(thoiGianKT) || thoiGianBD.isEqual(thoiGianKT)) {
            throw new RuntimeException("Vui lòng nhập đúng thời gian. Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc.");
        }else {

        }

//        List<LichLamViec> lichDaTonTai = lichLamViecReponsitory.getByIDTaiKhoan(taiKhoan.get_id());
//        for (LichLamViec lich : lichDaTonTai) {
//            LocalDateTime tgbd = lich.getThoiGianBD();
//            LocalDateTime tgkt = lich.getThoiGianKT();
//
//            if ((thoiGianBD.isBefore(tgkt) && thoiGianKT.isAfter(tgbd)) ||
//                    (thoiGianBD.isEqual(tgbd) || thoiGianKT.isEqual(tgkt))) {
//                return new Error("400", "Lỗi: Thời gian đã trùng với lịch làm việc khác.");
//            }
//        }
//        return Error;
    }

    public LichLamViec themLichKemThongBao(LichLamViec lichLamViec) {
        LichLamViec llv = lichLamViecReponsitory.save(lichLamViec);

        if (llv.getTaiKhoan() == null) {
            // Lấy danh sách tài khoản trong đơn vị của lịch làm việc
            List<TaiKhoan> taiKhoanList = taiKhoanService.getByDonViID(llv.getDonVi().get_id());

            for (TaiKhoan taiKhoan : taiKhoanList) {
                ThongBao thongBao = new ThongBao();
                thongBao.setNoiDung(llv.getTieuDe());
                thongBao.setThoiGian(llv.getThoiGianBD().minusMinutes(10));
                thongBao.setLichLamViec(llv);
                thongBao.setTaiKhoan(taiKhoan);
                thongBaoService.save(thongBao);
            }
        }
        else {
            ThongBao thongBao = new ThongBao();
            thongBao.setNoiDung(llv.getTieuDe());
            thongBao.setThoiGian(llv.getThoiGianBD().minusMinutes(10));
            thongBao.setLichLamViec(llv);
            thongBao.setTaiKhoan(llv.getTaiKhoan());
            thongBaoService.save(thongBao);
        }

        return llv;
    }

    public List<LichDonViTaiKhoanDTO> getLichTaiKhoanOfDonViQuanLy(HttpServletRequest httpServletRequest){
        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);
        DonVi donVi = donViService.getById(taiKhoan.getDonVi().get_id().toHexString());

        List<TaiKhoan> taiKhoanList = taiKhoanService.getByDonViID(donVi.get_id());

        // Lấy danh sách lịch làm việc của từng tài khoản trong đơn vị
        List<LichDonViTaiKhoanDTO> lichDonViTaiKhoanDTOList = new ArrayList<>();
        for (TaiKhoan tk : taiKhoanList) {

            List<LichLamViec> lichLamViecCuaTaiKhoan = getByTaiKhoanID(tk.get_id());

            LichDonViTaiKhoanDTO llv = new LichDonViTaiKhoanDTO();
            llv.setTaiKhoan(tk);
            llv.setLichLamViecList(lichLamViecCuaTaiKhoan);

            lichDonViTaiKhoanDTOList.add(llv);

        }
        return lichDonViTaiKhoanDTOList;
    }

    public List<LichLamViecDonViDTO> getLichDonVi(HttpServletRequest httpServletRequest){
        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);
        DonVi donVi = donViService.getById(taiKhoan.getDonVi().get_id().toHexString());

        List<LichLamViec> lichDonViList = getByIdDonVi(donVi.get_id());
        List<LichLamViecDonViDTO> lichLamViecDTOList = new ArrayList<>();
        for (LichLamViec llv: lichDonViList){
            LichLamViecDonViDTO lichLamViecDonViDTO = modelMapper.map(llv, LichLamViecDonViDTO.class);
            lichLamViecDonViDTO.setDonVi(taiKhoan.getDonVi().get_id());
            lichLamViecDTOList.add(lichLamViecDonViDTO);
        }
        return lichLamViecDTOList;
    }


    public List<LichCaNhanDTO> getLichTaiKhoan(HttpServletRequest httpServletRequest){
        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);

        List<LichLamViec> lichTaiKhoanList = getByTaiKhoanID(taiKhoan.get_id());
        List<LichCaNhanDTO> lichCaNhanDTOList = new ArrayList<>();
        for (LichLamViec llv: lichTaiKhoanList){
            LichCaNhanDTO lichCaNhanDTO = modelMapper.map(llv, LichCaNhanDTO.class);
            lichCaNhanDTO.setTaiKhoan(taiKhoan.get_id());
            lichCaNhanDTOList.add(lichCaNhanDTO);
        }
        return lichCaNhanDTOList;

    }

    public ResponseEntity<Object> taoLich(LichLamViecDTO lichLamViecDTO, HttpServletRequest httpRequest){

        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpRequest);

        LichLamViec lichLamViec = new LichLamViec();
        lichLamViec.setThoiGianBD(lichLamViecDTO.getThoiGianBD());
        lichLamViec.setThoiGianKT(lichLamViecDTO.getThoiGianKT());
        lichLamViec.setThoiGianTao(LocalDateTime.now());
        lichLamViec.setDiaDiem(lichLamViecDTO.getDiaDiem());
        lichLamViec.setNoiDung(lichLamViecDTO.getNoiDung());
        lichLamViec.setTieuDe(lichLamViecDTO.getTieuDe());
        lichLamViec.setGhiChu(lichLamViecDTO.getGhiChu());

        // Check thời gian bắt đầu và kết thục lịch
        kiemTraThoiGianHopLe(lichLamViec);

        if(lichLamViecDTO.getDonVi() == null){
            lichLamViec.setTaiKhoan(taiKhoan);

        }else {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            DonVi donVi = donViService.getById(lichLamViecDTO.getDonVi().toHexString());

            if(taiKhoan.getDonVi().equals(donVi) && taiKhoanService.kiemTraUserAdmin(authentication) == true){
                lichLamViec.setDonVi(donVi);
            }else {
                return new ResponseEntity<>("Bạn không có quền quản lý đơn vị này nên không thể thêm lịch cho đơn vị: " +donVi.getTenDonVi(), HttpStatus.UNAUTHORIZED);
            }

        }

        LichLamViec lichLamViecDaTao = themLichKemThongBao(lichLamViec);

        return new ResponseEntity<>("Thêm lịch làm việc thành công!",HttpStatus.OK);

    }

    public ResponseEntity<Object> editLich(LichLamViecDTO lichLamViecDTO, String idLich, HttpServletRequest httpServletRequest){

        LichLamViec lichLamViec = getById(idLich);

        lichLamViec.setThoiGianBD(lichLamViecDTO.getThoiGianBD());
        lichLamViec.setThoiGianKT(lichLamViecDTO.getThoiGianKT());
        lichLamViec.setDiaDiem(lichLamViecDTO.getDiaDiem());
        lichLamViec.setNoiDung(lichLamViecDTO.getNoiDung());
        lichLamViec.setTieuDe(lichLamViecDTO.getTieuDe());
        lichLamViec.setGhiChu(lichLamViecDTO.getGhiChu());
        lichLamViec.setThoiGianTao(LocalDateTime.now());

        // Check thời gian bắt đầu và kết thục lịch
        kiemTraThoiGianHopLe(lichLamViec);

        save(lichLamViec);

        // Cập nhật lại thông báo
        List<ThongBao> thongBaoList = thongBaoService.getByIdLich(lichLamViec.get_id());
        for (ThongBao tb: thongBaoList){
            tb.setNoiDung(lichLamViec.getTieuDe());
            tb.setThoiGian(lichLamViec.getThoiGianBD().minusMinutes(10));
            thongBaoService.save(tb);
        }

        return new  ResponseEntity<>("Cập nhật lịch thành công", HttpStatus.OK);

    }

    public ResponseEntity<Object> editLichCaNhan(LichLamViecDTO lichLamViecDTO, String idLich, HttpServletRequest httpServletRequest) {

        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);

        LichLamViec llv = getById(idLich);

        String tk =  llv.getTaiKhoan().get_id().toHexString();
        String tk2 = taiKhoan.get_id().toHexString();

        if (tk2.equals(tk)) {
            editLich(lichLamViecDTO, idLich, httpServletRequest);

        } else {
            return new ResponseEntity<>("Đây là lịch cá nhân bạn không có quyền chỉnh sữa", HttpStatus.BAD_REQUEST);
        }
        return new  ResponseEntity<>("Cập nhật lịch thành công", HttpStatus.OK);

    }

    public ResponseEntity<Object> deleteLich(String id,HttpServletRequest httpRequest){
        TaiKhoan tk = taiKhoanService.getTaiKhoanFromRequest(httpRequest);
        LichLamViec lichLamViec = getById(id);

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
            deleteByID(id);
            thongBaoService.deleteByLich(id);
            return new ResponseEntity<>("Xoá thành công lịch làm việc với ID:"+id, HttpStatus.FORBIDDEN);
        }else {

            if (lichLamViec.getDonVi() != null || lichLamViec.getTaiKhoan().get_id().toString().equals(tk.get_id().toString())) {

                deleteByID(id);
                thongBaoService.deleteByLich(id);
            }
            return new ResponseEntity<>("Xoá thành công lịch làm việc với ID: "+id, HttpStatus.OK);
        }

    }


}
