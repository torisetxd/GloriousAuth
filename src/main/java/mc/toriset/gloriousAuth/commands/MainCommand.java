package mc.toriset.gloriousAuth.commands;

import mc.toriset.gloriousAuth.GloriousAuth;
import mc.toriset.gloriousAuth.utils.AdventureUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player player) {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                commandSender.sendMessage(GloriousAuth.fullprefix);
                commandSender.sendMessage(AdventureUtils.parse("<gray>  /gauth setspawn <dark_gray>- <gray>Set the spawn for the WORLD type."));
                commandSender.sendMessage(AdventureUtils.parse("<gray>  /gauth reload <dark_gray>- <gray>Reload the config."));
                return true;
            }
            else if (args[0].equalsIgnoreCase("setspawn")) {
                GloriousAuth.getInstance().spawnLocation = player.getLocation();
                GloriousAuth.getInstance().saveSpawnLocation();
                commandSender.sendMessage(GloriousAuth.prefix.append(AdventureUtils.fromLegacy("&aSet spawn location successfully.")));
            }
            else if (args[0].equalsIgnoreCase("reload")) {
                GloriousAuth.getInstance().reloadConfig();
                commandSender.sendMessage(GloriousAuth.prefix.append(AdventureUtils.fromLegacy("&aConfig reloaded successfully.")));
            } else {
                commandSender.sendMessage(GloriousAuth.prefix.append(AdventureUtils.fromLegacy("&cUnknown command.")));
            }
            return true;
        } else {
            commandSender.sendMessage(GloriousAuth.prefix.append(AdventureUtils.fromLegacy("&cOnly players can run this command.")));
            return true;
        }
    }
}
