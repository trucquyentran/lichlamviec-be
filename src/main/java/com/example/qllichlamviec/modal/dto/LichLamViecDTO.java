package com.example.qllichlamviec.modal.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichLamViecDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    private LocalDateTime thoiGianBD;
    private  LocalDateTime thoiGianKT;
    private LocalDateTime thoiGianTao;
    private String diaDiem;
    private String ghiChu;
    private String noiDung;
    private String tieuDe;
    @Size(max = 15, message = "Nội dung không được vượt quá 15 ký tự")
    private String bg;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId taiKhoan;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId donVi;
}
