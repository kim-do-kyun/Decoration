package org.desp.decoration.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class StatsDto {
    private double mana;
    private double skillDMG;
    private double hp;
    private double criticalPercentage;
    private double speed;
    private double criticalDamage;
}
