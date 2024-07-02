package com.example.qllichlamviec.service;

import com.example.qllichlamviec.reponsitory.QuyenReponsitory;
import com.example.qllichlamviec.util.Quyen;
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
        return quyenReponsitory.getByID(id);
    }
    public List<Quyen> findAll(){
        return quyenReponsitory.findAll();
    }
    public void deleteByID(String id){
        quyenReponsitory.deleteById(new ObjectId(id));
    }
}
