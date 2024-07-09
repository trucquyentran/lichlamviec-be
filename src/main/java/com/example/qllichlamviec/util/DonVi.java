package com.example.qllichlamviec.util;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Document(collection = "DonVi")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonVi {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    @NotNull
    @Size(max = 300)
    private String tenDonVi;

    public DonVi(ObjectId _id){
        this._id = _id;
    }

}
