package com.example.qllichlamviec.modal.dto;

import com.example.qllichlamviec.modal.system.DonViNameDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoanNguoiDungDTO {
//    @Id
//    private ObjectId _id;
    private String hoTen;
    private String username;
    private String password;
    private Boolean gioiTinh;
    private LocalDate ngaySinh;
    private String email;
    private String sdt;
    private List<String> listQuyen;
    private String avatar;
    private ObjectId donVi;
}
