package com.mumflood.controller;

import com.mumflood.model.WaterLevelData;
import com.mumflood.service.WaterLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/water-level")
@CrossOrigin("*")
public class WaterLevelController {

    @Autowired
    private WaterLevelService service;

    // Optional: Manual submit (used only for testing)
    @PostMapping("/submit")
    public WaterLevelData submit(@RequestBody WaterLevelData data) {
        return service.save(data);
    }

    // Endpoint to get all saved water level records
    @GetMapping("/all")
    public List<WaterLevelData> getAll() {
        return service.getAll();
    }

    // Trigger live API fetch and store in database
    @GetMapping("/fetch-live")
    public String fetchFromApi() {
        service.fetchAndStoreWaterLevel();  // ✅ Correct method name
        return "Live water level data fetched and stored in DB.";
    }
}
