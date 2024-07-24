package com.example.qllichlamviec.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Document(collection = "LichLamViec")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LichLamViec {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime thoiGianBD;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private  LocalDateTime thoiGianKT;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime thoiGianTao;
    @Size(max = 300, message = "Tiêu đề không được vượt quá 300 ký tự")
    private String tieuDe;
    @Size(max = 100, message = "Địa điểm không được vượt quá 100 ký tự")
    private String diaDiem;
    @Size(max = 500,message = "Nội dung không được vượt quá 500 ký tự")
    private String noiDung;
    @Size(max = 300, message = "Ghi chu không được vượt quá 300 ký tự")
    private String ghiChu;
    @Size(max = 15, message = "Background không được vượt quá 15 ký tự")
    private String bg;
    @DocumentReference (lazy = false)
    private TaiKhoan taiKhoan;
    @DocumentReference(lazy = false)
    private DonVi donVi;

    public void valiLich() throws Exception{
        try {
            // Nếu không có ngoại lệ thì thoiGianBD hợp lệ
            if (thoiGianBD == null) {
                throw new Exception("Thời gian không được để trống");
            }
        } catch (DateTimeParseException e) {
            // Nếu có lỗi trong quá trình xử lý hoặc định dạng, ném ra ngoại lệ
            throw new Exception("Nhập sai định dạng thời gian (yyyy-MM-dd HH:mm:ss)");
        }
    }
}
