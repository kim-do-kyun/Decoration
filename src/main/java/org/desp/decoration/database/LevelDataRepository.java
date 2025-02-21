package org.desp.decoration.database;

import com.mongodb.client.MongoCollection;
import java.util.HashMap;
import java.util.Map;
import org.bson.Document;
import org.desp.decoration.dto.LevelDataDto;
import org.desp.decoration.dto.UserDataDto;

public class LevelDataRepository {

    private static LevelDataRepository instance;
    private final MongoCollection<Document> levelDataDB;
    private static final Map<String, LevelDataDto> levelDataMap = new HashMap<>();

    public LevelDataRepository() {
        DatabaseRegister database = new DatabaseRegister();
        this.levelDataDB = database.getDatabase().getCollection("LevelData");
        loadLevelData();
    }

    public static synchronized LevelDataRepository getInstance() {
        if (instance == null) {
            instance = new LevelDataRepository();
        }
        return instance;
    }

    public void loadLevelData() {
        for (Document document : levelDataDB.find()) {
            UserDataDto userData = UserDataDto.builder()
                    .user_id(document.getString("user_id"))
                    .uuid(document.getString("uuid"))
                    .equippedDeco(document.getString("equippedDeco"))
                    .unlockedDeco(document.getList("unlockedDeco", String.class))
                    .level(document.getInteger("level"))
                    .build();

//            levelDataMap.put(userData.getUuid());
        }
    }

//    public Map<String, UserDataDto> getLevelData() {
//        return levelDataMap;
//    }
}
