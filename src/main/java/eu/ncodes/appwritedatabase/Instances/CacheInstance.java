package eu.ncodes.appwritedatabase.Instances;

import com.google.gson.JsonObject;

public class CacheInstance {
    public Object value;
    public JsonObject document;

    public CacheInstance(Object value, JsonObject document) {
        this.value = value;
        this.document = document;
    }
}
