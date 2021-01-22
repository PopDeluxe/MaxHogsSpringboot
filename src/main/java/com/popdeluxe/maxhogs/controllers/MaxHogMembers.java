package com.popdeluxe.maxhogs.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.popdeluxe.maxhogs.hogs.Hog;
import com.popdeluxe.maxhogs.hogs.Hogs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

@RestController
@RequestMapping("/api/v1/maxhogs/clan")
public class MaxHogMembers {

    //@Autowired
    //@Qualifier("getMaxHogMembers")
    //private RestTemplate restTemplate;

    @Autowired ObjectMapper objectMapper;


    @CrossOrigin
    @GetMapping
    public ResponseEntity get(){

        //List<Hog> hogs = (List<Hog>) restTemplate.getForObject(
        //        "https://api.clashroyale.com/v1/clans/%23Y8JUGJPU/members", Hog.class);


        //return hogs;



        return getHogs();

    }



    private static ResponseEntity getHogs()  {



        // this entire block works
        RestTemplate restTemplate = new RestTemplate();

        final String baseUrl = "https://api.clashroyale.com/v1/clans/%23Y8JUGJPU/members";
        URI uri = null;
        try {
            uri = new URI(baseUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6IjZkYjRmYTIyLTA5MmQtNGIzNC04MzFlLTI2YzQzZmQ3NjJjMCIsImlhdCI6MTYxMDY3MjI5MSwic3ViIjoiZGV2ZWxvcGVyLzc0ODY0MGQxLTE0ZDktZGE4MS0wMjBjLWEwMGU2MGI2YzdjZSIsInNjb3BlcyI6WyJyb3lhbGUiXSwibGltaXRzIjpbeyJ0aWVyIjoiZGV2ZWxvcGVyL3NpbHZlciIsInR5cGUiOiJ0aHJvdHRsaW5nIn0seyJjaWRycyI6WyI2Ny4xOTEuMjM4LjQzIl0sInR5cGUiOiJjbGllbnQifV19.kUvajlSm2cpb-A3k-sxNh7F3SRCqFFeVPXKcyioNkfPEEjGbgRr4VLOUJqSFT1iITuy3rg9kwiOaJBIS9z3tSg");

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);


        JsonNode result = restTemplate.exchange(uri, HttpMethod.GET, entity, JsonNode.class).getBody();


        Gson gson = new Gson();
        Hogs hawgs = gson.fromJson(result.toString(), Hogs.class);

        /*
        hawgs.items.stream().forEach( (hog) ->
                System.out.println("name: " + hog.name + "  donations: " + hog.donations)

        );
*/



        return ResponseEntity.ok().body(result);



        //return response.getBody();
        //Use the response.getBody()

    }


    public JSONObject mergeJSONObjects(JSONObject json1, JSONObject json2) {
        JSONObject mergedJSON = new JSONObject();

        try {
            mergedJSON = new JSONObject(json1, JSONObject.getNames(json1));
            for (String name : JSONObject.getNames(json2)) {
                mergedJSON.put(name, json2.get(name));
            }

        } catch (JSONException e) {
            throw new RuntimeException("JSON Exception" + e);
        }
        return mergedJSON;
    }


}





