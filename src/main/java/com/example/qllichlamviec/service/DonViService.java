package com.example.qllichlamviec.service;

import com.example.qllichlamviec.modal.dto.DonViDTO;
import com.example.qllichlamviec.reponsitory.DonViReponsitory;
import com.example.qllichlamviec.util.DonVi;
import com.example.qllichlamviec.util.TaiKhoan;
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

        DonVi donVi = donViReponsitory.getByID(id);
        if (donVi == null){
            throw new RuntimeException("Không tìm thấy tài khoản nào thuộc nhóm quyền này.");
        }
        return donVi;
    }

    public DonVi getById2(ObjectId id) {
        DonVi donVi = donViReponsitory.findById(id).orElse(null);
        if (donVi == null) {
            throw new RuntimeException("Đơn vị không tồn tại trong dữ liệu hệ thống, vui lòng kiểm tra lại.");
        }
        return donVi;
    }

    public List<DonVi> findAll(){
        List<DonVi> donVi = donViReponsitory.findAll();
        if (donVi == null) {
            throw new RuntimeException("Hiện tại không có đơn vị nào trong hệ thống.");
        }
        return donVi;
    }
    public void deleteByID(String id){
        getById(id);
        donViReponsitory.deleteById(new ObjectId(id));
    }

    public List<DonViDTO> getSelectToanBoDV(TaiKhoan taiKhoan) {
        return donViReponsitory.getDonViConFromDonViCha(taiKhoan.getDonVi().get_id().toHexString());
    }
}
