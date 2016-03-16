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