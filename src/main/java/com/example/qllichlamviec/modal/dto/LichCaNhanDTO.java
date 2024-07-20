package com.example.qllichlamviec.modal.dto;

import com.example.qllichlamviec.util.TaiKhoan;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToEmptyObjectSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LichCaNhanDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId taiKhoan;
    private LocalDateTime thoiGianBD;
    private LocalDateTime thoiGianKT;
    @Size(max = 300, message = "Tiêu đề không được vượt quá 300 ký tự")
    private String tieuDe;
    @Size(max = 100, message = "Địa điểm không được vượt quá 100 ký tự")
    private String diaDiem;
    @Size(max = 500,message = "Nội dung không được vượt quá 500 ký tự")
    private String noiDung;
    @Size(max = 300, message = "Ghi chu không được vượt quá 300 ký tự")
    private String ghiChu;
    @Size(max = 15, message = "Background không được vượt quá 15 ký tự")
    private String bg;
    private LocalDateTime thoiGianTao;
}
