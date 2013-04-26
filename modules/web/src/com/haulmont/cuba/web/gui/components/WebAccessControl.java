/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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