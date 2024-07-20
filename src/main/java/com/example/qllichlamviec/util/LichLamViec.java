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
    @Size(max = 300, message = "Tiêu đề không được vượt quá 300 ký tự")
    private String tieuDe;
    @Size(max = 100, message = "Địa điểm không được vượt quá 100 ký tự")
    private String diaDiem;
    @Size(max = 500,message = "Nội dung không được vượt quá 500 ký tự")
    private String noiDung;
    @Size(max = 300, message = "Ghi chu không được vượt quá 300 ký tự")
    private String ghiChu;
    @Size(max = 15, message = "Nội dung không được vượt quá 15 ký tự")
    private String bg;
    @DocumentReference (lazy = false)
    private TaiKhoan taiKhoan;
    @DocumentReference(lazy = false)
    private DonVi donVi;
}
