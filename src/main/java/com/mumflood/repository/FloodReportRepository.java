package com.mumflood.repository;

import com.mumflood.model.FloodReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FloodReportRepository extends JpaRepository<FloodReport, Long> {
}
