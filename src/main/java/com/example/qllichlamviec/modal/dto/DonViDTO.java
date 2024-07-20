package com.example.qllichlamviec.modal.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;

@Document(collection = "DonVi")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonViDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;

    @Size(max = 100, message = "Tên đơn vị không được vượt quá 100 ký tự")
    private String tenDonVi;


}
