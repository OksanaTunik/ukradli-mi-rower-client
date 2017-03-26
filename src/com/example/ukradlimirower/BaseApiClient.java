package com.example.ukradlimirower;

public class BaseApiClient {
    //protected static String publicUrl = "http://ukradli-mi-rower.eu-gb.mybluemix.net";

    public final static String publicUrl = "http://192.168.56.1:3000";
    public final static String apiUrl = publicUrl + "/api";

    public static String getUrl(String postfix) {
        return apiUrl + postfix;
    }
}
