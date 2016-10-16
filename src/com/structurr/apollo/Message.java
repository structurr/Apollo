package com.structurr.apollo;

import java.util.Date;

/**
 * Developer: Sam Javed<br/>
 * Project: Apollo<br/>
 * Date: 10/15/2016<br/>
 * Time: 4:32 PM<br/>
 */

public class Message {

    private String sender;
    private String text;
    private Date time;
    private boolean isMe;

    public Message(String text, boolean isMe) {
        this.text = text;
        this.isMe = isMe;
    }

    public Message(String sender, String text, Date time, boolean isMe) {
        this.sender = sender;
        this.text = text;
        this.time = time;
        this.isMe = isMe;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public Date getTime() {
        return time;
    }

    public boolean isMe() {
        return isMe;
    }
}
