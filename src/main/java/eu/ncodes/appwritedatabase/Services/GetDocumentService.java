package eu.ncodes.appwritedatabase.Services;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GetDocumentService {

    /*
     *
     * Method for get document by UUID and Key
     *
     * Input:
     * 	String uuid : players uuid to search by
     * 	String key : collection key to search by
     *  Consumer callback : callback for to do after method is done
     *
     * Output:
     * 	void : nothing
     *
     */
    public static void GetDocument(String uuid, String key, Consumer<String> callback) {

        try {
            PluginVariables.AppwriteDatabase.listDocuments(
                    PluginVariables.DataCollectionID,
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
                                    boolean exists = false;
                                    json = response.body().string();
                                    JsonElement root = new JsonParser().parse(json);
                                    for(JsonElement e : root.getAsJsonObject().get("documents").getAsJsonArray()) {
                                        if(e.getAsJsonObject().get("minecraftUUID").getAsString().equals(uuid) &&
                                                e.getAsJsonObject().get("key").getAsString().equals(key)) {
                                            String output = e.getAsJsonObject().toString();
                                            exists = true;
                                            callback.accept(output);
                                        }
                                    }
                                    if(!exists)
                                        callback.accept("none");
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
