package com.example.qllichlamviec.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
public class JwtService {
    public static final String TRANGTHAI = "trangThai";
    public static final String TAIKHOANID = "taiKhoanID";
    public static final String NGUOIDUNGID = "nguoiDungId";
    public static final String TYPE = "type";
    public static final String SECRET_KEY = "SECRET_KEY_QuanLyLichLamViec_TranThiTrucquyen_01_07_2024_Least_256Bits30122024";
    public static final int EXPIRE_TIME = 86400000*1;

    public String generateTokenLogin(String idTaiKhoan, String nguoiDungID, Integer trangThai) {
        String token = null;
        try {
            JWSSigner signer = new MACSigner(generateShareSecret());

            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
            builder.claim(TAIKHOANID, idTaiKhoan);
            builder.claim(NGUOIDUNGID, nguoiDungID);
            builder.claim(TRANGTHAI, trangThai);
            builder.claim(TYPE, "0");
            builder.expirationTime(generateExpirationDate());
            JWTClaimsSet claimsSet = builder.build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);
            token = signedJWT.serialize();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    public  String generateRefreshToken(String idTaiKhoan, String nguoiDungID, Integer trangThai){
        String refreshToken = null;
        try {
            JWSSigner signer = new MACSigner(generateShareSecret());
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
            builder.claim(TAIKHOANID, idTaiKhoan);
            builder.claim(NGUOIDUNGID, nguoiDungID);
            builder.claim(TRANGTHAI, trangThai);
            builder.claim(TYPE,"1");
            builder.expirationTime(new Date(System.currentTimeMillis() + 86400000*3));
            JWTClaimsSet claimsSet = builder.build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);
            refreshToken = signedJWT.serialize();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  refreshToken;
    }
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + EXPIRE_TIME);
    }

    public String getToken(HttpServletRequest httpRequest) {
        String bearer = httpRequest.getHeader("authorization");
        if (bearer != null){
            String[] words= bearer.split("Bearer");
            return words[words.length-1];
        }
        return null;
    }

    public Boolean validateRefreshTokenLogin(String token) {
        if (token == null || token.trim().length() == 0) {
            return false;
        }
        String username = getIDTaiKhoanFromToken(token);
        String type = getTypeFromToken(token);
        if (username == null || username.isEmpty() || !type.equals("1")) {
            return false;
        }
        if (isTokenExpired(token)) {
            return false;
        }
        return true;
    }

    public boolean validateTokenLogin(String token) {
        if (token == null || token.trim().length() == 0){
            return false;
        }
        String username = getIDTaiKhoanFromToken(token);
        String type = getTypeFromToken(token);
        if (username == null || username.isEmpty() || !type.equals("0")){
            return false;
        }
        if (isTokenExpired(token)){
            return false;
        }
        return true;
    }

    private boolean isTokenExpired(String token) {
        if(token == null ){
            return true;
        }
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        Date expiration = null;
        JWTClaimsSet claims = getClaimsFromToken(token);
        expiration = claims.getExpirationTime();
        return expiration;
    }

    public String getIDDonViFromToken(String token) {
        String id = null;
        try {
            JWTClaimsSet claims = getClaimsFromToken(token);
            id = claims.getStringClaim(NGUOIDUNGID);
        } catch (Exception e) {
//			log.info("Token không hợp lệ");
        }
        return id;
    }

    private String getTypeFromToken(String token) {
        String type = null;
        try {
            JWTClaimsSet claims = getClaimsFromToken(token);
            type = claims.getStringClaim(TYPE);
        } catch (Exception e) {
//			log.info("Token không hợp lệ");
            return type;
        }
        return type;
    }

    private JWTClaimsSet getClaimsFromToken(String token) {
        JWTClaimsSet claims = null;
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(generateShareSecret());
            if (signedJWT.verify(verifier)) {
                claims = signedJWT.getJWTClaimsSet();
            }
        } catch (Exception e) {
        }
        return claims;
    }

    private byte[] generateShareSecret() {
        byte[] sharedSecret = new byte[32];
        sharedSecret = SECRET_KEY.getBytes();
        return sharedSecret;
    }

    public String getIDTaiKhoanFromToken(String token) {
        String username = null;
        try {
            JWTClaimsSet claims = getClaimsFromToken(token);
            username = claims.getStringClaim(TAIKHOANID);
        } catch (Exception e) {
//			log.info("Token không hợp lệ");
            return username;
        }
        return username;
    }

    public int getTrangThaiFromToken(String token) {
        Integer trangThai = null;
        try {
            JWTClaimsSet claims = getClaimsFromToken(token);
            trangThai = claims.getIntegerClaim(TRANGTHAI);
        } catch (Exception e) {
        }
        return trangThai;
    }
}
