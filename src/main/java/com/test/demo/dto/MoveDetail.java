package com.test.demo.dto;

public class MoveDetail {
    private int moveId;
    private String moveName;
    private String type;
    private Integer power;
    private Integer accuracy;
    private int pp;
    private String damageClass;
    private String description;
    private int priority;
    private String effect;
    private Integer effectChance;
    private String target;

    public MoveDetail() {}

    public MoveDetail(int moveId, String moveName, String type, Integer power, Integer accuracy,
                      int pp, String damageClass, String description, int priority, String effect,
                      Integer effectChance, String target) {
        this.moveId = moveId;
        this.moveName = moveName;
        this.type = type;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = pp;
        this.damageClass = damageClass;
        this.description = description;
        this.priority = priority;
        this.effect = effect;
        this.effectChance = effectChance;
        this.target = target;
    }

    // Getters and Setters
    public int getMoveId() { return moveId; }
    public void setMoveId(int moveId) { this.moveId = moveId; }

    public String getMoveName() { return moveName; }
    public void setMoveName(String moveName) { this.moveName = moveName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getPower() { return power; }
    public void setPower(Integer power) { this.power = power; }

    public Integer getAccuracy() { return accuracy; }
    public void setAccuracy(Integer accuracy) { this.accuracy = accuracy; }

    public int getPp() { return pp; }
    public void setPp(int pp) { this.pp = pp; }

    public String getDamageClass() { return damageClass; }
    public void setDamageClass(String damageClass) { this.damageClass = damageClass; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public String getEffect() { return effect; }
    public void setEffect(String effect) { this.effect = effect; }

    public Integer getEffectChance() { return effectChance; }
    public void setEffectChance(Integer effectChance) { this.effectChance = effectChance; }

    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
}
