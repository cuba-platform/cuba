/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.data.ComponentSize;
import com.haulmont.cuba.desktop.theme.DesktopTheme;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.IFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import javax.swing.*;
import java.util.Locale;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class DesktopAbstractComponent<C extends JComponent>
    implements
        DesktopComponent, Component.Wrapper, Component.HasXmlDescriptor, Component.BelongToFrame, Component.Expandable
{
    protected C impl;

    protected DesktopContainer container;

    protected String id;
    protected IFrame frame;
    protected Element xmlDescriptor;
    protected boolean expandable = true;

    protected ComponentSize widthSize, heightSize;
    protected Alignment alignment;

    protected Log log = LogFactory.getLog(getClass());

    protected C getImpl() {
        return impl;
    }

    public <A extends IFrame> A getFrame() {
        return (A) frame;
    }

    public void setFrame(IFrame frame) {
        this.frame = frame;
        frame.registerComponent(this);
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDebugId() {
        return null;
    }

    public void setDebugId(String id) {
    }

    public boolean isEnabled() {
        return impl.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        getComposition().setEnabled(enabled);
        requestContainerUpdate();
    }

    public boolean isVisible() {
        return impl.isVisible();
    }

    public void setVisible(boolean visible) {
        getComposition().setVisible(visible);
        requestContainerUpdate();
    }

    public void requestFocus() {
        impl.requestFocus();
    }

    public float getHeight() {
        return heightSize != null ? heightSize.value : 0.0f;
    }

    public int getHeightUnits() {
        return heightSize != null ? heightSize.unit : 0;
    }

    public void setHeight(String height) {
        heightSize = ComponentSize.parse(height);
        requestContainerUpdate();
    }

    public float getWidth() {
        return widthSize != null ? widthSize.value : 0.0f;
    }

    public int getWidthUnits() {
        return widthSize != null ? widthSize.unit : 0;
    }

    public void setWidth(String width) {
        widthSize = ComponentSize.parse(width);
        requestContainerUpdate();
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
        requestContainerUpdate();
    }

    public String getStyleName() {
        return null;
    }

    public void setStyleName(String name) {
        DesktopTheme theme = App.getInstance().getTheme();
        if (theme != null) {
            theme.applyStyle(this, name);
        }
    }

    public Element getXmlDescriptor() {
        return xmlDescriptor;
    }

    public void setXmlDescriptor(Element element) {
        xmlDescriptor = element;
    }

    public <T> T getComponent() {
        return (T) impl;
    }

    public JComponent getComposition() {
        return impl;
    }

    public void setContainer(DesktopContainer container) {
        this.container = container;
    }

    protected void requestContainerUpdate() {
        if (container != null) {
            container.updateComponent(this);
        }
    }

    @Override
    public void setExpanded(boolean expanded) {}

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
         * Format value for text field or lable
         * @param value Object value
         * @return Formatted string
         */
        public String formatValue(Object value) {
            String text = "";
            if (formatter == null) {
                if (value == null) {
                    text = "";
                } else {
                    if (metaProperty != null) {
                        Range range = metaProperty.getRange();
                        if (range.isDatatype()) {
                            text = range.asDatatype().format(value, locale);
                        } else if (range.isEnum()) {
                            text = MessageProvider.getMessage((Enum) value);
                        } else if (range.isClass()) {
                            text = InstanceUtils.getInstanceName((Instance) value);
                        } else
                            text = String.valueOf(value);
                    } else if (datatype != null) {
                        datatype.format(value, locale);
                    } else
                        text = String.valueOf(value);
                }
            } else {
                text = formatter.format(value);
            }
            return text;
        }
    }
}
