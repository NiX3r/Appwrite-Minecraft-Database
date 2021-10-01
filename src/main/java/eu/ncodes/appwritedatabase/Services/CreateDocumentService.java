package eu.ncodes.appwritedatabase.Services;

import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CreateDocumentService {

    public static void CreateDocument(String uuid, String key, String value, Consumer<String> callback) {

        Map<String, String> document = new HashMap<>();
        document.put("minecraftUUID", uuid);
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
                                    if(response.code() == 201)
                                        callback.accept("true");
                                    else
                                        callback.accept("false");
                                    response.close();
                                }
                            } catch (Throwable th) {
                                th.printStackTrace();
                                callback.accept("error");
                            }
                        }
                    }
            );

        }
        catch(Exception ex) {
            ex.printStackTrace();
            callback.accept("error");
        }

    }

}
