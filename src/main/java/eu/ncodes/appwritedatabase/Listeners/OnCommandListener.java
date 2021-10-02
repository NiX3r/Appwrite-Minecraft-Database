package eu.ncodes.appwritedatabase.Listeners;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import eu.ncodes.appwritedatabase.AppwriteDatabaseAPI;
import eu.ncodes.appwritedatabase.Instances.AppwriteCallbackError;
import eu.ncodes.appwritedatabase.Services.DocumentService;
import eu.ncodes.appwritedatabase.Utils.PluginUtils;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;

@CommandAlias("appwrite|aw")
public class OnCommandListener extends BaseCommand {
    @Subcommand("database|db")
    @Description("Commands related to Appwrite database")
    public class DatabaseClass extends BaseCommand {

        @Subcommand("player|p")
        @Description("Per-player data storage")
        public class PersonalClass extends BaseCommand{

            @Subcommand("set")
            @Syntax("<player> <key> <value>")
            @Description("Update current value of <key> to a new one on player storage")
            @CommandPermission("appwrite.player.set")
            @CommandCompletion("@players @key @value")
            public void SetCommand(CommandSender sender, String player, String key, String value) {
                String uuid = Bukkit.getOfflinePlayer(player).getUniqueId().toString();
                sharedSetCommand(sender, key, value, uuid, player);
            }

            @Subcommand("add")
            @Syntax("<player> <key> <value>")
            @Description("Increase the current value of <key> on player storage")
            @CommandPermission("appwrite.player.add")
            @CommandCompletion("@players @key @value")
            public void AddCommand(CommandSender sender, String player, String key, String value) {
                String uuid = Bukkit.getOfflinePlayer(player).getUniqueId().toString();
                sharedAddCommand(sender, key, value, uuid, player);
            }

            @Subcommand("take")
            @Syntax("<player> <key> <value>")
            @Description("Decrease the current value of <key> on player storage")
            @CommandPermission("appwrite.player.take")
            @CommandCompletion("@players @key @value")
            public void TakeCommand(CommandSender sender, String player, String key, String value) {
                String uuid = Bukkit.getOfflinePlayer(player).getUniqueId().toString();
                sharedTakeCommand(sender, key, value, uuid, player);
            }

            @Subcommand("get")
            @Syntax("<player> <key>")
            @Description("Get the current value of <key> on player storage")
            @CommandPermission("appwrite.player.get")
            @CommandCompletion("@players @key")
            public void GetCommand(CommandSender sender, String player, String key) {
                String uuid = Bukkit.getOfflinePlayer(player).getUniqueId().toString();
                sharedGetCommand(sender, key, uuid, player);
            }
        }

        @Subcommand("global|g")
        @Description("Globally shared data storage")
        public class GlobalClass extends BaseCommand{

            @Subcommand("set")
            @Syntax("<key> <value>")
            @Description("Update current value of <key> to a new one on global storage")
            @CommandPermission("appwrite.global.set")
            @CommandCompletion("@players @key @value")
            public void SetCommand(CommandSender sender, String key, String value) {
                String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;
                sharedSetCommand(sender, key, value, uuid, null);
            }

            @Subcommand("add")
            @Syntax("<key> <value>")
            @Description("Increase the current value of <key> on global storage")
            @CommandPermission("appwrite.global.add")
            @CommandCompletion("@players @key @value")
            public void AddCommand(CommandSender sender, String key, String value) {
                String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;
                sharedAddCommand(sender, key, value, uuid, null);
            }

            @Subcommand("take")
            @Syntax("<key> <value>")
            @Description("Decrease the current value of <key> on global storage")
            @CommandPermission("appwrite.global.take")
            @CommandCompletion("@players @key @value")
            public void TakeCommand(CommandSender sender, String key, String value) {
                String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;
                sharedTakeCommand(sender, key, value, uuid, null);
            }

            @Subcommand("get")
            @Syntax("<key>")
            @Description("Get the current value of <key> on global storage")
            @CommandPermission("appwrite.global.get")
            @CommandCompletion("@players @key")
            public void GetCommand(CommandSender sender, String key) {
                String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;
                sharedGetCommand(sender, key, uuid, null);
            }
        }
    }

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

