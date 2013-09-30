/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.server.ClientConnector;
import com.vaadin.ui.UI;

/**
 * @author tsarevskiy
 * @version $Id$
 */
@JavaScript("resources/swfobject/swfobject-2.2.js")
public class CubaSWFObjectIntegration extends AbstractJavaScriptExtension {

    @Override
    public void extend(AbstractClientConnector target) {
        super.extend(target);
    }

    @Override
    protected Class<? extends ClientConnector> getSupportedParentType() {
        return UI.class;
    }

}
