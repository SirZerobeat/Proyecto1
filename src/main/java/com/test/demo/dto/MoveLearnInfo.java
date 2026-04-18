package com.test.demo.dto;

public class MoveLearnInfo {
    private MoveDetail move;
    private String learnMethod;
    private Integer level;
    private String machineType;

    public MoveLearnInfo() {}

    public MoveLearnInfo(MoveDetail move, String learnMethod, Integer level, String machineType) {
        this.move = move;
        this.learnMethod = learnMethod;
        this.level = level;
        this.machineType = machineType;
    }

    public MoveDetail getMove() { return move; }
    public void setMove(MoveDetail move) { this.move = move; }

    public String getLearnMethod() { return learnMethod; }
    public void setLearnMethod(String learnMethod) { this.learnMethod = learnMethod; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public String getMachineType() { return machineType; }
    public void setMachineType(String machineType) { this.machineType = machineType; }
}
