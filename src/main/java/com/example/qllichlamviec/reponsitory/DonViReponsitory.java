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
    @Query("{'donVi' : ?0}")
    List<DonViSelectDTO> getSelectDV(String donVi);
    @Query("{'_id' : ?0}")
    List<DonViDTO> getDonViConFromDonViCha(String search);
}
