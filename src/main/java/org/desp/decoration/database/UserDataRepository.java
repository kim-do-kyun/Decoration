package org.desp.decoration.database;

import static org.desp.decoration.listener.DecorationListener.playerCache;
import static org.desp.decoration.listener.DecorationListener.userDataRepository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.desp.decoration.dto.UserDataDto;
import org.desp.decoration.listener.DecorationListener;

public class UserDataRepository {

    private static UserDataRepository instance;
    private final MongoCollection<Document> userDataDB;
//    private static final Map<String, UserDataDto> UserDataMap = new HashMap<>();

    public UserDataRepository() {
        DatabaseRegister database = new DatabaseRegister();
        this.userDataDB = database.getDatabase().getCollection("UserData");
//        loadUserData();
    }

    public static synchronized UserDataRepository getInstance() {
        if (instance == null) {
            instance = new UserDataRepository();
        }
        return instance;
    }

    public void loadUserData(Player player) {
        Document document = userDataDB.find(Filters.eq("uuid", player.getUniqueId().toString())).first();
        UserDataDto userData = UserDataDto.builder()
                .user_id(document.getString("user_id"))
                .uuid(document.getString("uuid"))
                .equippedDeco(document.getString("equippedDeco"))
                .unlockedDeco(document.getList("unlockedDeco", String.class))
                .level(document.getInteger("level"))
                .build();
        DecorationListener.getPlayerCache().put(userData.getUuid(), userData);
    }

    public void saveUserData(UserDataDto userData) {
        Document updateDocument = new Document()
                .append("user_id", userData.getUser_id())
                .append("uuid", userData.getUuid())
                .append("equippedDeco", userData.getEquippedDeco())
                .append("unlockedDeco", userData.getUnlockedDeco())
                .append("level", userData.getLevel());
        userDataDB.replaceOne(
                Filters.eq("uuid", userData.getUuid()),
                updateDocument,
                new ReplaceOptions().upsert(true)
        );

    }

    public void getUserData(Player player) {
        Document document = (Document) userDataDB.find(Filters.eq("uuid", player.getUniqueId().toString()));

    }

//    public void loadUserData() {
//        for (Document document : userDataDB.find()) {

//
//            UserDataMap.put(userData.getUuid(), userData);
//        }
//    }


    public void insertUserData(UserDataDto userData) {
        Document document = new Document()
                .append("user_id", userData.getUser_id())
                .append("uuid", userData.getUuid())
                .append("equippedDeco", userData.getEquippedDeco())
                .append("unlockedDeco", userData.getUnlockedDeco())
                .append("level", userData.getLevel());

        userDataDB.insertOne(document);
        System.out.println("Inserted user data");
    }

    public static void insertDefaultUserData(Player player, UUID playerUUID) {
        List<String> base = new ArrayList<>();
        UserDataDto newUser = UserDataDto.builder()
                .user_id(player.getName())
                .uuid(playerUUID.toString())
                .equippedDeco("")
                .unlockedDeco(base)
                .level(0)
                .build();
//        insertUserData(newUser);
//        userDataRepository = UserDataRepository.getInstance();
    }

    public boolean isPlayerDataExist(Player player) {

        Document document = userDataDB.find(Filters.eq("uuid", player.getUniqueId().toString())).first();
        if(document!=null){
            return true;
        }
        return false;
    }
}
