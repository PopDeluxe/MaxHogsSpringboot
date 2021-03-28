package com.popdeluxe.maxhogs.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.popdeluxe.maxhogs.hogs.CurrentRiverRace;
import com.popdeluxe.maxhogs.hogs.Hog;
import com.popdeluxe.maxhogs.hogs.Hogs;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v4/maxhogs/merged")
public class TotalHogsv4 {

    //@Autowired
    //@Qualifier("getMaxHogMembers")
    //private RestTemplate restTemplate;

    //private List<Hog> allhawgs; // = new ArrayList<Hog>();
    private Map<String, Hog> allhawgs;

    @Autowired ObjectMapper objectMapper;


    @CrossOrigin
    @GetMapping
    public ResponseEntity get(){

        //local
        //String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6IjExYzQyMGU2LTU5ZWYtNDliYS1iNTc5LTk0OTU5ZWIyN2Q5NCIsImlhdCI6MTYxMjY1MjQxMSwic3ViIjoiZGV2ZWxvcGVyLzc0ODY0MGQxLTE0ZDktZGE4MS0wMjBjLWEwMGU2MGI2YzdjZSIsInNjb3BlcyI6WyJyb3lhbGUiXSwibGltaXRzIjpbeyJ0aWVyIjoiZGV2ZWxvcGVyL3NpbHZlciIsInR5cGUiOiJ0aHJvdHRsaW5nIn0seyJjaWRycyI6WyI2Ny4xOTEuMjM4LjQzIl0sInR5cGUiOiJjbGllbnQifV19.WGx2lB4nDjeK473sX5dR16-y-pzLaH4UAzjbZAVgTWWoDhtqQrQu87nV6nXmkmyB3UPC9qUpBh_nW4QQuA_v3g";

        //fixie
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6ImMzYjBhYzRiLWQxMzYtNGJlOS05MmZhLWUwNTA5YTE2YWI0MyIsImlhdCI6MTYxMTUzMDcwMiwic3ViIjoiZGV2ZWxvcGVyLzc0ODY0MGQxLTE0ZDktZGE4MS0wMjBjLWEwMGU2MGI2YzdjZSIsInNjb3BlcyI6WyJyb3lhbGUiXSwibGltaXRzIjpbeyJ0aWVyIjoiZGV2ZWxvcGVyL3NpbHZlciIsInR5cGUiOiJ0aHJvdHRsaW5nIn0seyJjaWRycyI6WyI1NC4xNzUuMjMwLjI1MiIsIjU0LjE3My4yMjkuMjAwIl0sInR5cGUiOiJjbGllbnQifV19.CyltTGl78sEV0hido5U4WEXPX2X4xxWS3R97w1WxflToHSNGcfDKs33Xm88T5Z2aMOvvBTh247AECf4oQafspA";

        getCurrentHogs(token);

        getRiverRaceLogs(token);


        //convert Map back to List
        List<Hog> h = new ArrayList<Hog>(allhawgs.values());

        //sort hogs
        Collections.sort(h);

        Gson gson = new Gson();
        String response = gson.toJson(h);

        return new ResponseEntity(h, HttpStatus.OK);

    }


