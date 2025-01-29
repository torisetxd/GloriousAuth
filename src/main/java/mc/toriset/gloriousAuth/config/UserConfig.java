package mc.toriset.gloriousAuth.config;
import mc.toriset.gloriousAuth.GloriousAuth;
import mc.toriset.gloriousAuth.utils.*;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class UserConfig {
    private final File userFile;
    private YamlConfiguration config;
    private static final String DELIMITER = "::";

    public UserConfig(UUID uuid) {
        this.userFile = new File(GloriousAuth.getInstance().getAuthManager().getUserFolder(),
                uuid.toString() + ".yml");
        initializeConfig();
    }

    private void initializeConfig() {
        if (!userFile.exists()) {
            try {
                userFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create user config file", e);
            }
        }
        this.config = YamlConfiguration.loadConfiguration(userFile);
    }

    private void save() {
        try {
            config.save(userFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save user config", e);
        }
    }

    public boolean hasPassword() {
        return config.contains("password");
    }

    public boolean verifyPassword(String rawPassword) {
        String authData = config.getString("password");
        System.out.println("authData: " + authData);
        if (authData == null) return false;

        String[] parts = authData.split(DELIMITER);
        System.out.println("parts: " + Arrays.toString(parts));
        if (parts.length != 2) return false;


        HashAlgorithm algorithm = HashAlgorithm.valueOf(parts[0]);
        System.out.println("algo: " + algorithm);
        String hashedPassword = parts[1];
        System.out.println("hashedPassword: " + hashedPassword);
        return SecurityUtils.comparePassword(algorithm, rawPassword, hashedPassword);
    }

    public void setPassword(String rawPassword) {
        setAlgorithmPassword(GloriousAuth.getInstance().getAlgorithm(), rawPassword);
    }

    public void setAlgorithmPassword(HashAlgorithm algorithm, String rawPassword) {
        setAlgorithmSecuredField(algorithm, "password", rawPassword);
    }

    public void setAlgorithmSecuredField(HashAlgorithm algorithm, String key, String value) {
        String hashedValue = SecurityUtils.securePassword(algorithm, value);
        setAlgorithmRawField(algorithm, key, hashedValue);
    }

    public void setAlgorithmRawField(HashAlgorithm algorithm, String key, String value) {
        String securedData = algorithm.name() + DELIMITER + value;
        config.set(key, securedData);
        save();
    }

    public void setLogoutLocation(Location location) {
        config.set("logout", LocationUtil.dump(location));
        save();
    }

    public Location getLogoutLocation() {
        String logoutString = config.getString("logout");
        return LocationUtil.load(logoutString);
    }

    public YamlConfiguration raw() {
        return config;
    }
}