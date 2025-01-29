package mc.toriset.gloriousAuth.filter;

import mc.toriset.gloriousAuth.GloriousAuth;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class FilterHelper {
    public static void init(){
        ConsoleFilter filter = new ConsoleFilter();
        GloriousAuth.getInstance().getLogger().setFilter(filter);
        Bukkit.getLogger().setFilter(filter);
        Logger.getLogger("Minecraft").setFilter(filter);

        org.apache.logging.log4j.core.Logger rootLogger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        rootLogger.addFilter(new Log4JFilter());
    }

    public static boolean isSensitiveLog(String log) {
        return (log.contains("/gloriousauth:login") || log.contains("/login") || log.contains("/gloriousauth:register") || log.contains("/register"));
    }
}
