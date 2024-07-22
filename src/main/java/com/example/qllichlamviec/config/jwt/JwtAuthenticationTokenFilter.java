package com.example.qllichlamviec.config.jwt;

import com.example.qllichlamviec.service.JwtService;
import com.example.qllichlamviec.service.TaiKhoanService;
import com.example.qllichlamviec.service.ThongBaoService;
import com.example.qllichlamviec.util.QuyenTaiKhoan;
import com.example.qllichlamviec.util.TaiKhoan;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private final static String TOKEN_HEADER = "authorization";
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TaiKhoanService taiKhoanService;

    public static final ThreadLocal<UserDetails> authenticatedUser = new ThreadLocal<>();


    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String forwardedForHeader = httpRequest.getHeader("X-Forwarded-For");
        String ip = (forwardedForHeader != null && !forwardedForHeader.isEmpty()) ? forwardedForHeader.split(",\\s*")[0] : null;
        String xForwardedForHeader = httpRequest.getHeader("X-Forwarded-For") + ", " + httpRequest.getRemoteAddr();
        String username = "";
        String token = jwtService.getToken(httpRequest);
        if(!"null".equals(token)){
            if (jwtService.validateTokenLogin(token)) {
                String idTaiKhoan = jwtService.getIDTaiKhoanFromToken(token);
                int trangThai = jwtService.getTrangThaiFromToken(token);
                TaiKhoan taiKhoan = taiKhoanService.getByID(idTaiKhoan);
                if (taiKhoan != null  && taiKhoan.getTrangThai() == trangThai && taiKhoan.getTrangThai() == 1) {
                    username = taiKhoan.get_id().toHexString() + "-" + taiKhoan.getUsername();
                    boolean enabled = (taiKhoan.getTrangThai() == 1);
                    boolean accountNonExpired = true;
                    boolean credentialsNonExpired = true;
                    boolean accountNonLocked = true;
                    List<GrantedAuthority> autho = new ArrayList<GrantedAuthority>();
                    List<QuyenTaiKhoan> listQuyen = taiKhoanService.getListQuyenByID(taiKhoan.get_id().toHexString());
                    List<String> roles = new ArrayList<>();
                    for (int i = 0; i < listQuyen.size(); i++) {
                        for (int j = 0; j < listQuyen.size(); j++) {
                            roles.add("ROLE_" + listQuyen.get(j).getQuyen().getTenQuyen().toUpperCase());
                        }
                    }
                    autho.add(new SimpleGrantedAuthority("ROLE_USER"));
                    for (int i = 0; i < roles.size(); i++) {
                        autho.add(new SimpleGrantedAuthority(roles.get(i)));
                    }

                    UserDetails userDetail = new User(taiKhoan.get_id().toHexString(), taiKhoan.getPassword(), enabled, accountNonExpired,
                            credentialsNonExpired, accountNonLocked, autho);


                    // Lưu trữ người dùng đã được xác thực trong ThreadLocal để truy cập sau
                    authenticatedUser.set(userDetail);
                    getCurrentUser();


                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail,
                            null, userDetail.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        }
        log.info(request.getMethod() + "|" + request.getRequestURI() + (request.getQueryString() != null ? ("?" + request.getQueryString()) : "") + "►IP: " + ip + "/" + xForwardedForHeader + "►User:" + username);
//        filterChain.doFilter(request, response);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Dọn dẹp ThreadLocal sau khi quá trình xử lý yêu cầu hoàn tất
//            authenticatedUser.remove();
//            SecurityContextHolder.clearContext();
        }
    }

    public static UserDetails getCurrentUser() {
        log.info("Thong tin: "+authenticatedUser.get());
         return authenticatedUser.get();
    }

}

