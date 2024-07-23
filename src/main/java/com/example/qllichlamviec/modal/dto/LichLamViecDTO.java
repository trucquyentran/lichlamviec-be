package com.example.qllichlamviec.modal.dto;

import com.example.qllichlamviec.util.DonVi;
import com.example.qllichlamviec.util.TaiKhoan;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichLamViecDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    @NotNull(message = "Thời gian bắt đầu không được để trống")
    private LocalDateTime thoiGianBD;
    @NotNull(message = "Thời gian kết thúc không được để trống")
    private  LocalDateTime thoiGianKT;
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
    private LocalDateTime thoiGianTao;

    private NguoiDungDTO taiKhoan;
    private DonVi donVi;
}
