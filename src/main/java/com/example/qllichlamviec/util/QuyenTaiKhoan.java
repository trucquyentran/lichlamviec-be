package com.example.qllichlamviec.util;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.TimeZoneSerializer;
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
    @JsonSerialize(using = TimeZoneSerializer.class)
    private ObjectId _id;
    @Getter(AccessLevel.NONE)
    @DocumentReference(lazy = false)
    @NotNull
    private TaiKhoan taiKhoan;

    @DocumentReference(lazy = false)
    private Quyen quyen;


}
