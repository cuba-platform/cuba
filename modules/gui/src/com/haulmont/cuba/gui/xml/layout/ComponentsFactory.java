/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Timer;

/**
 * Factory to create UI components in client independent manner.
 * <p/> An instance of the factory can be injected into screen controllers or obtained through {@link com.haulmont.cuba.core.global.AppBeans}.
 *
 * @author Abramov
 * @version $Id$
 */
public interface ComponentsFactory {

    String NAME = "cuba_ComponentsFactory";

    /**
     * Create a component instance by its name.
     *
     * @param name component name. It is usually defined in NAME constant inside the component interface,
     *             e.g. {@link com.haulmont.cuba.gui.components.Label#NAME}.
     *             It is also usually equal to component's XML name.
     * @return component instance for the current client type (web or desktop)
     */
    <T extends Component> T createComponent(String name);

    /**
     * Create a timer instance.
     * @return client-specific implementation of the timer
     */
    Timer createTimer();
}