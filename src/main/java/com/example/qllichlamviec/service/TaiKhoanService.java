package com.example.qllichlamviec.service;

import com.example.qllichlamviec.reponsitory.DonViReponsitory;
import com.example.qllichlamviec.reponsitory.NguoiDungReponsitory;
import com.example.qllichlamviec.reponsitory.QuyenTaiKhoanReponsitory;
import com.example.qllichlamviec.reponsitory.TaiKhoanReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class TaiKhoanService {
    @Autowired
    private TaiKhoanReponsitory taiKhoanReponsitory;
    @Autowired
    private NguoiDungReponsitory nguoiDungReponsitory;
    @Autowired
    private DonViReponsitory donViReponsitory;
    @Autowired
    private QuyenTaiKhoanReponsitory quyenTaiKhoanReponsitory;
    @Autowired
    private ModuleLayer moduleLayer;
    @Autowired
    private MongoTemplate mongoTemplate;






}
