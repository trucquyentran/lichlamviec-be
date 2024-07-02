package com.example.qllichlamviec.service;

import com.example.qllichlamviec.reponsitory.QuyenTaiKhoanReponsitory;
import com.example.qllichlamviec.util.QuyenTaiKhoan;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuyenTaiKhoanService {
    @Autowired
    private QuyenTaiKhoanReponsitory quyenTaiKhoanReponsitory;

    public QuyenTaiKhoan save(QuyenTaiKhoan QuyenTaiKhoan){
        return quyenTaiKhoanReponsitory.save(QuyenTaiKhoan);
    }
    public QuyenTaiKhoan update(QuyenTaiKhoan QuyenTaiKhoan){
        return quyenTaiKhoanReponsitory.save(QuyenTaiKhoan);
    }
    public List<QuyenTaiKhoan> getByTaiKhoanID(String taiKhoan){
        return quyenTaiKhoanReponsitory.getByTaiKhoanID(taiKhoan);
    }
    public QuyenTaiKhoan getById(String id){
        return quyenTaiKhoanReponsitory.getByID(id);
    }
    public List<QuyenTaiKhoan> findAll(){
        return quyenTaiKhoanReponsitory.findAll();
    }
    public void deleteByID(String id){
        quyenTaiKhoanReponsitory.deleteById(new ObjectId(id));
    }
    public void deleteByTaiKhoan(ObjectId id){
        quyenTaiKhoanReponsitory.deleteByTaiKhoanID(id);
    }
}
