package eu.ncodes.appwritedatabase;

import co.aikar.commands.BukkitCommandManager;
import eu.ncodes.appwritedatabase.Instances.PluginMessagesInstance;
import eu.ncodes.appwritedatabase.Listeners.OnCommandListener;
import eu.ncodes.appwritedatabase.Services.CreateCollectionService;
import eu.ncodes.appwritedatabase.Services.GetCollectionListService;
import eu.ncodes.appwritedatabase.Utils.FileDefaults;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import io.appwrite.Client;
import io.appwrite.services.Database;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public final class AppwriteDatabase extends JavaPlugin {

    public void onEnable() {

        // Initialize minecraft plugin variable
        PluginVariables.Plugin = this;

        // Create config if needed and load it
        getConfig().options().copyDefaults(true);
        saveConfig();
        FileDefaults.ConfigYAML();

        // Create files defaults if needed
        FileDefaults.MessagesYAML();

        // Load data from config
        ReloadConfig();

        // Load data from message.yml
        ReloadMessages();

        // Initialize and register commands
        PluginVariables.CommandManager = new BukkitCommandManager(this);
        PluginVariables.CommandManager.registerCommand(new OnCommandListener());

        // Checks if collection data exists
        GetCollectionListService.GetListCollection((id) -> {
            if(id.equals("none")) {
                System.out.println("Collection does not exist. Creating one ...");
                CreateCollectionService.CreateCollection((newId) -> {
                    System.out.println("Collection created!");
                    PluginVariables.DataCollectionID = newId;
                });
            }
            else {
                PluginVariables.DataCollectionID = id;
            }
        });

    }

    public static boolean ReloadConfig() {

        try {
            File config = new File(PluginVariables.Plugin.getDataFolder() + "/config.yml");
            FileConfiguration fConfig = YamlConfiguration.loadConfiguration(config);

            PluginVariables.Prefix = fConfig.getString("Prefix");

            String endpoint = fConfig.getString("Appwrite.Endpoint");
            String project = fConfig.getString("Appwrite.Project");
            String key = fConfig.getString("Appwrite.Key");

            PluginVariables.AppwriteClient = new Client()
                    .setEndpoint(endpoint) // Your API Endpoint
                    .setProject(project) // Your project ID
                    .setKey(key); // Your secret API key

            PluginVariables.AppwriteDatabase = new Database(PluginVariables.AppwriteClient);

            return true;
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public static boolean ReloadMessages() {

        try {
            File msg = new File(PluginVariables.Plugin.getDataFolder() + "/messages.yml");
            FileConfiguration fMsg = YamlConfiguration.loadConfiguration(msg);

            PluginMessagesInstance.SomethingWrong = fMsg.getString("SomethingWrong");
            PluginMessagesInstance.SuccessReload = fMsg.getString("SuccessReload");
            PluginMessagesInstance.UnknownCommand = fMsg.getString("UnknownCommand");
            PluginMessagesInstance.UnSuccessReload = fMsg.getString("UnSuccessReload");
            PluginMessagesInstance.YouDoNotHavePerm = fMsg.getString("YouDoNotHavePerm");
            PluginMessagesInstance.DatabasePlayerGet = fMsg.getString("DatabasePlayerGet");
            PluginMessagesInstance.DatabasePlayerAdd = fMsg.getString("DatabasePlayerAdd");
            PluginMessagesInstance.DatabasePlayerSet = fMsg.getString("DatabasePlayerSet");
            PluginMessagesInstance.DatabasePlayerTake = fMsg.getString("DatabasePlayerTake");
            PluginMessagesInstance.DatabaseGlobalGet = fMsg.getString("DatabaseGlobalGet");
            PluginMessagesInstance.DatabaseGlobalSet = fMsg.getString("DatabaseGlobalSet");
            PluginMessagesInstance.DatabaseGlobalAdd = fMsg.getString("DatabaseGlobalAdd");
            PluginMessagesInstance.DatabaseGlobalTake = fMsg.getString("DatabaseGlobalTake");
            PluginMessagesInstance.CannotTakeNonExistsDocument = fMsg.getString("CannotTakeNonExistsDocument");
            PluginMessagesInstance.CannotGetNonExistsDocument = fMsg.getString("CannotGetNonExistsDocument");
            PluginMessagesInstance.UnccorectFormat = fMsg.getString("UnccorectFormat");
            return true;
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

}
