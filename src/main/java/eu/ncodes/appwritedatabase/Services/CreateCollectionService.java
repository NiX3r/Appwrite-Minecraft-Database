package eu.ncodes.appwritedatabase.Services;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import io.appwrite.exceptions.AppwriteException;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CreateCollectionService {

    public static void CreateCollection(Consumer<String> callback) {

        // Create default rules
        ArrayList<Map<String, Object>> rules = new ArrayList<Map<String, Object>>();

        Map<String, Object> uuid = new HashMap<>();
        uuid.put("label", "Minecraft UUID");
        uuid.put("key", "minecraftUUID");
        uuid.put("type", "text");
        uuid.put("default", "");
        uuid.put("required", true);
        uuid.put("array", false);

        Map<String, Object> key = new HashMap<>();
        key.put("label", "Key");
        key.put("key", "key");
        key.put("type", "text");
        key.put("default", "");
        key.put("required", true);
        key.put("array", false);

        Map<String, Object> value = new HashMap<>();
        value.put("label", "Value");
        value.put("key", "value");
        value.put("type", "text");
        value.put("default", "");
        value.put("required", true);
        value.put("array", false);

        rules.add(uuid);
        rules.add(key);
        rules.add(value);

        try {
            PluginVariables.AppwriteDatabase.createCollection(
                    "data",
                    null,
                    null,
                    rules,
                    new Continuation<Response>() {
                        @NotNull
                        @Override
                        public CoroutineContext getContext() {
                            return EmptyCoroutineContext.INSTANCE;
                        }

                        @Override
                        public void resumeWith(@NotNull Object o) {
                            String json = "";
                            try {
                                if (o instanceof Result.Failure) {
                                    Result.Failure failure = (Result.Failure) o;
                                    throw failure.exception;
                                } else {
                                    Response response = (Response) o;
                                    json = response.body().string();
                                    JsonElement root = new JsonParser().parse(json);
                                    callback.accept(root.getAsJsonObject().get("$id").getAsString());
                                    response.close();
                                }
                            } catch (Throwable th) {
                                th.printStackTrace();
                            }
                        }
                    }
            );
        } catch (AppwriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
