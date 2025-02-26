package org.desp.decoration.database;

import com.mongodb.client.MongoCollection;
import java.util.HashMap;
import java.util.Map;
import org.bson.Document;
import org.desp.decoration.dto.LevelDataDto;
import org.desp.decoration.dto.StatsDto;

public class LevelDataRepository {

    private static LevelDataRepository instance;
    private final MongoCollection<Document> levelDataDB;
    private static final Map<Integer, LevelDataDto> levelDataMap = new HashMap<>();

    public LevelDataRepository() {
        DatabaseRegister database = new DatabaseRegister();
        this.levelDataDB = database.getDatabase().getCollection("LevelData");
        loadLevelData();
    }

    public static LevelDataRepository getInstance() {
        if (instance == null) {
            instance = new LevelDataRepository();
        }
        return instance;
    }

    public void loadLevelData() {
        for (Document document : levelDataDB.find()) {
            Document statsDoc = document.get("stats", Document.class);

            StatsDto statsDto = StatsDto.builder()
                    .mana(statsDoc.getDouble("mana"))
                    .skillDMG(statsDoc.getDouble("skillDMG"))
                    .hp(statsDoc.getDouble("hp"))
                    .speed(statsDoc.getDouble("speed"))
                    .criticalPercentage(statsDoc.getDouble("criticalPercentage"))
                    .criticalDamage(statsDoc.getDouble("criticalDamage"))
                    .build();

            LevelDataDto levelData = LevelDataDto.builder()
                    .level(document.getInteger("level"))
                    .cost(document.getInteger("cost"))
                    .stats(statsDto)
                    .build();

            levelDataMap.put(levelData.getLevel(), levelData);
        }
    }

    public Map<Integer, LevelDataDto> getLevelData() {
        return levelDataMap;
    }
}
