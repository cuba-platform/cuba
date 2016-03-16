/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.core.sys.logging;

import ch.qos.logback.classic.Level;

import java.util.LinkedList;
import java.util.List;

/**
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