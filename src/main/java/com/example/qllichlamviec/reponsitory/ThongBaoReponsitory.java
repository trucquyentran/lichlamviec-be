package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.util.LichLamViec;
import com.example.qllichlamviec.util.ThongBao;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ThongBaoReponsitory extends MongoRepository<ThongBao, ObjectId> {
    @Query("{'_id': ?0}")
    ThongBao getByID(String id);

    @Query("{'taiKhoan': ObjectId('?0')}")
    List<ThongBao> getByTaiKhoanId(ObjectId taiKhoan);

    @Query("{'licLamViec': ObjectId('?0')}")
    List<ThongBao> getByLichId(ObjectId licLamViec);

    @Query("{'taiKhoan._id': ?0, 'thoiGian': {$gte: ?1, $lt: ?2}}")
    List<ThongBao> findByThoiGianTK(String taiKhoanId, LocalDateTime tgbdStart);

    @Query("{'thoiGian': ?0 ")
    List<ThongBao> findByThoiGian(LocalDateTime tgbdStart);


}
