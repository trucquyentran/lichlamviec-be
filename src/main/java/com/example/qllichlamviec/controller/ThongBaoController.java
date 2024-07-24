package com.example.qllichlamviec.controller;

import com.example.qllichlamviec.service.ThongBaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ql-lich/lich-lam-viec")
public class ThongBaoController {
    @Autowired
    private ThongBaoService thongBaoService;

//    @GetMapping("list-thong-bao")
//    public
}
