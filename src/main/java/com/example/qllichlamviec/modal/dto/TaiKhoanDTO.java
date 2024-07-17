package com.example.qllichlamviec.modal.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoanDTO {
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
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId donVi;
}
