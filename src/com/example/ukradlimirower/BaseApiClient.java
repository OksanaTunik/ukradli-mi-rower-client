package com.example.ukradlimirower;

public class BaseApiClient {
    protected static String apiUrl = "http://ukradli-mi-rower.eu-gb.mybluemix.net/api";

    public static String getUrl(String postfix) {
        return apiUrl + postfix;
    }
}
