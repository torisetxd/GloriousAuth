package mc.toriset.gloriousAuth.auth;

import mc.toriset.gloriousAuth.GloriousAuth;
import mc.toriset.gloriousAuth.config.UserConfig;
import mc.toriset.gloriousAuth.environment.EnvironmentUtil;
import mc.toriset.gloriousAuth.utils.*;
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
        getPlayer().sendMessage(AdventureUtils.fromLegacy("&aYou've successfully logged in."));
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