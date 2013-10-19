/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.BaseTheme;

public class RowsCount extends CustomComponent {

    private HorizontalLayout layout;
    private Button prevButton;
    private Button nextButton;
    private Label label;
    private Button countButton;

    public RowsCount() {
        layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setMargin(false, true, false, true);

        setCompositionRoot(layout);

        prevButton = new Button("<");
        prevButton.setStyleName("change-page");
        layout.addComponent(prevButton);

        label = new Label();
        layout.addComponent(label);

        countButton = new Button("[?]");
        countButton.setStyleName(BaseTheme.BUTTON_LINK);
        layout.addComponent(countButton);

        nextButton = new Button(">");
        nextButton.setStyleName("change-page");
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
