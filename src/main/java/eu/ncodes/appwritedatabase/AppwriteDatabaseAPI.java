package eu.ncodes.appwritedatabase;

import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Services.DocumentService;

import java.util.function.Consumer;

public class AppwriteDatabaseAPI {
    public static String GLOBAL_GROUP_NAME = "$global";

    // TODO: Add and take functions

    public static Object getValueSync(String group, String key) {
        // Value is returned instantly. Can be null

        Object cachedValue = CacheManager.getInstance().getValue(group, key).value;
        return cachedValue;
    }

    public static void getValueAsync(String group, String key, Consumer<Object> callback) {
        // Value will be returned in a callback

        DocumentService.getDocument(group, key, (response) -> {
            if(response.error != null) {
                callback.accept(null);
            }

            callback.accept(response.value);
        });
    }

    public static Object setValueSync(String group, String key, Object value) {
        // This will run async to get it into database, but you get response right away

        DocumentService.setDocument(group, key, value.toString(), (response) -> { });
        return CacheManager.getInstance().getValue(group, key).value;
    }

    public static void setValueAsync(String group, String key, Object value, Consumer<Object> callback) {
        // Value will be set sync and roll-backed if needed
        // Callback is called when value successfully arrives to Appwrite

        DocumentService.setDocument(group, key, value.toString(), (response) -> {
            if(response.error != null) {
                callback.accept(null);
            }

            callback.accept(response.value);
        });
    }
}
