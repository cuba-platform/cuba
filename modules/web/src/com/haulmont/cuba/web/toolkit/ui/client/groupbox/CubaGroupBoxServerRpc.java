/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.groupbox;

import com.vaadin.shared.communication.ServerRpc;

/**
 * @author artamonov
 * @version $Id$
 */
public interface CubaGroupBoxServerRpc extends ServerRpc {

    void expand();

    void collapse();
}