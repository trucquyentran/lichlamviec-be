package com.example.qllichlamviec.util;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;

@Document(collection = "Quyen")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Quyen {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    @Size(max = 100, message = "Tên quyền không hợp lệ, không được vượt quá 100 ký tự")
    private String tenQuyen;

    public Quyen(ObjectId _id){
        this._id = _id;
    }

}
