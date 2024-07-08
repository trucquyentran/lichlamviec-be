package com.example.qllichlamviec.service;

import com.example.qllichlamviec.reponsitory.DonViReponsitory;
import com.example.qllichlamviec.reponsitory.LichLamViecReponsitory;
import com.example.qllichlamviec.reponsitory.NguoiDungReponsitory;
import com.example.qllichlamviec.util.LichLamViec;
import com.example.qllichlamviec.util.NguoiDung;
import com.example.qllichlamviec.util.QuyenTaiKhoan;
import com.example.qllichlamviec.util.TaiKhoan;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public NguoiDung getByNguoiDungID(String nguoiDung){
        return lichLamViecReponsitory.getByIDNguoiDung(nguoiDung);
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

//    public LichLamViec khoiTaoLichLV(LichLamViec lichLamViec) {
//        LichLamViec llv = lichLamViecReponsitory.save(lichLamViec.getThoiGianBD());
//
////        lichLamViec.setNguoiDung(ngDungRS);
////        lichLamViec.setQuyenTaiKhoanList(null);
////        lichLamViec.setPassword(passwordEncoder.encode(lichLamViec.getPassword()));
////        TaiKhoan taiKhoanRs = taiKhoanReponsitory.save(lichLamViec);
//
//    }
}
