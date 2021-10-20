package eu.ncodes.appwritedatabase;

import co.aikar.commands.BukkitCommandManager;
import com.google.common.collect.ImmutableList;
import eu.ncodes.appwritedatabase.Instances.CacheInstance;
import eu.ncodes.appwritedatabase.Listeners.OnCommandListener;
import eu.ncodes.appwritedatabase.Listeners.OnPlayerJoin;
import eu.ncodes.appwritedatabase.Listeners.OnPlayerLeave;
import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Managers.FileManager;
import eu.ncodes.appwritedatabase.Services.CreateCollectionService;
import eu.ncodes.appwritedatabase.Services.GetCollectionListService;
import eu.ncodes.appwritedatabase.Utils.PlaceholderAPI;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import io.appwrite.Client;
import io.appwrite.services.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

public final class AppwriteDatabase extends JavaPlugin {

    public void onEnable() {

        // Initialize minecraft plugin variable
        PluginVariables.Plugin = this;
        PluginVariables.FileManager = new FileManager(this);

        // Create config if needed and load it
        PluginVariables.config = PluginVariables.FileManager.getConfig("config.yml");
        PluginVariables.config.copyDefaults(true).save();

        PluginVariables.lang = PluginVariables.FileManager.getConfig("messages.yml");
        PluginVariables.lang.copyDefaults(true).save();

        PluginVariables.defaults = PluginVariables.FileManager.getConfig("defaults.yml");
        PluginVariables.defaults.copyDefaults(true).save();

        if(PluginVariables.config.get("appwrite.api_endpoint").equals("https://[HOSTNAME_OR_IP]/v1") ||
                PluginVariables.config.get("appwrite.project_id").equals("5df5acd0d48c2") ||
                PluginVariables.config.get("appwrite.api_key").equals("919c2d18fb5d4...a2ae413da83346ad2")){
            getLogger().info(ChatColor.translateAlternateColorCodes('&', "&█ &7Config is a default template. Turning off plugin..."));
            Bukkit.getPluginManager().disablePlugin(this);
        }

        // Connect to Appwrite
        PluginVariables.AppwriteClient = new Client()
                .setEndpoint(PluginVariables.config.get("appwrite.api_endpoint"))
                .setProject(PluginVariables.config.get("appwrite.project_id"))
                .setKey(PluginVariables.config.get("appwrite.api_key"));

        PluginVariables.AppwriteDatabase = new Database(PluginVariables.AppwriteClient);

        //  Register events
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this);

        // Initialize and register commands
        // TODO - db global inspect auto complete write only '@page'
        PluginVariables.CommandManager = new BukkitCommandManager(this);

        PluginVariables.CommandManager.enableUnstableAPI("help");
        PluginVariables.CommandManager.getCommandCompletions().registerCompletion("key", c -> {
            return ImmutableList.of("<key>");
        });

        PluginVariables.CommandManager.getCommandCompletions().registerCompletion("value", c -> {
            return ImmutableList.of("<value>");
        });

        PluginVariables.CommandManager.getCommandCompletions().registerCompletion("playerkey", c -> {
            Player p = c.getPlayer();
            if(p != null) {
                String group = p.getUniqueId().toString();

                LinkedHashMap<String, CacheInstance> values = CacheManager.getInstance().getValues(group);

                String[] arr = new String[values.size() + 1];
                values.keySet().toArray(arr);

                arr[arr.length - 1] = "<value>";

                return ImmutableList.copyOf(arr);
            }

            return ImmutableList.of("<value>");
        });

        PluginVariables.CommandManager.getCommandCompletions().registerCompletion("globalkey", c -> {
            String group = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;

            LinkedHashMap<String, CacheInstance> values = CacheManager.getInstance().getValues(group);

            String[] arr = new String[values.size() + 1];
            
            values.keySet().toArray(arr);

            arr[arr.length - 1] = "<value>";

            return ImmutableList.copyOf(arr);
        });

        PluginVariables.CommandManager.registerCommand(new OnCommandListener());

        // Checks if collection data exists
        GetCollectionListService.GetListCollection((id) -> {
            if(id.equals("none")) {
                getLogger().info("Collection not found. Creating ...");
                CreateCollectionService.CreateCollection((newId) -> {
                    getLogger().info("Collection created!");
                    PluginVariables.DataCollectionID = newId;
                    afterEnable();
                });
            }
            else {
                PluginVariables.DataCollectionID = id;
                afterEnable();
            }
        });

        // Create instance of PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI().register();
        }

    }

    private void afterEnable() {
        OnPlayerJoin.OnJoin(AppwriteDatabaseAPI.GLOBAL_GROUP_NAME);

        for(Player p : Bukkit.getOnlinePlayers()) {
            OnPlayerJoin.OnJoin(p.getUniqueId().toString());
        }
    }

}
