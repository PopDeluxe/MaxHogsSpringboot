package com.popdeluxe.maxhogs.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.popdeluxe.maxhogs.hogs.Hog;
import com.popdeluxe.maxhogs.hogs.Hogs;
import com.popdeluxe.maxhogs.hogs.RiverRace;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/maxhogs/merged")
public class TotalHogsv2 {

    //@Autowired
    //@Qualifier("getMaxHogMembers")
    //private RestTemplate restTemplate;

    //private List<Hog> allhawgs; // = new ArrayList<Hog>();
    private Map<String, Hog> allhawgs;

    @Autowired ObjectMapper objectMapper;


    @CrossOrigin
    @GetMapping
    public ResponseEntity get() throws IOException {


        getCurrentHogs();


/*
        getRiverRaceLogs();


        //convert Map back to List
        List<Hog> h = new ArrayList<Hog>(allhawgs.values());

        //sort hogs
        Collections.sort(h);

        Gson gson = new Gson();
        String response = gson.toJson(h);


 */

        String miracle = "miracle has just happened";

        return new ResponseEntity(miracle, HttpStatus.OK);

    }


    private void getCurrentHogs() throws IOException {

        System.out.println("in current hogs method");

        String fixieUrl = "http://fixie:8WWkMPxq1vaNF7f@velodrome.usefixie.com:80";

        String[] fixieValues = fixieUrl.split("[/(:\\/@)/]+");
        String fixieUser = fixieValues[1];
        String fixiePassword = fixieValues[2];
        String fixieHost = fixieValues[3];
        int fixiePort = Integer.parseInt(fixieValues[4]);

        System.out.println("parsed fixie url");

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(fixieHost, fixiePort),
                new UsernamePasswordCredentials(fixieUser, fixiePassword));
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider).build();
        try {
            HttpHost proxy = new HttpHost(fixieHost, fixiePort);
            RequestConfig config = RequestConfig.custom()
                    .setProxy(proxy)
                    .build();
            //org.apache.http.HttpHeaders headers = new org.apache.http.HttpHeaders();

            HttpHost target = new HttpHost("api.clashroyale.com", 80, "https");

            HttpGet httpget = new HttpGet("/v1/clans/%23Y8JUGJPU/members");
            httpget.setConfig(config);
            httpget.setHeader(org.apache.http.HttpHeaders.CONTENT_TYPE, "application/json");
            httpget.setHeader(org.apache.http.HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6ImM0YjlhYTU5LTQ1M2EtNDU2Ny04N2M4LWQzYzcxMDhmODc2YiIsImlhdCI6MTYxMTQzOTc1Miwic3ViIjoiZGV2ZWxvcGVyLzc0ODY0MGQxLTE0ZDktZGE4MS0wMjBjLWEwMGU2MGI2YzdjZSIsInNjb3BlcyI6WyJyb3lhbGUiXSwibGltaXRzIjpbeyJ0aWVyIjoiZGV2ZWxvcGVyL3NpbHZlciIsInR5cGUiOiJ0aHJvdHRsaW5nIn0seyJjaWRycyI6WyI1NC4xNzMuMjI5LjIwMCJdLCJ0eXBlIjoiY2xpZW50In1dfQ.p0Oe_F9Bo9oC4J7Ote0iIva2jX6ukSHBbdTU6WlPrOLttdHD9rqawOA_yWJAPpGA8ZI0horPUXiZyxJ63PWW7Q");

            CloseableHttpResponse response = httpclient.execute(target, httpget);
            try {
                System.out.println(EntityUtils.toString(response.getEntity()));
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }


    }


    private void getRiverRaceLogs()  {




        final String baseUrl = "https://api.clashroyale.com/v1/clans/%23Y8JUGJPU/riverracelog";
        URI uri = null;
        try {
            uri = new URI(baseUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6ImM0YjlhYTU5LTQ1M2EtNDU2Ny04N2M4LWQzYzcxMDhmODc2YiIsImlhdCI6MTYxMTQzOTc1Miwic3ViIjoiZGV2ZWxvcGVyLzc0ODY0MGQxLTE0ZDktZGE4MS0wMjBjLWEwMGU2MGI2YzdjZSIsInNjb3BlcyI6WyJyb3lhbGUiXSwibGltaXRzIjpbeyJ0aWVyIjoiZGV2ZWxvcGVyL3NpbHZlciIsInR5cGUiOiJ0aHJvdHRsaW5nIn0seyJjaWRycyI6WyI1NC4xNzMuMjI5LjIwMCJdLCJ0eXBlIjoiY2xpZW50In1dfQ.p0Oe_F9Bo9oC4J7Ote0iIva2jX6ukSHBbdTU6WlPrOLttdHD9rqawOA_yWJAPpGA8ZI0horPUXiZyxJ63PWW7Q");


        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("http://fixie:8WWkMPxq1vaNF7f@velodrome.usefixie.com", 80));
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(proxy);

        RestTemplate restTemplate = new RestTemplate(requestFactory);


        HttpEntity<String> entity = new HttpEntity<String>(headers);

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





