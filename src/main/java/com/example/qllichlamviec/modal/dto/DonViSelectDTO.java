package com.example.qllichlamviec.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonViSelectDTO {
    @Size(max = 100, message = "Tên đơn vị không được vượt quá 100 ký tự")
    private String tenDonVi;
}
