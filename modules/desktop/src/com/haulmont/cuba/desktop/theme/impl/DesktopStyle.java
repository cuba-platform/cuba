/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.theme.impl;

import com.haulmont.cuba.desktop.theme.ComponentDecorator;

import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public class DesktopStyle {

    private String name;
    private List<ComponentDecorator> decorators;
    private List<Class> supportedComponents;

    public DesktopStyle(String name, List<ComponentDecorator> decorators, List<Class> supportedComponents) {
        this.name = name;
        this.decorators = decorators;
        this.supportedComponents = supportedComponents;
    }

    /*
     * If not specified supported components than supports all
     */
    public boolean isSupported(Class componentClass) {
        if (supportedComponents == null || supportedComponents.isEmpty()) {
            return true;
        }

        return supportedComponents.contains(componentClass);
    }

    public String getName() {
        return name;
    }

    public List<ComponentDecorator> getDecorators() {
        return decorators;
    }
}
