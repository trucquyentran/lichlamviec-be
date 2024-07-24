package com.example.qllichlamviec.modal.dto;

import com.example.qllichlamviec.modal.system.TaiKhoanNguoiDungDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichDonViTaiKhoanDTO {
    private TaiKhoanNguoiDungDTO taiKhoan;
    private List<LichLamViecHienThiDTO> lichLamViecList;
}
