package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.util.QuyenTaiKhoan;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface QuyenTaiKhoanReponsitory extends MongoRepository<QuyenTaiKhoan, ObjectId> {
    @Query("{'_id': ?0}")
    QuyenTaiKhoan getByID(String id);

    @Query("{'taiKhoan': ObjectId('?0')}")
    List<QuyenTaiKhoan> getByTaiKhoanID(String taiKhoan);

    @Query(value = "{'taiKhoan': ObjectId('?0')}", delete = true)
    void deleteByTaiKhoanID(ObjectId  taiKhoan);

}
