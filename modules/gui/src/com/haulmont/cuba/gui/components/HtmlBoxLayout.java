/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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