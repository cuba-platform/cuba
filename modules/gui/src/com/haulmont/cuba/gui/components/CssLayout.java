/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

/**
 * @author petunin
 */
public interface CssLayout extends Component.OrderedContainer, Component.BelongToFrame {

    String NAME = "cssLayout";

    boolean isResponsive();

    /**
     * Set layout component to be responsive by width and height.
     * "width-range" and "height-range" are set in scss theme files.
     */
    void setResponsive(boolean responsive);
}
