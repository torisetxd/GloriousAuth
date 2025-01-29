package mc.toriset.gloriousAuth.config;

import mc.toriset.gloriousAuth.GloriousAuth;
import mc.toriset.gloriousAuth.auth.AuthManager;
import mc.toriset.gloriousAuth.auth.AuthUser;
import mc.toriset.gloriousAuth.utils.HashAlgorithm;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.UUID;

public class ConfigUpdater {
    protected static final String CONFIG_VERSION_PATH = "do_not_edit.version";
    public static float findVersion(FileConfiguration config) {
        // there's no getFloat for some reason...
        return (float) config.getDouble(CONFIG_VERSION_PATH, 1.0);
    }

    public static void updateConfigs() {
        GloriousAuth gloriousAuth = GloriousAuth.getInstance();
        FileConfiguration config = GloriousAuth.getInstance().getConfig();
        AuthManager authManager = gloriousAuth.getAuthManager();
        ArrayList<UUID> uuids = authManager.getOfflineUsers();

        float configVersion = findVersion(config);
        float pluginVersion = Float.parseFloat(gloriousAuth.getPluginMeta().getVersion());
        while (configVersion < pluginVersion) {
            gloriousAuth.getLogger().info("Updating config from version " + configVersion + " to " + pluginVersion);

            // algorithm field got removed & merged with password aka secured field now.
            if (configVersion == 1.0f) {
                configVersion = 2.0f;

                for (UUID userUuid : uuids) {
                    AuthUser user = authManager.getUser(userUuid);
                    String algorithmString = user.getConfig().raw().getString("algorithm");
                    String password = user.getConfig().raw().getString("password");
                    HashAlgorithm algorithm = HashAlgorithm.valueOf(algorithmString);

                    user.getConfig().setAlgorithmPassword(algorithm, password);
                }
            }

            config.set(CONFIG_VERSION_PATH, configVersion);
        }

        // cleanup the bunch of users in memory
        for (UUID userUuid : uuids) {
            authManager.freeUser(userUuid);
        }
    }
}
