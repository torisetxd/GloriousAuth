package mc.toriset.gloriousAuth.listener;

import mc.toriset.gloriousAuth.GloriousAuth;
import mc.toriset.gloriousAuth.auth.AuthUser;
import mc.toriset.gloriousAuth.environment.EnvironmentType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;

public class AuthListener implements Listener {
    public AuthUser getAuthUser(Player player) {
        return GloriousAuth.getInstance().getAuthManager().getUser(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent damageByEntityEvent) {
        if (damageByEntityEvent.getDamager() instanceof Player attacker) {
            if (!getAuthUser(attacker).isLoggedIn()) {
                damageByEntityEvent.setCancelled(true);
            }
        } else if (damageByEntityEvent.getEntity() instanceof Player victim) {
            if (!getAuthUser(victim).isLoggedIn()) {
                damageByEntityEvent.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent commandSendEvent) {
        if (!getAuthUser(commandSendEvent.getPlayer()).isLoggedIn()) {
            String command = commandSendEvent.getMessage().replace("/", "");
            if (!(command.startsWith("login") || command.startsWith("register"))) {
                commandSendEvent.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent blockBreakEvent) {
        if (!getAuthUser(blockBreakEvent.getPlayer()).isLoggedIn()) {
            blockBreakEvent.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent blockPlaceEvent) {
        if (!getAuthUser(blockPlaceEvent.getPlayer()).isLoggedIn()) {
            blockPlaceEvent.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent dropItemEvent) {
        if (!getAuthUser(dropItemEvent.getPlayer()).isLoggedIn()) {
            dropItemEvent.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent moveEvent) {
        if (!getAuthUser(moveEvent.getPlayer()).isLoggedIn() && GloriousAuth.getInstance().getEnvType() != EnvironmentType.WORLD) {
            moveEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent playerJoinEvent) {
        AuthUser authUser = getAuthUser(playerJoinEvent.getPlayer());
        authUser.logOut();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        AuthUser user = getAuthUser(player);
        if (user.isLoggedIn()) {
            getAuthUser(player).saveLogoutLocation();
        }
        GloriousAuth.getInstance().getAuthManager().freeUser(player);
    }
}
