package com.example.qllichlamviec.modal.dto;

import com.example.qllichlamviec.util.Quyen;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToEmptyObjectSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NguoiDungDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    private String hoTen;
    private List<QuyenTaiKhoanDTO> quyenList;

}
