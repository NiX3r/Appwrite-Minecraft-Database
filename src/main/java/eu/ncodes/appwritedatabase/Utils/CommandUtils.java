package eu.ncodes.appwritedatabase.Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandUtils {

    public static JsonObject getDefaultsHashMap(int startIndex) {
        JsonObject output = new JsonObject();

        int outputIndex = 0, arrayIndex = 0;
        Set<String> keysSet = PluginVariables.defaults.get().getConfigurationSection("defaults").getKeys(false);
        String[] keys = new String[keysSet.size()];
        keysSet.toArray(keys);

        Arrays.sort(keys);

        for(String key : keys) {
                if(arrayIndex >= startIndex) {
                    output.addProperty(key, PluginVariables.defaults.get().getConfigurationSection("defaults").getString(key));
                    outputIndex++;
                }

                if(outputIndex == 9) {
                    break;
                }

                arrayIndex++;
        }

        return output;
    }

    public static int getDefaultsSize(){
        return PluginVariables.defaults.get().getKeys(true).size() - 1;
    }

    public static void sendFooter(CommandSender target, String command, int currentPage, int maxPage){
        // Footer visualtiazion
        String previous = "←--";
        String current = "&a" + currentPage + "/" + maxPage;
        String next = "--→";

        final ComponentBuilder message = new ComponentBuilder("");

        command = "/" + command;

        if(currentPage == 1){
            previous = "&7" + previous;
            message.append(previous.replace("&", "§"));
        }
        else{
            previous = "&a" + previous;
            message.append(previous.replace("&", "§"));
            message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command + " " + (currentPage - 1)));
            message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    TextComponent.fromLegacyText("§7Click to show previous page")));
        }

        message.append("   " + current.replace("&", "§") + "   ");
        message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command + " " + currentPage));
        message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                TextComponent.fromLegacyText("§7Click to refresh current page")));

        if(currentPage == maxPage){
            next = "&7" + next;
            message.append(next.replace("&", "§"));
        }
        else{
            message.append(next.replace("&", "§"));
            next = "&a" + next;
            message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command + " " + (currentPage + 1)));
            message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    TextComponent.fromLegacyText("§7Click to show next page")));
        }

        target.spigot().sendMessage(message.create());
    }
}
