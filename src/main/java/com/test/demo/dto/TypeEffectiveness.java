package com.test.demo.dto;

import java.util.List;

public class TypeEffectiveness {
    private List<String> weaknessesTo;
    private List<String> resistances;
    private List<String> immunities;
    private List<String> damageTo;

    public TypeEffectiveness() {}

    public TypeEffectiveness(List<String> weaknessesTo, List<String> resistances,
                             List<String> immunities, List<String> damageTo) {
        this.weaknessesTo = weaknessesTo;
        this.resistances = resistances;
        this.immunities = immunities;
        this.damageTo = damageTo;
    }

    public List<String> getWeaknessesTo() { return weaknessesTo; }
    public void setWeaknessesTo(List<String> weaknessesTo) { this.weaknessesTo = weaknessesTo; }

    public List<String> getResistances() { return resistances; }
    public void setResistances(List<String> resistances) { this.resistances = resistances; }

    public List<String> getImmunities() { return immunities; }
    public void setImmunities(List<String> immunities) { this.immunities = immunities; }

    public List<String> getDamageTo() { return damageTo; }
    public void setDamageTo(List<String> damageTo) { this.damageTo = damageTo; }
}
