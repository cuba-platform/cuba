/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea;

import com.vaadin.shared.communication.ServerRpc;

/**
 * @author gorelov
 * @version $Id$
 */
public interface CubaResizableTextAreaWrapperServerRpc extends ServerRpc {

    void sizeChanged(String width, String height);
}