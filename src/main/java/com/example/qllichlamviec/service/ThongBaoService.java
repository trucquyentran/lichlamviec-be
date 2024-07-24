package com.example.qllichlamviec.service;

import com.example.qllichlamviec.config.jwt.JwtAuthenticationTokenFilter;
import com.example.qllichlamviec.reponsitory.LichLamViecReponsitory;
import com.example.qllichlamviec.reponsitory.ThongBaoReponsitory;
import com.example.qllichlamviec.util.LichLamViec;
import com.example.qllichlamviec.util.TaiKhoan;
import com.example.qllichlamviec.util.ThongBao;
import com.example.qllichlamviec.util.pojo.FormatTime;
import com.example.qllichlamviec.websocket.NotificationHandler;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
@Slf4j
@Service
public class ThongBaoService {

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

    private UserDetails currentUser;

    @Autowired
    private NotificationHandler notificationHandler;

//    @Autowired
    private ScheduledExecutorService executorService;

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

    public List<ThongBao> getThongBaoByTaiKhoan(TaiKhoan taiKhoan){
        return thongBaoReponsitory.getByTaiKhoanId(taiKhoan.get_id());
    }

    public void deleteByLich (String id){
        thongBaoReponsitory.deleteByIdLich(new ObjectId(id));
    }

    public UserDetails getCurrentUser() {
        return JwtAuthenticationTokenFilter.getCurrentUser();
    }

    public void setCurrentUser(UserDetails user) {
        this.currentUser = user;
    }

    public void startScheduledTask() {
        if (executorService != null && !executorService.isShutdown()) {
            return; // Nếu đã có tác vụ đang chạy thì không khởi động lại
        }
        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(this::kiemTraVaGuiThongBao, 0,1 , TimeUnit.MINUTES);
        log.info("Tác vụ định kỳ đã được khởi động.");
    }

    public void stopScheduledTask() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            log.info("Tác vụ định kỳ đã được dừng.");
        }
    }

public void kiemTraVaGuiThongBao() {
    log.info("Thực thi hàm kiemTraVaGuiThongBao tại " + LocalDateTime.now());
    // Định dạng thời gian
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    LocalDateTime now = LocalDateTime.now();
    if (currentUser == null) {
        log.info("Xác thực là null hoặc người dùng không được xác thực");
        return; // Kết thúc phương thức nếu người dùng không được xác thực
    }

    log.info("Người dùng được xác thực: " + currentUser.getUsername());

    LocalDateTime tgbdStart = FormatTime.roundToMinute(now); // Thay đổi phạm vi thời gian theo yêu cầu

    // Lấy danh sách thông báo có thời gian nhắc trùng với thời gian hiện tại và tài khoản đang đăng nhập
    List<ThongBao> thongBaoList = thongBaoReponsitory.findByThoiGianTK(currentUser.getUsername(), tgbdStart);

    if (thongBaoList != null && !thongBaoList.isEmpty()) {
        for (ThongBao thongBao : thongBaoList) {
            LichLamViec lichLamViec = lichLamViecRepository.getByID(thongBao.getLichLamViec().get_id().toHexString());
            LocalDateTime thoiGianBD = lichLamViec.getThoiGianBD();
            LocalDateTime thoiGianKT = lichLamViec.getThoiGianKT();
            String formattedDateBD = thoiGianBD.format(formatter);
            String formattedDateKT = thoiGianKT.format(formatter);

            log.info(thongBao.getNoiDung()+ "\nDiễn ra từ " + formattedDateBD+" đến "+formattedDateKT);
            try {
                notificationHandler.sendNotification("<a href='#'>"+thongBao.getNoiDung() + "</a>\n<h3>Diễn ra từ</h3> " + formattedDateBD + formattedDateBD+" đến "+formattedDateKT);
            } catch (IOException e) {
                log.error("Lỗi gửi thông báo: ", e);
            }
        }
    } else {
        log.info("Không có thông báo nào trùng với thời gian hiện tại.");

    }
}

}

