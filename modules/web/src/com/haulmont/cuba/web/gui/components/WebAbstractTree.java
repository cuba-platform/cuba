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

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.web.toolkit.ui.CubaTree;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public abstract class WebAbstractTree<T extends CubaTree, E extends Entity>
        extends WebAbstractList<T, E> implements Tree<E> {

    protected List<Tree.StyleProvider> styleProviders; // lazily initialized List
    protected StyleGeneratorAdapter styleGenerator;    // lazily initialized field

    protected ButtonsPanel buttonsPanel;
    protected HorizontalLayout topPanel;
    protected VerticalLayout componentComposition;
    protected IconProvider<? super E> iconProvider;

    @Override
    public HierarchicalDatasource getDatasource() {
        return (HierarchicalDatasource) super.getDatasource();
    }

    @Override
    public void collapseTree() {
        component.collapseAll();
    }

    @Override
    public void expandTree() {
        component.expandAll();
    }

    @Override
    public void collapse(Object itemId) {
        component.collapseItemRecursively(itemId);
    }

    @Override
    public void expand(Object itemId) {
        component.expandItemWithParents(itemId);
    }

    @Override
    public void expandUpTo(int level) {
        component.expandUpTo(level);
    }

    @Override
    public boolean isExpanded(Object itemId) {
        return component.isExpanded(itemId);
    }

    @Override
    public boolean isEditable() {
        return !component.isReadOnly();
    }

    @Override
    public void setEditable(boolean editable) {
        component.setReadOnly(!editable);
    }

    @Override
    public ButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }

    @Override
    public com.vaadin.ui.Component getComposition() {
        return componentComposition;
    }

    @Override
    public void setButtonsPanel(ButtonsPanel panel) {
        if (buttonsPanel != null && topPanel != null) {
            topPanel.removeComponent(WebComponentsHelper.unwrap(buttonsPanel));
            buttonsPanel.setParent(null);
        }
        buttonsPanel = panel;
        if (panel != null) {
            if (panel.getParent() != null && panel.getParent() != this) {
                throw new IllegalStateException("Component already has parent");
            }

            if (topPanel == null) {
                topPanel = new HorizontalLayout();
                topPanel.setWidth("100%");

                componentComposition.addComponentAsFirst(topPanel);
            }
            topPanel.addComponent(WebComponentsHelper.unwrap(panel));
            panel.setParent(this);
        }
    }

    public void initComponent(CubaTree component) {
        componentComposition = new VerticalLayout();
        componentComposition.addComponent(component);

        componentComposition.setSpacing(true);
        componentComposition.setMargin(false);
        componentComposition.setWidth("-1px");

        component.setSizeFull();
        componentComposition.setExpandRatio(component, 1);
    }

    @Override
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }
        if (datasource != null && StringUtils.isNotEmpty(datasource.getId())) {
            return getClass().getSimpleName() + "_" + datasource.getId();
        }

        return getClass().getSimpleName();
    }

    protected StyleGeneratorAdapter createStyleGenerator() {
        return new StyleGeneratorAdapter();
    }

    @SuppressWarnings("unchecked")
    protected String getGeneratedStyle(Object itemId) {
        if (styleProviders == null) {
            return null;
        }

        Entity item = datasource.getItem(itemId);
        String joinedStyle = null;
        for (Tree.StyleProvider styleProvider : styleProviders) {
            String styleName = styleProvider.getStyleName(item);
            if (styleName != null) {
                if (joinedStyle == null) {
                    joinedStyle = styleName;
                } else {
                    joinedStyle += " " + styleName;
                }
            }
        }

        return joinedStyle;
    }

    @Override
    public void setStyleProvider(@Nullable Tree.StyleProvider<? super E> styleProvider) {
        if (styleProvider != null) {
            if (this.styleProviders == null) {
                this.styleProviders = new LinkedList<>();
            } else {
                this.styleProviders.clear();
            }

            this.styleProviders.add(styleProvider);

        } else {
            this.styleProviders = null;
        }

        if (this.styleGenerator == null) {
            this.styleGenerator = createStyleGenerator();
            component.setItemStyleGenerator(this.styleGenerator);
        } else {
            component.markAsDirty();
        }
    }

    @Override
    public void addStyleProvider(Tree.StyleProvider<? super E> styleProvider) {
        if (this.styleProviders == null) {
            this.styleProviders = new LinkedList<>();
        }

        if (!this.styleProviders.contains(styleProvider)) {
            this.styleProviders.add(styleProvider);

            if (this.styleGenerator == null) {
                this.styleGenerator = createStyleGenerator();
                component.setItemStyleGenerator(this.styleGenerator);
            } else {
                component.markAsDirty();
            }
        }
    }

    @Override
    public void removeStyleProvider(Tree.StyleProvider<? super E> styleProvider) {
        if (this.styleProviders != null) {
            if (this.styleProviders.remove(styleProvider)) {
                component.markAsDirty();
            }
        }
    }

    protected class StyleGeneratorAdapter implements com.vaadin.ui.Tree.ItemStyleGenerator {

        public static final String CUSTOM_STYLE_NAME_PREFIX = "cs ";

        @Override
        public String getStyle(com.vaadin.ui.Tree source, Object itemId) {
            String style = null;

            if (styleProviders != null) {
                String generatedStyle = getGeneratedStyle(itemId);
                if (generatedStyle != null) {
                    style = CUSTOM_STYLE_NAME_PREFIX + generatedStyle;
                }
            }

            return style == null ? null : (CUSTOM_STYLE_NAME_PREFIX + style);
        }
    }

    @Override
    public void repaint() {
        component.markAsDirty();
    }

    @Override
    public void setIconProvider(IconProvider<? super E> iconProvider) {
        if (this.iconProvider != iconProvider) {
            this.iconProvider = iconProvider;

            if (iconProvider == null) {
                component.setItemIconProvider(null);
            } else {
                component.setItemIconProvider(itemId -> {
                    @SuppressWarnings("unchecked")
                    E item = (E) datasource.getItem(itemId);
                    if (item == null) {
                        return null;
                    }

                    String resourceUrl = WebAbstractTree.this.iconProvider.getItemIcon(item);
                    if (StringUtils.isBlank(resourceUrl)) {
                        return null;
                    }
                    // noinspection ConstantConditions
                    if (!resourceUrl.contains(":")) {
                        resourceUrl = "theme:" + resourceUrl;
                    }
                    return WebComponentsHelper.getResource(resourceUrl);
                });
            }
        }
    }
}