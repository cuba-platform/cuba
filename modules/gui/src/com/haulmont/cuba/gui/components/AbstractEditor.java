/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 28.01.2009 10:18:35
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public class AbstractEditor extends AbstractWindow implements Window.Editor {
    public AbstractEditor(IFrame frame) {
        super(frame);
    }

    public Object getItem() {
        if (frame instanceof Window.Editor) {
            return ((Editor) frame).getItem();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void setItem(Object item) {
        if (frame instanceof Window.Editor) {
            ((Editor) frame).setItem(item);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public boolean isValid() {
        if (frame instanceof Window.Editor) {
            return ((Editor) frame).isValid();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void validate() throws ValidationException {
        if (frame instanceof Window.Editor) {
            ((Editor) frame).validate();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void commit() {
        if (frame instanceof Window.Editor) {
            ((Editor) frame).commit();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public String getStyleName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setStyleName(String name) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
