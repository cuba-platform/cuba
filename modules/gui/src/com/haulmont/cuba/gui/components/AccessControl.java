/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import java.util.Collection;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface AccessControl extends QuasiComponent {
    String NAME = "accessControl";

    void setRealComponents(Collection<Component> realComponents);
}