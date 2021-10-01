package eu.ncodes.appwritedatabase.Services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.ncodes.appwritedatabase.Instances.AppwriteCallback;
import eu.ncodes.appwritedatabase.Instances.AppwriteCallbackError;
import eu.ncodes.appwritedatabase.Instances.CacheInstance;
import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
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

public class DocumentService {

    public static void getDocument(String group, String key, Consumer<AppwriteCallback> callback) {
        getDocument(group, key, callback, false);
    }

    public static void getDocument(String group, String key, Consumer<AppwriteCallback> callback, boolean isDocumentImportant) {
        CacheInstance cacheValue = CacheManager.getInstance().getValue(group, key);

        if(cacheValue != null) {
            if(isDocumentImportant == false || cacheValue.document != null) {
                callback.accept(new AppwriteCallback(null, cacheValue.value, cacheValue.document));
                return;
            }
        }

        try {
            ArrayList<String> filters = new ArrayList<>();
            filters.add("minecraftUUID=" + group);
            filters.add("key=" + key);

            PluginVariables.AppwriteDatabase.listDocuments(
                    PluginVariables.DataCollectionID,
                    filters,
                    1,
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
                                        JsonObject document = root.getAsJsonObject().get("documents").getAsJsonArray().get(0).getAsJsonObject();
                                        String output = document.get("value").getAsString();

                                        CacheManager.getInstance().setValue(group, key, output, document);
                                        callback.accept(new AppwriteCallback(null, output, document));
                                    } catch(Exception err) {
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

    public static void setDocument(String group, String key, String value, Consumer<AppwriteCallback> callback) {
        CacheInstance cacheSnapshot = CacheManager.getInstance().getValue(group, key);

        if(cacheSnapshot != null) {
            CacheManager.getInstance().setValue(group, key, value, cacheSnapshot.document);
        } else {
            CacheManager.getInstance().setValue(group, key, value, null);
        }

        Consumer<Object> onError = (_d) -> {
            if(cacheSnapshot != null) {
                CacheManager.getInstance().setValue(group, key, cacheSnapshot.value, cacheSnapshot.document);
            } else {
                CacheManager.getInstance().removeValue(group, key);
            }
        };

        getDocument(group, key, (response) -> {
            if(response.error != null) {
                // createDocument
                Map<String, String> document = new HashMap<>();
                document.put("minecraftUUID", group);
                document.put("key", key);
                document.put("value", value);

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
                                            if(response.code() == 200) {
                                                String json = response.body().string();
                                                JsonElement root = new JsonParser().parse(json);
                                                callback.accept(new AppwriteCallback(null, value, root.getAsJsonObject()));
                                            } else {
                                                onError.accept(null);
                                                callback.accept(new AppwriteCallback(AppwriteCallbackError.DOCUMENT_NOT_FOUND, null, null));
                                            }
                                            response.close();
                                        }
                                    } catch (Throwable th) {
                                        th.printStackTrace();
                                        onError.accept(null);
                                        callback.accept(new AppwriteCallback(AppwriteCallbackError.UNEXPECTED_ERROR, null, null));
                                    }
                                }
                            }
                    );

                }
                catch(Exception ex) {
                    ex.printStackTrace();
                    onError.accept(null);
                    callback.accept(new AppwriteCallback(AppwriteCallbackError.UNEXPECTED_ERROR, null, null));
                }
            } else {
                // updateDocument
                String documentId = response.document.get("$id").getAsString();

                Map<String, String> document = new HashMap<>();
                document.put("minecraftUUID", group);
                document.put("key", key);
                document.put("value", value);

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
                                            if(response.code() == 200) {
                                                String json = response.body().string();
                                                JsonElement root = new JsonParser().parse(json);
                                                callback.accept(new AppwriteCallback(null, value, root.getAsJsonObject()));
                                            } else {
                                                onError.accept(null);
                                                callback.accept(new AppwriteCallback(AppwriteCallbackError.DOCUMENT_NOT_FOUND, null, null));
                                            }
                                            response.close();
                                        }
                                    } catch (Throwable th) {
                                        th.printStackTrace();
                                        onError.accept(null);
                                        callback.accept(new AppwriteCallback(AppwriteCallbackError.UNEXPECTED_ERROR, null, null));
                                    }
                                }
                            }
                    );

                }
                catch(Exception ex) {
                    ex.printStackTrace();
                    onError.accept(null);
                    callback.accept(new AppwriteCallback(AppwriteCallbackError.UNEXPECTED_ERROR, null, null));
                }
            }
        }, true);
    }
}
