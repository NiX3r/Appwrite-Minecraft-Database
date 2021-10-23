package eu.ncodes.appwritedatabase;

import eu.ncodes.appwritedatabase.Instances.AppwriteCallback;
import eu.ncodes.appwritedatabase.Instances.CacheValueInstance;
import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Services.DocumentService;
import eu.ncodes.appwritedatabase.Utils.PluginUtils;

import java.util.function.Consumer;

public class AppwriteDatabaseAPI {
    public static String GLOBAL_GROUP_NAME = "$global";

    public static Object getGlobalValueSync(String key){
        return getValueSync(GLOBAL_GROUP_NAME, key);
    }

    public static Object getValueSync(String group, String key) {
        CacheValueInstance cachedValue = CacheManager.getInstance().getValue(group, key);
        if(cachedValue == null) {
            return "0";
        }
        return cachedValue.value;
    }

    public static Object setValueSync(String group, String key, Object value, Boolean isRemote) {
        CacheManager.getInstance().setValue(group, key, value.toString(), isRemote);
        return CacheManager.getInstance().getValue(group, key).value;
    }

    public static Object setGlobalValueSync(String key, Object value, Boolean isRemote){
        return setValueSync(GLOBAL_GROUP_NAME, key, value, isRemote);
    }

    public static Object takeValueSync(String group, String key, Object value, Boolean isRemote) {
       try {
           String oldValue = CacheManager.getInstance().getValue(group, key).value.toString();
           if(oldValue != null) {
               String newValue = PluginUtils.takeValue(oldValue, value.toString());
               CacheManager.getInstance().setValue(group, key, newValue, isRemote);
               return CacheManager.getInstance().getValue(group, key).value;
           } else {
               throw  new Exception("Old value not set");
           }
       } catch(Exception exp) {
           return setValueSync(group, key, value, isRemote);
       }
    }

    public static Object takeGlobalValueSync(String key, Object value, Boolean isRemote){
        return takeValueSync(GLOBAL_GROUP_NAME, key, value, isRemote);
    }

    public static Object addValueSync(String group, String key, Object value, Boolean isRemote) {
        try {
            String oldValue = CacheManager.getInstance().getValue(group, key).value.toString();
            if(oldValue != null) {
                String newValue = PluginUtils.addValue(oldValue, value.toString());
                CacheManager.getInstance().setValue(group, key, newValue, isRemote);
                return CacheManager.getInstance().getValue(group, key).value;
            } else {
                throw  new Exception("Old value not set");
            }
        } catch(Exception exp) {
            return setValueSync(group, key, value, isRemote);
        }
    }

    public static Object addGlobalValueSync(String key, Object value, Boolean isRemote){
        return addValueSync(GLOBAL_GROUP_NAME, key, value, isRemote);
    }

    public static void saveGroupAsync(String group, Consumer<AppwriteCallback> callback){

        DocumentService.savePlayer(group, response ->{
                callback.accept(response);
        });

    }

    // TODO - Add save all method

}
