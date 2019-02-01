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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.RelatedEntities;
import com.haulmont.cuba.gui.components.actions.RelatedAction;
import com.haulmont.cuba.gui.components.data.meta.EntityDataUnit;
import com.haulmont.cuba.gui.components.security.RelatedEntitiesSecurity;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.sys.ScreensHelper;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.WindowManager.OpenType;

public class WebRelatedEntities extends WebPopupButton implements RelatedEntities {

    protected ListComponent listComponent;
    protected OpenType openType = OpenType.THIS_TAB;
    protected Map<String, PropertyOption> propertyOptions = new HashMap<>();

    protected String excludeRegex;
    protected ScreensHelper screensHelper;
    protected RelatedEntitiesSecurity relatedEntitiesSecurity;

    public WebRelatedEntities() {
    }

    @Inject
    public void setRelatedEntitiesSecurity(RelatedEntitiesSecurity relatedEntitiesSecurity) {
        this.relatedEntitiesSecurity = relatedEntitiesSecurity;
    }

    @Inject
    public void setScreensHelper(ScreensHelper screensHelper) {
        this.screensHelper = screensHelper;
    }

    @Inject
    public void setMessages(Messages messages) {
        setCaption(messages.getMainMessage("actions.Related"));
    }

    @Override
    public OpenType getOpenType() {
        return openType;
    }

    @Override
    public void setOpenType(OpenType openType) {
        checkNotNullArgument(openType);

        this.openType = openType;

        for (Action action : getActions()) {
            if (action instanceof RelatedAction) {
                ((RelatedAction) action).setOpenType(openType);
            }
        }
    }

    @Override
    public void setExcludePropertiesRegex(String excludeRegex) {
        this.excludeRegex = excludeRegex;

        refreshNavigationActions();
    }

    @Override
    public String getExcludePropertiesRegex() {
        return excludeRegex;
    }

    @Override
    public void addPropertyOption(String property, @Nullable String screen, @Nullable String caption, @Nullable String filterCaption) {
        if (StringUtils.isBlank(property)) {
            throw new IllegalArgumentException("Empty name for custom property option");
        }

        propertyOptions.put(property, new PropertyOption(screen, caption, filterCaption));

        refreshNavigationActions();
    }

    @Override
    public void removePropertyOption(String property) {
        propertyOptions.remove(property);

        refreshNavigationActions();
    }

    @Override
    public ListComponent getListComponent() {
        return listComponent;
    }

    @Override
    public void setListComponent(ListComponent listComponent) {
        this.listComponent = listComponent;

        refreshNavigationActions();
    }

    protected void refreshNavigationActions() {
        ComponentContainer actionContainer = (ComponentContainer) vPopupComponent;

        actionContainer.removeAllComponents();
        actionOrder.clear();

        if (listComponent != null) {
            MetaClass metaClass = getMetaClass(listComponent);

            Pattern excludePattern = null;
            if (excludeRegex != null) {
                excludePattern = Pattern.compile(excludeRegex);
            }

            for (MetaProperty metaProperty : metaClass.getProperties()) {
                if (relatedEntitiesSecurity.isSuitableProperty(metaProperty, metaClass)
                        && (excludePattern == null || !excludePattern.matcher(metaProperty.getName()).matches())) {
                    addNavigationAction(metaClass, metaProperty);
                }
            }

            if (actionContainer.getComponentCount() == 0) {
                Messages messages = beanLocator.get(Messages.NAME);
                actionContainer.addComponent(new Label(messages.getMainMessage("actions.Related.Empty")));
            }
        }
    }

    private MetaClass getMetaClass(ListComponent listComponent) {
        if (!(listComponent.getItems() instanceof EntityDataUnit)) {
            throw new IllegalStateException("ListComponent items is null or does not implement EntityDataUnit");
        }

        MetaClass metaClass = ((EntityDataUnit) listComponent.getItems()).getEntityMetaClass();
        if (metaClass == null) {
            throw new IllegalStateException("ListComponent is not bound to entity");
        }

        return metaClass;
    }

    protected void addNavigationAction(MetaClass metaClass, MetaProperty metaProperty) {
        // check if browse screen available
        PropertyOption propertyOption = propertyOptions.get(metaProperty.getName());

        WindowInfo defaultScreen = screensHelper.getDefaultBrowseScreen(metaProperty.getRange().asClass());
        if (defaultScreen != null
                || (propertyOption != null && StringUtils.isNotEmpty(propertyOption.getScreen()))) {
            RelatedAction relatedAction = RelatedAction.create("related" + actionOrder.size(), listComponent, metaClass, metaProperty);
            relatedAction.setOpenType(openType);

            if (defaultScreen != null) {
                relatedAction.setScreen(defaultScreen.getId());
            }

            if (propertyOption != null) {
                if (StringUtils.isNotEmpty(propertyOption.getCaption())) {
                    relatedAction.setCaption(propertyOption.getCaption());
                }
                if (StringUtils.isNotEmpty(propertyOption.getFilterCaption())) {
                    relatedAction.setFilterCaption(propertyOption.getFilterCaption());
                }
                if (StringUtils.isNotEmpty(propertyOption.getScreen())) {
                    relatedAction.setScreen(propertyOption.getScreen());
                }
            }

            addAction(relatedAction);
        }
    }

    protected static class PropertyOption {

        protected String screen;

        protected String caption;

        protected String filterCaption;

        public PropertyOption(String screen, String caption, String filterCaption) {
            this.screen = screen;
            this.caption = caption;
            this.filterCaption = filterCaption;
        }

        public String getScreen() {
            return screen;
        }

        public String getCaption() {
            return caption;
        }

        public String getFilterCaption() {
            return filterCaption;
        }
    }
}