package com.example.qllichlamviec.service;

import com.example.qllichlamviec.modal.dto.*;
import com.example.qllichlamviec.modal.system.TaiKhoanNguoiDungDTO;
import com.example.qllichlamviec.reponsitory.LichLamViecReponsitory;

import com.example.qllichlamviec.reponsitory.TaiKhoanReponsitory;
import com.example.qllichlamviec.util.LichLamViec;
import com.example.qllichlamviec.util.TaiKhoan;
import com.example.qllichlamviec.util.pojo.FormatTime;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class ThongKeService {
    @Autowired
    TaiKhoanReponsitory taiKhoanReponsitory;
    @Autowired
    private LichLamViecService lichLamViecService;
    @Autowired
    private LichLamViecReponsitory lichLamViecReponsitory;



    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ModelMapper modelMapper;

    public long soLuongLich(Date start, Date end){
        long soLuong = lichLamViecReponsitory.lichLamViec(FormatTime.roundToMinuteFromDate(start), FormatTime.roundToMinuteFromDate(end));
        return soLuong;

    }

    public List<ThongKeNhanVienDvDTO> countEmpByDonVi() {
        TypedAggregation<TaiKhoan> aggregation = Aggregation.newAggregation(TaiKhoan.class,
                Aggregation.group("donVi")
                        .count().as("soLuong"),
                Aggregation.lookup("DonVi", "_id", "_id", "donViInfo"),
                Aggregation.unwind("donViInfo"),
                Aggregation.project("soLuong")
                        .and("donViInfo.tenDonVi").as("tenDonVi")
        );

        AggregationResults<ThongKeNhanVienDvDTO> results = mongoTemplate.aggregate(aggregation, "TaiKhoan", ThongKeNhanVienDvDTO.class);
        return results.getMappedResults();
    }

    public long countUser(){
        return taiKhoanReponsitory.count();
    }

    public  long countEvent(){
        return lichLamViecReponsitory.count();
    }

    public List<ThongKeSoLuongLichCuaNguoiDung> countLichByUser() {
        TypedAggregation<LichLamViec> aggregation = Aggregation.newAggregation(LichLamViec.class,
                Aggregation.group("taiKhoan")
                        .count().as("soLuong"),
                Aggregation.lookup("TaiKhoan", "_id", "_id", "taiKhoanInfo"),
                Aggregation.unwind("taiKhoanInfo"),
                Aggregation.project("soLuong")
                        .and("taiKhoanInfo.hoTen").as("hoTen")
        );
        AggregationResults<ThongKeSoLuongLichCuaNguoiDung> results = mongoTemplate.aggregate(aggregation, "LichLamViec", ThongKeSoLuongLichCuaNguoiDung.class);
        return results.getMappedResults();
    }

    public List<ThongKeSLLoaiLichCuaMotNhanVien> countCateEventOfUser(ObjectId userId) {

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(org.springframework.data.mongodb.core.query.Criteria.where("taiKhoan").is(userId)),
                Aggregation.group("bg").count().as("soLuong"),
                Aggregation.project()
                        .andExclude("_id")
                        .and("soLuong").as("soLuong")
                        .and("_id").as("bg")
        );
        AggregationResults<ThongKeSLLoaiLichCuaMotNhanVien> results = mongoTemplate.aggregate(aggregation, "LichLamViec", ThongKeSLLoaiLichCuaMotNhanVien.class);
        return results.getMappedResults();
    }

    public List<LichByDateDTO> lichByDate(ObjectId id) {
        TypedAggregation<LichLamViec> aggregation = Aggregation.newAggregation(LichLamViec.class,
                Aggregation.match(Criteria.where("id").is(id)),
                Aggregation.group("thoiGian")
                        .push("$$ROOT").as("lichLamViecList"),
                Aggregation.lookup("TaiKhoan", "lichLamViecList.taiKhoanId", "_id", "taiKhoanInfo"),
                Aggregation.unwind("taiKhoanInfo"),
                Aggregation.project("thoiGian")
                        .and("lichLamViecList").as("lichLamViecList")
                        .and("taiKhoanInfo.hoTen").as("hoTen")
        );
        AggregationResults<LichByDateDTO> results = mongoTemplate.aggregate(aggregation, "LichLamViec", LichByDateDTO.class);
        return results.getMappedResults();
    }




//    public List<ThongKeNhanVienCoLichHomNayDTO> countUserHaveEventToday() {
//        // Lấy ngày hôm nay từ 0h đến 23h59
//        LocalDate start = LocalDate.now();
//        LocalDate dateEnd = start.plusDays(2);
//
//
//        // Aggregation pipeline
//        TypedAggregation<LichLamViec> aggregation = Aggregation.newAggregation(LichLamViec.class,
//                Aggregation.match(Criteria.where("thoiGianBD").gte(start).lt(dateEnd)),
//                Aggregation.group("donVi._id")
//                        .count().as("soLuong"),
//                Aggregation.lookup("DonVi", "_id", "_id", "donViInfo"),
//                Aggregation.unwind("donViInfo"),
//                Aggregation.project("soLuong")
//                        .and("donViInfo.tenDonVi").as("tenDonVi")
//        );
//
//        // Thực hiện aggregation
//        AggregationResults<ThongKeNhanVienCoLichHomNayDTO> results = mongoTemplate.aggregate(aggregation, "LichLamViec", ThongKeNhanVienCoLichHomNayDTO.class);
//        return results.getMappedResults();
//    }
//    public List<ThongKeDSLichByDateDTO> listEventByDate(Date start, Date end){
//
//    }
}
