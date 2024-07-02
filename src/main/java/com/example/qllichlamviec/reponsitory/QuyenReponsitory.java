package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.util.Quyen;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface QuyenReponsitory extends MongoRepository<Quyen, ObjectId> {
    @Query("{ '_id' : ?0 }")
    Quyen getByID(String id);
}
