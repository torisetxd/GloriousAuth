package mc.toriset.gloriousAuth.auth;

import mc.toriset.gloriousAuth.GloriousAuth;
import mc.toriset.gloriousAuth.utils.LoginState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class AuthManager {
    public HashMap<UUID, AuthUser> authUserHashMap;
    private final File userFolder;

    public AuthManager() {
        authUserHashMap = new HashMap<>();
        userFolder = new File(GloriousAuth.getInstance().getDataFolder(), "users");
        if (!userFolder.exists()) {
            userFolder.mkdirs();
        }
    }

    public AuthUser getUser(Player player) {
        return authUserHashMap.computeIfAbsent(player.getUniqueId(), AuthUser::new);
    }

    public void freeUser(Player player) {
        authUserHashMap.remove(player.getUniqueId());
    }

    public File getUserFolder() {
        return userFolder;
    }
}