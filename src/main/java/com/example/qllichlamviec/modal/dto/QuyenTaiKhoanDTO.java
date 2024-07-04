package com.example.qllichlamviec.modal.dto;

import com.example.qllichlamviec.util.Quyen;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuyenTaiKhoanDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    @DocumentReference(lazy = false)
    private Quyen quyen;

}
