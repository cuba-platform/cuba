/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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