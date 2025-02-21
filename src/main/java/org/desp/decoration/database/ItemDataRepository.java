package org.desp.decoration.database;

import com.mongodb.client.MongoCollection;
import java.util.HashMap;
import java.util.Map;
import org.bson.Document;
import org.desp.decoration.dto.ItemDataDto;

public class ItemDataRepository {

    private static ItemDataRepository instance;
    private final MongoCollection<Document> itemDataDB;
    private static final Map<String, ItemDataDto> itemDataMap = new HashMap<>();

    public ItemDataRepository() {
        DatabaseRegister database = new DatabaseRegister();
        this.itemDataDB = database.getDatabase().getCollection("ItemData");
        loadItemData();
    }

    public static synchronized ItemDataRepository getInstance() {
        if (instance == null) {
            instance = new ItemDataRepository();
        }
        return instance;
    }

    public void loadItemData() {
        for (Document document : itemDataDB.find()) {
            ItemDataDto itemData = ItemDataDto.builder()
                    .decoID(document.getString("decoID"))
                    .material(document.getString("material"))
                    .customModelData(document.getInteger("customModelData"))
                    .build();

            itemDataMap.put(itemData.getDecoID(), itemData);
        }
    }

    public Map<String, ItemDataDto> getItemData() {
        return itemDataMap;
    }
}

