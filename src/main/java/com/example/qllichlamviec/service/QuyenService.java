package com.example.qllichlamviec.service;

import com.example.qllichlamviec.reponsitory.QuyenReponsitory;
import com.example.qllichlamviec.util.Quyen;
import com.example.qllichlamviec.util.pojo.XuLyDauChuoiTimKiem;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuyenService {
    @Autowired
    private QuyenReponsitory quyenReponsitory;

    public Quyen save(Quyen Quyen){
        return quyenReponsitory.save(Quyen);
    }
    public Quyen upadate(Quyen Quyen){
        return quyenReponsitory.save(Quyen);
    }
    public Quyen getById(String id){
        Quyen quyen = quyenReponsitory.getByID(id);
        if (quyen == null){
            throw new RuntimeException("Không tìm thấy quyền này trong dữ liệu hệ thống, vui lòng kiểm tra lại.");
        }
        return quyen;
    }
    public List<Quyen> findAll(){
        List<Quyen> quyen = quyenReponsitory.findAll();
        if (quyen == null){
            throw new RuntimeException("Hiện tại không có quyền nào trong dữ liệu hệ thông, vui lòng kiểm tra lại.");
        }
        return quyen;
    }
    public List<Quyen> searchTen(String keyword){
        String regexKeyword = XuLyDauChuoiTimKiem.convertToRegex(keyword);
        return quyenReponsitory.getByTen(regexKeyword);
    }
    public void deleteByID(String id){
        quyenReponsitory.deleteById(new ObjectId(id));
    }
}
