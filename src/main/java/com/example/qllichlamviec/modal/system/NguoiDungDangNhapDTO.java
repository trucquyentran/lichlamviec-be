package com.example.qllichlamviec.modal.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDungDangNhapDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    private String hoTen;
    private String email;
    private String sdt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime ngaySinh;
    private Boolean gioiTinh;
    private List<String> listQuyen;
    private String avatar;
    private DonViNameDTO donVi;

}
