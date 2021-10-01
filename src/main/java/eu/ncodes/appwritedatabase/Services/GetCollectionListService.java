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

import java.util.function.Consumer;

public class GetCollectionListService {

    public static void GetListCollection(Consumer<String> callback) {

        try {
            PluginVariables.AppwriteDatabase.listCollections(new Continuation<Response>()
             {
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
                             for(JsonElement e : root.getAsJsonObject().get("collections").getAsJsonArray()) {
                                 if(e.getAsJsonObject().get("name").getAsString().equals("data")) {
                                     String output = e.getAsJsonObject().get("$id").getAsString();
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
                         callback.accept("none");
                     }
                 }
             }
            );
        } catch (AppwriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            callback.accept("none");
        }

    }

}
