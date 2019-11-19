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

import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.FilterDelegate;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.model.BaseCollectionLoader;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.vaadin.server.Sizeable;
import org.dom4j.Element;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.function.Consumer;

/**
 * Generic filter implementation for the web-client.
 */
public class WebFilter extends WebAbstractComponent<com.vaadin.ui.Component> implements Filter, FilterImplementation,
        Component.HasXmlDescriptor {

    protected static final String FILTER_STYLENAME = "c-generic-filter";

    protected FilterDelegate delegate;
    protected boolean settingsEnabled = true;

    protected PropertiesFilterPredicate propertiesFilterPredicate;

    public WebFilter() {
    }

    @Inject
    protected void setDelegate(FilterDelegate delegate) {
        this.delegate = delegate;

        delegate.setFilter(this);

        ComponentContainer layout = delegate.getLayout();

        layout.setParent(this);

        component = layout.unwrapComposition(com.vaadin.ui.Component.class);
        component.setWidth(100, Sizeable.Unit.PERCENTAGE);
        component.addStyleName(FILTER_STYLENAME);

        delegate.setExpandedStateChangeListener(e ->
                fireExpandStateChange(e.isExpanded(), e.isUserOriginated())
        );
        delegate.setCaptionChangedListener(this::updateCaptions);
    }

    @Override
    public void createLayout() {
        delegate.createLayout();
    }

    @Override
    public CollectionDatasource getDatasource() {
        return delegate.getDatasource();
    }

    @Override
    public void setDatasource(CollectionDatasource datasource) {
        delegate.setDatasource(datasource);
    }

    @Override
    public void setFilterEntity(FilterEntity filterEntity) {
        delegate.setFilterEntity(filterEntity);
    }

    @Override
    public boolean apply(FilterOptions options) {
        return delegate.apply(options);
    }

    @Override
    public boolean apply(boolean notifyInvalidConditions) {
        return apply(FilterOptions.create()
                .setNotifyInvalidConditions(notifyInvalidConditions));
    }

    @Override
    public void loadFiltersAndApplyDefault() {
        delegate.loadFiltersAndApplyDefault();
    }

    @Override
    public void setUseMaxResults(boolean useMaxResults) {
        delegate.setUseMaxResults(useMaxResults);
    }

    @Override
    public void setTextMaxResults(boolean textMaxResults) {
        delegate.setTextMaxResults(textMaxResults);
    }

    @Override
    public boolean getTextMaxResults() {
        return delegate.getTextMaxResults();
    }

    @Override
    public boolean getUseMaxResults() {
        return delegate.getUseMaxResults();
    }

    @Override
    public void setApplyTo(Component component) {
        delegate.setApplyTo(component);
    }

    @Override
    public Component getApplyTo() {
        return delegate.getApplyTo();
    }

    @Override
    public void setManualApplyRequired(Boolean manualApplyRequired) {
        delegate.setManualApplyRequired(manualApplyRequired);
    }

    @Override
    public Boolean getManualApplyRequired() {
        return delegate.getManualApplyRequired();
    }

    @Override
    public void setEditable(boolean editable) {
        delegate.setEditable(editable);
    }

    @Override
    public boolean isEditable() {
        return delegate.isEditable();
    }

    @Override
    public void setFolderActionsEnabled(boolean enabled) {
        delegate.setFolderActionsEnabled(enabled);
    }

    @Override
    public boolean isFolderActionsEnabled() {
        return delegate.isFolderActionsEnabled();
    }

    @Override
    public void applySettings(Element element) {
        if (isSettingsEnabled()) {
            delegate.applySettings(element);
        }
    }

    @Override
    public void applyDataLoadingSettings(Element element) {
        if (isSettingsEnabled()) {
            delegate.applyDataLoadingSettings(element);
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        return isSettingsEnabled() && delegate.saveSettings(element);
    }

    @Override
    public boolean isSettingsEnabled() {
        return settingsEnabled;
    }

    @Override
    public void setSettingsEnabled(boolean settingsEnabled) {
        this.settingsEnabled = settingsEnabled;
    }

    @Override
    public void setMargin(com.haulmont.cuba.gui.components.MarginInfo marginInfo) {
        HasOuterMargin layout = (HasOuterMargin) delegate.getLayout();
        layout.setOuterMargin(marginInfo);
    }

    @Override
    public com.haulmont.cuba.gui.components.MarginInfo getMargin() {
        HasOuterMargin layout = (HasOuterMargin) delegate.getLayout();
        return layout.getOuterMargin();
    }

    @Override
    public String getCaption() {
        if (delegate.isBorderVisible()) {
            return delegate.getCaption();
        } else {
            return component.getCaption();
        }
    }

    @Override
    public void setCaption(String caption) {
        delegate.setCaption(caption);

        updateCaptions(caption);
    }

    protected void updateCaptions(String caption) {
        if (delegate.isBorderVisible()) {
            component.setCaption(null);
            ((HasCaption) delegate.getLayout()).setCaption(caption);
        } else {
            component.setCaption(caption);
            ((HasCaption) delegate.getLayout()).setCaption(null);
        }
    }

    @Override
    public void setParamValue(String paramName, Object value) {
        delegate.setParamValue(paramName, value);
    }

    @Override
    public Object getParamValue(String paramName) {
        return delegate.getParamValue(paramName);
    }

    @Override
    public void addFilterEntityChangeListener(FilterEntityChangeListener listener) {
        delegate.addFilterEntityChangeListener(listener);
    }

    @Override
    public List<FilterEntityChangeListener> getFilterEntityChangeListeners() {
        return delegate.getFilterEntityChangeListeners();
    }

    @Override
    public void setColumnsCount(int columnsCount) {
        delegate.setColumnsCount(columnsCount);
    }

    @Override
    public int getColumnsCount() {
        return delegate.getColumnsCount();
    }

    @Override
    public boolean isExpanded() {
        return delegate.isExpanded();
    }

    @Override
    public void setExpanded(boolean expanded) {
        delegate.setExpanded(expanded);
    }

    @Override
    public boolean isCollapsable() {
        return delegate.isCollapsable();
    }

    @Override
    public void setCollapsable(boolean collapsable) {
        delegate.setCollapsable(collapsable);
    }

    @Override
    public Subscription addExpandedStateChangeListener(Consumer<ExpandedStateChangeEvent> listener) {
        getEventHub().subscribe(ExpandedStateChangeEvent.class, listener);
        return () -> removeExpandedStateChangeListener(listener);
    }

    @Override
    public void removeExpandedStateChangeListener(Consumer<ExpandedStateChangeEvent> listener) {
        unsubscribe(ExpandedStateChangeEvent.class, listener);
    }

    protected void fireExpandStateChange(boolean expanded, boolean userOriginated) {
        ExpandedStateChangeEvent event = new ExpandedStateChangeEvent(this, expanded, userOriginated);
        publish(ExpandedStateChangeEvent.class, event);
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return delegate.getComponent(id);
    }

    @Override
    public int getMaxResults() {
        return delegate.getMaxResults();
    }

    @Override
    public void setMaxResults(int maxResults) {
        delegate.setMaxResults(maxResults);
    }

    @Override
    public void focus() {
        delegate.requestFocus();
    }

    @Override
    public int getTabIndex() {
        return 0;
    }

    @Override
    public void setTabIndex(int tabIndex) {
        // filter does not support tab index
    }

    @Override
    public void setModeSwitchVisible(boolean modeSwitchVisible) {
        delegate.setModeSwitchVisible(modeSwitchVisible);
    }

    @Override
    public void switchFilterMode(FilterDelegate.FilterMode filterMode) {
        delegate.switchFilterMode(filterMode);
    }

    @Override
    public String getIcon() {
        return delegate.getIcon();
    }

    @Override
    public void setIcon(String icon) {
        delegate.setIcon(icon);
    }

    @Override
    public BeforeFilterAppliedHandler getBeforeFilterAppliedHandler() {
        return delegate.getBeforeFilterAppliedHandler();
    }

    @Override
    public void setBeforeFilterAppliedHandler(BeforeFilterAppliedHandler beforeFilterAppliedHandler) {
        delegate.setBeforeFilterAppliedHandler(beforeFilterAppliedHandler);
    }

    @Override
    public AfterFilterAppliedHandler getAfterFilterAppliedHandler() {
        return delegate.getAfterFilterAppliedHandler();
    }

    @Override
    public void setAfterFilterAppliedHandler(AfterFilterAppliedHandler afterFilterAppliedHandler) {
        delegate.setAfterFilterAppliedHandler(afterFilterAppliedHandler);
    }

    @Override
    public MetaClass getEntityMetaClass() {
        return delegate.getEntityMetaClass();
    }

    @Override
    public String getEntityAlias() {
        return delegate.getEntityAlias();
    }

    @Override
    public BaseCollectionLoader getDataLoader() {
        return delegate.getDataLoader();
    }

    @Override
    public void setDataLoader(BaseCollectionLoader loader) {
        delegate.setDataLoader(loader);
    }

    @Override
    public void setBorderVisible(boolean visible) {
        if (delegate.isBorderVisible() == visible) {
            return;
        }
        delegate.setBorderVisible(visible);

        String caption = visible ? component.getCaption() : delegate.getCaption();
        setCaption(caption);
    }

    @Override
    public boolean isBorderVisible() {
        return delegate.isBorderVisible();
    }

    @Override
    public void setFrame(Frame frame) {
        super.setFrame(frame);

        ComponentContainer layout = delegate.getLayout();
        if (layout instanceof BelongToFrame) {
            ((BelongToFrame) layout).setFrame(frame);
        }

        delegate.frameAssigned(frame);

        if (frame != null && frame.getId() == null) {
            LoggerFactory.getLogger(WebFilter.class).warn("Filter is embedded in a frame without ID");
        }
    }

    @Override
    public void setPropertiesFilterPredicate(PropertiesFilterPredicate predicate) {
        propertiesFilterPredicate = predicate;
    }

    @Override
    public PropertiesFilterPredicate getPropertiesFilterPredicate() {
        return propertiesFilterPredicate;
    }

    /**
     * Returns a {@link FilterDelegate} instance. FilterDelegate provides an access to filter internals not available from the high-level filter API.
     * For example, you may get a list of filter parameter and its values. That may be useful if you want to check that all parameters are filled
     * before the filter is applied:
     *
     * <pre>{@code
     *    filter.setBeforeFilterAppliedHandler(() -> {
     *    FilterDelegate delegate = ((WebFilter) filter).getDelegate();
     *        List<AbstractCondition> conditions = delegate.getConditionsTree().toConditionsList();
     *        for (AbstractCondition condition : conditions) {
     *            if (condition.getParam().getValue() == null) {
     *                notifications.create(Notifications.NotificationType.WARNING)
     *                        .withCaption("All parameters must be filled")
     *                        .show();
     *                return false;
     *            }
     *        }
     *        return true;
     *    });
     * }</pre>
     * <p>
     * WARNING: The API of the FilterDelegate is unstable and may be changed.
     */
    public FilterDelegate getDelegate() {
        return delegate;
    }

    @Override
    public void setApplyImmediately(boolean immediately) {
        delegate.setApplyImmediately(immediately);
    }

    @Override
    public boolean isApplyImmediately() {
        return delegate.isApplyImmediately();
    }

    @Override
    public String getControlsLayoutTemplate() {
        return delegate.getControlsLayoutTemplate();
    }

    @Override
    public void setControlsLayoutTemplate(String controlsLayoutTemplate) {
        delegate.setControlsLayoutTemplate(controlsLayoutTemplate);
    }

    @Override
    public boolean isWindowCaptionUpdateEnabled() {
        return delegate.isWindowCaptionUpdateEnabled();
    }

    @Override
    public void setWindowCaptionUpdateEnabled(boolean windowCaptionUpdateEnabled) {
        delegate.setWindowCaptionUpdateEnabled(windowCaptionUpdateEnabled);
    }
}