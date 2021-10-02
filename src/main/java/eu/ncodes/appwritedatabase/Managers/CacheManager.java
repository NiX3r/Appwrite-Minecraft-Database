package eu.ncodes.appwritedatabase.Managers;

import com.google.gson.JsonObject;
import eu.ncodes.appwritedatabase.Instances.CacheInstance;

import java.util.LinkedHashMap;

public class CacheManager {
    private static CacheManager _instance = null;
    public static CacheManager getInstance()
    {
        if (_instance == null)
            _instance = new CacheManager();

        return _instance;
    }

    // Key: Player UUID
    // Value: Key&Value pairs
    private LinkedHashMap<String, LinkedHashMap<String, CacheInstance>> cache = new LinkedHashMap();

    public CacheInstance getValue(String group, String key) {
        if(cache.containsKey(group)) {
            LinkedHashMap<String, CacheInstance> playerCache = cache.get((group));
            if(playerCache.containsKey(key)) {
                return playerCache.get(key);
            }
        }

        return null;
    }

    public LinkedHashMap<String, CacheInstance> getValues(String group) {
        if(cache.containsKey(group)) {
            LinkedHashMap<String, CacheInstance> playerCache = cache.get((group));
            return playerCache;
        }

        return null;
    }

    public void setValue(String group, String key, Object value, JsonObject document) {
        if(!cache.containsKey(group)) {
            cache.put(group, new LinkedHashMap());
        }

        LinkedHashMap<String, CacheInstance> playerCache = cache.get(group);
        playerCache.put(key, new CacheInstance(value, document));
    }

    public void removeValue(String group, String key) {
        if(!cache.containsKey(group)) {
            return;
        }

        LinkedHashMap<String, CacheInstance> playerCache = cache.get(group);
        playerCache.remove(key);
    }

    public void removeAll(String group) {
        cache.remove(group);
    }
}
