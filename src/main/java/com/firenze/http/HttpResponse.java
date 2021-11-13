package com.firenze.http;

public class HttpResponse {
    private final Integer statusCode;
    private final String status;
    private final String data;

    private HttpResponse(Integer statusCode, String status, String data) {
        this.statusCode = statusCode;
        this.status = status;
        this.data = data;
    }

    public static HttpResponse ok(String data) {
        return new HttpResponse(200, "OK", data);
    }

    public static HttpResponse error(String errorMessage) {
        return new HttpResponse(500, "Internal Service Error", errorMessage);
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "statusCode=" + statusCode +
                ", status='" + status + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getStatus() {
        return status;
    }

    public String getData() {
        return data;
    }
}
