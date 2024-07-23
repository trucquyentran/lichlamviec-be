package com.example.qllichlamviec.modal.dto;

import com.example.qllichlamviec.modal.system.TaiKhoanNguoiDungDTO;
import com.example.qllichlamviec.util.LichLamViec;
import com.example.qllichlamviec.util.TaiKhoan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichDonViTaiKhoanDTO {
    private TaiKhoanNguoiDungDTO taiKhoan;
    private List<LichLamViecDTO> lichLamViecList;
}
