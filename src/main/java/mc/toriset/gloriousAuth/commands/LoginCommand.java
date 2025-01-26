package mc.toriset.gloriousAuth.commands;

import mc.toriset.gloriousAuth.GloriousAuth;
import mc.toriset.gloriousAuth.auth.AuthUser;
import mc.toriset.gloriousAuth.utils.AdventureUtils;
import mc.toriset.gloriousAuth.utils.LoginState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player player) {
            AuthUser authUser = GloriousAuth.getInstance().getAuthManager().getUser(player);

            if (authUser.isLoggedIn()) {
                commandSender.sendMessage(AdventureUtils.fromLegacy("&cYou're already logged in, whatcha tryna do?"));
                return true;
            }

            if (args.length < 1) {
                commandSender.sendMessage(AdventureUtils.fromLegacy("&cYou need to provide the password to login with, duhh..."));
                return true;
            }

            String passwordInput = args[0];
            boolean success = authUser.isCorrectPassword(passwordInput);

            if (success) {
                authUser.logIn();
            } else {
                commandSender.sendMessage(AdventureUtils.fromLegacy("&cIncorrect password, try again."));
            }
            return true;
        } else {
            commandSender.sendMessage(AdventureUtils.fromLegacy("&cOnly players can login."));
            return true;
        }
    }
}
