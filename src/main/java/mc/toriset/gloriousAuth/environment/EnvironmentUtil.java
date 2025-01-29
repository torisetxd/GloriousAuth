package mc.toriset.gloriousAuth.environment;

import mc.toriset.gloriousAuth.GloriousAuth;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EnvironmentUtil {
    private static void applyEffect(Player player, PotionEffectType type, boolean apply) {
        if (apply) {
            PotionEffect effect = new PotionEffect(
                    type,
                    Integer.MAX_VALUE,
                    255,
                    false,
                    false
            );
            player.addPotionEffect(effect);
        } else if (player.hasPotionEffect(type)) {
            player.removePotionEffect(type);
        }
    }

    public static void prepareEnvironment(Player player) {
        EnvironmentType envType = GloriousAuth.getInstance().getEnvType();
        if (envType == EnvironmentType.SPAWN || envType == EnvironmentType.WORLD) {
            if (GloriousAuth.getInstance().spawnLocation != null) {
                player.teleport(GloriousAuth.getInstance().spawnLocation);
            }
        }
        if (envType != EnvironmentType.WORLD) {
            applyEffect(player, PotionEffectType.BLINDNESS, true);
            applyEffect(player, PotionEffectType.SLOWNESS, true);
            applyEffect(player, PotionEffectType.JUMP_BOOST, true);
        }
    }

    public static void cleanupEnvironment(Player player, Location logoutLocation) {
        EnvironmentType envType = GloriousAuth.getInstance().getEnvType();
        if (envType == EnvironmentType.SPAWN || envType == EnvironmentType.WORLD) {
            if (logoutLocation != null) {
                player.teleport(logoutLocation);
            }
        }
        if (envType != EnvironmentType.WORLD) {
            applyEffect(player, PotionEffectType.BLINDNESS, false);
            applyEffect(player, PotionEffectType.SLOWNESS, false);
            applyEffect(player, PotionEffectType.JUMP_BOOST, false);
        }
    }
}