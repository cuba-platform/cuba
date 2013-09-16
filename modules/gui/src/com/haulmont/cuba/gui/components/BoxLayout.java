/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author abramov
 * @version $Id$
 */
public interface BoxLayout extends ExpandingLayout, Component.Spacing, Component.Margin, Component.BelongToFrame {

    String VBOX = "vbox";
    String HBOX = "hbox";
}