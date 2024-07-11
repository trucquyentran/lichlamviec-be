package com.example.qllichlamviec.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LichCaNhanDTO {
    private LocalDateTime thoiGianBD;
    private  LocalDateTime thoiGianKT;
    private LocalDateTime thoiGianTao;
    private String diaDiem;
    private String ghiChu;
    private String noiDung;
    private String tieuDe;
}
