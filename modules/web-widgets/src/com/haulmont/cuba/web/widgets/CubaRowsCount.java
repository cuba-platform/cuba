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
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

public class CubaRowsCount extends CubaCssActionsLayout {

    protected Button prevButton;
    protected Button nextButton;
    protected Button firstButton;
    protected Button lastButton;
    protected Label label;
    protected Button countButton;

    public CubaRowsCount() {
        setStyleName("c-paging");
        setMargin(new MarginInfo(false, false, false, true));

        ComponentContainer contentLayout = createContentLayout();
        addComponent(contentLayout);

        setWidth(100, Unit.PERCENTAGE);
    }

    protected ComponentContainer createContentLayout() {
        CubaCssActionsLayout contentLayout = new CubaCssActionsLayout();
        contentLayout.setStyleName("c-paging-wrap");
        contentLayout.setSpacing(true);

        firstButton = new CubaButton();
        firstButton.setStyleName("c-paging-change-page");
        firstButton.addStyleName("c-paging-first");
        contentLayout.addComponent(firstButton);

        prevButton = new CubaButton();
        prevButton.setStyleName("c-paging-change-page");
        prevButton.addStyleName("c-paging-prev");
        contentLayout.addComponent(prevButton);

        label = new Label();
        label.setWidthUndefined();
        label.setStyleName("c-paging-status");
        contentLayout.addComponent(label);

        countButton = new CubaButton("[?]");
        countButton.setWidthUndefined();
        countButton.setStyleName(ValoTheme.BUTTON_LINK);
        countButton.addStyleName("c-paging-count");
        countButton.setTabIndex(-1);
        contentLayout.addComponent(countButton);

        nextButton = new CubaButton();
        nextButton.setStyleName("c-paging-change-page");
        nextButton.addStyleName("c-paging-next");
        contentLayout.addComponent(nextButton);

        lastButton = new CubaButton();
        lastButton.setStyleName("c-paging-change-page");
        lastButton.addStyleName("c-paging-last");
        contentLayout.addComponent(lastButton);

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