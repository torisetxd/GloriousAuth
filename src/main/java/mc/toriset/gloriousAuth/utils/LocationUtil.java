package mc.toriset.gloriousAuth.utils;

import mc.toriset.gloriousAuth.GloriousAuth;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtil {
    public static Location load(String locationString) {
        try {
            String[] logoutParts = locationString.split(",");
            World world = GloriousAuth.getInstance().getServer().getWorld(logoutParts[0]);
            double x = Double.parseDouble(logoutParts[1]);
            double y = Double.parseDouble(logoutParts[2]);
            double z = Double.parseDouble(logoutParts[3]);
            float yaw = Float.parseFloat(logoutParts[4]);
            float pitch = Float.parseFloat(logoutParts[5]);
            return new Location(world, x,y,z,yaw,pitch);
        } catch (Exception e) {
            return null;
        }
    }

    public static String dump(Location location){
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }

}
