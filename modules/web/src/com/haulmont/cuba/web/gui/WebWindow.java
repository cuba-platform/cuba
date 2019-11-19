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
package com.haulmont.cuba.web.gui;

import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.HtmlAttributes.CSS;
import com.haulmont.cuba.gui.components.security.ActionsPermissions;
import com.haulmont.cuba.gui.components.sys.FrameImplementation;
import com.haulmont.cuba.gui.components.sys.WindowImplementation;
import com.haulmont.cuba.gui.events.sys.UiEventsMulticaster;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.navigation.NavigationState;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebFrameActionsHolder;
import com.haulmont.cuba.web.gui.components.WebWrapperUtils;
import com.haulmont.cuba.web.widgets.CubaWindowVerticalLayout;
import com.haulmont.cuba.web.widgets.HtmlAttributesExtension;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Layout;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public abstract class WebWindow implements Window, Component.Wrapper, Component.HasXmlDescriptor, WrappedWindow,
        SecuredActionsHolder, Component.HasIcon, FrameImplementation, WindowImplementation {

    protected static final String C_WINDOW_LAYOUT = "c-window-layout";

    private static final Logger log = LoggerFactory.getLogger(WebWindow.class);

    protected String id;

    protected Set<Facet> facets = null; // lazily initialized linked hash set

    protected List<Component> ownComponents = new ArrayList<>();
    protected Map<String, Component> allComponents = new HashMap<>(4);

    protected String focusComponentId;

    protected AbstractOrderedLayout component;

    protected Screen frameOwner;

    protected Element element;

    protected WindowContext context;

    protected String icon;
    protected String caption;
    protected String description;

    protected WebFrameActionsHolder actionsHolder = new WebFrameActionsHolder(this);
    protected ActionsPermissions actionsPermissions = new ActionsPermissions(this);

    protected Icons icons;

    protected DialogOptions dialogOptions; // lazily initialized

    protected boolean closeable = true;

    private EventHub eventHub;

    protected NavigationState resolvedState;

    protected boolean defaultScreenWindow = false;

    public WebWindow() {
        component = createLayout();
    }

    protected EventHub getEventHub() {
        if (eventHub == null) {
            eventHub = new EventHub();
        }
        return eventHub;
    }

    protected <E> void publish(Class<E> eventType, E event) {
        if (eventHub != null) {
            eventHub.publish(eventType, event);
        }
    }

    protected <E> boolean unsubscribe(Class<E> eventType, Consumer<E> listener) {
        if (eventHub != null) {
            return eventHub.unsubscribe(eventType, listener);
        }
        return false;
    }

    @Inject
    protected void setIcons(Icons icons) {
        this.icons = icons;
    }

    protected void disableEventListeners() {
        List<ApplicationListener> uiEventListeners = UiControllerUtils.getUiEventListeners(frameOwner);
        if (uiEventListeners != null) {
            AppUI ui = AppUI.getCurrent();
            UiEventsMulticaster multicaster = ui.getUiEventsMulticaster();

            for (ApplicationListener listener : uiEventListeners) {
                multicaster.removeApplicationListener(listener);
            }
        }
    }

    protected void enableEventListeners() {
        List<ApplicationListener> uiEventListeners = UiControllerUtils.getUiEventListeners(frameOwner);
        if (uiEventListeners != null) {
            AppUI ui = AppUI.getCurrent();
            UiEventsMulticaster multicaster = ui.getUiEventsMulticaster();

            for (ApplicationListener listener : uiEventListeners) {
                multicaster.addApplicationListener(listener);
            }
        }
    }

    protected AbstractOrderedLayout createLayout() {
        CubaWindowVerticalLayout layout = new CubaWindowVerticalLayout();
        layout.setStyleName(C_WINDOW_LAYOUT);
        layout.setSizeFull();

        layout.addActionHandler(actionsHolder);

        return layout;
    }

    protected com.vaadin.ui.ComponentContainer getContainer() {
        return component;
    }

    @Override
    public void addFacet(Facet facet) {
        checkNotNullArgument(facet);

        if (facets == null) {
            facets = new HashSet<>();
        }

        if (!facets.contains(facet)) {
            facets.add(facet);
            facet.setOwner(this);
        }
    }

    @Nullable
    @Override
    public Facet getFacet(String id) {
        checkNotNullArgument(id);

        if (facets == null) {
            return null;
        }

        return facets.stream()
                .filter(f -> id.equals(f.getId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removeFacet(Facet facet) {
        checkNotNullArgument(facet);

        if (facets != null
                && facets.remove(facet)) {
            facet.setOwner(null);
        }
    }

    @Override
    public Stream<Facet> getFacets() {
        if (facets == null) {
            return Stream.empty();
        }
        return facets.stream();
    }

    @Override
    public void registerComponent(Component component) {
        if (component.getId() != null) {
            allComponents.put(component.getId(), component);
        }
    }

    @Override
    public void unregisterComponent(Component component) {
        if (component.getId() != null) {
            allComponents.remove(component.getId());
        }
    }

    @Nullable
    @Override
    public Component getRegisteredComponent(String id) {
        return allComponents.get(id);
    }

    @Override
    public String getStyleName() {
        return StringUtils.normalizeSpace(component.getStyleName().replace(C_WINDOW_LAYOUT, ""));
    }

    @Override
    public void setStyleName(String name) {
        getContainer().setStyleName(name);

        getContainer().addStyleName(C_WINDOW_LAYOUT);
    }

    @Override
    public void addStyleName(String styleName) {
        getContainer().addStyleName(styleName);
    }

    @Override
    public void removeStyleName(String styleName) {
        getContainer().removeStyleName(styleName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X unwrap(Class<X> internalComponentClass) {
        return (X) getComponent();
    }

    @Nullable
    @Override
    public <X> X unwrapOrNull(Class<X> internalComponentClass) {
        return internalComponentClass.isAssignableFrom(getComponent().getClass())
                ? internalComponentClass.cast(getComponent())
                : null;
    }

    @Override
    public <X> void withUnwrapped(Class<X> internalComponentClass, Consumer<X> action) {
        if (internalComponentClass.isAssignableFrom(getComponent().getClass())) {
            action.accept(internalComponentClass.cast(getComponent()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X unwrapComposition(Class<X> internalCompositionClass) {
        return (X) getComposition();
    }

    @Nullable
    @Override
    public <X> X unwrapCompositionOrNull(Class<X> internalCompositionClass) {
        return internalCompositionClass.isAssignableFrom(getComposition().getClass())
                ? internalCompositionClass.cast(getComposition())
                : null;
    }

    @Override
    public <X> void withUnwrappedComposition(Class<X> internalCompositionClass, Consumer<X> action) {
        if (internalCompositionClass.isAssignableFrom(getComposition().getClass())) {
            action.accept(internalCompositionClass.cast(getComposition()));
        }
    }

    @Override
    public boolean getSpacing() {
        if (getContainer() instanceof Layout.SpacingHandler) {
            return ((Layout.SpacingHandler) getContainer()).isSpacing();
        }
        return false;
    }

    @Override
    public void setSpacing(boolean enabled) {
        if (getContainer() instanceof Layout.SpacingHandler) {
            ((Layout.SpacingHandler) getContainer()).setSpacing(true);
        }
    }

    @Override
    public com.haulmont.cuba.gui.components.MarginInfo getMargin() {
        if (getContainer() instanceof Layout.MarginHandler) {
            MarginInfo vMargin = ((Layout.MarginHandler) getContainer()).getMargin();
            return new com.haulmont.cuba.gui.components.MarginInfo(vMargin.hasTop(), vMargin.hasRight(), vMargin.hasBottom(), vMargin.hasLeft());
        }
        return new com.haulmont.cuba.gui.components.MarginInfo(false);
    }

    @Override
    public void setMargin(com.haulmont.cuba.gui.components.MarginInfo marginInfo) {
        if (getContainer() instanceof Layout.MarginHandler) {
            MarginInfo vMargin = new MarginInfo(marginInfo.hasTop(), marginInfo.hasRight(), marginInfo.hasBottom(),
                    marginInfo.hasLeft());
            ((Layout.MarginHandler) getContainer()).setMargin(vMargin);
        }
    }

    @Override
    public void setMinWidth(String minWidth) {
        HtmlAttributesExtension.get(component)
                .setCssProperty(CSS.MIN_WIDTH, minWidth);
    }

    @Override
    public String getMinWidth() {
        return HtmlAttributesExtension.get(component)
                .getCssProperty(CSS.MIN_WIDTH);
    }

    @Override
    public void setMaxWidth(String maxWidth) {
        HtmlAttributesExtension.get(component)
                .setCssProperty(CSS.MAX_WIDTH, maxWidth);
    }

    @Override
    public String getMaxWidth() {
        return HtmlAttributesExtension.get(component)
                        .getCssProperty(CSS.MAX_WIDTH);
    }

    @Override
    public void setMinHeight(String minHeight) {
        HtmlAttributesExtension.get(component)
                .setCssProperty(CSS.MIN_HEIGHT, minHeight);
    }

    @Override
    public String getMinHeight() {
        return HtmlAttributesExtension.get(component)
                        .getCssProperty(CSS.MIN_HEIGHT);
    }

    @Override
    public void setMaxHeight(String maxHeight) {
        HtmlAttributesExtension.get(component)
                .setCssProperty(CSS.MAX_HEIGHT, maxHeight);
    }

    @Override
    public String getMaxHeight() {
        return HtmlAttributesExtension.get(component)
                .getCssProperty(CSS.MAX_HEIGHT);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void addAction(Action action) {
        checkNotNullArgument(action, "action must be non null");

        actionsHolder.addAction(action);
        actionsPermissions.apply(action);

        // force update of actions on client side
        if (action.getShortcutCombination() != null) {
            component.markAsDirty();
        }
    }

    @Override
    public void addAction(Action action, int index) {
        checkNotNullArgument(action, "action must be non null");

        actionsHolder.addAction(action, index);
        actionsPermissions.apply(action);

        // force update of actions on client side
        if (action.getShortcutCombination() != null) {
            component.markAsDirty();
        }
    }

    @Override
    public void removeAction(@Nullable com.haulmont.cuba.gui.components.Action action) {
        actionsHolder.removeAction(action);
    }

    @Override
    public void removeAction(@Nullable String id) {
        actionsHolder.removeAction(id);
    }

    @Override
    public void removeAllActions() {
        actionsHolder.removeAllActions();
    }

    @Override
    public Collection<com.haulmont.cuba.gui.components.Action> getActions() {
        return actionsHolder.getActions();
    }

    @Override
    @Nullable
    public com.haulmont.cuba.gui.components.Action getAction(String id) {
        return actionsHolder.getAction(id);
    }

    @Override
    public boolean isValid() {
        Collection<Component> components = ComponentsHelper.getComponents(this);
        for (Component component : components) {
            if (component instanceof Validatable) {
                Validatable validatable = (Validatable) component;
                if (validatable.isValidateOnCommit() && !validatable.isValid())
                    return false;
            }
        }
        return true;
    }

    @Override
    public void validate() throws ValidationException {
        ComponentsHelper.traverseValidatable(this, Validatable::validate);
    }

    @Override
    public boolean validate(List<Validatable> fields) {
        ValidationErrors errors = new ValidationErrors();

        for (Validatable field : fields) {
            try {
                field.validate();
            } catch (ValidationException e) {
                if (log.isTraceEnabled()) {
                    log.trace("Validation failed", e);
                } else if (log.isDebugEnabled()) {
                    log.debug("Validation failed: " + e);
                }

                ComponentsHelper.fillErrorMessages(field, e, errors);
            }
        }

        return handleValidationErrors(errors);
    }

    @Override
    public boolean validateAll() {
        ValidationErrors errors = new ValidationErrors();

        ComponentsHelper.traverseValidatable(this, v -> {
            try {
                v.validate();
            } catch (ValidationException e) {
                if (log.isTraceEnabled()) {
                    log.trace("Validation failed", e);
                } else if (log.isDebugEnabled()) {
                    log.debug("Validation failed: " + e);
                }
                ComponentsHelper.fillErrorMessages(v, e, errors);
            }
        });

        return handleValidationErrors(errors);
    }

    protected boolean handleValidationErrors(ValidationErrors errors) {
        Component firstComponent = errors.getFirstComponent();
        if (firstComponent != null) {
            ComponentsHelper.focusComponent(firstComponent);
            return false;
        }

        return true;
    }

    @Override
    public DialogOptions getDialogOptions() {
        if (dialogOptions == null) {
            dialogOptions = new DialogOptions();
        }
        return dialogOptions;
    }

    @Override
    public boolean isCloseable() {
        return closeable;
    }

    @Override
    public void setCloseable(boolean closeable) {
        this.closeable = closeable;
    }

    @Override
    public Screen getFrameOwner() {
        return frameOwner;
    }

    @Override
    public void setFrameOwner(Screen controller) {
        this.frameOwner = controller;
    }

    @Override
    public void initUiEventListeners() {
        component.addAttachListener(event -> enableEventListeners());
        component.addDetachListener(event -> disableEventListeners());
    }

    @Override
    public WindowContext getContext() {
        return context;
    }

    @Override
    public void setContext(FrameContext ctx) {
        this.context = (WindowContext) ctx;
    }

    protected Component.Focusable getComponentToFocus(Iterator<Component> componentsIterator) {
        while (componentsIterator.hasNext()) {
            Component child = componentsIterator.next();

            if (child instanceof com.haulmont.cuba.gui.components.TabSheet
                    || child instanceof Accordion) {
                // #PL-3176
                // we don't know about selected tab after request
                // may be focused component lays on not selected tab
                // it may break component tree
                continue;
            }

            if (child instanceof Component.Focusable) {

                if (!(child instanceof Editable) || ((Editable) child).isEditable()) {

                    com.vaadin.ui.Component vComponent = child.unwrapComposition(com.vaadin.ui.Component.class);
                    if (WebComponentsHelper.isComponentVisible(vComponent)
                            && WebComponentsHelper.isComponentEnabled(vComponent)) {
                        return (Component.Focusable) child;
                    }
                }
            }

            if (child instanceof ComponentContainer) {
                Collection<Component> components = ((ComponentContainer) child).getComponents();
                Component.Focusable result = getComponentToFocus(components.iterator());
                if (result != null) {
                    return result;
                }
            } else if (child instanceof HasInnerComponents) {
                Collection<Component> components = ((HasInnerComponents) child).getInnerComponents();
                Component.Focusable result = getComponentToFocus(components.iterator());
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    public String getFocusComponent() {
        return focusComponentId;
    }

    @Override
    public void setFocusComponent(String componentId) {
        this.focusComponentId = componentId;

        if (componentId != null) {
            Component focusComponent = getComponent(componentId);
            if (focusComponent instanceof Focusable) {
                ((Focusable) focusComponent).focus();
            } else if (focusComponent != null) {
                if (focusComponent instanceof ComponentContainer) {
                    ComponentContainer componentContainer = (ComponentContainer) focusComponent;
                    Component.Focusable focusableComponent = getComponentToFocus(componentContainer.getComponents().iterator());
                    if (focusableComponent != null) {
                        focusableComponent.focus();
                    }
                } else if (focusComponent instanceof HasInnerComponents) {
                    HasInnerComponents componentContainer = (HasInnerComponents) focusComponent;
                    Component.Focusable focusableComponent = getComponentToFocus(componentContainer.getInnerComponents().iterator());
                    if (focusableComponent != null) {
                        focusableComponent.focus();
                    }
                }
            } else {
                log.error("Can't find focus component: {}", componentId);
            }
        } else {
            findAndFocusChildComponent();
        }
    }

    @Override
    public Subscription addBeforeWindowCloseListener(Consumer<BeforeCloseEvent> listener) {
        return getEventHub().subscribe(BeforeCloseEvent.class, listener);
    }

    @Override
    public void removeBeforeWindowCloseListener(Consumer<BeforeCloseEvent> listener) {
        unsubscribe(BeforeCloseEvent.class, listener);
    }

    public void fireBeforeClose(BeforeCloseEvent event) {
        publish(BeforeCloseEvent.class, event);
    }

    @Override
    public void addTimer(Timer timer) {
        addFacet(timer);
    }

    @Override
    public Timer getTimer(String id) {
        if (facets == null) {
            return null;
        }

        return facets.stream()
                .filter(f -> f instanceof Timer)
                .map(f -> (Timer) f)
                .filter(timer -> Objects.equals(timer.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Element getXmlDescriptor() {
        return element;
    }

    @Override
    public void setXmlDescriptor(Element element) {
        this.element = element;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Deprecated
    @Override
    public WindowManager getWindowManager() {
        return (WindowManager) UiControllerUtils.getScreenContext(getFrameOwner()).getScreens();
    }

    @Override
    public void add(Component childComponent) {
        add(childComponent, ownComponents.size());
    }

    @Override
    public void add(Component childComponent, int index) {
        if (childComponent.getParent() != null && childComponent.getParent() != this) {
            throw new IllegalStateException("Component already has parent");
        }

        if (ownComponents.contains(childComponent)) {
            com.vaadin.ui.Component composition = childComponent.unwrapComposition(com.vaadin.ui.Component.class);
            int existingIndex = ((AbstractOrderedLayout)getContainer()).getComponentIndex(composition);
            if (index > existingIndex) {
                index--;
            }

            remove(childComponent);
        }

        com.vaadin.ui.ComponentContainer container = getContainer();
        com.vaadin.ui.Component vComponent = childComponent.unwrapComposition(com.vaadin.ui.Component.class);
        ((AbstractOrderedLayout)container).addComponent(vComponent, index);

        com.vaadin.ui.Alignment alignment = WebWrapperUtils.toVaadinAlignment(childComponent.getAlignment());
        ((AbstractOrderedLayout) container).setComponentAlignment(vComponent, alignment);

        if (childComponent instanceof BelongToFrame
                && ((BelongToFrame) childComponent).getFrame() == null) {
            ((BelongToFrame) childComponent).setFrame(this);
        } else {
            registerComponent(childComponent);
        }

        if (index == ownComponents.size()) {
            ownComponents.add(childComponent);
        } else {
            ownComponents.add(index, childComponent);
        }

        childComponent.setParent(this);
    }

    @Override
    public int indexOf(Component component) {
        return ownComponents.indexOf(component);
    }

    @Nullable
    @Override
    public Component getComponent(int index) {
        return ownComponents.get(index);
    }

    @Override
    public void remove(Component childComponent) {
        getContainer().removeComponent(childComponent.unwrapComposition(com.vaadin.ui.Component.class));
        ownComponents.remove(childComponent);

        childComponent.setParent(null);
    }

    @Override
    public void removeAll() {
        getContainer().removeAllComponents();
        for (Component childComponent : ownComponents) {
            if (childComponent.getId() != null) {
                allComponents.remove(childComponent.getId());
            }
        }

        Component[] childComponents = ownComponents.toArray(new Component[0]);
        ownComponents.clear();

        for (Component ownComponent : childComponents) {
            ownComponent.setParent(null);
        }
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    @Override
    public Stream<Component> getOwnComponentsStream() {
        return ownComponents.stream();
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;

        AppUI ui = AppUI.getCurrent();
        if (ui != null
                && ui.isPerformanceTestMode()) {
            getComponent().setId(ui.getTestIdManager().getTestId("window_" + id));
        }
    }

    @Override
    public Component getParent() {
        return null;
    }

    @Override
    public void setParent(Component parent) {
    }

    @Override
    public boolean isEnabled() {
        return component.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        component.setEnabled(enabled);
    }

    @Override
    public boolean isResponsive() {
        com.vaadin.ui.ComponentContainer container = getContainer();

        return container instanceof AbstractComponent
                && ((AbstractComponent) container).isResponsive();
    }

    @Override
    public void setResponsive(boolean responsive) {
        com.vaadin.ui.ComponentContainer container = getContainer();

        if (container instanceof AbstractComponent) {
            ((AbstractComponent) container).setResponsive(responsive);
        }
    }

    @Override
    public boolean isVisible() {
        return getComposition().isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isVisibleRecursive() {
        return isVisible();
    }

    @Override
    public boolean isEnabledRecursive() {
        return isEnabled();
    }

    @Override
    public float getHeight() {
        return component.getHeight();
    }

    @Override
    public void setHeight(String height) {
        component.setHeight(height);
    }

    @Override
    public SizeUnit getHeightSizeUnit() {
        return WebWrapperUtils.toSizeUnit(component.getHeightUnits());
    }

    @Override
    public float getWidth() {
        return component.getWidth();
    }

    @Override
    public void setWidth(String width) {
        component.setWidth(width);
    }

    @Override
    public SizeUnit getWidthSizeUnit() {
        return WebWrapperUtils.toSizeUnit(component.getWidthUnits());
    }

    @Override
    public Component getOwnComponent(String id) {
        Component nestedComponent = allComponents.get(id);
        if (ownComponents.contains(nestedComponent)) {
            return nestedComponent;
        }

        return null;
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return ComponentsHelper.getWindowComponent(this, id);
    }

    @Override
    public Alignment getAlignment() {
        return Alignment.MIDDLE_CENTER;
    }

    @Override
    public void setAlignment(Alignment alignment) {
    }

    @Override
    public void expand(Component component, String height, String width) {
        com.vaadin.ui.Component expandedComponent = component.unwrapComposition(com.vaadin.ui.Component.class);
        WebComponentsHelper.expand((AbstractOrderedLayout) getContainer(), expandedComponent, height, width);
    }

    @Override
    public void expand(Component component) {
        expand(component, "", "");
    }

    @Override
    public void resetExpanded() {
        if (getContainer() instanceof AbstractOrderedLayout) {
            AbstractOrderedLayout container = (AbstractOrderedLayout) getContainer();

            for (com.vaadin.ui.Component child : container) {
                container.setExpandRatio(child, 0.0f);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public boolean isExpanded(Component component) {
        return ownComponents.contains(component) && WebComponentsHelper.isComponentExpanded(component);
    }

    @Override
    public ExpandDirection getExpandDirection() {
        return ExpandDirection.VERTICAL;
    }

    @Override
    public com.vaadin.ui.Component getComponent() {
        return component;
    }

    @Override
    public com.vaadin.ui.Component getComposition() {
        return component;
    }

    public boolean findAndFocusChildComponent() {
        Component.Focusable focusComponent = getComponentToFocus(this.getComponents().iterator());
        if (focusComponent != null) {
            focusComponent.focus();
            return true;
        }
        return false;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Frame getFrame() {
        return this;
    }

    @Override
    public void setFrame(Frame frame) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ActionsPermissions getActionsPermissions() {
        return actionsPermissions;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public void setIconFromSet(Icons.Icon icon) {
        setIcon(icons.get(icon));
    }

    @Override
    public void setExpandRatio(Component component, float ratio) {
        com.vaadin.ui.Component vComponent = component.unwrap(com.vaadin.ui.Component.class);
        this.component.setExpandRatio(vComponent, ratio);
    }

    @Override
    public float getExpandRatio(Component component) {
        com.vaadin.ui.Component vComponent = component.unwrap(com.vaadin.ui.Component.class);
        return this.component.getExpandRatio(vComponent);
    }

    public NavigationState getResolvedState() {
        return resolvedState;
    }

    public void setResolvedState(NavigationState resolvedState) {
        this.resolvedState = resolvedState;
    }

    public boolean isDefaultScreenWindow() {
        return defaultScreenWindow;
    }

    public void setDefaultScreenWindow(boolean defaultScreenWindow) {
        this.defaultScreenWindow = defaultScreenWindow;
    }
}