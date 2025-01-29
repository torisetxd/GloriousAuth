package mc.toriset.gloriousAuth.auth;

import mc.toriset.gloriousAuth.GloriousAuth;
import mc.toriset.gloriousAuth.utils.LoginState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

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

    public ArrayList<UUID> getOfflineUsers() {
        ArrayList<UUID> userUUIDs = new ArrayList<>();
        Path userFolderPath = userFolder.toPath();

        try {
            Files.walk(userFolderPath)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        String fileName = file.getFileName().toString();
                        try {
                            String uuidString = fileName.substring(0, fileName.length() - 4);
                            UUID uuid = UUID.fromString(uuidString);
                            userUUIDs.add(uuid);
                        } catch (IllegalArgumentException e) {
                            GloriousAuth.getInstance().getLogger().log(Level.WARNING, "Skipping invalid UUID file: " + file, e);
                        }
                    });
        } catch (IOException e) {
            GloriousAuth.getInstance().getLogger().log(Level.SEVERE, "Error reading user files", e);
        }

        return userUUIDs;
    }

    public AuthUser getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    public AuthUser getUser(UUID uuid) {
        return authUserHashMap.computeIfAbsent(uuid, AuthUser::new);
    }

    public void freeUser(Player player) {
        freeUser(player.getUniqueId());
    }

    public void freeUser(UUID uuid) {
        authUserHashMap.remove(uuid);
    }

    public File getUserFolder() {
        return userFolder;
    }
}