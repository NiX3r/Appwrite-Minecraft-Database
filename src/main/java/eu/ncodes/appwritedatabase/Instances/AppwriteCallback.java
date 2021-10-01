package eu.ncodes.appwritedatabase.Instances;

import com.google.gson.JsonObject;

public class AppwriteCallback {
    public AppwriteCallbackError error;
    public Object value;
    public JsonObject document;

    public AppwriteCallback(AppwriteCallbackError error, Object value, JsonObject document) {
        this.error = error;
        this.value = value;
        this.document = document;
    }
}
