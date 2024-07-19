package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.modal.dto.DonViDTO;
import com.example.qllichlamviec.modal.dto.DonViSelectDTO;
import com.example.qllichlamviec.util.DonVi;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DonViReponsitory extends MongoRepository<DonVi, ObjectId> {
    @Query("{'_id': ?0}")
    DonVi getByID(String id);

    @Query("{'tenDonVi': {$regex: ?0, $options: 'i'}}")
    List<DonVi> getByTen(String ten);

    @Query("{'donVi' : ?0}")
    List<DonViSelectDTO> getSelectDV(String donVi);

    @Query("{ 'donViTrucThuoc' : { $eq: null } }")
    List<DonViDTO> getDonViConFromDonViCha(String search);

    @Query("{ 'donViTrucThuoc' : ObjectId('?0') }")
    List<DonViSelectDTO> getSelectDonViThuocTrucTiep(String search);
}
