package com.jinvicky.telegrambot.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;


@Slf4j
@Service
public class PaypalService {

    @Value("${paypal.clientId}")
    private String clientId;
    @Value("${paypal.clientSecret}")
    private String clientSecret;
    @Value("${paypal.baseUrl}")
    private String baseUrl;

    public String getAccessToken () { // 계정 로그인
        JSONObject jsonObject = new JSONObject();
        JSONParser jsonParser = new JSONParser();

        String authString = clientId + ":" + clientSecret;
        String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());

        try {
            String url =  baseUrl + "/v1/oauth2/token";
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("accept", "application/json");
            con.setRequestProperty("accept-language", "en_US");
            con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            con.setRequestProperty("authorization", "basic " + encodedAuthString);
            String body = "grant_type=client_credentials";

            // Send request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(body);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            jsonObject = (JSONObject) jsonParser.parse(response.toString());
//            log.info("파트너 인증 토큰 발급 response: " + jsonObject.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (String) jsonObject.get("access_token");
    }

    //결제 (일단 나중에)
    public void checkoutOrder () throws IOException {
        URL url = new URL(baseUrl + "/v2/checkout/orders");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("PayPal-Request-Id", "7b92603e-77ed-4896-8e78-5dea2050476a");
        httpConn.setRequestProperty("Authorization", "Bearer " + getAccessToken()); // 엑세스 토큰 설정

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write("{ " +
                "\"intent\": \"CAPTURE\", " +
                "\"purchase_units\": [ " +
                "{" +
                " \"amount\": { \"currency_code\": \"USD\", \"value\": \"10.00\" } " +
                "} ]" + " }");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
//        System.out.println("응답 테스트...." + response);
    }

    //주문정보 조회
    public String getOrderById (String id) throws Exception {
        String orderUrl = baseUrl + "/v2/checkout/orders/" + id;

        URL url = new URL(orderUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");

        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("PayPal-Request-Id", "7b92603e-77ed-4896-8e78-5dea2050476a");
        httpConn.setRequestProperty("Authorization", "Bearer " + getAccessToken()); // 엑세스 토큰 설정

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";

        return response;
    }



}
