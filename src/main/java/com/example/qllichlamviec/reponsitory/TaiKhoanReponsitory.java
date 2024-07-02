package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.util.TaiKhoan;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaiKhoanReponsitory extends MongoRepository<TaiKhoan, ObjectId> {
}
