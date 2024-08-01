package com.example.qllichlamviec.modal.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThongKeSLLoaiLichCuaMotNhanVien {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String hoTen;
   private  String bg;
   private long soLuong;

}
