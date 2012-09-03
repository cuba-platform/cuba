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

public interface PresentationsChangeListener {
    
    void currentPresentationChanged(Presentations presentations, Object oldPresentationId);

    void presentationsSetChanged(Presentations presentations);
}
