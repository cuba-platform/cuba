/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaRowsCount extends CustomComponent {

    protected Button prevButton;
    protected Button nextButton;
    protected Label label;
    protected Button countButton;

    public CubaRowsCount() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setStyleName("cuba-paging");
        layout.setSpacing(false);
        layout.setMargin(new MarginInfo(false, false, false, true));

        setCompositionRoot(layout);

        CubaPlaceHolder expander = new CubaPlaceHolder();
        expander.setWidth("100%");
        layout.addComponent(expander);
        layout.setExpandRatio(expander, 1);

        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setSpacing(true);
        contentLayout.setHeight("-1px");

        prevButton = new CubaButton("<");
        prevButton.setWidth("-1px");
        prevButton.setStyleName("cuba-paging-change-page");
        prevButton.addStyleName("cuba-paging-prev");
        contentLayout.addComponent(prevButton);

        label = new Label();
        label.setWidth("-1px");
        label.setStyleName("cuba-paging-status");
        contentLayout.addComponent(label);

        countButton = new CubaButton("[?]");
        countButton.setWidth("-1px");
        countButton.setStyleName(BaseTheme.BUTTON_LINK);
        countButton.addStyleName("cuba-paging-count");
        countButton.setTabIndex(-1);
        contentLayout.addComponent(countButton);

        nextButton = new CubaButton(">");
        nextButton.setWidth("-1px");
        nextButton.setStyleName("cuba-paging-change-page");
        nextButton.addStyleName("cuba-paging-next");
        contentLayout.addComponent(nextButton);

        layout.addComponent(contentLayout);

        layout.setWidth("100%");
        setWidth("100%");
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