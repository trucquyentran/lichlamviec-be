package com.example.qllichlamviec.modal.system;

import com.example.qllichlamviec.util.DonVi;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanNguoiDungDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    private DonVi donVi;
    private String username;
    private String hoTen;
    private Boolean gioiTinh;
    private LocalDate ngaySinh;
    private String email;
    private String sdt;
    private List<String> listQuyen;
    private Integer trangThai;
    private LocalDateTime ngayTao;
    private String avatar;

}
