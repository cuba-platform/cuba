/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.ScreensHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.RelatedEntities;
import com.haulmont.cuba.gui.components.RelatedEntitiesSecurity;
import com.haulmont.cuba.gui.components.actions.RelatedAction;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebRelatedEntities extends WebPopupButton implements RelatedEntities {

    protected ListComponent listComponent;
    protected WindowManager.OpenType openType = WindowManager.OpenType.THIS_TAB;
    protected Map<String, PropertyOption> propertyOptions = new HashMap<>();

    protected String excludeRegex;

    public WebRelatedEntities() {
        Messages messages = AppBeans.get(Messages.NAME);
        setCaption(messages.getMainMessage("actions.Related"));
    }

    @Override
    public WindowManager.OpenType getOpenType() {
        return openType;
    }

    @Override
    public void setOpenType(WindowManager.OpenType openType) {
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
            MetaClass metaClass = listComponent.getDatasource().getMetaClass();

            Pattern excludePattern = null;
            if (excludeRegex != null) {
                excludePattern = Pattern.compile(excludeRegex);
            }

            for (MetaProperty metaProperty : metaClass.getProperties()) {
                if (RelatedEntitiesSecurity.isSuitableProperty(metaProperty, metaClass)
                        && (excludePattern == null || !excludePattern.matcher(metaProperty.getName()).matches())) {
                    addNavigationAction(metaClass, metaProperty);
                }
            }

            if (actionContainer.getComponentCount() == 0) {
                Messages messages = AppBeans.get(Messages.NAME);
                actionContainer.addComponent(new Label(messages.getMainMessage("actions.Related.Empty")));
            }
        }
    }

    protected void addNavigationAction(MetaClass metaClass, MetaProperty metaProperty) {
        // check if browse screen available
        PropertyOption propertyOption = propertyOptions.get(metaProperty.getName());

        WindowInfo defaultScreen = AppBeans.get(ScreensHelper.class).getAvailableBrowseScreen(metaProperty.getRange().asClass());
        if (defaultScreen != null
                || (propertyOption != null && StringUtils.isNotEmpty(propertyOption.getScreen()))) {
            RelatedAction relatedAction = new RelatedAction("related" + actionOrder.size(), listComponent, metaClass, metaProperty);
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