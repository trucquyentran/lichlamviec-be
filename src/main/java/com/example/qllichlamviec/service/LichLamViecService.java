package com.example.qllichlamviec.service;

import com.example.qllichlamviec.modal.dto.*;
import com.example.qllichlamviec.modal.system.TaiKhoanNguoiDungDTO;
import com.example.qllichlamviec.reponsitory.DonViReponsitory;
import com.example.qllichlamviec.reponsitory.LichLamViecReponsitory;
import com.example.qllichlamviec.reponsitory.NguoiDungReponsitory;
import com.example.qllichlamviec.util.*;
import com.example.qllichlamviec.util.pojo.XuLyDauChuoiTimKiem;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
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

    public ResponseEntity<Object> getChiTietLich (String search, HttpServletRequest httpServletRequest){
        if (Pattern.compile("^[0-9a-fA-F]{24}$").matcher(search).matches()==true) {
            try {
            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);
            LichLamViec lichLamViec = lichLamViecReponsitory.getByID(search);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Kiểm tra quyền truy cập
            boolean hasAccess = (lichLamViec.getTaiKhoan() != null && taiKhoan.get_id().equals(lichLamViec.getTaiKhoan().get_id())) ||
                    (lichLamViec.getDonVi() != null && taiKhoan.getDonVi() != null && taiKhoan.getDonVi().get_id().equals(lichLamViec.getDonVi().get_id())) ||
                    taiKhoanService.kiemTraUserAdmin(authentication);

            if (hasAccess) {
                LichLamViec llv = getById(search);

                LichLamViecHienThiDTO lichLamViecHienThiDTO = modelMapper.map(llv, LichLamViecHienThiDTO.class);
//                if (llv.getTaiKhoan() != null) {
//                    lichLamViecDTO.setTaiKhoan(llv.getTaiKhoan().get_id());
//                } else {
//                    lichLamViecDTO.setDonVi(llv.getDonVi().get_id());
//                }
                return new ResponseEntity<>(lichLamViecHienThiDTO, HttpStatus.OK);
            } else {
                throw new RuntimeException("Lịch này không thuộc quyền quản lý của bạn");
            }
        }catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Trả về Forbidden nếu người dùng không có quyền
            }

        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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

    public ResponseEntity<Object> getLichByNguoiDung(ObjectId taiKhoanId){
        List<LichLamViec> llvList = getByTaiKhoanID(taiKhoanId);
        List<LichLamViecHienThiDTO> lichLamViecHienThiDTOList = new ArrayList<>();
        for (LichLamViec lichLamViec: llvList) {
            LichLamViecHienThiDTO lichLamViecList = modelMapper.map(lichLamViec, LichLamViecHienThiDTO.class);

            // Ánh xạ tài khoản và quyền của tài khoản
            NguoiDungDTO nguoiDungDTO = modelMapper.map(lichLamViec.getTaiKhoan(), NguoiDungDTO.class);

            // Khởi tạo danh sách quyền và ánh xạ quyền từ QuyenTaiKhoan
            List<QuyenTaiKhoanDTO> quyenList = new ArrayList<>();
            for (QuyenTaiKhoan qtk : lichLamViec.getTaiKhoan().getQuyenTaiKhoanList()) {
                QuyenTaiKhoanDTO quyenTaiKhoanDTO = modelMapper.map(qtk, QuyenTaiKhoanDTO.class);
                quyenList.add(quyenTaiKhoanDTO);
            }
            nguoiDungDTO.setQuyenList(quyenList);
            lichLamViecList.setTaiKhoan(nguoiDungDTO);


            lichLamViecHienThiDTOList.add(lichLamViecList);

        }
        return new ResponseEntity<>(lichLamViecHienThiDTOList, HttpStatus.OK);
    }

    public List<LichLamViecHienThiDTO> search(String tuKhoa){
        String regexKeyword = XuLyDauChuoiTimKiem.convertToRegex(tuKhoa);

        List<DonVi> donViList = donViService.search(tuKhoa);
        List<ObjectId> donViIds = donViList.stream().map(DonVi::get_id).collect(Collectors.toList());

        List<TaiKhoanNguoiDungDTO> taiKhoanNguoiDungDTOList = taiKhoanService.searchTaiKhoan(tuKhoa);
        List<ObjectId> taiKhoanIds = taiKhoanNguoiDungDTOList.stream().map(TaiKhoanNguoiDungDTO::get_id).collect(Collectors.toList());


        List<LichLamViec> lichLamViecList = lichLamViecReponsitory.searchLich(taiKhoanIds,donViIds,regexKeyword);
        List<LichLamViecHienThiDTO> llvList = new ArrayList<>();
        for (LichLamViec llv: lichLamViecList) {
            LichLamViecHienThiDTO lichLamViecHienThiDTOList = modelMapper.map(llv, LichLamViecHienThiDTO.class);
            llvList.add(lichLamViecHienThiDTOList);
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
            List<LichLamViecHienThiDTO> lichLamViecHienThiDTOList = new ArrayList<>();
            for (LichLamViec lich: lichLamViecCuaTaiKhoan){
                LichLamViecHienThiDTO lichLamViecHienThiDTO = modelMapper.map(lich, LichLamViecHienThiDTO.class);
                lichLamViecHienThiDTOList.add(lichLamViecHienThiDTO);

            }
            TaiKhoanNguoiDungDTO taiKhoan1 = modelMapper.map(tk,TaiKhoanNguoiDungDTO.class);

            LichDonViTaiKhoanDTO llv = new LichDonViTaiKhoanDTO();

            llv.setTaiKhoan(taiKhoan1);
            llv.setLichLamViecList(lichLamViecHienThiDTOList);

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
//            lichLamViecDonViDTO.setDonVi(taiKhoan.getDonVi().get_id());
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpRequest);

        LichLamViec lichLamViec = new LichLamViec();
        lichLamViec.setThoiGianBD(lichLamViecDTO.getThoiGianBD());
        lichLamViec.setThoiGianKT(lichLamViecDTO.getThoiGianKT());
        lichLamViec.setThoiGianTao(LocalDateTime.now());
        lichLamViec.setDiaDiem(lichLamViecDTO.getDiaDiem());
        lichLamViec.setNoiDung(lichLamViecDTO.getNoiDung());
        lichLamViec.setTieuDe(lichLamViecDTO.getTieuDe());
        lichLamViec.setGhiChu(lichLamViecDTO.getGhiChu());
        lichLamViec.setBg(lichLamViecDTO.getBg());

        //  Check thời gian bắt đầu và kết thục lịch
        kiemTraThoiGianHopLe(lichLamViec);

        boolean isManager = taiKhoanService.kiemTraManager(authentication);
        boolean isAdmin = taiKhoanService.kiemTraUserAdmin(authentication);

        //  Them lich ca nhan
        if(lichLamViecDTO.getDonVi() == null && lichLamViecDTO.getTaiKhoan() == null){
            lichLamViec.setTaiKhoan(taiKhoan);

        }

        //  Them lich cho nhan vien
        if (lichLamViecDTO.getDonVi() == null && lichLamViecDTO.getTaiKhoan() != null && isManager || lichLamViecDTO.getDonVi() == null && lichLamViecDTO.getTaiKhoan() != null  && isAdmin){
            TaiKhoan tk = taiKhoanService.getByID(lichLamViecDTO.getTaiKhoan().toHexString());

            String donViTaiKhoan = taiKhoan.getDonVi().get_id().toHexString();
            String donViNhanVien = tk.getDonVi().get_id().toHexString();
            String donViTrucThuocNV = tk.getDonVi().getDonViTrucThuoc().get_id().toHexString();

            if (isManager || isAdmin) {
                if (isManager && (!donViTaiKhoan.equals(donViNhanVien) )&& (!donViTaiKhoan.equals(donViTrucThuocNV))) {
                    return new ResponseEntity<>("Bạn không thể thêm lịch cho nhân viên này vì bạn không có quyền quản lý đơn vị: " + tk.getDonVi().getTenDonVi(), HttpStatus.UNAUTHORIZED);
                }
                lichLamViec.setTaiKhoan(tk);
            } else {
                return new ResponseEntity<>("Bạn không có quyền thực hiện hành động này.", HttpStatus.UNAUTHORIZED);
            }

        }

        //  Them lich cho don vi
        if (lichLamViecDTO.getDonVi() != null && lichLamViecDTO.getTaiKhoan() == null){
        //  Check quyen quan ly (phai la Admim va cung don vi voi don vi duoc them lich)
            DonVi donVi = donViService.getById(lichLamViecDTO.getDonVi().get_id().toHexString());
            DonVi donViTrucThuoc = donVi.getDonViTrucThuoc();
            List<TaiKhoan> taiKhoanList = taiKhoanService.getByDonViID(donVi.get_id());

            if (isManager || isAdmin){
                if(isManager && !taiKhoan.getDonVi().equals(donVi) && !taiKhoan.getDonVi().equals(donViTrucThuoc)){
                    return new ResponseEntity<>("Bạn không có quền quản lý đơn vị này nên không thể thêm lịch cho đơn vị: " +donVi.getTenDonVi(), HttpStatus.UNAUTHORIZED);
                }else {
                    if (taiKhoanList == null){
                        return new ResponseEntity<>("Hiện tại đơn vị " +donVi.getTenDonVi()+" chưa có thành viên nào nên không thể tạo lịch", HttpStatus.UNAUTHORIZED);
                    }else {
                        lichLamViec.setDonVi(donVi);
                    }
                }
            }

        }

        LichLamViec lichLamViecDaTao = themLichKemThongBao(lichLamViec);

        return new ResponseEntity<>("Thêm lịch làm việc thành công!",HttpStatus.OK);

    }

    public ResponseEntity<Object> editLich(LichLamViecHienThiDTO lichLamViecHienThiDTO, String idLich, HttpServletRequest httpServletRequest){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);

