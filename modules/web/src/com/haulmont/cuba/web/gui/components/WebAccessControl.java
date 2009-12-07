/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 03.12.2009 17:14:45
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.AccessControl;
import com.haulmont.cuba.gui.components.Component;

import java.util.Collection;

public class WebAccessControl extends WebAbstractQuasiComponent implements AccessControl {

    private Collection<Component> realComponents;

    public Collection<Component> getRealComponents() {
        return realComponents;
    }

    public void setRealComponents(Collection<Component> realComponents) {
        this.realComponents = realComponents;
    }
}
