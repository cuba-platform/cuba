/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.presentations;
/**
 * @author gorodnov
 * @version $Id$
 */
public interface PresentationsChangeListener {
    
    void currentPresentationChanged(Presentations presentations, Object oldPresentationId);

    void presentationsSetChanged(Presentations presentations);

    void defaultPresentationChanged(Presentations presentations, Object oldPresentationId);
}
