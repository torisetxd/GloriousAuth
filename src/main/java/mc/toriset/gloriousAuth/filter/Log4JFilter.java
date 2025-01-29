package mc.toriset.gloriousAuth.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

// thanks to botsentry devs
// also in authme but differently
// and this is the way i remembered it
public class Log4JFilter implements Filter {
    public Filter.Result checkLog(String string) {
        if (string == null || string.isEmpty()) {
            return Filter.Result.NEUTRAL;
        }
        if (FilterHelper.isSensitiveLog(string)) {
            return Filter.Result.DENY;
        }
        return Filter.Result.NEUTRAL;
    }

    public Filter.Result getOnMismatch() {
        return Filter.Result.NEUTRAL;
    }

    public Filter.Result getOnMatch() {
        return Filter.Result.NEUTRAL;
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object ... objectArray) {
        return this.checkLog(string);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object) {
        return this.checkLog(string);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2) {
        return this.checkLog(string);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3) {
        return this.checkLog(string);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3, Object object4) {
        return this.checkLog(string);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3, Object object4, Object object5) {
        return this.checkLog(string);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3, Object object4, Object object5, Object object6) {
        return this.checkLog(string);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3, Object object4, Object object5, Object object6, Object object7) {
        return this.checkLog(string);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3, Object object4, Object object5, Object object6, Object object7, Object object8) {
        return this.checkLog(string);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3, Object object4, Object object5, Object object6, Object object7, Object object8, Object object9) {
        return this.checkLog(string);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3, Object object4, Object object5, Object object6, Object object7, Object object8, Object object9, Object object10) {
        return this.checkLog(string);
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Object object, Throwable throwable) {
        return this.checkLog(object.toString());
    }

    public Filter.Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        return this.checkLog(message.getFormattedMessage());
    }

    public Filter.Result filter(LogEvent logEvent) {
        return this.checkLog(logEvent.getMessage().getFormattedMessage());
    }

    public State getState() {
        return null;
    }

    public void initialize() {
    }

    public void start() {
    }

    public void stop() {
    }

    public boolean isStarted() {
        return true;
    }

    public boolean isStopped() {
        return false;
    }

}
