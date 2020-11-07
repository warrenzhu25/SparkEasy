package com.warren.backend.data.entity;

public enum Cluster {
    Prime_CO4("");

    Cluster(String url){
        this.url = url;
    }
    private String url;
}
