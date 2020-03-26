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
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.HtmlAttributes.CSS;
import com.haulmont.cuba.gui.components.sys.FrameImplementation;
import com.haulmont.cuba.web.widgets.CubaHorizontalActionsLayout;
import com.haulmont.cuba.web.widgets.CubaScrollBoxLayout;
import com.haulmont.cuba.web.widgets.CubaVerticalActionsLayout;
import com.haulmont.cuba.web.widgets.HtmlAttributesExtension;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractOrderedLayout;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class WebScrollBoxLayout extends WebAbstractComponent<CubaScrollBoxLayout> implements ScrollBoxLayout {

    protected static final String SCROLLBOX_CONTENT_STYLENAME = "c-scrollbox-content";
    protected static final String SCROLLBOX_STYLENAME = "c-scrollbox";

    protected List<Component> ownComponents = new ArrayList<>();
    protected LayoutEvents.LayoutClickListener layoutClickListener;

    protected Orientation orientation = Orientation.VERTICAL;
    protected ScrollBarPolicy scrollBarPolicy = ScrollBarPolicy.VERTICAL;

    protected Map<ShortcutAction, ShortcutListener> shortcuts;

    public WebScrollBoxLayout() {
        component = new CubaScrollBoxLayout();
        component.setWidth(100, Sizeable.Unit.PERCENTAGE);
        component.setPrimaryStyleName(SCROLLBOX_STYLENAME);

        CubaVerticalActionsLayout content = new CubaVerticalActionsLayout();
        content.setWidth(100, Sizeable.Unit.PERCENTAGE);
        content.setStyleName(SCROLLBOX_CONTENT_STYLENAME);
        component.addComponent(content);

        getContent().setMargin(false);
    }

    protected AbstractOrderedLayout getContent() {
        return (AbstractOrderedLayout) component.getComponent(0);
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

        AbstractOrderedLayout newContent = null;
        if (orientation == Orientation.VERTICAL && !(getContent() instanceof CubaVerticalActionsLayout)) {
            newContent = new CubaVerticalActionsLayout();
            newContent.setWidth(100, Sizeable.Unit.PERCENTAGE);
        } else if (orientation == Orientation.HORIZONTAL && !(getContent() instanceof CubaHorizontalActionsLayout)) {
            newContent = new CubaHorizontalActionsLayout();
        }

        if (newContent != null) {
            newContent.setMargin((getContent()).getMargin());
            newContent.setSpacing((getContent()).isSpacing());
            newContent.setStyleName(SCROLLBOX_CONTENT_STYLENAME);

            com.vaadin.ui.Component oldContent = component.getComponent(0);
            newContent.setWidth(oldContent.getWidth(), oldContent.getWidthUnits());
            newContent.setHeight(oldContent.getHeight(), oldContent.getHeightUnits());

            component.removeAllComponents();
            component.addComponent(newContent);

            applyScrollBarsPolicy(scrollBarPolicy);
        }

        if (ownComponents.contains(childComponent)) {
            int existingIndex = getContent().getComponentIndex(WebComponentsHelper.getComposition(childComponent));
            if (index > existingIndex) {
                index--;
            }

            remove(childComponent);
        }

        com.vaadin.ui.Component vComponent = WebComponentsHelper.getComposition(childComponent);
        getContent().addComponent(vComponent, index);
        getContent().setComponentAlignment(vComponent, WebWrapperUtils.toVaadinAlignment(childComponent.getAlignment()));

        if (frame != null) {
            if (childComponent instanceof BelongToFrame
                    && ((BelongToFrame) childComponent).getFrame() == null) {
                ((BelongToFrame) childComponent).setFrame(frame);
            } else {
                ((FrameImplementation) frame).registerComponent(childComponent);
            }
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
    public void setStyleName(String styleName) {
        super.setStyleName(styleName);

        component.addStyleName(SCROLLBOX_STYLENAME);
    }

    @Override
    public String getStyleName() {
        return StringUtils.normalizeSpace(super.getStyleName().replace(SCROLLBOX_STYLENAME, ""));
    }

    @Override
    public void remove(Component childComponent) {
        getContent().removeComponent(childComponent.unwrapComposition(com.vaadin.ui.Component.class));
        ownComponents.remove(childComponent);

        childComponent.setParent(null);
    }

    @Override
    public void removeAll() {
        getContent().removeAllComponents();

        Component[] components = ownComponents.toArray(new Component[0]);
        ownComponents.clear();

        for (Component childComponent : components) {
            childComponent.setParent(null);
        }
    }

    @Override
    public void setFrame(Frame frame) {
        super.setFrame(frame);

        if (frame != null) {
            for (Component childComponent : ownComponents) {
                if (childComponent instanceof BelongToFrame
                        && ((BelongToFrame) childComponent).getFrame() == null) {
                    ((BelongToFrame) childComponent).setFrame(frame);
                }
            }
        }
    }

    @Override
    public Component getOwnComponent(String id) {
        Preconditions.checkNotNullArgument(id);

        return ownComponents.stream()
                .filter(component -> Objects.equals(id, component.getId()))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return ComponentsHelper.getComponent(this, id);
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
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        if (!Objects.equals(orientation, this.orientation)) {
            if (!ownComponents.isEmpty()) {
                throw new IllegalStateException("Unable to change scrollBox orientation after adding components to it");
            }

            this.orientation = orientation;
        }
    }

    @Override
    public ScrollBarPolicy getScrollBarPolicy() {
        return scrollBarPolicy;
    }

    @Override
    public void setScrollBarPolicy(ScrollBarPolicy scrollBarPolicy) {
        if (this.scrollBarPolicy != scrollBarPolicy) {
            applyScrollBarsPolicy(scrollBarPolicy);
        }
        this.scrollBarPolicy = scrollBarPolicy;
    }

    @Override
    public void setContentWidth(String width) {
        getContent().setWidth(width);
    }

    @Override
    public float getContentWidth() {
        return getContent().getWidth();
    }

    @Override
    public SizeUnit getContentWidthSizeUnit() {
        return WebWrapperUtils.toSizeUnit(getContent().getWidthUnits());
    }

    @Override
    public void setContentHeight(String height) {
        getContent().setHeight(height);
    }

    @Override
    public float getContentHeight() {
        return getContent().getHeight();
    }

    @Override
    public SizeUnit getContentHeightSizeUnit() {
        return WebWrapperUtils.toSizeUnit(getContent().getHeightUnits());
    }

    @Override
    public void setContentMinWidth(String minWidth) {
        HtmlAttributesExtension.get(getContent())
                .setCssProperty(CSS.MIN_WIDTH, minWidth);
    }

    @Override
    public String getContentMinWidth() {
        return HtmlAttributesExtension.get(getContent())
                .getCssProperty(CSS.MIN_WIDTH);
    }

    @Override
    public void setContentMaxWidth(String maxWidth) {
        HtmlAttributesExtension.get(getContent())
                .setCssProperty(CSS.MAX_WIDTH, maxWidth);
    }

    @Override
    public String getContentMaxWidth() {
        return HtmlAttributesExtension.get(getContent())
                .getCssProperty(CSS.MAX_WIDTH);
    }

    @Override
    public void setContentMinHeight(String minHeight) {
        HtmlAttributesExtension.get(getContent())
                .setCssProperty(CSS.MIN_HEIGHT, minHeight);
    }

    @Override
    public String getContentMinHeight() {
        return HtmlAttributesExtension.get(getContent())
                .getCssProperty(CSS.MIN_HEIGHT);
    }

    @Override
    public void setContentMaxHeight(String maxHeight) {
        HtmlAttributesExtension.get(getContent())
                .setCssProperty(CSS.MAX_HEIGHT, maxHeight);
    }

    @Override
    public String getContentMaxHeight() {
        return HtmlAttributesExtension.get(getContent())
                .getCssProperty(CSS.MAX_HEIGHT);
    }

    @Override
    public Subscription addLayoutClickListener(Consumer<LayoutClickNotifier.LayoutClickEvent> listener) {
        if (layoutClickListener == null) {
            layoutClickListener = event -> {
                // scrollBoxLayout always has vertical or horizontal layout as first child element
                // choose vertical or horizontal layout as a parent to find the correct child
                com.vaadin.ui.Component child = findChildComponent(event.getClickedComponent());

                Component childComponent = findExistingComponent(child);
                Component clickedComponent = findExistingComponent(event.getClickedComponent());
                MouseEventDetails mouseEventDetails = WebWrapperUtils.toMouseEventDetails(event);

                LayoutClickNotifier.LayoutClickEvent layoutClickEvent =
                        new LayoutClickNotifier.LayoutClickEvent(this, childComponent, clickedComponent, mouseEventDetails);

                publish(LayoutClickNotifier.LayoutClickEvent.class, layoutClickEvent);
            };
            component.addLayoutClickListener(layoutClickListener);
        }

        getEventHub().subscribe(LayoutClickNotifier.LayoutClickEvent.class, listener);
        return () -> removeLayoutClickListener(listener);
    }

    protected com.vaadin.ui.Component findChildComponent(com.vaadin.ui.Component vComponent) {
        while (vComponent != null
                && vComponent.getParent() != component.getComponent(0)) {
            vComponent = vComponent.getParent();
        }
        return vComponent;
    }

    protected Component findExistingComponent(com.vaadin.ui.Component vComponent) {
        for (Component component : getComponents()) {
            if (component.unwrapComposition(com.vaadin.ui.Component.class) == vComponent) {
                return component;
            }
        }
        return null;
    }

    @Override
    public void removeLayoutClickListener(Consumer<LayoutClickEvent> listener) {
        unsubscribe(LayoutClickEvent.class, listener);

        if (!hasSubscriptions(LayoutClickEvent.class)) {
            component.removeLayoutClickListener(layoutClickListener);
            layoutClickListener = null;
        }
    }

    protected void applyScrollBarsPolicy(ScrollBarPolicy scrollBarPolicy) {
        switch (scrollBarPolicy) {
            case VERTICAL:
                getContent().setHeightUndefined();
                getContent().setWidth(100, Sizeable.Unit.PERCENTAGE);
                break;

            case HORIZONTAL:
                getContent().setHeight(100, Sizeable.Unit.PERCENTAGE);
                getContent().setWidthUndefined();
                break;

            case BOTH:
                getContent().setSizeUndefined();
                break;

            case NONE:
                getContent().setSizeFull();
                break;
        }
    }

    @Override
    public void setMargin(com.haulmont.cuba.gui.components.MarginInfo marginInfo) {
        MarginInfo vMargin = new MarginInfo(marginInfo.hasTop(), marginInfo.hasRight(), marginInfo.hasBottom(),
                marginInfo.hasLeft());
        component.setMargin(vMargin);
    }

    @Override
    public com.haulmont.cuba.gui.components.MarginInfo getMargin() {
        MarginInfo vMargin = getContent().getMargin();
        return new com.haulmont.cuba.gui.components.MarginInfo(vMargin.hasTop(), vMargin.hasRight(), vMargin.hasBottom(),
                vMargin.hasLeft());
    }

    @Override
    public void setSpacing(boolean enabled) {
        getContent().setSpacing(enabled);
    }

    @Override
    public boolean getSpacing() {
        return getContent().isSpacing();
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return component.isRequiredIndicatorVisible();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean visible) {
        component.setRequiredIndicatorVisible(visible);
    }

    @Override
    public void addShortcutAction(ShortcutAction action) {
        KeyCombination keyCombination = action.getShortcutCombination();
        com.vaadin.event.ShortcutListener shortcut =
                new ContainerShortcutActionWrapper(action, this, keyCombination);
        component.addShortcutListener(shortcut);

        if (shortcuts == null) {
            shortcuts = new HashMap<>(4);
        }
        shortcuts.put(action, shortcut);
    }

    @Override
    public void removeShortcutAction(ShortcutAction action) {
        if (shortcuts != null) {
            component.removeShortcutListener(shortcuts.remove(action));

            if (shortcuts.isEmpty()) {
                shortcuts = null;
            }
        }
    }

    @Override
    public int getScrollLeft() {
        return component.getScrollLeft();
    }

    @Override
    public void setScrollLeft(int scrollLeft) {
        component.setScrollLeft(scrollLeft);
    }

    @Override
    public int getScrollTop() {
        return component.getScrollTop();
    }

    @Override
    public void setScrollTop(int scrollTop) {
        component.setScrollTop(scrollTop);
    }

    @Override
    public void attached() {
        super.attached();

        for (Component component : ownComponents) {
            ((AttachNotifier) component).attached();
        }
    }

    @Override
    public void detached() {
        super.detached();

        for (Component component : ownComponents) {
            ((AttachNotifier) component).detached();
        }
    }
}