/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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