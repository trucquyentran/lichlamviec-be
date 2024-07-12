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
import java.util.List;

@Service
public class LichLamViecService {
    @Autowired
    private LichLamViecReponsitory lichLamViecReponsitory;
    @Autowired
    private DonViReponsitory donViReponsitory;
    @Autowired
    private NguoiDungReponsitory nguoiDungReponsitory;

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
}
