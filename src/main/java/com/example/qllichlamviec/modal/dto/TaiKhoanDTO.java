package com.example.qllichlamviec.modal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoanDTO {
//    @Id
//    private ObjectId _id;
    @Size(max = 70, message = "Tên không hợp lệ, không được vượt quá 70 ký tự")
    private String hoTen;
    @Size(max = 50, message = "Username không hợp lệ, không được vượt quá 50 ký tự")
    private String username;
    private String password;
    private Boolean gioiTinh;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngaySinh;
    @Email(message = "Email không hợp lệ")
    private String email;
    @NotNull(message = "Số điện thoai không được để trống")
    private String sdt;
    private List<String> listQuyen;
    private String avatar;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId donVi;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime ngayTao;
}
