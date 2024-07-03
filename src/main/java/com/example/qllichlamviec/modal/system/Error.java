package com.example.qllichlamviec.modal.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error {
    private String error;
    private String message;
    private String exceptions;

    public Error(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
