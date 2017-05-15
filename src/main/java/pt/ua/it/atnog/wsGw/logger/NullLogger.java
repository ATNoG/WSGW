package pt.ua.it.atnog.wsGw.logger;

import org.eclipse.jetty.util.log.Logger;

/**
 * Null logger to suppress the logging messages of jetty.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class NullLogger implements Logger {
    @Override
    public String getName() {
        return "null";
    }

    @Override
    public void warn(String msg, Object... args) {
    }

    @Override
    public void warn(Throwable thrown) {
    }

    @Override
    public void warn(String msg, Throwable thrown) {
    }

    @Override
    public void info(String msg, Object... args) {
    }

    @Override
    public void info(Throwable thrown) {
    }

    @Override
    public void info(String msg, Throwable thrown) {
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void setDebugEnabled(boolean enabled) {
    }

    @Override
    public void debug(String msg, Object... args) {
    }

    @Override
    public void debug(String s, long l) {
    }

    @Override
    public void debug(Throwable thrown) {
    }

    @Override
    public void debug(String msg, Throwable thrown) {
    }

    @Override
    public Logger getLogger(String name) {
        return this;
    }

    @Override
    public void ignore(Throwable ignored) {
    }
}
