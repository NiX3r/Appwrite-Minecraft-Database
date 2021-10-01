package eu.ncodes.appwritedatabase.Utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import eu.ncodes.appwritedatabase.Enums.TypeOfValueEnum;
import eu.ncodes.appwritedatabase.Instances.PluginMessagesInstance;
import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Services.DocumentService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PluginUtils {

    public static void SendMessage(Player target, boolean usePrefix, String message) {

        String toSend = "";

        if(usePrefix) {
            toSend = PluginVariables.Prefix;
        }

        toSend += message;

        target.sendMessage(ChatColor.translateAlternateColorCodes('&', toSend));

    }

    public static void SendMessage(CommandSender target, boolean usePrefix, String message) {

        String toSend = "";

        if(usePrefix) {
            toSend = PluginVariables.Prefix;
        }

        toSend += message;

        target.sendMessage(ChatColor.translateAlternateColorCodes('&', toSend));

    }

    public static void SendMessage(Player target, String message) {
        SendMessage(target, true, message);
    }

    public static void SendMessage(CommandSender target, String message) {
        SendMessage(target, true, message);
    }

    public static String addValue(String oldValue, String newValue) {
        TypeOfValueEnum newValueType = PluginUtils.GetValueType(newValue);
        TypeOfValueEnum oldValueType = PluginUtils.GetValueType(oldValue);

        String value = newValue;

        if(oldValueType.equals(TypeOfValueEnum.STRING)) {
            value = oldValue + newValueType;
        } else if(oldValueType.equals(TypeOfValueEnum.INT)) {
            value = ((Integer)(Integer.parseInt(oldValue) + Integer.parseInt(newValue))).toString();
        } else if(oldValueType.equals(TypeOfValueEnum.DOUBLE)) {
            value = ((Double)(Double.parseDouble(oldValue) + Double.parseDouble(newValue))).toString();
        }

        return value;
    }

    public static String takeValue(String oldValue, String newValue) {
        TypeOfValueEnum oldValueType = PluginUtils.GetValueType(oldValue);

        String value = newValue;

        if(oldValueType.equals(TypeOfValueEnum.STRING)) {
            value = oldValue.replaceAll(newValue, "");
        } else if(oldValueType.equals(TypeOfValueEnum.INT)) {
            value = ((Integer)(Integer.parseInt(oldValue) - Integer.parseInt(newValue))).toString();
        } else if(oldValueType.equals(TypeOfValueEnum.DOUBLE)) {
            value = ((Double)(Double.parseDouble(oldValue) - Double.parseDouble(newValue))).toString();
        }

        return value;
    }

    public static TypeOfValueEnum GetValueType(String value) {
        try {
            Integer.parseInt(value);
            return TypeOfValueEnum.INT;
        } catch(Exception ex) {}

        try {
            Double.parseDouble(value);
            return TypeOfValueEnum.DOUBLE;
        } catch(Exception ex) {}

        return TypeOfValueEnum.STRING;
    }

}
