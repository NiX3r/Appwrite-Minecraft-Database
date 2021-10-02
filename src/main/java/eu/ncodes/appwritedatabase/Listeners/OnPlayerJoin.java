package eu.ncodes.appwritedatabase.Listeners;

import com.google.gson.JsonObject;
import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Utils.PluginUtils;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class OnPlayerJoin implements Listener {
    // TODO - nerob to po 10 ale po 100

    @EventHandler
    public void On(PlayerJoinEvent e)
    {
        OnJoin(e.getPlayer().getUniqueId().toString());
    }

    public static void OnJoin(String uuid) {
        Set<String> defaultKeys = PluginVariables.defaults.getConfig().getConfigurationSection("defaults").getKeys(false);

        for(Object key : defaultKeys.toArray()) {
            Object value = PluginVariables.defaults.getConfig().get("defaults." + key.toString());
            CacheManager.getInstance().setValue(uuid, key.toString(), value, null);
        }

        PluginUtils.getAllDocuments(uuid, (response) -> {
            if(response.error != null) {
                // Could not load. Fine? Will be loaded later. Should not occur, only if 5XX error
                return;
            }

            LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) response.value;

            for(String key : data.keySet()) {
                HashMap<String, Object> value = (HashMap<String, Object>) data.get(key);
                CacheManager.getInstance().setValue(uuid, key, value.get("value"), (JsonObject) value.get("document"));
            }
        });
    }
}
