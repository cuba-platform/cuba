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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.data.ComponentSize;
import com.haulmont.cuba.desktop.gui.executors.impl.DesktopBackgroundWorker;
import com.haulmont.cuba.desktop.theme.DesktopTheme;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.icons.Icons;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.swing.*;
import java.util.*;

public abstract class DesktopAbstractComponent<C extends JComponent>
        implements
            DesktopComponent, Component.Wrapper, Component.HasXmlDescriptor, Component.BelongToFrame, Component.HasIcon {

    public static final String SWING_PROPERTY_CLASS = "cubaClass";
    public static final String SWING_PROPERTY_ID = "cubaId";

    protected C impl;

    protected DesktopContainer container;

    protected String id;
    protected Frame frame;
    protected Component parent;

    protected Element xmlDescriptor;

    protected ComponentSize widthSize;
    protected ComponentSize heightSize;

    protected Alignment alignment;

    protected boolean visible = true;
    protected boolean enabled = true;
    protected boolean responsive = false;

    protected boolean parentEnabled = true;

    protected int tabIndex = 0; // just stub

    protected String debugId;

    // lazily initialized list
    protected List<String> styles;

    protected String caption;

    protected C getImpl() {
        return impl;
    }

    protected String getSwingPropertyId() {
        return SWING_PROPERTY_ID;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        if (!Objects.equals(getCaption(), caption)) {
            this.caption = caption;

            setCaptionToComponent(caption);

            if (this instanceof Field
                    && parent instanceof DesktopFieldGroup) {
                ((DesktopFieldGroup) parent).updateCaptionText(this);
            }
        }
    }

    protected void setCaptionToComponent(String caption) {
    }

    @Override
    public Component getParent() {
        return parent;
    }

    @Override
    public void setParent(Component parent) {
        this.parent = parent;
    }

    @Override
    public Frame getFrame() {
        return frame;
    }

    @Override
    public void setFrame(Frame frame) {
        this.frame = frame;
        if (frame != null) {
            frame.registerComponent(this);
        }

        assignAutoDebugId();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        if (!ObjectUtils.equals(this.id, id)) {
            if (frame != null) {
                frame.unregisterComponent(this);
            }

            this.id = id;
            C impl = getImpl();
            if (impl != null) {
                impl.putClientProperty(getSwingPropertyId(), id);
                impl.setName(id);
            }

            if (frame != null) {
                frame.registerComponent(this);
            }
        }
    }

    public void assignAutoDebugId() {
        App app = App.getInstance();
        if (app != null && app.isTestMode()) {
            C impl = getImpl();
            // always change name, do not assign auto id for components
            if (getId() == null && impl != null) {
                String alternativeDebugId = getAlternativeDebugId();

                impl.setName(alternativeDebugId);
            }
        }
    }

    /**
     * @return id that is suitable for auto debug id
     */
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }

        return getClass().getSimpleName();
    }

    protected void assignClassDebugProperty(JComponent c) {
        c.putClientProperty(SWING_PROPERTY_CLASS, getClass().getSimpleName());
    }

    @Override
    public String getDebugId() {
        return debugId;
    }

    @Override
    public void setDebugId(String id) {
        this.debugId = id;
    }

    @Override
    public boolean isEnabled() {
        return DesktopComponentsHelper.isRecursivelyEnabled(getComposition());
    }

    @Override
    public void setEnabled(boolean enabled) {
        DesktopBackgroundWorker.checkSwingUIAccess();

        if (this.enabled != enabled) {
            this.enabled = enabled;

            updateEnabled();
        }
    }

    @Override
    public boolean isResponsive() {
        return responsive;
    }

    @Override
    public void setResponsive(boolean responsive) {
        this.responsive = responsive;
    }

    protected void updateEnabled() {
        getComposition().setEnabled(isEnabledWithParent());

        requestContainerUpdate();

        if (parent instanceof DesktopFieldGroup) {
            ((DesktopFieldGroup) parent).updateChildEnabled(this);
        }
    }

    protected boolean isEnabledWithParent() {
        return enabled && parentEnabled;
    }

    public boolean isComponentVisible() {
        return getComposition().isVisible();
    }

    @Override
    public boolean isVisible() {
        return DesktopComponentsHelper.isRecursivelyVisible(getComposition());
    }

    @Override
    public void setVisible(boolean visible) {
        DesktopBackgroundWorker.checkSwingUIAccess();

        if (this.visible != visible) {
            this.visible = visible;

            getComposition().setVisible(visible);

            if (parent instanceof DesktopFieldGroup) {
                ((DesktopFieldGroup) parent).updateCaptionVisibility(this);
            }

            requestContainerUpdate();
        }
    }

    @Override
    public boolean isVisibleItself() {
        return visible;
    }

    @Override
    public boolean isEnabledItself() {
        return enabled;
    }

    @Override
    public void requestFocus() {
        SwingUtilities.invokeLater(() ->
                impl.requestFocus()
        );
    }

    @Override
    public float getHeight() {
        return heightSize != null ? heightSize.value : -1;
    }

    @Override
    public int getHeightUnits() {
        return heightSize != null ? heightSize.unit : 0;
    }

    @Override
    public void setHeight(String height) {
        heightSize = ComponentSize.parse(height);
        requestContainerUpdate();
    }

    @Override
    public float getWidth() {
        return widthSize != null ? widthSize.value : -1;
    }

    @Override
    public int getWidthUnits() {
        return widthSize != null ? widthSize.unit : 0;
    }

    @Override
    public void setWidth(String width) {
        widthSize = ComponentSize.parse(width);
        requestContainerUpdate();
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public void setAlignment(Alignment alignment) {
        if (this.alignment != alignment) {
            this.alignment = alignment;
            requestContainerUpdate();
        }
    }

    @Override
    public String getStyleName() {
        if (CollectionUtils.isEmpty(styles))
            return StringUtils.EMPTY;

        return String.join(" ", styles);
    }

    @Override
    public void setStyleName(String styleName) {
        if (styles == null)
            styles = new LinkedList<>();

        styles.clear();

        parseAndApplyTheme(styleName);
    }

    @Override
    public void addStyleName(String styleName) {
        if (styles == null)
            styles = new LinkedList<>();

        if (StringUtils.isEmpty(styleName) || styles.contains(styleName))
            return;

        parseAndApplyTheme(styleName);
    }

    protected void parseAndApplyTheme(String styleName) {
        String style = null;
        if (StringUtils.isNotEmpty(styleName)) {
            StringTokenizer tokenizer = new StringTokenizer(styleName, " ");
            while (tokenizer.hasMoreTokens()) {
                style = tokenizer.nextToken();
                styles.add(style);
            }
        }

        DesktopTheme appTheme = App.getInstance().getTheme();
        if (appTheme != null)
            appTheme.applyStyle(this, style);
    }

    @Override
    public void removeStyleName(String styleName) {
        if (StringUtils.isEmpty(styleName) || CollectionUtils.isEmpty(styles) || !styles.contains(styleName))
            return;

        List<String> passedStyles = new ArrayList<>();

        StringTokenizer tokenizer = new StringTokenizer(styleName, " ");
        while (tokenizer.hasMoreTokens()) {
            passedStyles.add(tokenizer.nextToken());
        }

        String appliedStyleName = styles.get(styles.size() - 1);
        if (passedStyles.contains(appliedStyleName)) {
            DesktopTheme appTheme = App.getInstance().getTheme();
            if (appTheme != null)
                appTheme.applyStyle(this, null);
        }

        styles.removeAll(passedStyles);
    }

    @Override
    public Element getXmlDescriptor() {
        return xmlDescriptor;
    }

    @Override
    public void setXmlDescriptor(Element element) {
        xmlDescriptor = element;
    }

    @Override
    public JComponent getComponent() {
        return impl;
    }

    @Override
    public JComponent getComposition() {
        return impl;
    }

    @Override
    public void setContainer(DesktopContainer container) {
        this.container = container;
    }

    protected void requestContainerUpdate() {
        if (container != null) {
            container.updateComponent(this);
        }
    }

    @Override
    public void setExpanded(boolean expanded) {
    }

    public boolean isParentEnabled() {
        return parentEnabled;
    }

    public void setParentEnabled(boolean parentEnabled) {
        this.parentEnabled = parentEnabled;

        updateEnabled();
    }

    /**
     * Default formatter for {@link DesktopLabel} and {@link DesktopTextField}
     */
    protected static class DefaultValueFormatter {

        private MetaProperty metaProperty;
        private Formatter formatter;
        private Datatype datatype;
        private Locale locale;

        public DefaultValueFormatter(Locale locale) {
            this.locale = locale;
        }

        public Formatter getFormatter() {
            return formatter;
        }

        public void setFormatter(Formatter formatter) {
            this.formatter = formatter;
        }

        public Datatype getDatatype() {
            return datatype;
        }

        public void setDatatype(Datatype datatype) {
            this.datatype = datatype;
        }

        public MetaProperty getMetaProperty() {
            return metaProperty;
        }

        public void setMetaProperty(MetaProperty metaProperty) {
            this.metaProperty = metaProperty;
        }

        /**
         * Format value for text field or label
         *
         * @param value Object value
         * @return Formatted string
         */
        public String formatValue(Object value) {
            String text;
            if (formatter == null) {
                if (value == null) {
                    text = "";
                } else {
                    MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);

                    if (metaProperty != null) {
                        text = metadataTools.format(value, metaProperty);
                    } else if (datatype != null) {
                        text = datatype.format(value, locale);
                    } else {
                        text = metadataTools.format(value);
                    }
                }
            } else {
                text = formatter.format(value);
            }
            return text;
        }
    }

    @Override
    public <X> X unwrap(Class<X> internalComponentClass) {
        return (X) getComponent();
    }

    @Override
    public <X> X unwrapComposition(Class<X> internalCompositionClass) {
        return (X) getComposition();
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public void setIcon(String icon) {
        // do nothing
    }

    @Override
    public void setIconByName(Icons.Icon icon) {
        // do nothing
    }

    // Just stub
    public int getTabIndex() {
        return tabIndex;
    }

    // Just stub
    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }
}