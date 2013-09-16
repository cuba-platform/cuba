/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tabsheet;

import com.vaadin.shared.communication.ClientRpc;

/**
 * @author artamonov
 * @version $Id$
 */
public interface CubaTabSheetClientRpc extends ClientRpc {

    public void showTabContextMenu(int tabIndex, ClientAction[] actions);
}