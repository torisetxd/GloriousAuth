package mc.toriset.gloriousAuth;

import mc.toriset.gloriousAuth.auth.AuthManager;
import mc.toriset.gloriousAuth.commands.MainCommand;
import mc.toriset.gloriousAuth.commands.LoginCommand;
import mc.toriset.gloriousAuth.commands.RegisterCommand;
import mc.toriset.gloriousAuth.listener.AuthListener;
import mc.toriset.gloriousAuth.utils.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public final class GloriousAuth extends JavaPlugin {
    static GloriousAuth instance;
    AuthManager authManager;
    public Location spawnLocation;
    public static final Component prefix = AdventureUtils.parse("<dark_gray>» <gradient:#7f78f6:#1dbad7:#00ffe2>GAuth <gray>");
    public static final Component fullprefix = AdventureUtils.parse("<dark_gray>» <gradient:#7f78f6:#1dbad7:#00ffe2>Glorious Auth <gray>");

    @Override
    public void onEnable() {
        saveDefaultConfig();

        instance = this;
        authManager = new AuthManager();

        this.getCommand("login").setExecutor(new LoginCommand());
        this.getCommand("register").setExecutor(new RegisterCommand());
        this.getCommand("gloriousauth").setExecutor(new MainCommand());

        this.getServer().getPluginManager().registerEvents(new AuthListener(), this);

        loadSpawnLocation();

        for (Player player : Bukkit.getOnlinePlayers()) {
            // no need to rejoin if reloading plugin, although still not intended
            authManager.getUser(player).logOut();
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (authManager.getUser(player).getState() == LoginState.LOGGING_IN) {
                    player.sendMessage(AdventureUtils.fromLegacy("&6Please login using &7/login <password>&6."));
                }
            }
        }, 0L, 100L);
    }

    @Override
    public void onDisable() {
        saveSpawnLocation();
    }

    public void loadSpawnLocation(){
        spawnLocation = LocationUtil.load(getConfig().getString("do_not_edit.spawn_location"));
    }

    public void saveSpawnLocation() {
        getConfig().set("do_not_edit.spawn_location", LocationUtil.dump(spawnLocation));
        saveConfig();
    }

    public PasswordAlgorithm getAlgorithm() {
        return PasswordAlgorithm.valueOf(getConfig().getString("security.algorithm"));
    }

    public EnvironmentType getEnvType() {
        return EnvironmentType.valueOf(getConfig().getString("environment.type"));
    }

    public static GloriousAuth getInstance() {
        return instance;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }
}
