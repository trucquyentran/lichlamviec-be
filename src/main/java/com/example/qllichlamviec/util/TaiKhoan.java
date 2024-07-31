package com.example.qllichlamviec.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import com.example.qllichlamviec.util.pojo.Session;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Document(collection = "TaiKhoan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoan {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;

    @Indexed(unique = true)
    @Size(max = 50, message = "Username không hợp lệ, không được vượt quá 50 ký tự")
    private String username;

    @Size(max = 100)
    private String password;

    @ReadOnlyProperty
    @DocumentReference(collection = "QuyenTaiKhoan", lookup = "{'taiKhoan':?#{#self._id}}")
    private List<QuyenTaiKhoan> quyenTaiKhoanList;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime ngayTao;
    private Integer trangThai;
    private List<Session> listSession;
    @Size(max = 70, message = "Tên không hợp lệ, không được vượt quá 70 ký tự")
    private String hoTen;

    @Email(message = "Email không hợp lệ")
    private String email;

    @NotNull(message = "Số điện thoai không được để trống")
    private String sdt;
    private Boolean gioiTinh;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngaySinh;

    @DocumentReference(lazy = false)
    private DonVi donVi;

    public void TaiKhoan(ObjectId _id){
        this._id = _id;
    }
    public void validate() throws Exception{
        if (getHoTen() == null || getHoTen().isEmpty() || getHoTen().length()>70){
            throw  new Exception("Tên không hợp lệ");
        }
        if (getSdt().isEmpty() || getSdt() == null || !Pattern.compile("($|[0-9]{10})").matcher(getSdt()).matches()){
            throw new Exception("Số điện thoại không được để trống và có độ dài 10 kí tự");
        }
        if (getEmail() == null && !getEmail().isEmpty()){
            if (!Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(getEmail()).matches()){
                throw new Exception("Email không hợp lệ");
            }
        }
    }


}
