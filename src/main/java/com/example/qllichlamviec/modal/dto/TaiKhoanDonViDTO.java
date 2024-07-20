package com.example.qllichlamviec.modal.dto;

import com.example.qllichlamviec.modal.system.TaiKhoanNguoiDungDTO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanDonViDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    @Size(max = 100, message = "Tên đơn vị không được vượt quá 100 ký tự")
    private String tenDonVi;
    private List<TaiKhoanNguoiDungDTO> taiKhoanList;
}
