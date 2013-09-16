/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import java.util.Collection;
import java.util.Map;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public abstract class AbstractAccessData {

    protected Map<String, Object> params;

    protected List<String> disabledComponents;

    public AbstractAccessData(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * Invoked for each component under AccessControl
     *
     * @param component visited component
     * @param components all components under this AccessControl
     */
    public void visitComponent(Component component, Collection<Component> components) {
    }

    public List<String> getDisabledComponents() {
        return disabledComponents;
    }
}