package com.example.util;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Json2CsvConvertUtil {

    public static Map<Integer, String> getHeaders(JsonObject jsonObject) {
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

    public static String convertToHeaderLine(Map<Integer, String> indexHeaderMap) {
        StringBuilder headerLine = new StringBuilder();
        for (int i = 0; i < indexHeaderMap.size(); i++) {
            headerLine.append(indexHeaderMap.get(i));
            if (i < indexHeaderMap.size() - 1) {
                headerLine.append(",");
            }
        }
        return headerLine.toString();
    }

    public static String convertJsonObjectToCsv(JsonObject jsonObject, Map<Integer, String> indexHeaderMap) {
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

    public static Map<String, String> convertJsonObjectToCsvMap(JsonObject jsonObject) {
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
