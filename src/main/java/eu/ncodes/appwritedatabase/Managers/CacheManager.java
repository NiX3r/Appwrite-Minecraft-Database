package eu.ncodes.appwritedatabase.Managers;

import eu.ncodes.appwritedatabase.Instances.CacheInstance;
import eu.ncodes.appwritedatabase.Instances.CacheValueInstance;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CacheManager {
    private static CacheManager _instance = null;
    public static CacheManager getInstance()
    {
        if (_instance == null)
            _instance = new CacheManager();

        return _instance;
    }

    private LinkedHashMap<String, CacheInstance> cache = new LinkedHashMap();

    public void ensureCache(String group, String docId, LinkedHashMap<String, CacheValueInstance> values){
        if(!cache.containsKey(group)){
            cache.put(group, new CacheInstance(group, docId, values));
        }
    }

    public CacheValueInstance getValue(String group, String key) {
        if(cache.containsKey(group)) {
            CacheInstance playerCache = cache.get((group));
            if(playerCache.Values.containsKey(key)) {
                return playerCache.Values.get(key);
            }
        }

        return null;
    }

    public LinkedHashMap<String, CacheValueInstance> getValues(String group) {
        if(cache.containsKey(group)) {
            LinkedHashMap<String, CacheValueInstance> playerCache = cache.get(group).Values;
            return playerCache;
        }

        return null;
    }

    public void setValue(String group, String key, Object value, Boolean isRemote) {
        LinkedHashMap<String, CacheValueInstance> playerCache = cache.get(group).Values;
        playerCache.remove(key);
        playerCache.put(key, new CacheValueInstance(value, isRemote));
    }

    public void removeValue(String group, String key) {
        if(!cache.containsKey(group)) {
            return;
        }

        LinkedHashMap<String, CacheValueInstance> playerCache = cache.get(group).Values;
        playerCache.remove(key);
    }

    public void removeAll(String group) {
        cache.remove(group);
    }

    public String getDocumentID(String group){
        return cache.get(group).DocumentID;
    }

}
