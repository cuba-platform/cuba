/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

    String getFormat();
    void setFormat(String timeFormat);
}