/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tokenlistlabel;

import com.vaadin.shared.communication.ServerRpc;

/**
 * @author devyatkin
 * @version $Id$
 */
public interface CubaTokenListLabelServerRpc extends ServerRpc {
    void removeToken();
    void itemClick();
}
