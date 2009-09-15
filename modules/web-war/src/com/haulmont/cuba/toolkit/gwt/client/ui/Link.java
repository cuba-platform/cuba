/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 26.12.2008 12:43:43
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.ui.Label;

@Deprecated
public class Link extends Label {

    public static final String CLASSNAME = "v-link";

    private String href = null;

    public Link(String text, String href) {
        super(text);
        this.href = href;
        setStyleName(CLASSNAME);
    }

    public Link(String text, boolean wordWrap, String href) {
        super(text, wordWrap);
        this.href = href;
        setStyleName(CLASSNAME);
    }

    public String getHref() {
        return href;
    }
}
