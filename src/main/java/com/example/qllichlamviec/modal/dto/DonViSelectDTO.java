package com.example.qllichlamviec.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonViSelectDTO {
    @Size(max = 100, message = "Tên đơn vị không được vượt quá 100 ký tự")
    private String tenDonVi;
    @DocumentReference(collection = "DonVi", lookup = "{'donViTrucThuoc':?#{#self._id}}")
    private List<DonViSelectDTO> donViCon;
}
