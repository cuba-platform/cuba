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

public interface PropertyVisitor {

    void visit(Instance instance, MetaProperty property);

}
