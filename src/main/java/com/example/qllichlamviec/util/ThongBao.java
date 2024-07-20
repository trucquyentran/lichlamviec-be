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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    @NotNull(message = "Tài khoản không được để trống")
    private TaiKhoan taiKhoan;
    @Size(max = 300)
    private String noiDung;
    @DocumentReference(lazy = false)
    private LichLamViec lichLamViec;



}
