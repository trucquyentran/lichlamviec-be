package com.example.qllichlamviec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
//xử lý các lỗi phát sinh trong ứng dụng
@RestControllerAdvice
public class XuLyNgoaiLe {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Lấy tất cả các lỗi từ BindingResult
        for (org.springframework.validation.ObjectError error : ex.getBindingResult().getAllErrors()) {
            // Chuyển đổi lỗi thành FieldError để lấy thông tin trường và thông báo lỗi
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                // Thêm lỗi vào Map
                errors.put(fieldName, errorMessage);
            }
        }
        // Trả về ResponseEntity chứa lỗi và mã trạng thái HTTP 400
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException e) {
        return new ResponseEntity<>("Thời gian không hợp lệ. Vui lòng nhập đúng định dạng!", HttpStatus.BAD_REQUEST);
    }
}
