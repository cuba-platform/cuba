/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.appui;

import com.vaadin.shared.communication.ClientRpc;

/**
 * @author artamonov
 * @version $Id$
 */
public interface AppUIClientRpc extends ClientRpc {

    /**
     * Discard next events in client-side event queue, exclude CubaTimer events and polling.
     */
    void discardAccumulatedEvents();
}