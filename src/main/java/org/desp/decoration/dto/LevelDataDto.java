package org.desp.decoration.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class LevelDataDto {
    private int level;
    private StatusDto status;
}

