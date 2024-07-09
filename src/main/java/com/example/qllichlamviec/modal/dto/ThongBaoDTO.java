package com.example.qllichlamviec.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThongBaoDTO {
    private String noiDung;
    private LocalDateTime thoiGian;
    private List<ObjectId> listNguoiDung;
    private ObjectId lichLamViec;

}
