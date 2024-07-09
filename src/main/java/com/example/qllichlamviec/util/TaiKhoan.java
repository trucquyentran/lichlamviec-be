package com.example.qllichlamviec.util;

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
    @Size(max = 50, message = "Username không hợp lệ")
    private String username;

    @Size(max = 100)
    private String password;

//    @DocumentReference (lazy = false)
//    private NguoiDung nguoiDung;
    @ReadOnlyProperty
    @DocumentReference(collection = "QuyenTaiKhoan", lookup = "{'taiKhoan':?#{#self._id}}")
    private List<QuyenTaiKhoan> quyenTaiKhoanList;
    private LocalDateTime ngayTao;
    private Integer trangThai;
    private List<Session> listSession;

    @Size(max = 50, message = "Tên không hợp lệ")
    private String hoTen;

    @Email(message = "Email không hợp lệ")
    private String email;

    @NotNull(message = "Không được để trống")
    private String sdt;
    private Boolean gioiTinh;
    private LocalDate ngaySinh;

    @DocumentReference(lazy = false)
    private DonVi donVi;

//    public void NguoiDung(ObjectId _id){
//        this._id = _id;
//    }
    public void validate() throws Exception{
        if (getHoTen() == null || getHoTen().isEmpty() || getHoTen().length()>50){
            throw  new Exception("Tên không hợp lệ");
        }
        if (getSdt() == null || !Pattern.compile("($|[0-9]{10})").matcher(getSdt()).matches()){
            throw new Exception("Số điện thoại không hợp lệ");
        }
        if (getEmail() == null && !getEmail().isEmpty()){
            if (!Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(getEmail()).matches()){
                throw new Exception("Email không hợp lệ");
            }
        }
    }


}
