/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author abramov
 * @version $Id$
 */
public interface BoxLayout extends ExpandingLayout, Component.OrderedContainer,
                                   Component.Spacing, Component.Margin, Component.BelongToFrame {
    /**
     * @deprecated Use {@link VBoxLayout#NAME}
     */
    @Deprecated
    String VBOX = "vbox";

    /**
     * @deprecated Use {@link HBoxLayout#NAME}
     */
    @Deprecated
    String HBOX = "hbox";
}