package com.example.qllichlamviec.modal.system;

import com.example.qllichlamviec.modal.dto.QuyenTaiKhoanDTO;
import com.example.qllichlamviec.util.DonVi;
import com.example.qllichlamviec.util.Quyen;
import com.example.qllichlamviec.util.QuyenTaiKhoan;
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
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanNguoiDungDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    private DonVi donVi;
    @Size(max = 50, message = "Username không hợp lệ, không được vượt quá 50 ký tự")
    private String username;
    @Size(max = 70, message = "Tên không hợp lệ, không được vượt quá 70 ký tự")
    private String hoTen;
    private Boolean gioiTinh;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngaySinh;
    @Email(message = "Email không hợp lệ")
    private String email;
    @NotNull(message = "Số điện thoai không được để trống")
    private String sdt;
    private List<QuyenTaiKhoanDTO> listQuyen;
    private Integer trangThai;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime ngayTao;
    private String avatar;

}
