package com.example.qllichlamviec.reponsitory;

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

    @Query("{'lichLamViec': ObjectId('?0')}")
    List<ThongBao> getByLichId(ObjectId lichLamViec);

    @Query("{'taiKhoan': ObjectId('?0'), 'thoiGian': {$eq: ?1}}")
    List<ThongBao> findByThoiGianTK(String taiKhoanId, LocalDateTime tgbdStart);

    @Query("{'thoiGian': ?0 ")
    List<ThongBao> findByThoiGian(LocalDateTime tgbdStart);

    @Query(value = "{'lichLamViec': ObjectId('?0')}", delete = true)
    void deleteByIdLich(ObjectId  lichLamViec);


}
