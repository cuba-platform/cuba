/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class AbstractCompanion {

    protected AbstractFrame frame;

    public AbstractCompanion(AbstractFrame frame) {
        this.frame = frame;
        frame.setCompanion(this);
    }
}
