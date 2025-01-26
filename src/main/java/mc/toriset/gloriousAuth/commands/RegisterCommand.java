package mc.toriset.gloriousAuth.commands;

import mc.toriset.gloriousAuth.GloriousAuth;
import mc.toriset.gloriousAuth.auth.AuthUser;
import mc.toriset.gloriousAuth.utils.AdventureUtils;
import mc.toriset.gloriousAuth.utils.LoginState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RegisterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player player) {
            AuthUser authUser = GloriousAuth.getInstance().getAuthManager().getUser(player);

            if (authUser.isLoggedIn()) {
                commandSender.sendMessage(AdventureUtils.fromLegacy("&cYou're already logged in, whatcha tryna do?"));
                return true;
            }

            if (authUser.isRegistered()) {
                commandSender.sendMessage(AdventureUtils.fromLegacy("&cYou're already registered, please login instead."));
                return true;
            }

            if (args.length < 2) {
                commandSender.sendMessage(AdventureUtils.fromLegacy("&cPlease use /register <password> <password again>"));
                return true;
            }

            String firstPassword = args[0];
            String secondPassword = args[1];

            if (!firstPassword.equals(secondPassword)) {
                commandSender.sendMessage(AdventureUtils.fromLegacy("&cHey, the passwords dont match, maybe you made a typo?"));
                return true;
            }

            authUser.savePassword(firstPassword);
            authUser.logIn();

            return true;
        } else {
            commandSender.sendMessage(AdventureUtils.fromLegacy("&cOnly players can login."));
            return true;
        }
    }
}
