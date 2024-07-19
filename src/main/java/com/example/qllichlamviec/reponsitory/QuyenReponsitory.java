package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.util.DonVi;
import com.example.qllichlamviec.util.Quyen;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface QuyenReponsitory extends MongoRepository<Quyen, ObjectId> {
    @Query("{ '_id' : ?0 }")
    Quyen getByID(String id);

    @Query("{'tenQuyen': {$regex: ?0, $options: 'i'}}")
    List<Quyen> getByTen(String id);
}
