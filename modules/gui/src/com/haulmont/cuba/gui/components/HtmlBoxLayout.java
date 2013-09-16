/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface HtmlBoxLayout extends Component.Container, Component.BelongToFrame {

    String NAME = "htmlBox";

    String getTemplateName();
    void setTemplateName(String templateName);
}