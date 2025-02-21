package org.desp.decoration.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class StatusDto {
    private float mana;
    private float skillDMG;
    private float hp;
    private float criticalPercentage;
    private float speed;
    private float criticalDamage;
}
