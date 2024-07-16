package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.util.DonVi;
import com.example.qllichlamviec.util.LichLamViec;
import com.example.qllichlamviec.util.NguoiDung;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface LichLamViecReponsitory extends MongoRepository<LichLamViec, ObjectId> {
    @Query("{'_id': ?0}")
    LichLamViec getByID(String id);
    @Query("{'taiKhoan': ?0}")
    List<LichLamViec> getByIDTaiKhoan(ObjectId taiKhoan);

    @Query("{'donVi': ?0}")
    List<LichLamViec> getByIdDonVi(ObjectId donVi);

    @Query(value = "{'nguoiDung': ?0}",delete = true)
    void deleteByNguoiDungID(ObjectId nguoiDung);

    @Query("{ 'thoiGianBD': {$gte: ?1, $lt: ?2}}")
    List<LichLamViec> findByThoiGianBDBetween( LocalDateTime thoiGianBD);



}
