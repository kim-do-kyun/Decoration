package org.desp.decoration.utils;

import static org.desp.decoration.listener.DecorationListener.userDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.desp.decoration.database.UserDataRepository;
import org.desp.decoration.dto.UserDataDto;

public class DecorationUtils {

//    public static void insertDefaultUserData(Player player, UUID playerUUID) {
//        List<String> base = new ArrayList<>();
//        UserDataDto newUser = UserDataDto.builder()
//                .user_id(player.getName())
//                .uuid(playerUUID.toString())
//                .equippedDeco("")
//                .unlockedDeco(base)
//                .level(0)
//                .build();
//        userDataRepository.insertUserData(newUser);
//        userDataRepository = UserDataRepository.getInstance();
//    }
}
