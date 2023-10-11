package com.jinvicky.telegrambot.controller;


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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.Map;

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
        log.info("{}...", id);
        return "redirect:/detail?id=" + id;
    }

    @GetMapping("/detail")
    public String orderDetailView(String id, Model m) throws Exception{ //페이팔 주문(결제) 아이디
        // get 호출해서 응답값 가져와서 금액이랑 정보들 가져오기
        String resp =  paypalService.getOrderById(id);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(resp);
//        JSONObject jsonObj = (JSONObject) obj;

        // model에 담기
        m.addAttribute("orderDetail", obj);
        // telegram 알림 보내기 (관리자 수집용)
//        telegramAlert.sendAlert(resp); //ok

        return "orderDetail";
    }
}