    private void getCurrentHogs(String token){

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
        //local
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        //fixie
        //headers.add(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6ImM0YjlhYTU5LTQ1M2EtNDU2Ny04N2M4LWQzYzcxMDhmODc2YiIsImlhdCI6MTYxMTQzOTc1Miwic3ViIjoiZGV2ZWxvcGVyLzc0ODY0MGQxLTE0ZDktZGE4MS0wMjBjLWEwMGU2MGI2YzdjZSIsInNjb3BlcyI6WyJyb3lhbGUiXSwibGltaXRzIjpbeyJ0aWVyIjoiZGV2ZWxvcGVyL3NpbHZlciIsInR5cGUiOiJ0aHJvdHRsaW5nIn0seyJjaWRycyI6WyI1NC4xNzMuMjI5LjIwMCJdLCJ0eXBlIjoiY2xpZW50In1dfQ.p0Oe_F9Bo9oC4J7Ote0iIva2jX6ukSHBbdTU6WlPrOLttdHD9rqawOA_yWJAPpGA8ZI0horPUXiZyxJ63PWW7Q");

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        //ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        JsonNode result = restTemplate.exchange(uri, HttpMethod.GET, entity, JsonNode.class).getBody();


        Gson gson = new Gson();
        Hogs hawgs = gson.fromJson(result.toString(), Hogs.class);

        List<Hog> compositeHogList = new ArrayList<Hog>();

        System.out.println("v1 list size: " + hawgs.items.size());

        hawgs.items.forEach(hawg -> {

            Hog h = new Hog();
            h.setName(hawg.name);
            h.setTag(hawg.tag);
            h.setDonations(hawg.donations);
            h.setDonationsReceived(hawg.donationsReceived);
            h.setExpLevel(hawg.expLevel);
            h.setRole(hawg.role);
            h.setTrophies(hawg.trophies);

            if(hawg.donations >= 200) { h.setDonationScore(4); }
            else if (hawg.donations <200 && hawg.donations >= 100)  {h.setDonationScore(3);}
            else if (hawg.donations <100 && hawg.donations >= 50)  {h.setDonationScore(2);}
            else if (hawg.donations < 50 && hawg.donations > 0)  {h.setDonationScore(1);}
            else {h.setDonationScore(0);}


            compositeHogList.add(h);

        });




        allhawgs = compositeHogList.stream().collect(Collectors.toMap(Hog::getTag, hog -> hog));


    }


    private void getRiverRaceLogs(String token)  {




        // this entire block works
        RestTemplate restTemplate = new RestTemplate();

        final String baseUrl = "https://api.clashroyale.com/v1/clans/%23Y8JUGJPU/currentriverrace";
        URI uri = null;
        try {
            uri = new URI(baseUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        //local
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        //fixie
        //headers.add(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6ImM0YjlhYTU5LTQ1M2EtNDU2Ny04N2M4LWQzYzcxMDhmODc2YiIsImlhdCI6MTYxMTQzOTc1Miwic3ViIjoiZGV2ZWxvcGVyLzc0ODY0MGQxLTE0ZDktZGE4MS0wMjBjLWEwMGU2MGI2YzdjZSIsInNjb3BlcyI6WyJyb3lhbGUiXSwibGltaXRzIjpbeyJ0aWVyIjoiZGV2ZWxvcGVyL3NpbHZlciIsInR5cGUiOiJ0aHJvdHRsaW5nIn0seyJjaWRycyI6WyI1NC4xNzMuMjI5LjIwMCJdLCJ0eXBlIjoiY2xpZW50In1dfQ.p0Oe_F9Bo9oC4J7Ote0iIva2jX6ukSHBbdTU6WlPrOLttdHD9rqawOA_yWJAPpGA8ZI0horPUXiZyxJ63PWW7Q");

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        //ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);


        JsonNode result = restTemplate.exchange(uri, HttpMethod.GET, entity, JsonNode.class).getBody();


        Gson gson = new Gson();
        CurrentRiverRace r = gson.fromJson(result.toString(), CurrentRiverRace.class);


        List <CurrentRiverRace.Participant> hogs = r.clan.participants;

        hogs.forEach(hog -> {


            if(allhawgs.containsKey(hog.tag)) {


                //current river attacks
                int attacks = hog.decksUsed;
                int hogScore = 0;

                if(attacks>=12){ hogScore = 4; } //allhawgs.get(hawg.tag).setRiverScore(4);}
                else if(attacks>=8 && attacks<12){ hogScore = 3; }
                else if(attacks>=4 && attacks<8){ hogScore = 2; }
                else if(attacks>=1 && attacks<4) { hogScore = 1; }
                else if(attacks==0){ hogScore = 0; }


                allhawgs.get(hog.tag).setCurrentRiverAttacks(attacks);
                allhawgs.get(hog.tag).setHogScore(hogScore);

            }

        });




    }

    ////
    public boolean containsTag(List<Hog> list, String tag){
        return list.stream().anyMatch(o -> Objects.equals(o.getTag(), tag));
    }
    ////


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





