package com.example.webapplication.service;

import com.example.webapplication.model.User;
import com.example.webapplication.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {
    @Autowired
    private UserRepository userRepository;
    private static Map<String, String> stringToMap(String inputString) {
        Map<String, String> outputMap = new HashMap<>();
        String[] keyValuePairs = inputString.replaceAll("\\{","").replaceAll("\\}","").split(",");
        for (String pair : keyValuePairs) {
            String[] entry = pair.trim().split("=");
            outputMap.put(entry[0], entry[1]);
        }
        return outputMap;
    }
    public String createToken(String userId){
        Map <String,String> tokenJson=new HashMap();
        LocalDateTime now=LocalDateTime.now();
        tokenJson.put("user",userId);
        tokenJson.put("token",UUID.randomUUID().toString());
        tokenJson.put("createdDate", String.valueOf(now.toEpochSecond(ZoneOffset.UTC)));
        tokenJson.put("expiryDate", String.valueOf(now.plusMinutes(30).toEpochSecond(ZoneOffset.UTC)));
        return Base64.getEncoder().encodeToString(tokenJson.toString().getBytes());
    }
    private static Map tokenToMap(String base64Token) throws JsonProcessingException {
        Map<String, String> tokenMap=new HashMap<>();
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Token);
            String tokenJson = new String(decodedBytes, StandardCharsets.UTF_8);
            tokenMap=stringToMap(tokenJson);

        }catch (Exception e){
            e.printStackTrace();
        }
       return tokenMap;
    }
    public boolean validateToken(String token){
        try{
            Map tokenMap= tokenToMap(token);
            if (tokenMap.containsKey("expiryDate")){
                String expiryDate= (String) tokenMap.get("expiryDate");
                LocalDateTime dateTime = LocalDateTime.now();
                Integer timestamp =  Integer.parseInt(expiryDate);
                Integer currentTimestamp = Math.toIntExact(dateTime.toEpochSecond(ZoneOffset.UTC));
                if ( timestamp > currentTimestamp){
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
