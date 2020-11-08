package com.warren.backend.data.entity;

public enum Cluster {
    Prime_CO4("http://livy0-ivip.mtprime-prod-co4.co4.ap.gbl");

    Cluster(String url){
        this.url = url;
    }
    private final String url;

    public String getUrl() {
        return url;
    }
}
