package com.example.qllichlamviec.modal.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToEmptyObjectSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NguoiDungDTO {
    @JsonSerialize(using = ToEmptyObjectSerializer.class)
    private ObjectId _id;
    private String hoTen;

}
