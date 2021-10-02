package com.example.service2.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@RestController
public class Service2Controller {
    private static final Gson GSON = new Gson();
    private static final String SCHEDULE_FILE = "src/main/resources/schedule_file.json";

    Service2Controller() {
        RestTemplate restTemplate = new RestTemplate();
        String httpRequest = "http://localhost:8081/schedule/generate";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(httpRequest, String.class);
        String schedule = responseEntity.getBody();
        try (FileWriter fileWriter = new FileWriter(SCHEDULE_FILE)) {
            assert schedule != null;
            fileWriter.write(schedule);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/schedules")
    public ResponseEntity<String> getJSON() {
        return getJSON(SCHEDULE_FILE);
    }

    @GetMapping("/schedules/custom_path")
    public ResponseEntity<String> getJSON(@RequestParam(value = "filename", defaultValue = SCHEDULE_FILE) String fileName) {
        try (FileReader fileReader = new FileReader(fileName)) {
            JsonElement jsonElement = GSON.fromJson(fileReader, JsonElement.class);
            return new ResponseEntity<>(GSON.toJson(jsonElement), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "File not found");
    }

    @PostMapping(value = "/schedules/save", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> saveResults(@RequestBody String result) {
        try (FileWriter fileWriter = new FileWriter("src/main/resources/result.json")) {
            assert result != null;
            fileWriter.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
