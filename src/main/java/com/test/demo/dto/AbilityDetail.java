package com.test.demo.dto;

public class AbilityDetail {
    private String name;
    private boolean hidden;
    private String description;
    private String effect;

    public AbilityDetail() {}

    public AbilityDetail(String name, boolean hidden, String description, String effect) {
        this.name = name;
        this.hidden = hidden;
        this.description = description;
        this.effect = effect;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isHidden() { return hidden; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEffect() { return effect; }
    public void setEffect(String effect) { this.effect = effect; }
}
