package com.ayaan.githubaccess.controller;

import com.ayaan.githubaccess.dto.UserAccessReportDto;
import com.ayaan.githubaccess.service.AccessReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccessReportController {


    private final AccessReportService accessReportService;


    public AccessReportController(AccessReportService accessReportService) {
        this.accessReportService = accessReportService;
    }


    @GetMapping("/access-report")
    public List<UserAccessReportDto> getAccessReport(){

        return accessReportService.generateAccessReport();
    }

}