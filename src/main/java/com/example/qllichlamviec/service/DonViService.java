package com.example.qllichlamviec.service;

import com.example.qllichlamviec.modal.dto.DonViDTO;
import com.example.qllichlamviec.modal.dto.DonViSelectDTO;
import com.example.qllichlamviec.modal.dto.DonViTrucThuocDTO;
import com.example.qllichlamviec.reponsitory.DonViReponsitory;
import com.example.qllichlamviec.util.DonVi;
import com.example.qllichlamviec.util.TaiKhoan;
import com.example.qllichlamviec.util.pojo.XuLyDauChuoiTimKiem;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DonViService {
    @Autowired
    private DonViReponsitory donViReponsitory;
//    @Autowired
//    private TaiKhoanService taiKhoanService;
    @Autowired
    private ModelMapper modelMapper;


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

    public List<DonVi> findAll(int pageNumber , int size){
        Pageable pageable = PageRequest.of(pageNumber-1,size);

        Page<DonVi> donVi = donViReponsitory.findAll(pageable);
        if (donVi == null) {
            throw new RuntimeException("Hiện tại không có đơn vị nào trong hệ thống.");
        }
        return donVi.getContent();
    }

    public List<DonVi> search(String keyword){
        String regexKeyword = XuLyDauChuoiTimKiem.convertToRegex(keyword);
        List<DonVi> donViList = donViReponsitory.getByTen(regexKeyword);
//        if (donViList.isEmpty()) {
//            throw new RuntimeException("Hiện tại không có đơn vị nào trong hệ thống.");
//        }
        return donViList;
    }

    public void deleteByID(String id){
        getById(id);
        donViReponsitory.deleteById(new ObjectId(id));
    }

    // Lay toan bo don vi con cua don vi dang quan ly
    public List<DonViSelectDTO> getSelectDonViTrucThuoc(TaiKhoan taiKhoan) {

        return donViReponsitory.getSelectDonViCon(taiKhoan.getDonVi().get_id().toHexString());
    }

    public List<DonViTrucThuocDTO> getDonViConById(String dv) {
        return donViReponsitory.getAll(dv);
    }

    // Lay don vi toan bo don vi
    public List<DonViTrucThuocDTO> getSelectToanBoDonVi() {
        DonVi donViRoot = donViReponsitory.getDonViRoot();
        List<DonViTrucThuocDTO> donViTrucThuocDTOList = donViReponsitory.getAll(donViRoot.get_id().toHexString());

        return donViTrucThuocDTOList;
    }

    // Lấy cấu trúc cây của đơn vị
    public DonViTrucThuocDTO getDonViTree(ObjectId rootId) {
        List<DonViTrucThuocDTO> allDonVi = donViReponsitory.getAll(rootId.toHexString());
        Map<ObjectId, DonViTrucThuocDTO> donViMap = new HashMap<>();
        for (DonViTrucThuocDTO donVi : allDonVi) {
            donViMap.put(donVi.get_id(), donVi);
        }

        // Xây dựng cấu trúc cây
        return buildTree(rootId, donViMap);
    }

    private DonViTrucThuocDTO buildTree(ObjectId rootId, Map<ObjectId, DonViTrucThuocDTO> donViMap) {
        DonViTrucThuocDTO root = donViMap.get(rootId);
        if (root == null) {
            return null;
        }
        return root;
    }





}
