package com.warren.backend.data.entity;

public enum Cluster {
    Prime_CO4_0("mtprime-prod-co4", "co4", 0);

    private static final String HISTORY_URL_PATTERN = "http://proxy%s-ivip.%s.%s.ap.gbl:81/proxy/";
    private static final String LIVY_URL_PATTERN = "http://livy%s-ivip.%s.%s.ap.gbl";

    private final String name;
    private final String dc;
    private final int subcluster;

    Cluster(String name, String dc, int subcluster) {
        this.name = name;
        this.dc = dc;
        this.subcluster = subcluster;
    }

    public String getLivyUrl() {
        return String.format(LIVY_URL_PATTERN, subcluster, name, dc);
    }
    public String getHistoryUrl() {
        return String.format(HISTORY_URL_PATTERN, subcluster, name, dc);
    }

}
