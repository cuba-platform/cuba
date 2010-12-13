/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 09.12.2010 17:08:21
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public interface TimeField extends Field {
    
    boolean getShowSeconds();
    void setShowSeconds(boolean showSeconds);
}
