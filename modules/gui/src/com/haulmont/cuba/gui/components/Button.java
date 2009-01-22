/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:12:05
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public interface Button extends Component, Component.HasCaption {
    Action getAction();
    void setAction(Action action);
}
