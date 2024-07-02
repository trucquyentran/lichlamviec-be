package com.example.qllichlamviec.util;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.TimeZoneSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.Size;

@Document(collection = "QuyenTaiKhoan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuyenTaiKhoan {
    @Id
    @JsonSerialize(using = TimeZoneSerializer.class)
    private ObjectId _id;

    @DocumentReference(lazy = false)
    private Quyen quyen;

    @DocumentReference(lazy = false)
    private TaiKhoan taiKhoan;
}
