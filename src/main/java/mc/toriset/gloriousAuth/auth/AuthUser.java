package mc.toriset.gloriousAuth.auth;

import mc.toriset.gloriousAuth.GloriousAuth;
import mc.toriset.gloriousAuth.config.UserConfig;
import mc.toriset.gloriousAuth.environment.EnvironmentUtil;
import mc.toriset.gloriousAuth.utils.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AuthUser {
    private final UUID uuid;
    private LoginState state;
    private final UserConfig config;

    public AuthUser(UUID uuid) {
        this.uuid = uuid;
        this.config = new UserConfig(uuid);
    }

    public Player getPlayer() {
        return GloriousAuth.getInstance().getServer().getPlayer(uuid);
    }

    public boolean isRegistered() {
        return config.hasPassword();
    }

    public boolean isCorrectPassword(String rawPassword) {
        return config.verifyPassword(rawPassword);
    }

    public void savePassword(String rawPassword) {
        config.setPassword(rawPassword);
    }

    public void saveLogoutLocation() {
        config.setLogoutLocation(getPlayer().getLocation());
    }

    public Location getLogoutLocation() {
        return config.getLogoutLocation();
    }

    public LoginState getState() {
        return state;
    }

    public boolean isLoggedIn() {
        return state == LoginState.LOGGED_IN;
    }

    public void setState(LoginState state) {
        this.state = state;
    }

    public void logIn() {
        setState(LoginState.LOGGED_IN);
        Component title = AdventureUtils.gradient("Success!", "ff6b6b", "ffd93d");
        Component subtitle = AdventureUtils.gradient("Successfully logged in!", "5df362", "30e836");

        getPlayer().showTitle(net.kyori.adventure.title.Title.title(
                title,
                subtitle,
                net.kyori.adventure.title.Title.Times.times(
                        java.time.Duration.ofMillis(250),
                        java.time.Duration.ofMillis(1250),
                        java.time.Duration.ofMillis(250)
                )
        ));
        getPlayer().sendActionBar("");

        cleanupLoginEnvironment();
    }

    public void logOut() {
        setState(LoginState.LOGGING_IN);
        prepareLoginEnvironment();
    }

    public void prepareLoginEnvironment() {
        EnvironmentUtil.prepareEnvironment(getPlayer());
    }

    public void cleanupLoginEnvironment() {
        EnvironmentUtil.cleanupEnvironment(getPlayer(), getLogoutLocation());
    }

    public UserConfig getConfig() {
        return config;
    }
}