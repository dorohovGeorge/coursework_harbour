package com.example.service1.controller;

import com.example.service1.generator.Schedule;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Service1Controller {
    private final int COUNT_OF_SHIP = 1000;
    private static final Gson GSON = new Gson();
    private static String schedule;


    @GetMapping("/schedule/generate")
    public ResponseEntity<String> createSchedule() {
        return new ResponseEntity<>(schedule = GSON.toJson(new Schedule(COUNT_OF_SHIP)), HttpStatus.CREATED);
    }

    @GetMapping("/schedule")
    public ResponseEntity<String> getSchedule() {
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

}
