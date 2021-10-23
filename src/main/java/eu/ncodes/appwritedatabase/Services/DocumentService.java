package eu.ncodes.appwritedatabase.Services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import eu.ncodes.appwritedatabase.Instances.AppwriteCallback;
import eu.ncodes.appwritedatabase.Instances.AppwriteCallbackError;
import eu.ncodes.appwritedatabase.Instances.CacheValueInstance;
import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import okhttp3.Response;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DocumentService {

    public static void getPlayer(String group, Consumer<AppwriteCallback> callback) {
        try {
            ArrayList<String> filters = new ArrayList<>();
            filters.add("minecraftUUID=" + group);

            PluginVariables.AppwriteDatabase.listDocuments(
                    PluginVariables.DataCollectionID,
                    filters,
                    new Continuation<Response>() {
                        @NotNull
                        @Override
                        public CoroutineContext getContext() {
                            return EmptyCoroutineContext.INSTANCE;
                        }

                        @Override
                        public void resumeWith(@NotNull Object o) {
                            try {
                                if (o instanceof Result.Failure) {
                                    Result.Failure failure = (Result.Failure) o;
                                    throw failure.exception;
                                } else {
                                    Response response = (Response) o;
                                    String json = response.body().string();

                                    JsonElement root = new JsonParser().parse(json);

                                    try {
                                        JsonObject responseJson = root.getAsJsonObject();
                                        JsonObject playerData = responseJson.getAsJsonArray("documents").get(0).getAsJsonObject();
                                        callback.accept(new AppwriteCallback(null, playerData, null));
                                    } catch(Exception err) {
                                        createPlayer(group, callback);
                                    }

                                    response.close();
                                }
                            } catch (Throwable th) {
                                th.printStackTrace();
                                callback.accept(new AppwriteCallback(AppwriteCallbackError.UNEXPECTED_ERROR, null, null));
                            }
                        }
                    }
            );
        }
        catch(Exception ex) {
            ex.printStackTrace();
            callback.accept(new AppwriteCallback(AppwriteCallbackError.UNEXPECTED_ERROR, null, null));
        }
    }

    private static void createPlayer(String group, Consumer<AppwriteCallback> callback){

        JsonObject value = new JsonObject();

        Map<String, String> document = new LinkedHashMap<>();
        document.put("minecraftUUID", group);
        document.put("value", value.toString());

        try {
            PluginVariables.AppwriteDatabase.createDocument(
                    PluginVariables.DataCollectionID,
                    document,
                    new Continuation<Response>() {
                        @NotNull
                        @Override
                        public CoroutineContext getContext() {
                            return EmptyCoroutineContext.INSTANCE;
                        }

                        @Override
                        public void resumeWith(@NotNull Object o) {
                            try {
                                if (o instanceof Result.Failure) {
                                    Result.Failure failure = (Result.Failure) o;
                                    throw failure.exception;
                                } else {
                                    Response response = (Response) o;
                                    if(response.code() == 200 || response.code() == 201) {
                                        String json = response.body().string();
                                        JsonElement root = new JsonParser().parse(json);
                                        callback.accept(new AppwriteCallback(null, root.getAsJsonObject(), null));
                                    } else {
                                        System.out.println("GET Error22: " + response.code());
                                        callback.accept(new AppwriteCallback(AppwriteCallbackError.UNEXPECTED_ERROR, null, null));
                                    }
                                    response.close();
                                }
                            } catch (Throwable th) {
                                th.printStackTrace();
                                callback.accept(new AppwriteCallback(AppwriteCallbackError.UNEXPECTED_ERROR, null, null));
                            }
                        }
                    }
            );

        }
        catch(Exception ex) {
            ex.printStackTrace();
            callback.accept(new AppwriteCallback(AppwriteCallbackError.UNEXPECTED_ERROR, null, null));
        }

    }

    public static void savePlayer(String group, Consumer<AppwriteCallback> callback) {

        String documentId = CacheManager.getInstance().getDocumentID(group);
        JsonObject value = null;
        LinkedHashMap<String, CacheValueInstance> cache =  CacheManager.getInstance().getValues(group);
        for (String item : cache.keySet()) {
            if (NumberUtils.isNumber(cache.get(item).value.toString())) {
                value.addProperty(item, (Number) cache.get(item).value);
            } else {
                value.addProperty(item, cache.get(item).value.toString());
            }
        }

        Map<String, String> document = new LinkedHashMap<>();
        document.put("minecraftUUID", group);
        document.put("value", value.getAsString());

        try {
            PluginVariables.AppwriteDatabase.updateDocument(
                    PluginVariables.DataCollectionID,
                    documentId,
                    document,
                    new Continuation<Response>() {
                        @NotNull
                        @Override
                        public CoroutineContext getContext() {
                            return EmptyCoroutineContext.INSTANCE;
                        }

                        @Override
                        public void resumeWith(@NotNull Object o) {
                            try {
                                if (o instanceof Result.Failure) {
                                    Result.Failure failure = (Result.Failure) o;
                                    throw failure.exception;
                                } else {
                                    Response response = (Response) o;
                                    if(response.code() == 200 || response.code() == 201) {
                                        String json = response.body().string();
                                        JsonElement root = new JsonParser().parse(json);
                                        callback.accept(new AppwriteCallback(null, value, root.getAsJsonObject()));
                                    } else {
                                        System.out.println("GET Error2: " + response.code());
                                        callback.accept(new AppwriteCallback(AppwriteCallbackError.DOCUMENT_NOT_FOUND, null, null));
                                    }
                                    response.close();
                                }
                            } catch (Throwable th) {
                                th.printStackTrace();
                                callback.accept(new AppwriteCallback(AppwriteCallbackError.UNEXPECTED_ERROR, null, null));
                            }
                        }
                    }
            );

        }
        catch(Exception ex) {
            ex.printStackTrace();
            callback.accept(new AppwriteCallback(AppwriteCallbackError.UNEXPECTED_ERROR, null, null));
        }
    }
}