//        boolean isManager = taiKhoanService.kiemTraManager(authentication);

        LichLamViec lichLamViec = getById(idLich);

        lichLamViec.setThoiGianBD(lichLamViecHienThiDTO.getThoiGianBD());
        lichLamViec.setThoiGianKT(lichLamViecHienThiDTO.getThoiGianKT());
        lichLamViec.setDiaDiem(lichLamViecHienThiDTO.getDiaDiem());
        lichLamViec.setNoiDung(lichLamViecHienThiDTO.getNoiDung());
        lichLamViec.setTieuDe(lichLamViecHienThiDTO.getTieuDe());
        lichLamViec.setGhiChu(lichLamViecHienThiDTO.getGhiChu());

        lichLamViec.setBg(lichLamViecHienThiDTO.getBg());

        // Check thời gian bắt đầu và kết thục lịch
        kiemTraThoiGianHopLe(lichLamViec);



//        //  Cap nhat lich nhan vien
//        if (lichLamViec.getDonVi() == null && lichLamViec.getTaiKhoan() != null) {
//
//            TaiKhoan tk = taiKhoanService.getByID(lichLamViec.getTaiKhoan().get_id().toHexString());
//            DonVi donVi = donViService.getById(lichLamViec.getDonVi().get_id().toHexString());
//
//
//            String donViTaiKhoan = taiKhoan.getDonVi().get_id().toHexString();
//            String donViNhanVien = tk.getDonVi().get_id().toHexString();
//            String donViTrucThuocNV = tk.getDonVi().getDonViTrucThuoc().get_id().toHexString();
//            if (isManager && (!donViTaiKhoan.equals(donViNhanVien) )&& (!donViTaiKhoan.equals(donViTrucThuocNV))) {
//                return new ResponseEntity<>("Bạn không thể thêm lịch cho nhân viên này vì bạn không có quyền quản lý đơn vị: " + tk.getDonVi().getTenDonVi(), HttpStatus.UNAUTHORIZED);
//            }
//            save(lichLamViec);
//        }
//
//        // Cap nhat lich don vi
//        if (lichLamViec.getDonVi() != null && lichLamViec.getTaiKhoan() == null){
//            //  Check quyen quan ly (phai la Admim va cung don vi voi don vi duoc them lich)
//            DonVi donVi = donViService.getById(lichLamViec.getDonVi().get_id().toHexString());
//            DonVi donViTrucThuoc = donVi.getDonViTrucThuoc();
//            List<TaiKhoan> taiKhoanList = taiKhoanService.getByDonViID(donVi.get_id());
//
//            if(isManager && !taiKhoan.getDonVi().equals(donVi) && !taiKhoan.getDonVi().equals(donViTrucThuoc)){
//                return new ResponseEntity<>("Bạn không có quền quản lý đơn vị này nên không thể thêm lịch cho đơn vị: " +donVi.getTenDonVi(), HttpStatus.UNAUTHORIZED);
//            }else {
//                //  Check don vi co nhan vien nao chua
//                if (taiKhoanList == null){
//                    return new ResponseEntity<>("Hiện tại đơn vị " +donVi.getTenDonVi()+" chưa có thành viên nào nên không thể tạo lịch", HttpStatus.UNAUTHORIZED);
//                }else {
//                    save(lichLamViec);
//                }
//            }
//
//        }

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



    public ResponseEntity<Object> editLichCaNhan(LichLamViecHienThiDTO lichLamViecHienThiDTO, String idLich, HttpServletRequest httpServletRequest) {

        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);

        LichLamViec llv = getById(idLich);

        String tk =  llv.getTaiKhoan().get_id().toHexString();
        String tk2 = taiKhoan.get_id().toHexString();

        if (tk2.equals(tk)) {
            editLich(lichLamViecHienThiDTO, idLich, httpServletRequest);

        } else {
            return new ResponseEntity<>("Đây là lịch cá nhân bạn không có quyền chỉnh sữa", HttpStatus.BAD_REQUEST);
        }
        return new  ResponseEntity<>("Cập nhật lịch thành công", HttpStatus.OK);

    }

    public ResponseEntity<Object> editLichDonViOrNhanVien(LichLamViecHienThiDTO lichLamViecHienThiDTO, String idLich, HttpServletRequest httpServletRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isManager = taiKhoanService.kiemTraManager(authentication);

        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);

        LichLamViec llv = getById(idLich);

        //  Cap nhat lich nhan vien
        if (llv.getDonVi() == null && llv.getTaiKhoan() != null) {

            TaiKhoan tk = taiKhoanService.getByID(llv.getTaiKhoan().get_id().toHexString());
            DonVi donVi = donViService.getById(llv.getDonVi().get_id().toHexString());


            String donViTaiKhoan = taiKhoan.getDonVi().get_id().toHexString();
            String donViNhanVien = tk.getDonVi().get_id().toHexString();
            String donViTrucThuocNV = tk.getDonVi().getDonViTrucThuoc().get_id().toHexString();
            if (isManager && (!donViTaiKhoan.equals(donViNhanVien) )&& (!donViTaiKhoan.equals(donViTrucThuocNV))) {
                return new ResponseEntity<>("Bạn không thể thêm lịch cho nhân viên này vì bạn không có quyền quản lý đơn vị: " + tk.getDonVi().getTenDonVi(), HttpStatus.UNAUTHORIZED);
            }
            editLich(lichLamViecHienThiDTO, idLich,httpServletRequest);
        }

        // Cap nhat lich don vi
        if (llv.getDonVi() != null && llv.getTaiKhoan() == null){
            //  Check quyen quan ly (phai la Admim va cung don vi voi don vi duoc them lich)
            DonVi donVi = donViService.getById(llv.getDonVi().get_id().toHexString());
            DonVi donViTrucThuoc = donVi.getDonViTrucThuoc();
            List<TaiKhoan> taiKhoanList = taiKhoanService.getByDonViID(donVi.get_id());

            if(isManager && !taiKhoan.getDonVi().equals(donVi) && !taiKhoan.getDonVi().equals(donViTrucThuoc)){
                return new ResponseEntity<>("Bạn không có quền quản lý đơn vị này nên không thể thêm lịch cho đơn vị: " +donVi.getTenDonVi(), HttpStatus.UNAUTHORIZED);
            }else {
                //  Check don vi co nhan vien nao chua
                if (taiKhoanList == null){
                    return new ResponseEntity<>("Hiện tại đơn vị " +donVi.getTenDonVi()+" chưa có thành viên nào nên không thể tạo lịch", HttpStatus.UNAUTHORIZED);
                }else {
                    editLich(lichLamViecHienThiDTO, idLich,httpServletRequest);
                }
            }

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
