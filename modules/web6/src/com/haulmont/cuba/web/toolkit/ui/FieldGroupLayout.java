/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 30.06.2010 16:55:55
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.haulmont.cuba.toolkit.gwt.client.ui.VFieldGroupLayout;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.GridLayout;

@SuppressWarnings("serial")
@ClientWidget(VFieldGroupLayout.class)
public class FieldGroupLayout extends GridLayout {

    public static final int CAPTION_ALIGN_LEFT = 0;
    public static final int CAPTION_ALIGN_TOP = 1;

    private int captionAlignment = CAPTION_ALIGN_LEFT;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addAttribute("verticalCaption", captionAlignment != CAPTION_ALIGN_LEFT);
    }

    public int getCaptionAlignment() {
        return captionAlignment;
    }

    public void setCaptionAlignment(int captionAlignment) {
        this.captionAlignment = captionAlignment;
        requestRepaint();
    }
}
