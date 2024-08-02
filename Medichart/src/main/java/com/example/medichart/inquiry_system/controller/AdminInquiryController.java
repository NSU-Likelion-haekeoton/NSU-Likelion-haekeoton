package com.example.medichart.inquiry_system.controller;

import com.example.medichart.inquiry_system.model.Inquiry;
import com.example.medichart.inquiry_system.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminInquiryController {

    @Autowired
    private InquiryService inquiryService;

    @GetMapping("/test")
    public String test() {
        return "Admin API is working!";
    }

    @PostMapping("/inquiries")
    public Inquiry createInquiry(@RequestBody Inquiry inquiry) {
        return inquiryService.saveInquiry(inquiry);
    }
}
