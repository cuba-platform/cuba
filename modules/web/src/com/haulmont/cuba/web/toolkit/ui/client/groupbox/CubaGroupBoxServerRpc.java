/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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