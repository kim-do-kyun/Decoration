package org.desp.decoration.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.desp.decoration.database.UserDataRepository;
import org.desp.decoration.dto.UserDataDto;

public class DecorationUtils {

    public static void insertDefaultUserData(Player player, UUID playerUUID) {
        List<String> base = new ArrayList<>();
        UserDataDto newUser = UserDataDto.builder()
                .user_id(player.getName())
                .uuid(playerUUID.toString())
                .equippedDeco("")
                .unlockedDeco(base)
                .level(0)
                .build();
        // 리뷰어가 되도 되나 ㅇㅇ
        // ㅋㅋㅋ 궁ㄱ
        //맞네 나 그냥 별 생각없이 기본값 넣는거라는거만 생각하고 유틸로 뺏네
        // 읽다가 궁금한거 생기면 남겨놓을테니 한번 니 의도를 정리해보셈 생각을
        UserDataRepository.getInstance().insertUserData(newUser);
    }
}
