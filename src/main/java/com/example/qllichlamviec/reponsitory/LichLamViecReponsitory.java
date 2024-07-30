package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.modal.dto.LichLamViecHomNayDTO;
import com.example.qllichlamviec.util.DonVi;
import com.example.qllichlamviec.util.LichLamViec;
import com.example.qllichlamviec.util.NguoiDung;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface LichLamViecReponsitory extends MongoRepository<LichLamViec, ObjectId> {


    @Query("{'_id': ?0}")
    LichLamViec getByID(String id);
    @Query("{'taiKhoan': ?0}")
    List<LichLamViec> getByIDTaiKhoan(ObjectId taiKhoan);

    @Query("{'donVi': ?0}")
    List<LichLamViec> getByIdDonVi(ObjectId donVi);

    @Query("{ $or: [ " +
            "{ 'taiKhoan': { $in: ?0 } }, " +
            "{ 'donVi': { $in: ?1 } }, " +
            "{ 'tieuDe': { $regex: ?2, $options: 'i' } }, " +
            "{ 'diaDiem': { $regex: ?2, $options: 'i' } }, " +
            "{ 'noiDung': { $regex: ?2, $options: 'i' } }, " +
            "{ 'ghiChu': { $regex: ?2, $options: 'i' } } " +
            "] }")
    List<LichLamViec> searchLich(List<ObjectId> taiKhoan, List<ObjectId> donVi, String tuKhoa);

    @Query(value = "{'nguoiDung': ?0}",delete = true)
    void deleteByNguoiDungID(ObjectId nguoiDung);

    @Query("{ 'thoiGianBD': {$gte: ?0, $lt: ?1}}")
    List<LichLamViec> findByThoiGianBDBetween(LocalDate ngayBatDau, LocalDate ngayKetThuc);

    @Query(value = "{ 'thoiGianBD': { $gte: ?0, $lt: ?1 } }", count = true)
    long lichLamViec (Date ngayBatDau, Date ngayKetThuc);




}
