package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.util.DonVi;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface DonViReponsitory extends MongoRepository<DonVi, ObjectId> {
    @Query("{'_id': ?0}")
    DonVi getByID(String id);

}
