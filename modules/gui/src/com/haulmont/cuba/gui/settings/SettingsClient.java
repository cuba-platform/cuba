/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.settings;

import javax.annotation.Nullable;

/**
 * User settings provider for client application. May use caching for settings.
 *
 * @author artamonov
 * @version $Id$
 */
public interface SettingsClient {

    String NAME = "cuba_SettingsClient";

    String getSetting(String name);
    void setSetting(String name, @Nullable String value);

    void deleteSettings(String name);
}