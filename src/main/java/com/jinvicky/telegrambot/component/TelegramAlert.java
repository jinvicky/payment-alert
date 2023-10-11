package com.jinvicky.telegrambot.component;



import com.google.gson.Gson;
import com.jinvicky.telegrambot.domain.AlertMsg;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Component
public class TelegramAlert {

    private Logger logger = LoggerFactory.getLogger(TelegramAlert.class);

    @Value("${notification.telegram.enabled}")
    private boolean telegramEnabled;

    @Value("${notification.telegram.bot.token}")
    private String token;

    @Value("${notification.telegram.chat.id}")
    private String chatId;

    public void sendAlert (String contents) {
        BufferedReader in = null;
        String url = "https://api.telegram.org/bot" + token + "/sendMessage?chat_id="+ chatId + "&text=";

        try {
            // 메세지 생성
            StringBuffer sb = new StringBuffer();
            sb.append("주문 결과 : ").append(contents);

            url += sb.toString();

            URL obj = new URL(url); // 호출할 url
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();

            con.setRequestMethod("GET");

            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            String line;
            StringBuffer buffer = new StringBuffer();
            while((line = in.readLine()) != null) { // response를 차례대로 출력
                buffer.append(line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 다른 방법
    public void sendAlert2 (String contents) {
        String url = "https://api.telegram.org/bot" + token + "/sendMessage";
//
        try {
            AlertMsg telegramAlert = new AlertMsg(chatId, contents);
            String param = new Gson().toJson(telegramAlert);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<String> entity = new HttpEntity<>(param, headers); // <> 타입 에러 발생
            restTemplate.postForEntity(url, entity, String.class);
        } catch (Exception e) {
            logger.error("telegram alert send failed", e);
        }
    }


    private String generatedMessage() {
        StringBuilder sb = new StringBuilder();

        sb.append("[Notification]").append(System.getProperty("line.separator"))
                .append("[Name] : ").append("Tester").append(System.getProperty("line.separator"))
                .append("[Message] : ").append("테스트 메세지 !!");
        return sb.toString();
    }

    public void sendTelegram() {
        logger.info("#### send Notification. ");

        String contents = generatedMessage();

        //send telegram
        sendAlert2(contents);
    }

}
