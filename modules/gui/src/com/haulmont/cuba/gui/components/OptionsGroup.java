/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author abramov
 * @version $Id$
 */
public interface OptionsGroup extends OptionsField {

    String NAME = "optionsGroup";

    Orientation getOrientation();
    void setOrientation(Orientation orientation);

    enum Orientation {
        VERTICAL,
        HORIZONTAL
    }
}