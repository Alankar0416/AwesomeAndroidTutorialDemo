package com.alankar.awesometutorials;
/**
 * Created by alankargupta on 07/09/16.
 */

public enum DemoType {
    SAMPLE_INTRODUCTION("DEMO_SAMPLE_INTRODUCTION"),
    QUOTATION("QUOTATION"),
    LOCATION_MAP("LOCATION_MAP"),
    WORK_PHOTOS("WORK_PHOTOS");

    private String key;

    DemoType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
