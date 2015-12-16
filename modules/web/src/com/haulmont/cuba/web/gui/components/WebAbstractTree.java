/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.web.toolkit.ui.CubaTree;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author gorelov
 * @version $Id$
 */
public abstract class WebAbstractTree<T extends CubaTree, E extends Entity>
        extends WebAbstractList<T, E> implements Tree<E> {

    protected List<Tree.StyleProvider> styleProviders; // lazily initialized List
    protected StyleGeneratorAdapter styleGenerator;    // lazily initialized field

    protected IconProvider iconProvider;

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
    public void setStyleProvider(@Nullable Tree.StyleProvider styleProvider) {
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
    public void addStyleProvider(Tree.StyleProvider styleProvider) {
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
    public void removeStyleProvider(Tree.StyleProvider styleProvider) {
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
    public void setIconProvider(IconProvider iconProvider) {
        if (this.iconProvider != iconProvider) {
            this.iconProvider = iconProvider;

            if (iconProvider == null) {
                component.setItemIconProvider(null);
            } else {
                component.setItemIconProvider(itemId -> {
                    Entity item = datasource.getItem(itemId);
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