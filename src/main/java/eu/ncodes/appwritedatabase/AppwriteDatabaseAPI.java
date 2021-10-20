package eu.ncodes.appwritedatabase;

import eu.ncodes.appwritedatabase.Instances.CacheInstance;
import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Services.DocumentService;
import eu.ncodes.appwritedatabase.Utils.PluginUtils;

import java.util.function.Consumer;

// TODO - Add async methods

public class AppwriteDatabaseAPI {
    public static String GLOBAL_GROUP_NAME = "$global";

    public static Object getGlobalValueSync(String key){
        return getValueSync(GLOBAL_GROUP_NAME, key);
    }

    public static Object getValueSync(String group, String key) {
        // Value is returned instantly. Can be null

        CacheInstance cachedValue = CacheManager.getInstance().getValue(group, key);
        if(cachedValue == null) {
            return "0";
        }
        return cachedValue.value;
    }

    public static void getValueAsync(String group, String key, Consumer<Object> callback) {
        // Value will be returned in a callback

        DocumentService.getDocument(group, key, (response) -> {
            if(response.error != null) {
                callback.accept("0");
            }

            callback.accept(response.value);
        });
    }

    public static Object setValueSync(String group, String key, Object value) {
        // This will run async to get it into database, but you get response right away

        DocumentService.setDocument(group, key, value.toString(), (response) -> { });
        return CacheManager.getInstance().getValue(group, key).value;
    }

    public static Object setGlobalValueSync(String key, Object value){
        return setValueSync(GLOBAL_GROUP_NAME, key, value);
    }

    public static void setValueAsync(String group, String key, Object value, Consumer<Object> callback) {
        // Value will be set sync and roll-backed if needed
        // Callback is called when value successfully arrives to Appwrite

        DocumentService.setDocument(group, key, value.toString(), (response) -> {
            if(response.error != null) {
                callback.accept("0");
            }

            callback.accept(response.value);
        });
    }

    public static Object takeValueSync(String group, String key, Object value) {
        // This will run async to get it into database, but you get response right away
        // We use value from cache instead of trying to contact server if needed

        String oldValue = CacheManager.getInstance().getValue(group, key).value.toString();

       try {
           if(oldValue != null) {
               String newValue = PluginUtils.takeValue(oldValue, value.toString());
               DocumentService.setDocument(group, key, newValue, (response) -> { });
               return CacheManager.getInstance().getValue(group, key).value;
           } else {
               throw  new Exception("Old valut not set");
           }
       } catch(Exception exp) {
           return setValueSync(group, key, value);
       }
    }

    public static Object takeGlobalValueSync(String key, Object value){
        return takeValueSync(GLOBAL_GROUP_NAME, key, value);
    }

    public static Object addValueSync(String group, String key, Object value) {
        // This will run async to get it into database, but you get response right away
        // We use value from cache instead of trying to contact server if needed

        try {
            String oldValue = CacheManager.getInstance().getValue(group, key).value.toString();
            if(oldValue != null) {
                String newValue = PluginUtils.addValue(oldValue, value.toString());
                DocumentService.setDocument(group, key, newValue, (response) -> { });
                return CacheManager.getInstance().getValue(group, key).value;
            } else {
                throw  new Exception("Old valut not set");
            }
        } catch(Exception exp) {
            return setValueSync(group, key, value);
        }
    }

    public static Object addGlobalValueSync(String key, Object value){
        return addValueSync(GLOBAL_GROUP_NAME, key, value);
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
                    callback.accept("0");
                    return;
                }

                callback.accept(setResponse.value);
            });
        });
    }
}
