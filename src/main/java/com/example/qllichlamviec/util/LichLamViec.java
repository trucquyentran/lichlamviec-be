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

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Document(collection = "LichLamViec")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LichLamViec {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    private LocalDateTime thoiGianBD;
    private  LocalDateTime thoiGianKT;
    private LocalDateTime thoiGianTao;
    @Size(max = 700)
    private String tieuDe;
    @Size(max = 100)
    private String diaDiem;
    @Size(max = 300)
    private String noiDung;
    @Size(max = 300)
    private String ghiChu;
    @DocumentReference (lazy = false)
    private TaiKhoan taiKhoan;
    @DocumentReference(lazy = false)
    private DonVi donVi;
}
