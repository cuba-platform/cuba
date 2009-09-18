/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.07.2009 11:04:07
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import java.util.Collection;

/**
 * Component which is not added to the screen, but instead added its {@link #getRealComponents()}.
 * <p>
 * May be used to create different sets of components depending on runtime conditions.
 */
public interface QuasiComponent extends Component {

    /**
     * Collection of components defined inside the QuasiComponent. 
     * They will be added to screen instead of the enclosing QuasiComponent.
     */
    Collection<Component> getRealComponents();
}
