package eu.ncodes.appwritedatabase;

import co.aikar.commands.BukkitCommandManager;
import com.google.common.collect.ImmutableList;
import eu.ncodes.appwritedatabase.Listeners.OnCommandListener;
import eu.ncodes.appwritedatabase.Listeners.OnPlayerJoin;
import eu.ncodes.appwritedatabase.Listeners.OnPlayerLeave;
import eu.ncodes.appwritedatabase.Managers.FileManager;
import eu.ncodes.appwritedatabase.Services.CreateCollectionService;
import eu.ncodes.appwritedatabase.Services.GetCollectionListService;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import io.appwrite.Client;
import io.appwrite.services.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
        PluginVariables.CommandManager = new BukkitCommandManager(this);

        PluginVariables.CommandManager.enableUnstableAPI("help");
        PluginVariables.CommandManager.getCommandCompletions().registerCompletion("key", c -> {
            return ImmutableList.of("<key>");
        });
        PluginVariables.CommandManager.getCommandCompletions().registerCompletion("value", c -> {
            return ImmutableList.of("<value>");
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
    }

    private void afterEnable() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            OnPlayerJoin.OnJoin(p);
        }
    }

}
