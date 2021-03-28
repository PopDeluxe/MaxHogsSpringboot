package com.popdeluxe.maxhogs.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.popdeluxe.maxhogs.hogs.CurrentRiverRace;
import com.popdeluxe.maxhogs.hogs.Hog;
import com.popdeluxe.maxhogs.hogs.Hogs;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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

    //local
    //String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6IjExYzQyMGU2LTU5ZWYtNDliYS1iNTc5LTk0OTU5ZWIyN2Q5NCIsImlhdCI6MTYxMjY1MjQxMSwic3ViIjoiZGV2ZWxvcGVyLzc0ODY0MGQxLTE0ZDktZGE4MS0wMjBjLWEwMGU2MGI2YzdjZSIsInNjb3BlcyI6WyJyb3lhbGUiXSwibGltaXRzIjpbeyJ0aWVyIjoiZGV2ZWxvcGVyL3NpbHZlciIsInR5cGUiOiJ0aHJvdHRsaW5nIn0seyJjaWRycyI6WyI2Ny4xOTEuMjM4LjQzIl0sInR5cGUiOiJjbGllbnQifV19.WGx2lB4nDjeK473sX5dR16-y-pzLaH4UAzjbZAVgTWWoDhtqQrQu87nV6nXmkmyB3UPC9qUpBh_nW4QQuA_v3g";

    //fixie
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6ImMzYjBhYzRiLWQxMzYtNGJlOS05MmZhLWUwNTA5YTE2YWI0MyIsImlhdCI6MTYxMTUzMDcwMiwic3ViIjoiZGV2ZWxvcGVyLzc0ODY0MGQxLTE0ZDktZGE4MS0wMjBjLWEwMGU2MGI2YzdjZSIsInNjb3BlcyI6WyJyb3lhbGUiXSwibGltaXRzIjpbeyJ0aWVyIjoiZGV2ZWxvcGVyL3NpbHZlciIsInR5cGUiOiJ0aHJvdHRsaW5nIn0seyJjaWRycyI6WyI1NC4xNzUuMjMwLjI1MiIsIjU0LjE3My4yMjkuMjAwIl0sInR5cGUiOiJjbGllbnQifV19.CyltTGl78sEV0hido5U4WEXPX2X4xxWS3R97w1WxflToHSNGcfDKs33Xm88T5Z2aMOvvBTh247AECf4oQafspA";

    String fixieUrl = "http://fixie:8WWkMPxq1vaNF7f@velodrome.usefixie.com:80";

    String[] fixieValues = fixieUrl.split("[/(:\\/@)/]+");
    String fixieUser = fixieValues[1];
    String fixiePassword = fixieValues[2];
    String fixieHost = fixieValues[3];
    int fixiePort = Integer.parseInt(fixieValues[4]);



    @Autowired ObjectMapper objectMapper;


    @CrossOrigin
    @GetMapping
    public ResponseEntity get() throws IOException {




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


    private void getCurrentHogs(String token) throws IOException {

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

            HttpGet getRequest = new HttpGet("https://api.clashroyale.com/v1/clans/%23Y8JUGJPU/members");
            getRequest.setConfig(config);
            getRequest.setHeader(org.apache.http.HttpHeaders.CONTENT_TYPE, "application/json");
            getRequest.setHeader(org.apache.http.HttpHeaders.AUTHORIZATION, "Bearer " + token);

            //System.out.println("about to call royale api");

            HttpResponse response = httpclient.execute(getRequest);

            String apiResponse = EntityUtils.toString(response.getEntity());
            System.out.println(apiResponse);

            Gson gson = new Gson();
            Hogs hawgs = gson.fromJson(apiResponse, Hogs.class);

            System.out.println("after gson");


            List<Hog> compositeHogList = new ArrayList<Hog>();

            System.out.println("hawg size: " + hawgs.items.size());

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

        } finally {
            httpclient.close();
        }



    }


    private void getRiverRaceLogs(String token) throws IOException {


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

            HttpGet getRequest = new HttpGet("https://api.clashroyale.com/v1/clans/%23Y8JUGJPU/currentriverrace");
            getRequest.setConfig(config);
            getRequest.setHeader(org.apache.http.HttpHeaders.CONTENT_TYPE, "application/json");
            getRequest.setHeader(org.apache.http.HttpHeaders.AUTHORIZATION, "Bearer " + token);

            System.out.println("about to call royale api");

            HttpResponse response = httpclient.execute(getRequest);

            String apiResponse = EntityUtils.toString(response.getEntity());


            Gson gson = new Gson();
            CurrentRiverRace r = gson.fromJson(apiResponse.toString(), CurrentRiverRace.class);


            List<CurrentRiverRace.Participant> hogs = r.clan.participants;

            hogs.forEach(hog -> {


                if (allhawgs.containsKey(hog.tag)) {


                    //current river attacks
                    int attacks = hog.decksUsed;
                    int hogScore = 0;

                    if (attacks >= 12) {
                        hogScore = 4;
                    } //allhawgs.get(hawg.tag).setRiverScore(4);}
                    else if (attacks >= 8 && attacks < 12) {
                        hogScore = 3;
                    } else if (attacks >= 4 && attacks < 8) {
                        hogScore = 2;
                    } else if (attacks >= 1 && attacks < 4) {
                        hogScore = 1;
                    } else if (attacks == 0) {
                        hogScore = 0;
                    }


                    allhawgs.get(hog.tag).setCurrentRiverAttacks(attacks);
                    allhawgs.get(hog.tag).setHogScore(hogScore);

                }

            });


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
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





