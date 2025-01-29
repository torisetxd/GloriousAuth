package mc.toriset.gloriousAuth.filter;

import mc.toriset.gloriousAuth.GloriousAuth;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;

import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

// credits to Xephi59 from AuthMeReloaded
public class ConsoleFilter implements Filter {
    @Override
    public boolean isLoggable(LogRecord record) {
        if (record == null || record.getMessage() == null) {
            return true;
        }

        return FilterHelper.isSensitiveLog(record.getMessage());
    }

}