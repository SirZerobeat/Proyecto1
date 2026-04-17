package com.test.demo.dto;

public class StatDetail {
    private String name;
    private int baseStat;

    public StatDetail() {}

    public StatDetail(String name, int baseStat) {
        this.name = name;
        this.baseStat = baseStat;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getBaseStat() { return baseStat; }
    public void setBaseStat(int baseStat) { this.baseStat = baseStat; }
}
