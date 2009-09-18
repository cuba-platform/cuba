/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 16.07.2009 19:19:04
 *
 * $Id$
 */
package com.haulmont.cuba.gui;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;

/**
 * Visitor used by {@link MetadataHelper#walkProperties(com.haulmont.chile.core.model.Instance, PropertyVisitor)}
 */
public interface PropertyVisitor {

    /**
     * @param instance visiting instance
     * @param property visiting property
     */
    void visit(Instance instance, MetaProperty property);

}
