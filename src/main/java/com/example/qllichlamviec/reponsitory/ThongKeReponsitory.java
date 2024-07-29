package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.modal.dto.ThongKeDTO;
import com.example.qllichlamviec.util.LichLamViec;
import com.example.qllichlamviec.util.ThongBao;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;


public interface ThongKeReponsitory extends MongoRepository<LichLamViec, ObjectId> {

    @Query(value = "{ 'thoiGianBD': { $gte: ?0, $lt: ?1 } }", count = true)
    long lichLamViec (Date ngayBatDau, Date ngayKetThuc);




}
