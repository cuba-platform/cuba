/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author abramov
 * @version $Id$
 */
public interface Label extends DatasourceComponent, Component.HasFormatter {

    String NAME = "label";

    boolean isHtmlEnabled();
    void setHtmlEnabled(boolean htmlEnabled);
}