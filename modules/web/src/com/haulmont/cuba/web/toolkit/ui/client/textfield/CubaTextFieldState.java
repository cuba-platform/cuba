/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.textfield;

import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.ui.textfield.AbstractTextFieldState;

/**
 * @author gorbunkov
 * @version $Id$
 */
public class CubaTextFieldState extends AbstractTextFieldState {

    @NoLayout
    public boolean readOnlyFocusable;
}