/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.core.app;

import org.jgroups.logging.CustomLogFactory;
import org.jgroups.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JGroupsLoggerFactory implements CustomLogFactory {
    public static JGroupsLoggerFactory INSTANCE = new JGroupsLoggerFactory();

    @Override
    public Log getLog(Class clazz) {
        Logger logger = LoggerFactory.getLogger(clazz);
        return new Slf4jLog(logger);
    }

    @Override
    public Log getLog(String category) {
        Logger logger = LoggerFactory.getLogger(category);
        return new Slf4jLog(logger);
    }

    protected static class Slf4jLog implements Log {
        protected final Logger logger;

        public Slf4jLog(Logger logger) {
            this.logger = logger;
        }

        @Override
        public boolean isFatalEnabled() {
            return isErrorEnabled();
        }

        @Override
        public boolean isErrorEnabled() {
            return logger.isErrorEnabled();
        }

        @Override
        public boolean isWarnEnabled() {
            return logger.isWarnEnabled();
        }

        @Override
        public boolean isInfoEnabled() {
            return logger.isInfoEnabled();
        }

        @Override
        public boolean isDebugEnabled() {
            return logger.isDebugEnabled();
        }

        @Override
        public boolean isTraceEnabled() {
            return logger.isTraceEnabled();
        }

        @Override
        public void fatal(String msg) {
            error(msg);
        }

        @Override
        public void fatal(String msg, Object... args) {
            error(replaceParams(msg), args);
        }

        @Override
        public void fatal(String msg, Throwable throwable) {
            error(msg, throwable);
        }

        @Override
        public void error(String msg) {
            logger.error(msg);
        }

        @Override
        public void error(String format, Object... args) {
            logger.error(format, args);
        }

        @Override
        public void error(String msg, Throwable throwable) {
            logger.error(msg, throwable);
        }

        @Override
        public void warn(String msg) {
            logger.warn(msg);
        }

        @Override
        public void warn(String msg, Object... args) {
            logger.warn(replaceParams(msg), args);
        }

        @Override
        public void warn(String msg, Throwable throwable) {
            logger.warn(msg, throwable);
        }

        @Override
        public void info(String msg) {
            logger.info(msg);
        }

        @Override
        public void info(String msg, Object... args) {
            logger.info(replaceParams(msg), args);
        }

        @Override
        public void debug(String msg) {
            logger.debug(msg);
        }

        @Override
        public void debug(String msg, Object... args) {
            logger.debug(replaceParams(msg), args);
        }

        @Override
        public void debug(String msg, Throwable throwable) {
            logger.debug(msg, throwable);
        }

        @Override
        public void trace(Object msg) {
            if (msg != null) {
                if (msg instanceof Throwable) {
                    trace("", (Throwable) msg);
                } else {
                    trace(msg.toString());
                }
            }
        }

        @Override
        public void trace(String msg) {
            logger.trace(msg);
        }

        @Override
        public void trace(String msg, Object... args) {
            logger.trace(replaceParams(msg), args);
        }

        @Override
        public void trace(String msg, Throwable throwable) {
            logger.trace(msg, throwable);
        }

        @Override
        public void setLevel(String level) {
            //Do nothing. Set logger level by standard logback.xml
        }

        @Override
        public String getLevel() {
            if (logger instanceof ch.qos.logback.classic.Logger) {
                return ((ch.qos.logback.classic.Logger) logger).getLevel().toString();
            } else {
                return "INFO";
            }
        }

        protected String replaceParams(String msg) {
            if (msg != null) {
                msg = msg.replace("%s", "{}");
                return msg.replace("%d", "{}");
            }
            return null;
        }
    }
}
