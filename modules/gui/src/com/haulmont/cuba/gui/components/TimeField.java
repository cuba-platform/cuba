/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface TimeField extends Field {

    String NAME = "timeField";

    boolean getShowSeconds();
    void setShowSeconds(boolean showSeconds);
}