package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.util.ThongBao;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ThongBaoReponsitory extends MongoRepository<ThongBao, ObjectId> {
    @Query("{'_id': ?0}")
    ThongBao getByID(String id);
}
