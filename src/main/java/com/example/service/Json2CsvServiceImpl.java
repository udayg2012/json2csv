package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.util.Json2CsvConvertUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class Json2CsvServiceImpl implements Json2CsvService {
    
    @Override
    public String convertJsonToCsv(String jsonInput) {
        Map<Integer, String> indexHeaderMap;
        List<String> csvRows = new ArrayList<>();
        
        JsonElement jsonElement = JsonParser.parseString(jsonInput);
        if (!jsonElement.isJsonObject() && !jsonElement.isJsonArray()) {
            throw new IllegalArgumentException("Invalid JSON input");
        }
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            if (jsonArray.size() == 0) {
                return ""; // Return empty CSV for empty JSON array
            }
            // Assuming all objects in the array have the same structure
            JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
            indexHeaderMap = Json2CsvConvertUtil.getHeaders(firstObject);
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                String csvRow = Json2CsvConvertUtil.convertJsonObjectToCsv(jsonObject, indexHeaderMap);
                csvRows.add(csvRow);
            }
        } else {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            indexHeaderMap = Json2CsvConvertUtil.getHeaders(jsonObject);
            String csvRow = Json2CsvConvertUtil.convertJsonObjectToCsv(jsonObject, indexHeaderMap);
            csvRows.add(csvRow);
        }

        String headers = Json2CsvConvertUtil.convertToHeaderLine(indexHeaderMap);
        StringBuilder csvOutput = new StringBuilder();
        csvOutput.append(headers).append("\n");
        for (String row : csvRows) {
            csvOutput.append(row).append("\n");
        }   
        return csvOutput.toString(); // Return the actual CSV output
    }
}
