package eu.ncodes.appwritedatabase;

import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Services.DocumentService;
import eu.ncodes.appwritedatabase.Utils.PluginUtils;

import java.util.function.Consumer;

public class AppwriteDatabaseAPI {
    public static String GLOBAL_GROUP_NAME = "$global";

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

    public static Object takeValueSync(String group, String key, Object value) {
        // This will run async to get it into database, but you get response right away
        // We use value from cache instead of trying to contact server if needed

        String oldValue = CacheManager.getInstance().getValue(group, key).toString();

        if(oldValue != null) {
            String newValue = PluginUtils.takeValue(oldValue, value.toString());
            DocumentService.setDocument(group, key, newValue, (response) -> { });
            return CacheManager.getInstance().getValue(group, key).value;
        } else {
            return setValueSync(group, key, value);
        }
    }

    public static Object addValueSync(String group, String key, Object value) {
        // This will run async to get it into database, but you get response right away
        // We use value from cache instead of trying to contact server if needed

        String oldValue = CacheManager.getInstance().getValue(group, key).toString();

        if(oldValue != null) {
            String newValue = PluginUtils.addValue(oldValue, value.toString());
            DocumentService.setDocument(group, key, newValue, (response) -> { });
            return CacheManager.getInstance().getValue(group, key).value;
        } else {
            return setValueSync(group, key, value);
        }
    }

    public static void takeValueAsync(String group, String key, Object value, Consumer<Object> callback) {
        // Value will be set sync and roll-backed if needed
        // Callback is called when value successfully arrives to Appwrite

        DocumentService.getDocument(group, key, (response) -> {
            if(response.error != null) {
                setValueAsync(group, key, value, (setResponse) -> {
                    callback.accept(setResponse);
                });
                return;
            }

            String oldValue = response.value.toString();
            String newValue = PluginUtils.takeValue(oldValue, value.toString());

            DocumentService.setDocument(group, key, newValue, (setResponse) -> {
                if(setResponse.error != null) {
                    callback.accept(null);
                    return;
                }

                callback.accept(setResponse.value);
            });
        });
    }
}
