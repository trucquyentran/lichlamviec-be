package com.example.qllichlamviec.scheduler;

import com.example.qllichlamviec.config.jwt.JwtAuthenticationTokenFilter;
import com.example.qllichlamviec.service.TaiKhoanService;
import com.example.qllichlamviec.service.ThongBaoService;
import com.example.qllichlamviec.util.TaiKhoan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
@Service
public class SchedulerExample {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerExample.class);

    @Autowired
    private ThongBaoService thongBaoService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void startScheduler() {
        scheduler.scheduleAtFixedRate(this::checkNotifications, 0, 1, TimeUnit.MINUTES);
    }

    public TaiKhoan getCurrentTaiKhoan() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof TaiKhoan) {
                return (TaiKhoan) principal;
            } else {
                // Log thông tin principal để kiểm tra
                logger.warn("Principal không phải là TaiKhoan: " + principal);
            }
        }
        return null; // hoặc throw exception nếu cần
    }

    private void checkNotifications() {
        try {
            TaiKhoan taiKhoan = getCurrentTaiKhoan(); // Cần phải có phương thức để lấy tài khoản hiện tại
            if (taiKhoan != null) {
                thongBaoService.checkAndNotify(taiKhoan);
            } else {
                logger.warn("Không có tài khoản hiện tại để kiểm tra thông báo.");
            }
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra thông báo: ", e);
        }
    }
    ApplicationRunner applicationRunner(UserDetails userDetails){
        logger.info("Thong tin tai khoan"+userDetails.getUsername());
        return null;
    }

}
