/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;

public class CubaRowsCount extends CustomComponent {

    protected Button prevButton;
    protected Button nextButton;
    protected Button firstButton;
    protected Button lastButton;
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

        AbstractOrderedLayout contentLayout = createContentLayout();
        layout.addComponent(contentLayout);

        layout.setWidth("100%");
        setWidth("100%");
    }

    protected AbstractOrderedLayout createContentLayout() {
        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setSpacing(true);
        contentLayout.setHeight("-1px");

        ThemeConstants themeConstants = AppBeans.get(ThemeConstantsManager.class).getConstants();
        String buttonWidth = themeConstants.get("cuba.gui.rowsCount.arrowButton.width");
        firstButton = new CubaButton();
        firstButton.setIcon(WebComponentsHelper.getIcon("icons/rows-count-first.png"));
        firstButton.setWidth(buttonWidth);
        firstButton.setStyleName("cuba-paging-change-page");
        firstButton.addStyleName("cuba-paging-first");
        contentLayout.addComponent(firstButton);

        contentLayout.setComponentAlignment(firstButton, Alignment.MIDDLE_CENTER);

        prevButton = new CubaButton();
        prevButton.setIcon(WebComponentsHelper.getIcon("icons/rows-count-prev.png"));
        prevButton.setWidth(buttonWidth);
        prevButton.setStyleName("cuba-paging-change-page");
        prevButton.addStyleName("cuba-paging-prev");
        contentLayout.addComponent(prevButton);
        contentLayout.setComponentAlignment(prevButton, Alignment.MIDDLE_CENTER);

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
        contentLayout.setComponentAlignment(countButton, Alignment.MIDDLE_CENTER);

        nextButton = new CubaButton();
        nextButton.setIcon(WebComponentsHelper.getIcon("icons/rows-count-next.png"));
        nextButton.setWidth(buttonWidth);
        nextButton.setStyleName("cuba-paging-change-page");
        nextButton.addStyleName("cuba-paging-next");
        contentLayout.addComponent(nextButton);
        contentLayout.setComponentAlignment(nextButton, Alignment.MIDDLE_CENTER);

        lastButton = new CubaButton();
        lastButton.setIcon(WebComponentsHelper.getIcon("icons/rows-count-last.png"));
        lastButton.setWidth(buttonWidth);
        lastButton.setStyleName("cuba-paging-change-page");
        lastButton.addStyleName("cuba-paging-last");
        contentLayout.addComponent(lastButton);
        contentLayout.setComponentAlignment(lastButton, Alignment.MIDDLE_CENTER);

        return contentLayout;
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

    public Button getFirstButton() {
        return firstButton;
    }

    public Button getLastButton() {
        return lastButton;
    }
}