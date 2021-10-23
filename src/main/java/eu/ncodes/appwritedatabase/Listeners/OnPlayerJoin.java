package eu.ncodes.appwritedatabase.Listeners;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.ncodes.appwritedatabase.Instances.CacheValueInstance;
import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Services.DocumentService;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class OnPlayerJoin implements Listener {

    // I know that this function

    @EventHandler
    public void On(@NotNull AsyncPlayerPreLoginEvent e)
    {

        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        AtomicReference<Boolean> isFinished = new AtomicReference<>(false);
        AtomicReference<Boolean> shouldKick = new AtomicReference<>(false);

        OnJoin(e.getUniqueId().toString(), isSuccess -> {
            isFinished.set(true);
            shouldKick.set(!isSuccess);
        });

        while (!isFinished.get()){

            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }

        if(shouldKick.get()){
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Could not load data. Please try again.");
        }

    }

    @EventHandler
    public void OnJoin(PlayerJoinEvent e){
        System.out.println(">>>>>>>>>>>>> JOINED <<<<<<<<<<<");
    }

    public static void OnJoin(String uuid, Consumer<Boolean> callback) {
        /*
        Set<String> defaultKeys = PluginVariables.defaults.getConfig().getConfigurationSection("defaults").getKeys(false);

        for(Object key : defaultKeys.toArray()) {
            Object value = PluginVariables.defaults.getConfig().get("defaults." + key.toString());
            CacheManager.getInstance().setValue(uuid, key.toString(), value, null);
        }
        */


        DocumentService.getPlayer(uuid, (response) -> {
            if(response.error != null) {
                callback.accept(false);
                return;
            }

            JsonObject data = (JsonObject) response.value;
            CacheManager.getInstance().ensureCache(uuid, data.get("$id").getAsString(), new LinkedHashMap<String, CacheValueInstance>());

            JsonObject jsonData = JsonParser.parseString(data.get("value").getAsString()).getAsJsonObject();

            for(String key : jsonData.keySet()) {
                Object value = jsonData.get(key);
                CacheManager.getInstance().setValue(uuid, key, value, true);
            }
            callback.accept(true);

        });
    }
}