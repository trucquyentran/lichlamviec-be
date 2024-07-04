package com.example.qllichlamviec.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/quyen")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class QuyenController {
}
