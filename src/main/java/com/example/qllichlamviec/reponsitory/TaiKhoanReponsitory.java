package com.example.qllichlamviec.reponsitory;

import com.example.qllichlamviec.modal.dto.TaiKhoanDangNhapDTO;
import com.example.qllichlamviec.util.TaiKhoan;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TaiKhoanReponsitory extends MongoRepository<TaiKhoan, ObjectId> {
    @Query("{'_id': ?0}")
    TaiKhoan getByID(String id);
    @Query("{ 'nguoiDung' : ObjectId('?0') }")
    TaiKhoan  getByIDNguoiDung(String idnguoiDng);
    @Query("{ 'username' : ?0 }")
    TaiKhoan getByUsername(String username);
    @Query("{ 'username' : ?0 }")
    List<TaiKhoanDangNhapDTO> timKiemTaiKhoanSelect(String key);

}
