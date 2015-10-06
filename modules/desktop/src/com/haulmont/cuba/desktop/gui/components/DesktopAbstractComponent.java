/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.data.ComponentSize;
import com.haulmont.cuba.desktop.theme.DesktopTheme;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.Frame;
import org.apache.commons.lang.ObjectUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.Locale;

/**
 * @author krivopustov
 * @version $Id$
 */
public abstract class DesktopAbstractComponent<C extends JComponent>
        implements
            DesktopComponent, Component.Wrapper, Component.HasXmlDescriptor, Component.BelongToFrame {

    public static final String SWING_PROPERTY_CLASS = "cubaClass";
    public static final String SWING_PROPERTY_ID = "cubaId";

    protected Logger log = LoggerFactory.getLogger(getClass());

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

    protected boolean parentEnabled = true;

    protected String debugId;

    protected String styleName;

    protected C getImpl() {
        return impl;
    }

    protected String getSwingPropertyId() {
        return SWING_PROPERTY_ID;
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
        this.enabled = enabled;

        updateEnabled();
    }

    protected void updateEnabled() {
        getComposition().setEnabled(isEnabledWithParent());

        requestContainerUpdate();
    }

    protected boolean isEnabledWithParent() {
        return enabled && parentEnabled;
    }

    @Override
    public boolean isVisible() {
        return DesktopComponentsHelper.isRecursivelyVisible(getComposition());
    }

    @Override
    public void setVisible(boolean visible) {
        if (this.visible != visible) {
            this.visible = visible;

            getComposition().setVisible(visible);
            requestContainerUpdate();
        }
    }

    @Override
    public void requestFocus() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                impl.requestFocus();
            }
        });
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
        return styleName;
    }

    @Override
    public void setStyleName(String name) {
        DesktopTheme theme = App.getInstance().getTheme();
        if (theme != null) {
            this.styleName = name;
            theme.applyStyle(this, name);
        }
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
}