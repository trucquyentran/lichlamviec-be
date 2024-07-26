package com.example.qllichlamviec.modal.dto;

import com.example.qllichlamviec.util.DonVi;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonViTrucThuocDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    private String tenDonVi;

    private List<DonVi> donViCon;
}
