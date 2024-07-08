package com.example.qllichlamviec.modal.dto;

import com.example.qllichlamviec.util.DonVi;
import com.example.qllichlamviec.util.NguoiDung;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichLanViecDTO {
    private LocalDateTime thoiGianBD;
    private  LocalDateTime thoiGianKT;
    private String diaDiem;
    private String ghiChu;
    private String noiDung;
    private String tieuDe;
    private ObjectId nguoiDung;
    private ObjectId donVi;
}
