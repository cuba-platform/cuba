/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import java.util.Collection;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface ButtonsPanel extends BoxLayout {

    String NAME = "buttonsPanel";

    interface Provider {
        Collection<Component> getButtons();
    }
}