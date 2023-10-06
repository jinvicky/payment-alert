package com.jinvicky.telegrambot;

import com.jinvicky.telegrambot.component.AlertSender;
import com.jinvicky.telegrambot.component.TelegramAlert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TelegrambotApplicationTests {

    @Autowired
    private TelegramAlert telegramAlert;

    @Autowired
    private AlertSender alertSender;
    @Test
    void contextLoads() {
    }

    @Test
    void telegramTest () {
//        telegramAlert.sendAlert("test"); //
        alertSender.sendTelegram();
    }

}
