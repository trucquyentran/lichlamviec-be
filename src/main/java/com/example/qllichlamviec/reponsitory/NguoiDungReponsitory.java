package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.util.NguoiDung;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface NguoiDungReponsitory extends MongoRepository<NguoiDung, ObjectId> {
    @Query("{'_id' : ?0}")
    NguoiDung getByID(String id);

    @Query("{'email': ?0}")
    NguoiDung getByEmail(String email);

    @Query("{ 'sdt' : ?0 }")
    NguoiDung getBySdt(String sdt);



}
