package com.test.demo.dto;

public class EvolutionDetail {
    private String evolvesTo;
    private String evolvesFrom;
    private String method;
    private Integer minLevel;
    private String item;
    private String trigger;
    private String otherDetails;

    public EvolutionDetail() {}

    public EvolutionDetail(String evolvesTo, String evolvesFrom, String method,
                           Integer minLevel, String item, String trigger, String otherDetails) {
        this.evolvesTo = evolvesTo;
        this.evolvesFrom = evolvesFrom;
        this.method = method;
        this.minLevel = minLevel;
        this.item = item;
        this.trigger = trigger;
        this.otherDetails = otherDetails;
    }

    public String getEvolvesTo() { return evolvesTo; }
    public void setEvolvesTo(String evolvesTo) { this.evolvesTo = evolvesTo; }

    public String getEvolvesFrom() { return evolvesFrom; }
    public void setEvolvesFrom(String evolvesFrom) { this.evolvesFrom = evolvesFrom; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public Integer getMinLevel() { return minLevel; }
    public void setMinLevel(Integer minLevel) { this.minLevel = minLevel; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public String getTrigger() { return trigger; }
    public void setTrigger(String trigger) { this.trigger = trigger; }

    public String getOtherDetails() { return otherDetails; }
    public void setOtherDetails(String otherDetails) { this.otherDetails = otherDetails; }
}
