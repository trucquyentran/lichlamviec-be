package com.example.qllichlamviec.modal.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDungDangNhapDTO {
    private ObjectId _id;
    private String hoTen;
    private String email;
    private String sdt;
    private String ngaySinh;
    private Boolean gioiTinh;
    private List<String> listQuyen;
    private String avatar;
    private DonViNameDTO donVi;
}
