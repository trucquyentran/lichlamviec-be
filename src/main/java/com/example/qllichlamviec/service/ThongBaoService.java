package com.example.qllichlamviec.service;

import com.example.qllichlamviec.config.jwt.JwtAuthenticationTokenFilter;
import com.example.qllichlamviec.modal.dto.LichLamViecDTO;
import com.example.qllichlamviec.modal.dto.LichLamViecHienThiDTO;
import com.example.qllichlamviec.modal.dto.ThongBaoDTO;
import com.example.qllichlamviec.reponsitory.LichLamViecReponsitory;
import com.example.qllichlamviec.reponsitory.ThongBaoReponsitory;
import com.example.qllichlamviec.util.LichLamViec;
import com.example.qllichlamviec.util.TaiKhoan;
import com.example.qllichlamviec.util.ThongBao;
import com.example.qllichlamviec.util.pojo.FormatTime;
import com.example.qllichlamviec.websocket.NotificationHandler;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    @Autowired
    private ModelMapper modelMapper;

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

    public List<ThongBaoDTO> getThongBaoByTaiKhoan(TaiKhoan taiKhoan){
        List<ThongBao> thongBaoList = thongBaoReponsitory.getByTaiKhoanId(taiKhoan.get_id());

        LocalDateTime now = LocalDateTime.now();
        List<ThongBaoDTO> lichLamViecDTO = new ArrayList<>();
        for (ThongBao tb: thongBaoList){
            LichLamViec lichLamViec = lichLamViecRepository.getByID(tb.getLichLamViec().get_id().toHexString());
            if (lichLamViec.getThoiGianBD().isBefore(now) || lichLamViec.getThoiGianBD().isEqual(now)) {
                LichLamViecHienThiDTO lichLamViecHienThiDTO = modelMapper.map(lichLamViec,LichLamViecHienThiDTO.class);

                ThongBaoDTO lichLamViecDTO1 = modelMapper.map(lichLamViec, ThongBaoDTO.class);
                lichLamViecDTO1.setThoiGian(tb.getThoiGian());
                lichLamViecDTO1.setLichLamViec(lichLamViecHienThiDTO);

                lichLamViecDTO.add(lichLamViecDTO1);
            }
        }
        return lichLamViecDTO;
    }

    public ThongBaoDTO getChiTiet(String tbId) {
        // Lấy thông báo từ repository bằng ID
        ThongBao thongBao = thongBaoReponsitory.getByID(tbId);

        // Lấy lịch làm việc từ repository bằng ID của lịch làm việc trong thông báo
        LichLamViec lichLamViec = lichLamViecRepository.getByID(thongBao.getLichLamViec().get_id().toHexString());

        // Ánh xạ đối tượng LichLamViec thành DTO
        LichLamViecHienThiDTO lichLamViecHienThiDTO = modelMapper.map(lichLamViec, LichLamViecHienThiDTO.class);

        // Ánh xạ đối tượng ThongBao thành DTO
        ThongBaoDTO thongBaoDTO = modelMapper.map(thongBao, ThongBaoDTO.class);

        // Thiết lập thời gian và lịch làm việc vào DTO
        thongBaoDTO.setThoiGian(thongBao.getThoiGian());
        thongBaoDTO.setLichLamViec(lichLamViecHienThiDTO);

        return thongBaoDTO;
    }


    public void deleteByLich (String id){
        thongBaoReponsitory.deleteByIdLich(new ObjectId(id));
    }

//    public UserDetails getCurrentUser() {
//        return JwtAuthenticationTokenFilter.getCurrentUser();
//    }

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
                notificationHandler.sendNotification("<h4><b>"+thongBao.getNoiDung() + "</b></h4></br><p>Diễn ra từ " + formattedDateBD+" đến "+formattedDateKT+"</p>");
            } catch (IOException e) {
                log.error("Lỗi gửi thông báo: ", e);
            }
        }
    } else {
        log.info("Không có thông báo nào trùng với thời gian hiện tại.");

    }
}

}

