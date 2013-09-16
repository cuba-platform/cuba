/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.timer;

import com.vaadin.shared.communication.ServerRpc;

/**
 * @author artamonov
 * @version $Id$
 */
public interface CubaTimerServerRpc extends ServerRpc {

    void onTimer();
}