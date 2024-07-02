package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.util.ThongBao;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ThongBaoReponsitory extends MongoRepository<ThongBao, ObjectId> {
}
