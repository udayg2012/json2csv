package com.example.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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
            indexHeaderMap = getHeaders(firstObject);
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                String csvRow = convertJsonObjectToCsv(jsonObject, indexHeaderMap);
                csvRows.add(csvRow);
            }
        } else {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            indexHeaderMap = getHeaders(jsonObject);
            String csvRow = convertJsonObjectToCsv(jsonObject, indexHeaderMap);
            csvRows.add(csvRow);
        }

        String headers = convertToHeaderLine(indexHeaderMap);
        StringBuilder csvOutput = new StringBuilder();
        csvOutput.append(headers).append("\n");
        for (String row : csvRows) {
            csvOutput.append(row).append("\n");
        }   
        return csvOutput.toString(); // Return the actual CSV output
    }

    private Map<Integer, String> getHeaders(JsonObject jsonObject) {
        Map<Integer, String> indexHeaderMap = new LinkedHashMap<>();
        jsonObject.entrySet().forEach(entry -> {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (value.isJsonPrimitive()) {
                indexHeaderMap.put(indexHeaderMap.size(), key);
            } else if (value.isJsonObject()) {
                JsonObject nestedObject = value.getAsJsonObject();
                Map<Integer, String> nestedHeaders = getHeaders(nestedObject);
                nestedHeaders.forEach((index, nestedKey) -> indexHeaderMap.put(indexHeaderMap.size(), key + "." + nestedKey));
            } else if (value.isJsonArray()) {
                JsonArray jsonArray = value.getAsJsonArray();
                if (jsonArray.size() > 0) {
                    for (int i=0; i < jsonArray.size(); i++) {
                        final int index = i;
                        JsonElement element = jsonArray.get(index);
                        if (element.isJsonObject()) {
                            JsonObject obj = element.getAsJsonObject();
                            Map<Integer, String> arrayHeaders = getHeaders(obj);
                            arrayHeaders.forEach((idx, arrayKey) -> indexHeaderMap.put(indexHeaderMap.size(), key + "[" + idx + "]" + "." + arrayKey));
                        } else {
                            indexHeaderMap.put(indexHeaderMap.size(), key + "[" + index + "]");
                        }
                    }
                }
            }
        });
        return indexHeaderMap;
    }

    private String convertToHeaderLine(Map<Integer, String> indexHeaderMap) {
        StringBuilder headerLine = new StringBuilder();
        for (int i = 0; i < indexHeaderMap.size(); i++) {
            headerLine.append(indexHeaderMap.get(i));
            if (i < indexHeaderMap.size() - 1) {
                headerLine.append(",");
            }
        }
        return headerLine.toString();
    }

    private String convertJsonObjectToCsv(JsonObject jsonObject, Map<Integer, String> indexHeaderMap) {
        Map<String, String> csvValueMap = convertJsonObjectToCsvMap(jsonObject);
        StringBuilder csvRow = new StringBuilder();
        for (int i = 0; i < indexHeaderMap.size(); i++) {
            String key = indexHeaderMap.get(i);
            if (csvValueMap.containsKey(key)) {
                csvRow.append(csvValueMap.get(key));
            } 
            if (i < indexHeaderMap.size() - 1) {
                csvRow.append(",");
            }
        }
        return csvRow.toString();
    }

    private Map<String, String> convertJsonObjectToCsvMap(JsonObject jsonObject) {
        Map<String, String> csvValueMap = new LinkedHashMap<>();
        // Implement processing logic here
        jsonObject.entrySet().forEach(entry -> {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            // Process each key-value pair
            if (value.isJsonPrimitive()) {
                csvValueMap.put(key, value.getAsString());
            } else if (value.isJsonObject()) {
                Map<String, String> nestedCsvValueMap = convertJsonObjectToCsvMap(value.getAsJsonObject());
                nestedCsvValueMap.forEach((nestedKey, nestedValue) -> 
                    csvValueMap.put(key + "." + nestedKey, nestedValue)
                );
            } else if (value.isJsonArray()) {
                JsonArray jsonArray = value.getAsJsonArray();
                if (jsonArray.size() > 0) {
                    for (int i=0; i < jsonArray.size(); i++) {
                        final int index = i;
                        JsonElement element = jsonArray.get(index);
                        if (element.isJsonObject()) {
                            JsonObject obj = element.getAsJsonObject();
                            Map<String, String> arrayCsvValueMap = convertJsonObjectToCsvMap(obj);
                            arrayCsvValueMap.forEach((arrayKey, arrayValue) -> 
                                csvValueMap.put(key + "[" + index + "]" + "." + arrayKey, arrayValue)
                            );
                        } else {
                            csvValueMap.put(key + "[" + index + "]", element.getAsString());
                        }
                    }
                }
            }
        });
        return csvValueMap;
    }
}
