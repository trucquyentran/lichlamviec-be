package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.util.LichLamViec;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface LichLamViecReponsitory extends MongoRepository<LichLamViec, ObjectId> {
    @Query("{'_id': ?0}")
    LichLamViec getByID(String id);


}
