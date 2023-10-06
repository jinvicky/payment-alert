package com.jinvicky.telegrambot.domain;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AlertMsg {

    @SerializedName("chat_id")
    private String chatId;
    private String text;

    public AlertMsg () {}
}
