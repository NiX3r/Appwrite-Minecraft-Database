package eu.ncodes.appwritedatabase;

import co.aikar.commands.BukkitCommandManager;
import com.google.common.collect.ImmutableList;
import eu.ncodes.appwritedatabase.Instances.CacheValueInstance;
import eu.ncodes.appwritedatabase.Listeners.OnCommandListener;
import eu.ncodes.appwritedatabase.Listeners.OnPlayerJoin;
import eu.ncodes.appwritedatabase.Listeners.OnPlayerLeave;
import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Managers.FileManager;
import eu.ncodes.appwritedatabase.Services.CreateCollectionService;
import eu.ncodes.appwritedatabase.Services.DocumentService;
import eu.ncodes.appwritedatabase.Services.GetCollectionListService;
import eu.ncodes.appwritedatabase.Utils.PlaceholderAPI;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import io.appwrite.Client;
import io.appwrite.services.Database;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;

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
            getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "!! Config is a default template. Turning off plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        else{ // Countinue loading plugin

            // Connect to Appwrite
            PluginVariables.AppwriteClient = new Client()
                    .setEndpoint(PluginVariables.config.get("appwrite.api_endpoint"))
                    .setProject(PluginVariables.config.get("appwrite.project_id"))
                    .setKey(PluginVariables.config.get("appwrite.api_key"));

            PluginVariables.AppwriteDatabase = new Database(PluginVariables.AppwriteClient);

            //  Register events
            System.out.println("Registering events .... 1");
            getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
            System.out.println("Registering events .... 2");
            getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this);
            System.out.println("Registering events .... 3");

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
                try{
                    Player p = c.getPlayer();
                    if(p != null) {
                        String group = p.getUniqueId().toString();

                        LinkedHashMap<String, CacheValueInstance> values = CacheManager.getInstance().getValues(group);


                        String[] arr = new String[values.size() + 1];
                        values.keySet().toArray(arr);

                        arr[arr.length - 1] = "<value>";

                        return ImmutableList.copyOf(arr);
                    }
                }
                catch (Exception ex){
                    return ImmutableList.of("<value>");
                }
                return ImmutableList.of("<value>");

            });

            PluginVariables.CommandManager.getCommandCompletions().registerCompletion("globalkey", c -> {
                String group = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;

                LinkedHashMap<String, CacheValueInstance> values = CacheManager.getInstance().getValues(group);

                String[] arr = new String[values.size() + 1];

                values.keySet().toArray(arr);

                arr[arr.length - 1] = "<value>";

                return ImmutableList.copyOf(arr);
            });

            PluginVariables.CommandManager.registerCommand(new OnCommandListener());

            // Surround it with try-catch
            try{
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
            catch (Exception ex){
                ex.printStackTrace();
                getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "!! Config connect to Appwrite database. Turning off plugin...");
                Bukkit.getPluginManager().disablePlugin(this);
            }

            // Create instance of PlaceholderAPI
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                new PlaceholderAPI().register();
            }

            // Create instance of bStats
            Metrics metrics = new Metrics(this, 13089);

        }

    }

    public void onDisable(){

        // Save global data
        DocumentService.savePlayer(AppwriteDatabaseAPI.GLOBAL_GROUP_NAME, response -> {
            System.out.println("---->  SAVED!  <----");
        });

    }

    private void afterEnable() {

        OnPlayerJoin.OnJoin(AppwriteDatabaseAPI.GLOBAL_GROUP_NAME, isSuccess -> {
            if(!isSuccess){
                Bukkit.getScheduler().runTask(PluginVariables.Plugin, x -> {
                    Bukkit.getPluginManager().disablePlugin(PluginVariables.Plugin);
                });
            }
        });

        for(Player p : Bukkit.getOnlinePlayers()) {
            OnPlayerJoin.OnJoin(p.getUniqueId().toString(), isSuccess ->{
                if(!isSuccess){
                    Bukkit.getScheduler().runTask(PluginVariables.Plugin, x -> {
                        p.kickPlayer("Could not load data. Please try again.");
                    });
                }
            });
        }
    }

}
