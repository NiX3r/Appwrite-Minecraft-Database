package eu.ncodes.appwritedatabase.Listeners;

import com.google.gson.JsonObject;
import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Services.DocumentService;
import eu.ncodes.appwritedatabase.Utils.PluginUtils;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class OnPlayerJoin implements Listener {

    @EventHandler
    public void On(PlayerJoinEvent e)
    {
        OnJoin(e.getPlayer().getUniqueId().toString());
    }

    public static void OnJoin(String uuid) {
        /*
        Set<String> defaultKeys = PluginVariables.defaults.getConfig().getConfigurationSection("defaults").getKeys(false);

        for(Object key : defaultKeys.toArray()) {
            Object value = PluginVariables.defaults.getConfig().get("defaults." + key.toString());
            CacheManager.getInstance().setValue(uuid, key.toString(), value, null);
        }
        */

        DocumentService.getPlayer(uuid, (response) -> {
            if(response.error != null) {
                Bukkit.getPlayer(uuid).kickPlayer("Could not load data. Please try again.");
                return;
            }

            JsonObject data = (JsonObject) response.value;

            for(String key : data.keySet()) {
                Object value = data.get(key);
                CacheManager.getInstance().setValue(uuid, key, value, true);
            }
        });
    }
}
