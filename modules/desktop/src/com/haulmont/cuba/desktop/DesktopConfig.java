/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Prefix;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInt;

/**
 * Desktop UI configuration parameters.
 * <p/>
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Source(type = SourceType.APP)
@Prefix("cuba.desktop.")
public interface DesktopConfig extends Config {

    /**
     * List of root directories where resources (icons, images, theme config) are located.
     * Resources are local to each desktop theme.
     * For each root directory must exist subdirectory with name of current theme.
     *
     * @return list of root resource directories
     */
    @Default("com/haulmont/cuba/desktop/res")
    String getResourceLocations();

    @DefaultInt(25)
    int getMainTabCaptionLength();

    /**
     * Desktop theme name.
     * Theme configuration file is located as <code>${resourceLocation}/${themeName}/${themeName}.xml</code>.
     *
     * @return desktop theme name
     */
    @Default("nimbus")
    String getTheme();

    @DefaultBoolean(false)
    boolean isDialogNotificationsEnabled();
}
