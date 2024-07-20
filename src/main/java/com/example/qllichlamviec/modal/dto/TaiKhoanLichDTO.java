package com.example.qllichlamviec.modal.dto;

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
public class TaiKhoanLichDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    @Size(max = 50, message = "Username không hợp lệ, không được vượt quá 50 ký tự")
    private String username;
    private List<String> listQuyen;
}
