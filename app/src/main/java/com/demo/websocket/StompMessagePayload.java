package com.demo.websocket;

import com.google.gson.annotations.SerializedName;

public class StompMessagePayload {
    @SerializedName("headers111")
    private String headers;

    @SerializedName("body")
    private String body;

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
