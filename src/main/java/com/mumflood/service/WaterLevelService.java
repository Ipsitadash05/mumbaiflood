package com.mumflood.service;

import com.mumflood.model.WaterLevelData;
import com.mumflood.repository.WaterLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class WaterLevelService {

    @Autowired
    private WaterLevelRepository repository;

    private final Map<Integer, String> stationMap = Map.ofEntries(
            Map.entry(14541, "Hindamata Cinema"),
            Map.entry(14501, "Gandhi Market Laxmi Bai Kealkar Marg"),
            Map.entry(14447, "R.A Kidwai Road Wadala"),
            Map.entry(14442, "Mumbai University Vakola Nala"),
            Map.entry(14413, "Andheri West"),
            Map.entry(12472, "BMC 08 MLD Sewage Treatment Plant")
    );

    private final List<Integer> stationIds = new ArrayList<>(stationMap.keySet());

    // ✅ Manual data entry (used by submit API)
    public WaterLevelData save(WaterLevelData data) {
        data.setTimestamp(LocalDateTime.now());
        return repository.save(data);
    }

    // ✅ Used by `/fetch-live` endpoint and scheduler
    public void fetchAndStoreWaterLevel() {
        String url = "https://app.aurassure.com/-/api/iot-platform/v1.1.0/clients/10082/applications/16/things/data";

        long currentEpoch = Instant.now().getEpochSecond();
        long fromEpoch = currentEpoch - 900; // 15 minutes ago

        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Id", "lX1d9akADFVLiYhB");
        headers.set("Access-Key", "NsKeyQDu9zgbED9KINEeYhIvRzbcSr1VKtDhbTMaUQMlAtPA8sOyjDm8Q85CBH9d");
        headers.setContentType(MediaType.APPLICATION_JSON);

        for (Integer stationId : stationIds) {
            String requestBody = "{\n" +
                    "    \"data_type\": \"raw\",\n" +
                    "    \"aggregation_period\": 0,\n" +
                    "    \"parameters\": [\"us_mb\"],\n" +
                    "    \"parameter_attributes\": [],\n" +
                    "    \"things\": [" + stationId + "],\n" +
                    "    \"from_time\": " + fromEpoch + ",\n" +
                    "    \"upto_time\": " + currentEpoch + "\n" +
                    "}";

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();

            try {
                ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

                System.out.println("Raw API Response for station " + stationId + ": " + response.getBody());

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.getBody().get("data");

                    for (Map<String, Object> entry : dataList) {
                        Map<String, String> paramValues = (Map<String, String>) entry.get("parameter_values");

                        for (Map.Entry<String, String> param : paramValues.entrySet()) {
                            WaterLevelData data = new WaterLevelData();
                            data.setThingId((Integer) entry.get("thing_id"));
                            data.setParameter(param.getKey());
                            data.setValue(param.getValue());
                            data.setStationName(stationMap.getOrDefault(stationId, "Unknown"));

                            // Convert epoch seconds to LocalDateTime
                            long timestampSeconds = ((Number) entry.get("time")).longValue();
                            data.setTimestamp(LocalDateTime.ofInstant(Instant.ofEpochSecond(timestampSeconds), TimeZone.getDefault().toZoneId()));

                            repository.save(data);
                        }
                    }
                }

            } catch (Exception e) {
                System.err.println("Failed to fetch for station " + stationId + ": " + e.getMessage());
            }
        }
    }

    // ✅ Scheduler runs every 15 minutes
    @Scheduled(fixedRate = 900000)
    public void fetchWaterLevelScheduled() {
        fetchAndStoreWaterLevel();
    }

    // ✅ Return all saved data
    public List<WaterLevelData> getAll() {
        return repository.findAll();
    }
}
