package com.example.service3.controller;

import com.example.service3.generator.Schedule;
import com.example.service3.simulator.HarbourSimulation;
import com.google.gson.Gson;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class Service3Controller {
    private final int[] CRANES_COUNT = {3, 3, 3};
    private final Gson GSON = new Gson();
    private final String statistic;
    private RestTemplate restTemplate;
    private static final String SCHEDULE_FILE = "src/main/resources/schedule_file.json";

    Service3Controller() {
        restTemplate = new RestTemplate();
        String httpRequest = "http://localhost:8082/schedules";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(httpRequest, String.class);
        Schedule schedule = GSON.fromJson(responseEntity.getBody(), Schedule.class);
        statistic = GSON.toJson(HarbourSimulation.analyzeDate(schedule, CRANES_COUNT));
    }

    @GetMapping("/statistics")
    public ResponseEntity<String> getJSON() {
        return new ResponseEntity<>(GSON.toJson(statistic), HttpStatus.OK);
    }


    @PostMapping(value = "/statistics", produces = "application/json")
    public ResponseEntity<String> sendStatistics() {
        restTemplate = new RestTemplate();
        String results = statistic;
        String url = "http://localhost:8082/schedules/save";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(results, httpHeaders);
        restTemplate.postForEntity(url, request, String.class);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

}
