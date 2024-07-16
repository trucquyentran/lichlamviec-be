package com.example.qllichlamviec.service;

import com.example.qllichlamviec.modal.system.Error;
import com.example.qllichlamviec.reponsitory.DonViReponsitory;
import com.example.qllichlamviec.reponsitory.LichLamViecReponsitory;
import com.example.qllichlamviec.reponsitory.NguoiDungReponsitory;
import com.example.qllichlamviec.util.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public LichLamViec save(LichLamViec lichLamViec){
        return lichLamViecReponsitory.save(lichLamViec);
    }
    public LichLamViec update(LichLamViec lichLamViec){
        return lichLamViecReponsitory.save(lichLamViec);
    }
    public List<LichLamViec> getByTaiKhoanID(ObjectId taiKhoan){
        return lichLamViecReponsitory.getByIDTaiKhoan(taiKhoan);
    }

    public List<LichLamViec> getByIdDonVi(ObjectId donVi){
        return lichLamViecReponsitory.getByIdDonVi(donVi);
    }

    public LichLamViec getById(String id){
        return lichLamViecReponsitory.getByID(id);
    }
    public List<LichLamViec>findAll(){
        return lichLamViecReponsitory.findAll();
    }
    public void deleteByID(String id){
        lichLamViecReponsitory.deleteById(new ObjectId(id));
    }
    public void deleteByNguoiDungID(ObjectId id){
        lichLamViecReponsitory.deleteByNguoiDungID(id);
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

}
