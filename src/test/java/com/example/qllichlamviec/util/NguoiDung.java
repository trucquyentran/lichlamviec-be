package com.example.qllichlamviec.util;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
//import javax.validation.constraints.Size;

@Document(collection = "NguoiDung")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class NguoiDung implements Serializable {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;

//    @Size(max = 50, massage = "Tên không hợp lệ")
//    private String hoTen;






}
