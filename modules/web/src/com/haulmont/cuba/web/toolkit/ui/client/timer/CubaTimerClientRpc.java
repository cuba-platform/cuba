/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.timer;

import com.vaadin.shared.annotations.Delayed;
import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.communication.ClientRpc;

/**
 * @author artamonov
 * @version $Id$
 */
public interface CubaTimerClientRpc extends ClientRpc {

    @NoLayout
    @Delayed(lastOnly = true)
    void setRunning(boolean running);

    @NoLayout
    void requestCompleted();
}