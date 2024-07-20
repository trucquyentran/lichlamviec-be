package com.example.qllichlamviec.util;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.TimeZoneSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Document(collection = "QuyenTaiKhoan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuyenTaiKhoan {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    @Getter(AccessLevel.NONE)
    @DocumentReference(lazy = false)
    @NotNull(message = "Tài khoản không được để trống")
    private TaiKhoan taiKhoan;

    @DocumentReference(lazy = false)
    @NotNull(message = "Quyền không được để trống")
    private Quyen quyen;


}
