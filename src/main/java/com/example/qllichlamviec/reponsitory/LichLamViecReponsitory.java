package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.util.DonVi;
import com.example.qllichlamviec.util.LichLamViec;
import com.example.qllichlamviec.util.NguoiDung;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface LichLamViecReponsitory extends MongoRepository<LichLamViec, ObjectId> {
    @Query("{'_id': ?0}")
    LichLamViec getByID(String id);
    @Query("{'nguoiDung': ?0}")
    List<LichLamViec> getByIDNguoiDung(ObjectId nguoiDung);

    @Query("{'donVi': ?0}")
    List<LichLamViec> getByIdDonVi(ObjectId donVi);

    @Query(value = "{'nguoiDung': ?0}",delete = true)
    void deleteByNguoiDungID(ObjectId nguiDung);


}
