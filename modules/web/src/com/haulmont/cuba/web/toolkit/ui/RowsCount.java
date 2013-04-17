/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.BaseTheme;

/**
 * @author krivopustov
 * @version $Id$
 */
public class RowsCount extends CustomComponent {

    protected Button prevButton;
    protected Button nextButton;
    protected Label label;
    protected Button countButton;

    public RowsCount() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setMargin(new MarginInfo(false, true, false, true));

        setCompositionRoot(layout);

        prevButton = new Button("<");
        prevButton.setStyleName("cuba-paging-change-page");
        layout.addComponent(prevButton);

        label = new Label();
        layout.addComponent(label);

        countButton = new Button("[?]");
        countButton.setStyleName(BaseTheme.BUTTON_LINK);
        layout.addComponent(countButton);

        nextButton = new Button(">");
        nextButton.setStyleName("cuba-paging-change-page");
        layout.addComponent(nextButton);

        layout.setWidth("-1px");
        setWidth("-1px");
    }

    public Label getLabel() {
        return label;
    }

    public Button getCountButton() {
        return countButton;
    }

    public Button getPrevButton() {
        return prevButton;
    }

    public Button getNextButton() {
        return nextButton;
    }
}