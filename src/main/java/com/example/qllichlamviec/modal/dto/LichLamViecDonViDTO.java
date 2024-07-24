package com.example.qllichlamviec.modal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LichLamViecDonViDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;

    private DonViDTO donVi;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime thoiGianBD;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime thoiGianTao;

}
