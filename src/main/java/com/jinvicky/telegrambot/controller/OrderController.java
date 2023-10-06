package com.jinvicky.telegrambot.controller;


import com.jinvicky.telegrambot.component.AlertSender;
import com.jinvicky.telegrambot.component.TelegramAlert;
import com.jinvicky.telegrambot.service.PaypalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Slf4j
@Controller
@RequestMapping("/order")
public class OrderController {

    @Value("${paypal.clientId}")
    private String clientId;

    @Value("${paypal.baseUrl}")
    private String baseUrl;

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private AlertSender alertSender;

    @Autowired
    private TelegramAlert telegramAlert;

    @GetMapping("/paypal")
    public String orderPayPalView(Model m) {
//        m.addAttribute("clientId", clientId); //sdk js를 위한 clientId 저장
//        try {
//            paypalService.checkoutOrder();
//        } catch (IOException e) {
//            log.error("paypay order error");
//        }
        return "paypalOrder";
    }
    
    // 결제
    @PostMapping("/checkout")
    public String order (String id) throws IOException {

        return "redirect:/detail?id=" + id;
    }

    @GetMapping("/detail")
    public String orderDetailView(String id, Model m) throws Exception{ //페이팔 주문(결제) 아이디
        //https://api-m.sandbox.paypal.com/v2/checkout/orders/4SM60893S32948104
        String orderUrl = baseUrl + "/v2/checkout/orders/" + id;
        
        // get 호출해서 응답값 가져와서 금액이랑 정보들 가져오기
        String resp =  paypalService.getOrderById(id);

        // model에 담기
        m.addAttribute("detail", resp);
        // telegram 알림 보내기 (관리자 수집용)
//        telegramAlert.sendAlert(resp); ok

        return "orderDetail";
    }
}
