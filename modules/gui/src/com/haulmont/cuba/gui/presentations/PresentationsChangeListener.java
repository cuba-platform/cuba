/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 20.09.2010 17:11:16
 *
 * $Id$
 */
package com.haulmont.cuba.gui.presentations;

import java.io.Serializable;

public interface PresentationsChangeListener extends Serializable {
    
    void currentPresentationChanged(Presentations presentations, Object oldPresentationId);

    void presentationsSetChanged(Presentations presentations);
}