    private void sharedSetCommand(CommandSender sender, String key, String value, String uuid, String playerName) {
        // player name NULL if its global
        String messageKeyPrefix = "commands.set." + (playerName != null ? "player." : "global.");

        DocumentService.setDocument(uuid, key, value, (response) -> {
            if(response.error != null) {
                if(response.error.equals(AppwriteCallbackError.DOCUMENT_NOT_FOUND)) {
                    PluginUtils.SendMessage(sender, messageKeyPrefix + "not_found", new LinkedHashMap<String, String>(){{
                        put("player", playerName);
                        put("key", key);
                    }});
                } else if(response.error.equals(AppwriteCallbackError.UNEXPECTED_ERROR)) {
                    PluginUtils.SendMessage(sender, messageKeyPrefix + "unexpected_error", new LinkedHashMap<String, String>(){{
                        put("player", playerName);
                        put("key", key);
                    }});
                }
            } else {
                PluginUtils.SendMessage(sender, messageKeyPrefix + "success", new LinkedHashMap<String, String>(){{
                    put("player", playerName);
                    put("key", key);
                    put("value", value);
                }});
            }
        });
    }

    private void sharedGetCommand(CommandSender sender, String key, String uuid, String playerName) {
        String messageKeyPrefix = "commands.get." + (playerName != null ? "player." : "global.");

        DocumentService.getDocument(uuid, key, (response) -> {
            if(response.error != null) {
                if(response.error.equals(AppwriteCallbackError.DOCUMENT_NOT_FOUND)) {
                    PluginUtils.SendMessage(sender, messageKeyPrefix + "not_found", new LinkedHashMap<String, String>(){{
                        put("player", playerName);
                        put("key", key);
                    }});
                } else if(response.error.equals(AppwriteCallbackError.UNEXPECTED_ERROR)) {
                    PluginUtils.SendMessage(sender, messageKeyPrefix + "unexpected_error", new LinkedHashMap<String, String>(){{
                        put("player", playerName);
                        put("key", key);
                    }});
                }
            } else {
                String value = response.value.toString();
                PluginUtils.SendMessage(sender, messageKeyPrefix + "success", new LinkedHashMap<String, String>(){{
                    put("player", playerName);
                    put("key", key);
                    put("value", value);
                }});
            }
        });
    }

    private void sharedAddCommand(CommandSender sender, String key, String value, String uuid, String playerName) {
        String messageKeyPrefix = "commands.add." + (playerName != null ? "player." : "global.");

        DocumentService.getDocument(uuid, key, (response) -> {
            if(response.error != null) {
                sharedSetCommand(sender, key, value, uuid, playerName);
                return;
            }

            String oldValue = response.value.toString();
            String newValue = PluginUtils.addValue(oldValue, value);

            DocumentService.setDocument(uuid, key, newValue, (setResponse) -> {
                if(setResponse.error != null) {
                    if(setResponse.error.equals(AppwriteCallbackError.DOCUMENT_NOT_FOUND)) {
                        PluginUtils.SendMessage(sender, messageKeyPrefix + "not_found", new LinkedHashMap<String, String>(){{
                            put("player", playerName);
                            put("key", key);
                        }});
                    } else if(setResponse.error.equals(AppwriteCallbackError.UNEXPECTED_ERROR)) {
                        PluginUtils.SendMessage(sender, messageKeyPrefix + "unexpected_error", new LinkedHashMap<String, String>(){{
                            put("player", playerName);
                            put("key", key);
                        }});
                    }
                } else {
                    PluginUtils.SendMessage(sender, messageKeyPrefix + "success", new LinkedHashMap<String, String>(){{
                        put("player", playerName);
                        put("key", key);
                        put("value", newValue);
                    }});
                }
            });
        });
    }

    private void sharedTakeCommand(CommandSender sender, String key, String value, String uuid, String playerName) {
        String messageKeyPrefix = "commands.take." + (playerName != null ? "player." : "global.");

        DocumentService.getDocument(uuid, key, (response) -> {
            if(response.error != null) {
                sharedSetCommand(sender, key, value, uuid, playerName);
                return;
            }

            String oldValue = response.value.toString();
            String newValue = PluginUtils.takeValue(oldValue, value);

            DocumentService.setDocument(uuid, key, newValue, (setResponse) -> {
                if(setResponse.error != null) {
                    if(setResponse.error.equals(AppwriteCallbackError.DOCUMENT_NOT_FOUND)) {
                        PluginUtils.SendMessage(sender, messageKeyPrefix + "not_found", new LinkedHashMap<String, String>(){{
                            put("player", playerName);
                            put("key", key);
                        }});
                    } else if(setResponse.error.equals(AppwriteCallbackError.UNEXPECTED_ERROR)) {
                        PluginUtils.SendMessage(sender, messageKeyPrefix + "unexpected_error", new LinkedHashMap<String, String>(){{
                            put("player", playerName);
                            put("key", key);
                        }});
                    }
                } else {
                    PluginUtils.SendMessage(sender, messageKeyPrefix + "success", new LinkedHashMap<String, String>(){{
                        put("player", playerName);
                        put("key", key);
                        put("value", value);
                    }});
                }
            });
        });
    }
}
