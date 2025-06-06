package com.mumflood.repository;

import com.mumflood.model.WaterLevelData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterLevelRepository extends JpaRepository<WaterLevelData, Long> {
}
