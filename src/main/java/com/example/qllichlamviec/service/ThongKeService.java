package com.example.qllichlamviec.service;

import com.example.qllichlamviec.modal.dto.ThongKeNhanVienDvDTO;
import com.example.qllichlamviec.modal.system.TaiKhoanNguoiDungDTO;
import com.example.qllichlamviec.reponsitory.LichLamViecReponsitory;
import com.example.qllichlamviec.reponsitory.ThongKeReponsitory;

import com.example.qllichlamviec.util.TaiKhoan;
import com.example.qllichlamviec.util.pojo.FormatTime;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class ThongKeService {
    @Autowired
    private LichLamViecService lichLamViecService;
    @Autowired
    private LichLamViecReponsitory lichLamViecReponsitory;
    @Autowired
    private ThongKeReponsitory thongKeReponsitory;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ModelMapper modelMapper;

    public long soLuongLich(Date start, Date end){


        long soLuong = thongKeReponsitory.lichLamViec(FormatTime.roundToMinuteFromDate(start), FormatTime.roundToMinuteFromDate(end));
        return soLuong;

    }

    public List<ThongKeNhanVienDvDTO> countByDonVi() {
        // Tạo Aggregation Pipeline
        TypedAggregation<TaiKhoan> aggregation = Aggregation.newAggregation(TaiKhoan.class,
                Aggregation.group("donVi") // Nhóm theo trường 'donVi'
                        .count().as("soLuong"), // Đếm số lượng tài liệu trong mỗi nhóm và lưu vào trường 'soLuong'
                Aggregation.lookup("DonVi", "_id", "_id", "donViInfo"), // Kết hợp dữ liệu từ collection 'DonVi'
                Aggregation.unwind("donViInfo"), // Tách mảng 'donViInfo' thành các tài liệu riêng biệt
                Aggregation.project("soLuong") // Chỉ bao gồm trường 'soLuong'
                        .and("donViInfo.tenDonVi").as("tenDonVi"), // Lấy tên đơn vị từ 'donViInfo'
                Aggregation.sort(Sort.by(Sort.Order.desc("soLuong"))) // Sắp xếp kết quả theo trường 'count' giảm dần
        );

        // Thực hiện aggregation trên collection 'TaiKhoan'

        AggregationResults<ThongKeNhanVienDvDTO> results = mongoTemplate.aggregate(aggregation, "TaiKhoan", ThongKeNhanVienDvDTO.class);


        // Trả về kết quả dưới dạng danh sách các bản đồ (Map)
        return results.getMappedResults();
    }

}
