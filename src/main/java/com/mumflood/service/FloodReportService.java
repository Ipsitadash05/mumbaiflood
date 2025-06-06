package com.mumflood.service;

import com.mumflood.model.FloodReport;
import com.mumflood.repository.FloodReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FloodReportService {

    @Autowired
    private FloodReportRepository repository;

    public FloodReport saveReport(FloodReport report) {
        report.setTimestamp(LocalDateTime.now());
        return repository.save(report);
    }

    public List<FloodReport> getAllReports() {
        return repository.findAll();
    }
}
