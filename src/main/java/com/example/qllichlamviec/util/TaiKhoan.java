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
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

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

    @DocumentReference (lazy = false)
    private NguoiDung nguoiDung;
    @ReadOnlyProperty
    @DocumentReference(collection = "QuyenTaiKhoan", lookup = "{'taiKhoan':?#{#self._id}}")
    private List<QuyenTaiKhoan> quyenTaiKhoanList;
    private LocalDateTime ngayTao;
    private Integer trangThai;
//    private List<Session> listSession;
//    private List<Session> listSession;
private List<Session> listSession;

}
