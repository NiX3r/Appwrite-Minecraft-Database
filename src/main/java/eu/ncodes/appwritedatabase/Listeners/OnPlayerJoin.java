package eu.ncodes.appwritedatabase.Listeners;

import com.google.gson.JsonObject;
import eu.ncodes.appwritedatabase.Instances.CacheValueInstance;
import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Services.DocumentService;
import eu.ncodes.appwritedatabase.Utils.PluginUtils;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        OnJoin(e.getPlayer().getUniqueId().toString(), e.getPlayer());
    }

    public static void OnJoin(String uuid, Player p) {
        /*
        Set<String> defaultKeys = PluginVariables.defaults.getConfig().getConfigurationSection("defaults").getKeys(false);

        for(Object key : defaultKeys.toArray()) {
            Object value = PluginVariables.defaults.getConfig().get("defaults." + key.toString());
            CacheManager.getInstance().setValue(uuid, key.toString(), value, null);
        }
        */

        DocumentService.getPlayer(uuid, (response) -> {
            if(response.error != null) {
                if(p != null) {
                    Bukkit.getScheduler().runTask(PluginVariables.Plugin, x -> {
                        p.kickPlayer("Could not load data. Please try again.");
                    });
                }
                else{
                    Bukkit.getScheduler().runTask(PluginVariables.Plugin, x -> {
                        Bukkit.getPluginManager().disablePlugin(PluginVariables.Plugin);
                    });
                }
                return;
            }

            JsonObject data = (JsonObject) response.value;
            System.out.println(data);
            CacheManager.getInstance().ensureCache(uuid, data.get("$id").toString(), new LinkedHashMap<String, CacheValueInstance>());

            for(String key : data.keySet()) {
                if(key.equals("$id") || key.equals("$permissions") || key.equals("$collection")){
                    continue;
                }
                Object value = data.get(key);
                CacheManager.getInstance().setValue(uuid, key, value, true);
            }

        });
    }
}