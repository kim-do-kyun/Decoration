package org.desp.decoration.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class UserDataDto {
    private String user_id;
    private String uuid;
    private String equippedDeco;
    private List<String> unlockedDeco;
    private int level;
}
