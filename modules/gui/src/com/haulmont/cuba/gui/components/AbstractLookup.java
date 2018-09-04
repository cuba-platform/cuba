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
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Fragments;
import com.haulmont.cuba.gui.components.Window.Lookup;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.Subscribe;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.core.annotation.Order;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Base class for lookup screen controllers.
 */
public class AbstractLookup extends AbstractWindow implements Lookup {

    private Predicate<ValidationContext> lookupValidator;
    private Consumer<Collection> lookupHandler;

    private Component lookupComponent;

    public AbstractLookup() {
        addInitListener(this::initLookupActions);
    }

    protected void initLookupActions(@SuppressWarnings("unused") InitEvent event) {
        addAction(new SelectAction(this));

        Messages messages = getBeanLocator().get(Messages.NAME);
        addAction(new BaseAction(LOOKUP_CANCEL_ACTION_ID)
                .withCaption(messages.getMainMessage("actions.Cancel"))
                .withHandler(e ->
                        close("cancel")
                ));
    }

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 10)
    @Subscribe
    protected void afterInit(AfterInitEvent event) {
        initLookupLayout();
    }

    @Override
    public Component getLookupComponent() {
        return lookupComponent;
    }

    @Override
    public void setLookupComponent(Component lookupComponent) {
        this.lookupComponent = lookupComponent;
    }

    @Override
    public void initLookupLayout() {
        Action selectAction = getAction(LOOKUP_SELECT_ACTION_ID);

        if (selectAction != null && selectAction.getOwner() == null) {
            WindowConfig windowConfig = getBeanLocator().get(WindowConfig.NAME);
            Fragments fragments = getBeanLocator().get(Fragments.NAME);

            ScreenFragment lookupWindowActions = fragments.create(this, windowConfig.getWindowInfo("lookupWindowActions"));
            lookupWindowActions.getFragment().setId("lookupWindowActions");
            lookupWindowActions.getFragment().setVisible(false);

            getFrame().add(lookupWindowActions.getFragment());

            fragments.initialize(lookupWindowActions);
        }

        Element element = ((Component.HasXmlDescriptor) getFrame()).getXmlDescriptor();
        String lookupComponent = element.attributeValue("lookupComponent");
        if (!StringUtils.isEmpty(lookupComponent)) {
            Component component = getFrame().getComponent(lookupComponent);
            setLookupComponent(component);
        }
    }

    @Override
    public Consumer<Collection> getSelectHandler() {
        return lookupHandler;
    }

    @Override
    public Predicate<ValidationContext> getSelectValidator() {
        return lookupValidator;
    }

    @Override
    public void setSelectValidator(Predicate lookupValidator) {
        this.lookupValidator = lookupValidator;
    }

    @Override
    public void setSelectHandler(Consumer lookupHandler) {
        this.lookupHandler = lookupHandler;

        if (lookupHandler != null) {
            getComponentNN("lookupWindowActions").setVisible(true);
        }
    }
}