/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.logging;

import ch.qos.logback.classic.Level;

import java.util.LinkedList;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class LoggingHelper {

    public static List<Level> getLevels() {
        List<Level> levels = new LinkedList<>();
        levels.add(Level.ALL);
        levels.add(Level.TRACE);
        levels.add(Level.DEBUG);
        levels.add(Level.INFO);
        levels.add(Level.WARN);
        levels.add(Level.ERROR);
        levels.add(Level.OFF);
        return levels;
    }

    public static Level getLevelFromString(String levelString) {
        for (Level logLevel : getLevels()) {
            if (logLevel.toString().equalsIgnoreCase(levelString))
                return logLevel;
        }
        return null;
    }
}