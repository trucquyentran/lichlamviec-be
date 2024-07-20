package com.example.qllichlamviec.scheduler;

import com.example.qllichlamviec.service.TaiKhoanService;
import com.example.qllichlamviec.service.ThongBaoService;
import com.example.qllichlamviec.util.TaiKhoan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void startScheduler() {
        scheduler.scheduleAtFixedRate(this::checkNotifications, 0, 1, TimeUnit.MINUTES);
    }

    public TaiKhoan getCurrentTaiKhoan() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof TaiKhoan) {
            return (TaiKhoan) authentication.getPrincipal();
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
}
