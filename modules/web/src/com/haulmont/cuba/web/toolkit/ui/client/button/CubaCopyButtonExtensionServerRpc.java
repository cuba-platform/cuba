/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.button;

import com.vaadin.shared.communication.ServerRpc;

/**
 * @author gorelov
 */
public interface CubaCopyButtonExtensionServerRpc extends ServerRpc {
    void copied(boolean success);
}
