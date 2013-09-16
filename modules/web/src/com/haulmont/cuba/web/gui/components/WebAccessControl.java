/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.AccessControl;
import com.haulmont.cuba.gui.components.Component;

import java.util.Collection;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WebAccessControl extends WebAbstractQuasiComponent implements AccessControl {

    private Collection<Component> realComponents;

    @Override
    public Collection<Component> getRealComponents() {
        return realComponents;
    }

    @Override
    public void setRealComponents(Collection<Component> realComponents) {
        this.realComponents = realComponents;
    }
}