/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.timer;

import com.vaadin.shared.AbstractComponentState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTimerState extends AbstractComponentState {

    public boolean repeating = false;

    public int delay = -1;

    public boolean listeners = false;

    public String timerId = "";
}