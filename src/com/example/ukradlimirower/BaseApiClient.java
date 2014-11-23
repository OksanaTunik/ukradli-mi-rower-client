package com.example.ukradlimirower;

public class BaseApiClient {
    // protected static String apiUrl = "http://ukradli-mi-rower.eu-gb.mybluemix.net/api";
    protected static String apiUrl = "http://192.168.56.1:3000/api";

    public static String getUrl(String postfix) {
        return apiUrl + postfix;
    }
}
