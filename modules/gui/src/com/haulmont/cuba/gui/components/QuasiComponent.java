/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import java.util.Collection;

/**
 * Component which is not added to the screen, but instead added its {@link #getRealComponents()}.
 * <p>
 * May be used to create different sets of components depending on runtime conditions.
 *
 * @author krivopustov
 * @version $Id$
 */
@Deprecated
public interface QuasiComponent extends Component {

    /**
     * Collection of components defined inside the QuasiComponent. 
     * They will be added to screen instead of the enclosing QuasiComponent.
     */
    Collection<Component> getRealComponents();
}