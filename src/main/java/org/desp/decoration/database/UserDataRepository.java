package org.desp.decoration.database;

import com.mongodb.client.MongoCollection;
import java.util.HashMap;
import java.util.Map;
import org.bson.Document;
import org.desp.decoration.dto.UserDataDto;

public class UserDataRepository {

    private static UserDataRepository instance;
    private final MongoCollection<Document> userDataDB;
    private static final Map<String, UserDataDto> UserDataMap = new HashMap<>();

    public UserDataRepository() {
        DatabaseRegister database = new DatabaseRegister();
        this.userDataDB = database.getDatabase().getCollection("UserData");
        loadUserData();
    }

    public static synchronized UserDataRepository getInstance() {
        if (instance == null) {
            instance = new UserDataRepository();
        }
        return instance;
    }

    public void loadUserData() {
        for (Document document : userDataDB.find()) {
            UserDataDto userData = UserDataDto.builder()
                    .user_id(document.getString("user_id"))
                    .uuid(document.getString("uuid"))
                    .equippedDeco(document.getString("equippedDeco"))
                    .unlockedDeco(document.getList("unlockedDeco", String.class))
                    .level(document.getInteger("level"))
                    .build();

            UserDataMap.put(userData.getUuid(), userData);
        }
    }

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

    public Map<String, UserDataDto> getUserData() {
        return UserDataMap;
    }
}
