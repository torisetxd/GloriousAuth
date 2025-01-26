package mc.toriset.gloriousAuth.auth;

import mc.toriset.gloriousAuth.GloriousAuth;
import mc.toriset.gloriousAuth.utils.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AuthUser {
    UUID uuid;
    LoginState state;

    public File getUserFile() {
        File userFile = new File(GloriousAuth.getInstance().getAuthManager().getUserFolder(), uuid.toString() + ".yml");
        if (!userFile.exists()) {
            try {
                userFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return userFile;
    }

    private YamlConfiguration loadDataInternal() {
        File userFile = getUserFile();
        return YamlConfiguration.loadConfiguration(userFile);
    }

    private void saveDataInternal(YamlConfiguration config) {
        File userFile = getUserFile();
        try {
            config.save(userFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthUser(UUID uuid) {
        this.uuid = uuid;
    }

    public Player getPlayer() {
        return GloriousAuth.getInstance().getServer().getPlayer(uuid);
    }

    public boolean isRegistered() {
        return loadDataInternal().contains("password");
    }

    public boolean isCorrectPassword(String rawPassword){
        YamlConfiguration data = loadDataInternal();
        String algorithmString = data.getString("algorithm");
        String hashedPassword = data.getString("password");
        PasswordAlgorithm algorithm = PasswordAlgorithm.valueOf(algorithmString);
        return SecurityUtils.comparePassword(algorithm, rawPassword, hashedPassword);
    }

    public void savePassword(String rawPassword){
        YamlConfiguration data = loadDataInternal();
        PasswordAlgorithm algorithm = GloriousAuth.getInstance().getAlgorithm();
        String hashedPassword = SecurityUtils.securePassword(algorithm, rawPassword);
        data.set("algorithm", algorithm.name());
        data.set("password", hashedPassword);
        saveDataInternal(data);
    }

    public void saveLogoutLocation() {
        Location location = getPlayer().getLocation();
        YamlConfiguration data = loadDataInternal();
        data.set("logout", LocationUtil.dump(location));
        saveDataInternal(data);
    }

    public Location getLogoutLocation() {
        YamlConfiguration data = loadDataInternal();
        String logoutString = data.getString("logout");
        return LocationUtil.load(logoutString);
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

    private void givePermanentEffect(PotionEffectType potionEffectType) {
        PotionEffect effect = new PotionEffect(
                potionEffectType,
                Integer.MAX_VALUE,
                255,
                false,
                false
        );
        getPlayer().addPotionEffect(effect);
    }

    private void removePermanentEffect(PotionEffectType potionEffectType) {
        if (getPlayer().hasPotionEffect(potionEffectType)) {
            getPlayer().removePotionEffect(potionEffectType);
        }
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
        EnvironmentType envType = GloriousAuth.getInstance().getEnvType();
        if (envType == EnvironmentType.SPAWN || envType == EnvironmentType.WORLD) {
            if (GloriousAuth.getInstance().spawnLocation != null){
                getPlayer().teleport(GloriousAuth.getInstance().spawnLocation);
            }
        }
        if (envType != EnvironmentType.WORLD) {
            givePermanentEffect(PotionEffectType.BLINDNESS);
            givePermanentEffect(PotionEffectType.SLOWNESS);
            givePermanentEffect(PotionEffectType.JUMP_BOOST);
        }
    }

    public void cleanupLoginEnvironment() {
        EnvironmentType envType = GloriousAuth.getInstance().getEnvType();
        if (envType == EnvironmentType.SPAWN || envType == EnvironmentType.WORLD) {
            if (getLogoutLocation() != null) {
                getPlayer().teleport(getLogoutLocation());
            }
        }
        if (envType != EnvironmentType.WORLD) {
            removePermanentEffect(PotionEffectType.BLINDNESS);
            removePermanentEffect(PotionEffectType.SLOWNESS);
            removePermanentEffect(PotionEffectType.JUMP_BOOST);
        }
    }
}
