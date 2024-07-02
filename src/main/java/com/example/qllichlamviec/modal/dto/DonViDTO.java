package com.example.qllichlamviec.modal.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "DonVi")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonViDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    private String tenDonVi;


}
