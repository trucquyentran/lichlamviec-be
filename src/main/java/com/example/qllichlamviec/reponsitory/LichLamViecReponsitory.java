package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.util.LichLamViec;
import com.example.qllichlamviec.util.NguoiDung;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface LichLamViecReponsitory extends MongoRepository<LichLamViec, ObjectId> {
    @Query("{'_id': ?0}")
    LichLamViec getByID(String id);
    @Query("{'nguoiDung': ObjectId('?0')}")
    NguoiDung getByIDNguoiDung(String nguoiDung);
    @Query(value = "{'nguoiDung': ?0}",delete = true)
    void deleteByNguoiDungID(ObjectId nguiDung);


}
