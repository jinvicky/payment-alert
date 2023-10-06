package com.jinvicky.telegrambot.component;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AlertSender {

    private Logger logger = LoggerFactory.getLogger(AlertSender.class);

    private final TelegramAlert telegramAlert;

    public AlertSender(TelegramAlert telegramAlert) {
        this.telegramAlert = telegramAlert;
    }

    public void sendTelegram() {
        logger.info("#### send Notification. ");

        String contents = generatedMessage();

        //send telegram
        telegramAlert.sendAlert2(contents);
    }

    private String generatedMessage() {
        StringBuilder sb = new StringBuilder();

        sb.append("[Notification]").append(System.getProperty("line.separator"))
                .append("[Name] : ").append("Tester").append(System.getProperty("line.separator"))
                .append("[Message] : ").append("테스트 메세지 !!");
        return sb.toString();
    }

}
