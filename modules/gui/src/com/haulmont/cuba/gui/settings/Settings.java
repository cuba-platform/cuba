/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.settings;

import org.dom4j.Element;

/**
 * Interface defining methods for working with screen settings.
 * <p>Screen settings are saved in the database for the current user.
 *
 * @see com.haulmont.cuba.gui.components.AbstractWindow#getSettings()
 * @see com.haulmont.cuba.gui.components.AbstractWindow#applySettings(Settings)
 */
public interface Settings {

    /**
     * @return root element of the screen settings. Never null.
     */
    Element get();

    /**
     * @return an element of the screen settings for the given component. Never null.
     * For example:
     * <pre>
     *     getSettings().get(hintBox.getId()).addAttribute("visible", "false");
     * </pre>
     */
    Element get(String componentId);

    /**
     * INTERNAL. The lifecycle of settings is controlled by the framework.
     */
    void setModified(boolean modified);

    /**
     * INTERNAL. The lifecycle of settings is controlled by the framework.
     */
    void commit();

    /**
     * INTERNAL. The lifecycle of settings is controlled by the framework.
     */
    void delete();
}