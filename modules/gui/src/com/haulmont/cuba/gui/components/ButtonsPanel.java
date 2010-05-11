/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 15.12.2009 16:06:40
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import java.util.Collection;

public interface ButtonsPanel extends GridLayout {
    void addButton(Button actionButton);

    void removeButton(Button actionButton);

    Collection<Button> getButtons();

    Button getButton(String id);

    public interface Provider {
        Collection<Button> getButtons();
    }
}
