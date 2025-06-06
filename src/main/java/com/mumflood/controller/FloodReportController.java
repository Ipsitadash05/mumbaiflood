package com.mumflood.controller;

import com.mumflood.model.FloodReport;
import com.mumflood.service.FloodReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flood-report")
@CrossOrigin("*")
public class FloodReportController {

    @Autowired
    private FloodReportService reportService;

    @PostMapping("/submit")
    public FloodReport submitReport(@RequestBody FloodReport report) {
        return reportService.saveReport(report);
    }

    @GetMapping("/all")
    public List<FloodReport> getAllReports() {
        return reportService.getAllReports();
    }
}
