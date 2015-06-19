/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.timer;

import com.vaadin.shared.AbstractComponentState;
import com.vaadin.shared.annotations.NoLayout;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTimerState extends AbstractComponentState {

    @NoLayout
    public boolean repeating = false;

    @NoLayout
    public int delay = -1;

    @NoLayout
    public boolean listeners = false;

    @NoLayout
    public String timerId = "";

    @NoLayout
    public boolean running = false;
}