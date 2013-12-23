/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

/**
 * HTML link component
 *
 * @author artamonov
 * @version $Id$
 */
public interface Link extends Component, Component.HasCaption, Component.BelongToFrame {

    String NAME = "link";

    void setUrl(String url);
    String getUrl();

    void setTarget(String target);
    String getTarget();
}