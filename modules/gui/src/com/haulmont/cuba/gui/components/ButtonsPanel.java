/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

import java.util.Collection;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface ButtonsPanel extends BoxLayout {

    String NAME = "buttonsPanel";

    void addButton(Button actionButton);

    void removeButton(Button actionButton);

    Collection<Button> getButtons();

    Button getButton(String id);

    public interface Provider {
        Collection<Button> getButtons();
    }
}