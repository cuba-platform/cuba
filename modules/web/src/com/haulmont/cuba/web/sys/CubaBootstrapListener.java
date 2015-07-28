/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.sys;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import org.jsoup.nodes.Element;

import javax.annotation.ManagedBean;

/**
 * Event listener notified when the bootstrap HTML is about to be generated and
 * send to the client. The bootstrap HTML is first constructed as an in-memory
 * DOM representation which registered listeners can modify before the final
 * HTML is generated.
 *
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(CubaBootstrapListener.NAME)
public class CubaBootstrapListener implements BootstrapListener {

    public static final String NAME = "cuba_BootstrapListener";

    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        Element head = response.getDocument().getElementsByTag("head").get(0);

        Element jquery = response.getDocument().createElement("script");
        jquery.attr("src", "VAADIN/resources/jquery/jquery-1.10.2.min.js");
        head.appendChild(jquery);
    }
}