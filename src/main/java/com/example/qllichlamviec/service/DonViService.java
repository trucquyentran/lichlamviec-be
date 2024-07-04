package com.example.qllichlamviec.service;

import com.example.qllichlamviec.reponsitory.DonViReponsitory;
import com.example.qllichlamviec.util.DonVi;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonViService {
    @Autowired
    private DonViReponsitory donViReponsitory;
    public DonVi save(DonVi donVi){
        return donViReponsitory.save(donVi);
    }
    public DonVi update(DonVi donVi){
        return donViReponsitory.save(donVi);
    }
    public DonVi getById(String id){
        return donViReponsitory.getByID(id);
    }

    public DonVi getById2(ObjectId id) {
        return donViReponsitory.findById(id).orElse(null);
    }


    public List<DonVi> findAll(){
        return donViReponsitory.findAll();
    }
    public void deleteByID(String id){
        donViReponsitory.deleteById(new ObjectId(id));
    }

}
