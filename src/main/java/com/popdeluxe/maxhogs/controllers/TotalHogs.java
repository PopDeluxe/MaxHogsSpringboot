package com.popdeluxe.maxhogs.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.popdeluxe.maxhogs.hogs.Hog;
import com.popdeluxe.maxhogs.hogs.Hogs;

import com.popdeluxe.maxhogs.hogs.RiverRace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

@RestController
@RequestMapping("/api/v1/maxhogs/merged")
public class TotalHogs {

    //@Autowired
    //@Qualifier("getMaxHogMembers")
    //private RestTemplate restTemplate;

    //private List<Hog> allhawgs; // = new ArrayList<Hog>();
    private Map<String, Hog> allhawgs;

    @Autowired ObjectMapper objectMapper;


    @CrossOrigin
    @GetMapping
    public ResponseEntity get(){


        getCurrentHogs();

        getRiverRaceLogs();


        //convert Map back to List
        List<Hog> h = new ArrayList<Hog>(allhawgs.values());

        //sort hogs
        Collections.sort(h);

        Gson gson = new Gson();
        String response = gson.toJson(h);

        return new ResponseEntity(h, HttpStatus.OK);

    }


    private void getCurrentHogs(){
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

        List<Hog> compositeHogList = new ArrayList<Hog>();

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


    private void getRiverRaceLogs()  {




        // this entire block works
        RestTemplate restTemplate = new RestTemplate();

        final String baseUrl = "https://api.clashroyale.com/v1/clans/%23Y8JUGJPU/riverracelog";
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
        RiverRace r = gson.fromJson(result.toString(), RiverRace.class);

        List<RiverRace.Item> itm = r.items;



        itm.forEach(season -> {
            System.out.print(season.seasonId + " ");

            List<RiverRace.Standing> standings = season.standings;

            standings.forEach(standing -> {

                if(standing.clan.name.equals("Max Hogs")) {

                    List<RiverRace.Participant> hawgs = standing.clan.participants;

                    hawgs.forEach(hawg -> {

                        /*
                        if(containsTag(allhawgs, hawg.tag)) {

                            System.out.println(hawg.name + " found in clan");


                        }
                        else{
                            System.out.println(hawg.name + " not found");
                        }
                        */

                        if(allhawgs.containsKey(hawg.tag)) {

                            //update master Hog map with weekly stats

                            //fame
                            int fame = allhawgs.get(hawg.tag).getFame();
                            allhawgs.get(hawg.tag).setFame(fame + hawg.fame);




                            //repairs
                            int repairs = allhawgs.get(hawg.tag).getRepairs();
                            allhawgs.get(hawg.tag).setRepairs(repairs + hawg.repairPoints);

                            //boat attacks
                            int boatAttacks = allhawgs.get(hawg.tag).getBoatAttacks();
                            allhawgs.get(hawg.tag).setBoatAttacks(boatAttacks + hawg.boatAttacks);





                            //System.out.println(hawg.name + " found in clan");
                        }
                        else{
                            //System.out.println(hawg.name + " not found");
                        }

                    });


                    allhawgs.entrySet().stream().forEach(hog -> {

                        int fame = hog.getValue().getFame();
                        int fameScore = 0;



                        if(fame>10000){ fameScore = 4; } //allhawgs.get(hawg.tag).setRiverScore(4);}
                        else if(fame>=5000 && fame<10000){ fameScore = 3; }
                        else if(fame>=1000 && fame<5000){ fameScore = 2; }
                        else if(fame>0 && fame<1000){ fameScore = 1; }
                        else if(fame==0){ fameScore = 0; }

                        hog.getValue().setRiverScore(fameScore);
                        //allhawgs.get(hawg.tag).setRiverScore(fameScore);


                        //calculate Hog Score (unrounded and rounded)
                        int donationScore = hog.getValue().getDonationScore();
                        hog.getValue().setHogScore( (fameScore + donationScore) /  2 );

                        BigDecimal unroundedHogScore = BigDecimal.valueOf((Double.valueOf(fameScore) + Double.valueOf(donationScore) )/  2);

                        System.out.println(hog.getValue().getName() + "==>");

                        System.out.println("fame: " + fame);
                        System.out.println("donations: " + hog.getValue().getDonations());
                        System.out.println("fame score: " + fameScore );
                        System.out.println("donation score: " + donationScore );
                        System.out.println("calc'd hog score (rounded): " + (fameScore + donationScore) /  2);
                        System.out.println("calc'd hog score (unrounded): " + unroundedHogScore);
                        System.out.println("----");
                        System.out.println("");

                        hog.getValue().setUnroundedHogScore( unroundedHogScore);

                    });




                }
            });





        });





        //return ResponseEntity.ok().body(result);

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





