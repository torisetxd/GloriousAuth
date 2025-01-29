package mc.toriset.gloriousAuth;

import mc.toriset.gloriousAuth.auth.AuthManager;
import mc.toriset.gloriousAuth.auth.AuthUser;
import mc.toriset.gloriousAuth.commands.MainCommand;
import mc.toriset.gloriousAuth.commands.LoginCommand;
import mc.toriset.gloriousAuth.commands.RegisterCommand;
import mc.toriset.gloriousAuth.config.ConfigUpdater;
import mc.toriset.gloriousAuth.environment.EnvironmentType;
import mc.toriset.gloriousAuth.listener.AuthListener;
import mc.toriset.gloriousAuth.utils.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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

        ConfigUpdater.updateConfigs();

        loadSpawnLocation();

        for (Player player : Bukkit.getOnlinePlayers()) {
            // no need to rejoin if reloading plugin, although still not intended
            authManager.getUser(player).logOut();
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                AuthUser authUser = authManager.getUser(player);
                if (authUser.getState() == LoginState.LOGGING_IN) {
                    Component title = AdventureUtils.gradient("Authentication Required", "ff6b6b", "ffd93d");
                    Component subtitle;

                    if (authUser.getConfig().hasPassword()) {
                        subtitle = AdventureUtils.hex("ffd93d", "/login <password>");
                    } else {
                        subtitle = AdventureUtils.hex("ffd93d", "/register <password> <password>");
                    }

                    Component actionBar = AdventureUtils.gradient("Please authenticate to continue playing", "#4ecdc4", "#45b7af");
                    player.showTitle(net.kyori.adventure.title.Title.title(
                            title,
                            subtitle,
                            net.kyori.adventure.title.Title.Times.times(
                                    java.time.Duration.ofMillis(0),
                                    java.time.Duration.ofMillis(1250),
                                    java.time.Duration.ofMillis(250)
                            )
                    ));

                    player.sendActionBar(actionBar);
                }
            }
        }, 0L, 20L);
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

    public HashAlgorithm getAlgorithm() {
        return HashAlgorithm.valueOf(getConfig().getString("security.algorithm"));
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
