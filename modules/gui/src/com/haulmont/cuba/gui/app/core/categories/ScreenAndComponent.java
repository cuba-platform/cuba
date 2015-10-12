/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.categories;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

/**
 * @author degtyarjov
 * @version $Id$
 */
@MetaClass(name = "sys$ScreenAndComponent")
@SystemLevel
public class ScreenAndComponent extends AbstractNotPersistentEntity {
    @MetaProperty
    protected String screen;

    @MetaProperty
    protected String component;

    public ScreenAndComponent() {
    }

    public ScreenAndComponent(String screen, String component) {
        this.screen = screen;
        this.component = component;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}