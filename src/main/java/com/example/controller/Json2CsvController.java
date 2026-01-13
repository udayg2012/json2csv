package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.service.Json2CsvService;

@RestController
@RequestMapping("/json2csv")
public class Json2CsvController {

    @Autowired
    private Json2CsvService json2CsvService;

    /**
     * API endpoint to check aliveness
     * @return ResponseEntity
     */
    @GetMapping(value = "/ping")
    public ResponseEntity<Object> ping() {
        return new ResponseEntity<>("PONG", HttpStatus.OK);
    }

    @PostMapping(value = "/convert")
    public ResponseEntity<Object> convertJsonToCsv(@RequestBody String jsonInput) {
        String csvOutput = json2CsvService.convertJsonToCsv(jsonInput);
        return new ResponseEntity<>(csvOutput, HttpStatus.OK);
    }
}
