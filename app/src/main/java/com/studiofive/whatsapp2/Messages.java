package com.studiofive.whatsapp2;

public class Messages {
    private String from;
    private String message;
    private String type;
    private String to;
    private String messageId;
    private String time;
    private String date;
    private String name;

    public Messages() {
    }

    public Messages(String from, String message, String type, String to, String messageId, String time, String date, String name) {
        this.from = from;
        this.message =message;
        this.type = type;
        this.to = to;
        this.messageId = messageId;
        this.time = time;
        this.date = date;
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
