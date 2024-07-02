package com.example.qllichlamviec.util;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "ThongBao")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThongBao {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    private LocalDateTime thoiGian;
    @DocumentReference(lazy = false)
    private User user;
    @DocumentReference(lazy = false)
    private LichLamViec lichLamViec;


}
