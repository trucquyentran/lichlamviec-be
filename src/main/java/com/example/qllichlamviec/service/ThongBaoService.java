package com.example.qllichlamviec.service;

import com.example.qllichlamviec.reponsitory.LichLamViecReponsitory;
import com.example.qllichlamviec.reponsitory.ThongBaoReponsitory;
import com.example.qllichlamviec.util.DonVi;
import com.example.qllichlamviec.util.LichLamViec;
import com.example.qllichlamviec.util.TaiKhoan;
import com.example.qllichlamviec.util.ThongBao;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class ThongBaoService {

    private static final Logger logger = LoggerFactory.getLogger(ThongBaoService.class);

    @Autowired
    private LichLamViecReponsitory lichLamViecRepository;

    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private ThongBaoReponsitory thongBaoReponsitory;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    public ThongBao save(ThongBao thongBao){
        return thongBaoReponsitory.save(thongBao);
    }

    public List<ThongBao> getByIdLich(ObjectId id){
        List<ThongBao> thongBaoList = thongBaoReponsitory.getByLichId(id);
        if (thongBaoList == null){
            throw new RuntimeException("Hiện không có thông báo nào của lịch là việc trên");
        }
        return thongBaoList;
    }

//    @Scheduled(fixedRate = 60000)  // Chạy mỗi phút một lần
//    public void kiemTraVaGuiThongBao() {
//
//        logger.info("Thực thi hàm kiemTraVaGuiThongBao tại " + LocalDateTime.now());
//
//
//        LocalDateTime now = LocalDateTime.now();
////        LocalDateTime reminderTime = now.plusMinutes(20);
//
////        List<ThongBao> thongBaoList = thongBaoReponsitory.findByThoiGian(now);
////
////        for (ThongBao tbl : thongBaoList) {
////           taoThongBao(tbl.getTaiKhoan());
////        }
//    }
//
//    public void guiThongBaoLichLamViec(TaiKhoan taiKhoan, LichLamViec lichLamViec) {
//        // Thay bằng logic gửi thông báo thực tế, ví dụ như gửi email, SMS, thông báo đẩy, v.v.
//        String noiDungThongBao = "Bạn có lịch làm việc vào lúc " + lichLamViec.getThoiGianBD();
//         }
//
//    public void taoThongBao(TaiKhoan tk, HttpServletRequest httpServletRequest){
//        LocalDateTime now = LocalDateTime.now();
//
//        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanFromRequest(httpServletRequest);
//        List<ThongBao> thongBaoList = thongBaoReponsitory.findByThoiGianTK(taiKhoan.get_id().toHexString(), now);
//
//        for (ThongBao tb : thongBaoList) {
//            guiThongBao(taiKhoan, tb);
//        }
//    }
//
//    public void guiThongBao(TaiKhoan taiKhoan, ThongBao thongBao) {
//        // Thay bằng logic gửi thông báo thực tế, ví dụ như gửi email, SMS, thông báo đẩy, v.v.
//        String noiDungThongBao = "Bạn có lịch làm việc vào lúc " + thongBao.getThoiGian();
//    }
}
