/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.timer;

import com.vaadin.shared.annotations.Delayed;
import com.vaadin.shared.communication.ClientRpc;

/**
 * @author artamonov
 * @version $Id$
 */
public interface CubaTimerClientRpc extends ClientRpc {

    @Delayed(lastOnly = true)
    void setRunning(boolean running);

    void requestCompleted();
}