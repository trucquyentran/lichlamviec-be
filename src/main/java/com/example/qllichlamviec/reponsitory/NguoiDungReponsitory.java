package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.util.NguoiDung;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface NguoiDungReponsitory extends MongoRepository<NguoiDung, ObjectId> {
    @Query("{'_id' : ?0}")
    NguoiDung getByID(String id);
//    @Query("{'_id' : ?0")


}
