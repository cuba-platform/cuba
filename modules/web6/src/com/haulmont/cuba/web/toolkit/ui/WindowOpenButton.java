/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.toolkit.gwt.client.ui.VWindowOpenButton;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Button;
import com.vaadin.ui.ClientWidget;
import org.apache.commons.lang.StringUtils;

/**
 * Special button that opens new tab in browser and get Url for it from server-side
 *
 * @author artamonov
 * @version $Id$
 */
@ClientWidget(VWindowOpenButton.class)
public class WindowOpenButton extends Button {

    protected transient String openUrl;

    protected UrlProvider urlProvider;

    public WindowOpenButton() {
        addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if (urlProvider != null) {
                    openUrl = urlProvider.getUrl();
                } else {
                    openUrl = null;
                }
                requestRepaint();
            }
        });
    }

    public UrlProvider getUrlProvider() {
        return urlProvider;
    }

    public void setUrlProvider(UrlProvider urlProvider) {
        this.urlProvider = urlProvider;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        if (StringUtils.isNotBlank(openUrl)) {
            target.addAttribute("openUrl", openUrl);
        }
    }

    public interface UrlProvider {

        String getUrl();
    }
}