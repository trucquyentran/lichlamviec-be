package com.example.qllichlamviec.service;

import com.example.qllichlamviec.modal.dto.LichCaNhanDTO;
import com.example.qllichlamviec.modal.dto.LichDonViTaiKhoanDTO;
import com.example.qllichlamviec.modal.dto.LichLamViecDTO;
import com.example.qllichlamviec.modal.dto.LichLamViecDonViDTO;
import com.example.qllichlamviec.modal.system.Error;
import com.example.qllichlamviec.modal.system.NguoiDungDangNhapDTO;
import com.example.qllichlamviec.reponsitory.DonViReponsitory;
import com.example.qllichlamviec.reponsitory.LichLamViecReponsitory;
import com.example.qllichlamviec.reponsitory.NguoiDungReponsitory;
import com.example.qllichlamviec.util.*;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpClient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public List<LichLamViec> search(String tuKhoa){
        return lichLamViecReponsitory.searchLich(tuKhoa);
    }

    public Error kiemTraThoiGianHopLe(LichLamViec lichLamViec) {
        LocalDateTime thoiGianBD = lichLamViec.getThoiGianBD();
        LocalDateTime thoiGianKT = lichLamViec.getThoiGianKT();

        LocalDateTime now = LocalDateTime.now();

        if (thoiGianBD.isBefore(now)) {
            return new Error("400", "Lỗi: Thời gian bắt đầu không được nằm trong quá khứ.");
        }

        if (thoiGianBD.isAfter(thoiGianKT) || thoiGianBD.isEqual(thoiGianKT)) {
            return new Error("400", "Lỗi: Vui lòng nhập đúng thời gian. Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc.");
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
        return null;
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
        Error error = kiemTraThoiGianHopLe(lichLamViec);
        if (error != null){
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }

        if(lichLamViecDTO.getDonVi() == null){
            lichLamViec.setTaiKhoan(taiKhoan);
        }else {

            DonVi donVi = donViService.getById(lichLamViecDTO.getDonVi().toHexString());
            if(taiKhoan.getDonVi().equals(donVi)){
                lichLamViec.setDonVi(donVi);
            }else {
                return new ResponseEntity<>("Bạn không có quền quản lý đơn vị này nên không thể thêm lịch cho đơn vị: " +donVi.getTenDonVi(), HttpStatus.UNAUTHORIZED);
            }

        }

        LichLamViec lichLamViecDaTao = themLichKemThongBao(lichLamViec);

        return ResponseEntity.ok(lichLamViecDaTao);

    }

}
