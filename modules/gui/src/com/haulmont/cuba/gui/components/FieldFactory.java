/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;

/**
 * @author artamonov
 * @version $Id$
 */
public interface FieldFactory {

    Component createField(Datasource datasource, String property, Element xmlDescriptor);
}