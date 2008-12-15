/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.12.2008 16:09:59
 *
 * $Id$
 */
package com.haulmont.cuba.web.config;

import java.util.List;
import java.util.ArrayList;

public class MenuItem
{
    private MenuItem parent;
    private List<MenuItem> children = new ArrayList<MenuItem>();

    private String caption;
    private ScreenAction action;

    public MenuItem(MenuItem parent, String caption) {
        this.parent = parent;
        this.caption = caption;
    }

    public MenuItem getParent() {
        return parent;
    }

    public List<MenuItem> getChildren() {
        return children;
    }

    public String getCaption() {
        return caption;
    }

    public ScreenAction getAction() {
        return action;
    }

    public void setAction(ScreenAction action) {
        this.action = action;
    }

    public String toString() {
        return caption;
    }
}
