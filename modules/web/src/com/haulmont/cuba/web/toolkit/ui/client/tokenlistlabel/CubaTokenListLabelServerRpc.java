/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
