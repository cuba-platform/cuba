/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.02.11 14:57
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.BaseTheme;

public class RowsCount extends CustomComponent {

    private HorizontalLayout layout;
    private Label label;
    private Button link;

    public RowsCount() {
        layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setMargin(false, true, false, true);

        setCompositionRoot(layout);

        label = new Label();
        layout.addComponent(label);

        link = new Button("[?]");
        link.setStyleName(BaseTheme.BUTTON_LINK);
        layout.addComponent(link);

        layout.setWidth("-1px");
        setWidth("-1px");
    }

    public Label getLabel() {
        return label;
    }

    public Button getLink() {
        return link;
    }
}
