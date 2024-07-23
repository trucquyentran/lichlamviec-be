package com.example.qllichlamviec.service;

import ch.qos.logback.core.util.TimeUtil;
import com.example.qllichlamviec.config.jwt.JwtAuthenticationTokenFilter;
import com.example.qllichlamviec.reponsitory.LichLamViecReponsitory;
import com.example.qllichlamviec.reponsitory.ThongBaoReponsitory;
import com.example.qllichlamviec.util.DonVi;
import com.example.qllichlamviec.util.LichLamViec;
import com.example.qllichlamviec.util.TaiKhoan;
import com.example.qllichlamviec.util.ThongBao;
import com.example.qllichlamviec.util.pojo.FormatTime;
import com.example.qllichlamviec.websocket.NotificationHandler;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
@Slf4j
@Service
public class ThongBaoService {

//    private static final Logger logger = LoggerFactory.getLogger(ThongBaoService.class);

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

//    @Scheduled(fixedRate = 60000)  // Chạy mỗi phút một lần
public void kiemTraVaGuiThongBao() {
    log.info("Thực thi hàm kiemTraVaGuiThongBao tại " + LocalDateTime.now());
    LocalDateTime now = LocalDateTime.now();
    if (currentUser == null) {
        log.info("Authentication is null or user is not authenticated");
        return; // Kết thúc phương thức nếu người dùng không được xác thực
    }

    log.info("User is authenticated: " + currentUser.getUsername());

    LocalDateTime tgbdStart = FormatTime.roundToMinute(now); // Thay đổi phạm vi thời gian theo yêu cầu
    LocalDateTime tgbdEnd = FormatTime.roundToMinute(now.plusMinutes(1)); // Thời gian hiện tại

    // Lấy danh sách thông báo có thời gian nhắc trùng với thời gian hiện tại và tài khoản đang đăng nhập
    List<ThongBao> thongBaoList = thongBaoReponsitory.findByThoiGianTK(currentUser.getUsername(), tgbdStart);

    if (thongBaoList != null && !thongBaoList.isEmpty()) {
        for (ThongBao thongBao : thongBaoList) {
            log.info("Nhắc lịch: Thông báo nội dung: " + thongBao.getNoiDung());
            try {
                notificationHandler.sendNotification("Nhắc lịch: Bạn có lịch: " + thongBao.getNoiDung() + " diễn ra vào lúc " + thongBao.getThoiGian().minusMinutes(10));
            } catch (IOException e) {
                log.error("Error sending notification: ", e);
            }
        }
    } else {
        log.info("Không có thông báo nào trùng với thời gian hiện tại.");
    }
}

}

