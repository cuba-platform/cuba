/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */
package com.haulmont.cuba.web.widgets;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class CubaRowsCount extends CustomComponent {

    protected Button prevButton;
    protected Button nextButton;
    protected Button firstButton;
    protected Button lastButton;
    protected Label label;
    protected Button countButton;

    public CubaRowsCount() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setStyleName("c-paging");
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
        contentLayout.setStyleName("c-paging-wrap");
        contentLayout.setSpacing(true);

        firstButton = new CubaButton();
        firstButton.setStyleName("c-paging-change-page");
        firstButton.addStyleName("c-paging-first");
        contentLayout.addComponent(firstButton);

        contentLayout.setComponentAlignment(firstButton, Alignment.MIDDLE_CENTER);

        prevButton = new CubaButton();
        prevButton.setStyleName("c-paging-change-page");
        prevButton.addStyleName("c-paging-prev");
        contentLayout.addComponent(prevButton);
        contentLayout.setComponentAlignment(prevButton, Alignment.MIDDLE_CENTER);

        label = new Label();
        label.setWidthUndefined();
        label.setStyleName("c-paging-status");
        contentLayout.addComponent(label);
        contentLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);

        countButton = new CubaButton("[?]");
        countButton.setWidthUndefined();
        countButton.setStyleName(ValoTheme.BUTTON_LINK);
        countButton.addStyleName("c-paging-count");
        countButton.setTabIndex(-1);
        contentLayout.addComponent(countButton);
        contentLayout.setComponentAlignment(countButton, Alignment.MIDDLE_CENTER);

        nextButton = new CubaButton();
        nextButton.setStyleName("c-paging-change-page");
        nextButton.addStyleName("c-paging-next");
        contentLayout.addComponent(nextButton);
        contentLayout.setComponentAlignment(nextButton, Alignment.MIDDLE_CENTER);

        lastButton = new CubaButton();
        lastButton.setStyleName("c-paging-change-page");
        lastButton.addStyleName("c-paging-last");
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