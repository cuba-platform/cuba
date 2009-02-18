/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 28.01.2009 10:20:22
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import org.dom4j.Element;
import com.haulmont.cuba.gui.data.DsContext;

import java.util.Map;

public class AbstractWindow extends AbstractFrame implements Window, Component.HasXmlDescriptor {
    public AbstractWindow(IFrame frame) {
        super(frame);
    }

    public Element getXmlDescriptor() {
        if (frame instanceof HasXmlDescriptor) {
            return ((HasXmlDescriptor) frame).getXmlDescriptor();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void setXmlDescriptor(Element element) {
        if (frame instanceof HasXmlDescriptor) {
            ((HasXmlDescriptor) frame).setXmlDescriptor(element);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public DsContext getDsContext() {
        if (frame instanceof Window) {
            return ((Window) frame).getDsContext();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void setDsContext(DsContext dsContext) {
        if (frame instanceof Window) {
            ((Window) frame).setDsContext(dsContext);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public String getCaption() {
        if (frame instanceof Window) {
            return ((Window) frame).getCaption();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void setCaption(String caption) {
        if (frame instanceof Window) {
            ((Window) frame).setCaption(caption);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    protected void init(Map<String, Object> params) {

    }
}
