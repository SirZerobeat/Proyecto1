package com.test.demo.dto;

import java.util.List;

public class PokemonSpeciesInfo {
    private double malePercentage;
    private double femalePercentage;
    private List<String> eggGroups;
    private int hatchSteps;
    private int baseHappiness;
    private String growthRate;
    private int captureRate;

    public PokemonSpeciesInfo() {}

    public PokemonSpeciesInfo(double malePercentage, double femalePercentage,
                              List<String> eggGroups, int hatchSteps, int baseHappiness,
                              String growthRate, int captureRate) {
        this.malePercentage = malePercentage;
        this.femalePercentage = femalePercentage;
        this.eggGroups = eggGroups;
        this.hatchSteps = hatchSteps;
        this.baseHappiness = baseHappiness;
        this.growthRate = growthRate;
        this.captureRate = captureRate;
    }

    public double getMalePercentage() { return malePercentage; }
    public void setMalePercentage(double malePercentage) { this.malePercentage = malePercentage; }

    public double getFemalePercentage() { return femalePercentage; }
    public void setFemalePercentage(double femalePercentage) { this.femalePercentage = femalePercentage; }

    public List<String> getEggGroups() { return eggGroups; }
    public void setEggGroups(List<String> eggGroups) { this.eggGroups = eggGroups; }

    public int getHatchSteps() { return hatchSteps; }
    public void setHatchSteps(int hatchSteps) { this.hatchSteps = hatchSteps; }

    public int getBaseHappiness() { return baseHappiness; }
    public void setBaseHappiness(int baseHappiness) { this.baseHappiness = baseHappiness; }

    public String getGrowthRate() { return growthRate; }
    public void setGrowthRate(String growthRate) { this.growthRate = growthRate; }

    public int getCaptureRate() { return captureRate; }
    public void setCaptureRate(int captureRate) { this.captureRate = captureRate; }
}
