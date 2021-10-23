package eu.ncodes.appwritedatabase.Listeners;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.ncodes.appwritedatabase.AppwriteDatabaseAPI;
import eu.ncodes.appwritedatabase.Enums.TypeOfListEnum;
import eu.ncodes.appwritedatabase.Instances.AppwriteCallback;
import eu.ncodes.appwritedatabase.Instances.AppwriteCallbackError;
import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Services.DocumentService;
import eu.ncodes.appwritedatabase.Utils.CommandUtils;
import eu.ncodes.appwritedatabase.Utils.PluginUtils;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static co.aikar.commands.ACFBukkitUtil.sendMsg;

@CommandAlias("appwrite|aw")
public class OnCommandListener extends BaseCommand {

    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        sendMsg(sender, PluginUtils.GetMessage("commands.help"));
        help.showHelp();
    }

    @Subcommand("version|ver|v")
    @Description("Command for get plugin version")
    @CommandPermission("appwrite.version")
    public void GetVersion(CommandSender sender){
        PluginUtils.SendMessage(sender,  "commands.version", new LinkedHashMap<String, String>(){{
            put("version", PluginVariables.Plugin.getDescription().getVersion() + " - powered by Appwrite " + ChatColor.DARK_RED + "❤");
        }});
    }

    @Subcommand("database|db")
    @Description("Commands related to Appwrite database")
    public class DatabaseClass extends BaseCommand {

        /* TODO - Repair
        @Subcommand("defaults|def")
        @Description("Database defaults")
        public class DefaultsClass extends BaseCommand {

            @Subcommand("inspect")
            @Syntax("<page>")
            @Description("Get 10 defaults per <page>")
            @CommandPermission("appwrite.defaults.inspect")
            @CommandCompletion("@page")
            public void InspectCommand(CommandSender sender, @Default("1") int page) {
                sharedListCommand(sender, page, TypeOfListEnum.DEFAULTS, null);
            }

        }
         */
        @Subcommand("player|p")
        @Description("Per-player data storage")
        public class PersonalClass extends BaseCommand {

            /* TODO - Repair
            @Subcommand("inspect")
            @Syntax("<page>")
            @Description("Get 10 player variables per <page>")
            @CommandPermission("appwrite.player.inspect")
            @CommandCompletion("@players @page")
            public void InspectCommand(CommandSender sender, String player, @Default("1") int page) {
                String uuid = Bukkit.getOfflinePlayer(player).getUniqueId().toString();
                sharedListCommand(sender, page, TypeOfListEnum.GLOBAL, uuid);
            }*/

            @Subcommand("set-local")
            @Syntax("<player> <key> <value>")
            @Description("Update current value of local <key> to a new one on player storage")
            @CommandPermission("appwrite.player.set.local")
            @CommandCompletion("@players @playerkey @value")
            public void SetCommandLocal(CommandSender sender, String player, String key, String value) {
                String uuid = Bukkit.getOfflinePlayer(player).getUniqueId().toString();
                sharedSetCommand(sender, key, value, uuid, player, false);
            }
            @Subcommand("set-remote")
            @Syntax("<player> <key> <value>")
            @Description("Update current value of remote <key> to a new one on player storage")
            @CommandPermission("appwrite.player.set.remote")
            @CommandCompletion("@players @playerkey @value")
            public void SetCommandRemote(CommandSender sender, String player, String key, String value) {
                String uuid = Bukkit.getOfflinePlayer(player).getUniqueId().toString();
                sharedSetCommand(sender, key, value, uuid, player, true);
            }

            @Subcommand("add-local")
            @Syntax("<player> <key> <value>")
            @Description("Increase the current value of local <key> on player storage")
            @CommandPermission("appwrite.player.add.local")
            @CommandCompletion("@players @playerkey @value")
            public void AddCommandLocal(CommandSender sender, String player, String key, String value) {
                String uuid = Bukkit.getOfflinePlayer(player).getUniqueId().toString();
                sharedAddCommand(sender, key, value, uuid, player, false);
            }

            @Subcommand("add-remote")
            @Syntax("<player> <key> <value>")
            @Description("Increase the current value of remote <key> on player storage")
            @CommandPermission("appwrite.player.add.remote")
            @CommandCompletion("@players @playerkey @value")
            public void AddCommandRemote(CommandSender sender, String player, String key, String value) {
                String uuid = Bukkit.getOfflinePlayer(player).getUniqueId().toString();
                sharedAddCommand(sender, key, value, uuid, player, true);
            }

            @Subcommand("take-local")
            @Syntax("<player> <key> <value>")
            @Description("Decrease the current value of local <key> on player storage")
            @CommandPermission("appwrite.player.take.local")
            @CommandCompletion("@players @playerkey @value")
            public void TakeCommandLocal(CommandSender sender, String player, String key, String value) {
                String uuid = Bukkit.getOfflinePlayer(player).getUniqueId().toString();
                sharedTakeCommand(sender, key, value, uuid, player, false);
            }

            @Subcommand("take-remote")
            @Syntax("<player> <key> <value>")
            @Description("Decrease the current value of remote <key> on player storage")
            @CommandPermission("appwrite.player.take.remote")
            @CommandCompletion("@players @playerkey @value")
            public void TakeCommandRemote(CommandSender sender, String player, String key, String value) {
                String uuid = Bukkit.getOfflinePlayer(player).getUniqueId().toString();
                sharedTakeCommand(sender, key, value, uuid, player, true);
            }

            @Subcommand("get")
            @Syntax("<player> <key>")
            @Description("Get the current value of <key> on player storage")
            @CommandPermission("appwrite.player.get")
            @CommandCompletion("@players @playerkey")
            public void GetCommand(CommandSender sender, String player, String key) {
                String uuid = Bukkit.getOfflinePlayer(player).getUniqueId().toString();
                sharedGetCommand(sender, key, uuid, player);
            }
        }

        @Subcommand("global|g")
        @Description("Globally shared data storage")
        public class GlobalClass extends BaseCommand {
            /* TODO - Repair
            @Subcommand("inspect")
            @Syntax("<page>")
            @Description("Get 10 global variables per <page>")
            @CommandPermission("appwrite.global.inspect")
            @CommandCompletion("@page")
            public void InspectCommand(CommandSender sender, @Default("1") int page){
                sharedListCommand(sender, page, TypeOfListEnum.GLOBAL, null);
            }*/

            @Subcommand("set-local")
            @Syntax("<key> <value>")
            @Description("Update current value of local <key> to a new one on global storage")
            @CommandPermission("appwrite.global.set.local")
            @CommandCompletion("@globalkey @value")
            public void SetCommandLocal(CommandSender sender, String key, String value) {
                String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;
                sharedSetCommand(sender, key, value, uuid, null, false);
            }

            @Subcommand("set-remote")
            @Syntax("<key> <value>")
            @Description("Update current value of remote <key> to a new one on global storage")
            @CommandPermission("appwrite.global.set.remote")
            @CommandCompletion("@globalkey @value")
            public void SetCommandRemote(CommandSender sender, String key, String value) {
                String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;
                sharedSetCommand(sender, key, value, uuid, null, true);
            }

            @Subcommand("add-local")
            @Syntax("<key> <value>")
            @Description("Increase the current value of local <key> on global storage")
            @CommandPermission("appwrite.global.add.local")
            @CommandCompletion("@globalkey @value")
            public void AddCommandLocal(CommandSender sender, String key, String value) {
                String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;
                sharedAddCommand(sender, key, value, uuid, null, false);
            }

            @Subcommand("add-remote")
            @Syntax("<key> <value>")
            @Description("Increase the current value of remote <key> on global storage")
            @CommandPermission("appwrite.global.add.remote")
            @CommandCompletion("@globalkey @value")
            public void AddCommandRemote(CommandSender sender, String key, String value) {
                String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;
                sharedAddCommand(sender, key, value, uuid, null, true);
            }

            @Subcommand("take-local")
            @Syntax("<key> <value>")
            @Description("Decrease the current value of local <key> on global storage")
            @CommandPermission("appwrite.global.take.local")
            @CommandCompletion("@globalkey @value")
            public void TakeCommandLocal(CommandSender sender, String key, String value) {
                String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;
                sharedTakeCommand(sender, key, value, uuid, null, false);
            }

            @Subcommand("take-remote")
            @Syntax("<key> <value>")
            @Description("Decrease the current value of remote <key> on global storage")
            @CommandPermission("appwrite.global.take.remote")
            @CommandCompletion("@globalkey @value")
            public void TakeCommandRemote(CommandSender sender, String key, String value) {
                String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;
                sharedTakeCommand(sender, key, value, uuid, null, true);
            }

            @Subcommand("get")
            @Syntax("<key>")
            @Description("Get the current value of <key> on global storage")
            @CommandPermission("appwrite.global.get")
            @CommandCompletion("@globalkey")
            public void GetCommand(CommandSender sender, String key) {
                String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;
                sharedGetCommand(sender, key, uuid, null);
            }
        }
    }

    // TODO - reloads dont work (NullPointerException)
    @Subcommand("reload|r")
    public class ReloadClass extends BaseCommand{

        @Subcommand("all|a")
        @CommandPermission("appwrite.reload.all")
        public void AllCommand(CommandSender sender) {
            PluginVariables.FileManager.reloadConfig("config.yml");
            PluginVariables.FileManager.reloadConfig("messages.yml");
            PluginUtils.SendMessage(sender, "commands.reload.all");
        }

        @Subcommand("messages|msg|m")
        @CommandPermission("appwrite.reload.messages")
        public void MessagesCommand(CommandSender sender) {
            PluginVariables.FileManager.reloadConfig("messages.yml");
            PluginUtils.SendMessage(sender, "commands.reload.messages");
        }

        @Subcommand("config|c")
        @CommandPermission("appwrite.reload.config")
        public void ConfigCommand(CommandSender sender) {
            PluginVariables.FileManager.reloadConfig("config.yml");
            PluginUtils.SendMessage(sender, "commands.reload.config");
        }
    }

    /* TODO - Repair
    private void sharedListCommand(CommandSender sender, int page, TypeOfListEnum type, String group) {
        Consumer<JsonObject> onFinish = (data) -> {

            String msg = "";

            String cmd = data.get("cmd").getAsString();
            int size = data.get("size").getAsInt();
            JsonObject list = data.get("list").getAsJsonObject();

            Set<Map.Entry<String, JsonElement>> entries = list.entrySet();
            for (Map.Entry<String, JsonElement> entry: entries) {
                msg += "\n&b" + entry.getKey() + ": &7" + entry.getValue().getAsString();
            }

            msg = ChatColor.translateAlternateColorCodes('&', msg);
            sender.sendMessage(msg);
            CommandUtils.sendFooter(sender, cmd, page, ((size / 10) + 1));
        };

        if(type == TypeOfListEnum.DEFAULTS) {
            JsonObject dataObject = new JsonObject();

            dataObject.addProperty("size", CommandUtils.getDefaultsSize());
            dataObject.addProperty("cmd", "aw db def inspect");
            dataObject.add("list", CommandUtils.getDefaultsHashMap((page - 1) * 10));

            onFinish.accept(dataObject);
        } else if(type == TypeOfListEnum.GLOBAL) {
            String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;

            DocumentService.listDocuments(uuid, page, (response) -> {
                if(response.error != null) {
                    // TODO: Napis error message
                    return;
                }

                JsonObject documentListObject = (JsonObject) response.value;
                Integer sum = documentListObject.get("sum").getAsInt();

                JsonObject dataJson = new JsonObject();

                for(JsonElement documentEl : documentListObject.get("documents").getAsJsonArray()) {
                    JsonObject document = documentEl.getAsJsonObject();

                    dataJson.add(document.get("key").getAsString(), document.get("value"));
                }

                JsonObject dataObject = new JsonObject();

                dataObject.addProperty("size", sum);
                dataObject.addProperty("cmd", "aw db g inspect");
                dataObject.add("list", dataJson);

                onFinish.accept(dataObject);
            });
        } else if(type == TypeOfListEnum.PLAYER) {

            DocumentService.listDocuments(group, page, (response) -> {
                if(response.error != null) {
                    // TODO: Napis error message
                    return;
                }

                JsonObject documentListObject = (JsonObject) response.value;
                Integer sum = documentListObject.get("sum").getAsInt();
                JsonObject dataJson = new JsonObject();

                for(JsonElement documentEl : documentListObject.get("documents").getAsJsonArray()) {
                    JsonObject document = documentEl.getAsJsonObject();

                    dataJson.add(document.get("key").getAsString(), document.get("value"));
                }

                JsonObject dataObject = new JsonObject();

                dataObject.addProperty("size", sum);
                dataObject.addProperty("cmd", "aw db g inspect");
                dataObject.add("list", dataJson);

                onFinish.accept(dataObject);
            });
        }

    }
    */
    private void sharedGetCommand(CommandSender sender, String key, String uuid, String playerName) {

        String messageKeyPrefix = "commands.get." + (playerName != null ? "player." : "global.");

        if(CacheManager.getInstance().getValues(uuid).containsKey(key)) {
            PluginUtils.SendMessage(sender, messageKeyPrefix + "not_found", new LinkedHashMap<String, String>(){{
                put("player", playerName);
                put("key", key);
            }});
        }else {
            PluginUtils.SendMessage(sender, messageKeyPrefix + "not_found", new LinkedHashMap<String, String>(){{
                put("player", playerName);
                put("key", key);
            }});
        }

    }

    private void sharedSetCommand(CommandSender sender, String key, String value, String uuid, String playerName, Boolean isRemote) {
        String messageKeyPrefix = "commands.set." + (playerName != null ? "player." : "global.");

        AppwriteDatabaseAPI.setValueSync(uuid, key, value, isRemote);
        PluginUtils.SendMessage(sender, messageKeyPrefix + "success", new LinkedHashMap<String, String>(){{
            put("player", playerName);
            put("key", key);
            put("value", value);
        }});

    }

    private void sharedAddCommand(CommandSender sender, String key, String value, String uuid, String playerName, Boolean isRemote) {
        String messageKeyPrefix = "commands.add." + (playerName != null ? "player." : "global.");
        String newValue = AppwriteDatabaseAPI.addValueSync(uuid, key, value, isRemote).toString();

        PluginUtils.SendMessage(sender, messageKeyPrefix + "success", new LinkedHashMap<String, String>(){{
            put("player", playerName);
            put("key", key);
            put("value", newValue);
        }});
    }

    private void sharedTakeCommand(CommandSender sender, String key, String value, String uuid, String playerName, Boolean isRemote) {
        String messageKeyPrefix = "commands.take." + (playerName != null ? "player." : "global.");
        String newValue = AppwriteDatabaseAPI.takeValueSync(uuid, key, value, isRemote).toString();

        PluginUtils.SendMessage(sender, messageKeyPrefix + "success", new LinkedHashMap<String, String>(){{
            put("player", playerName);
            put("key", key);
            put("value", newValue);
        }});
    }
}
