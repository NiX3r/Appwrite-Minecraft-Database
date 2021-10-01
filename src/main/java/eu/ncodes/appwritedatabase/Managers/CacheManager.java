package eu.ncodes.appwritedatabase.Managers;

import com.google.gson.JsonObject;
import eu.ncodes.appwritedatabase.Instances.CacheInstance;

import java.util.HashMap;

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
    private HashMap<String, HashMap<String, CacheInstance>> cache = new HashMap();

    public CacheInstance getValue(String group, String key) {
        if(cache.containsKey(group)) {
            HashMap<String, CacheInstance> playerCache = cache.get((group));
            if(playerCache.containsKey(key)) {
                return playerCache.get(key);
            }
        }

        return null;
    }

    public void setValue(String group, String key, Object value, JsonObject document) {
        if(!cache.containsKey(group)) {
            cache.put(group, new HashMap());
        }

        HashMap<String, CacheInstance> playerCache = cache.get(group);
        playerCache.put(key, new CacheInstance(value, document));
    }

    public void removeValue(String group, String key) {
        if(!cache.containsKey(group)) {
            return;
        }

        HashMap<String, CacheInstance> playerCache = cache.get(group);
        playerCache.remove(key);
    }
}
