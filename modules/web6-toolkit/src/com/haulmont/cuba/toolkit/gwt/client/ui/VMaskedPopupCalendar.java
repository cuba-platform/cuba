/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.VOrderedLayout;
import com.vaadin.terminal.gwt.client.ui.VPopupCalendar;

import java.util.HashSet;
import java.util.Set;

/**
 * @author devyatkin
 * @version $Id$
 */
public class VMaskedPopupCalendar extends VPopupCalendar {

    private static final String MASKED_FIELD_CLASS = "v-maskedfield-onlymask";

    private static final boolean isDebug = false;

    private void debug(String msg) {
        if (isDebug)
            VConsole.log(msg);
    }

    public VMaskedPopupCalendar() {
        if (BrowserInfo.get().isIE7()) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    Set<Paintable> childrens = new HashSet<Paintable>();
                    childrens.add(VMaskedPopupCalendar.this);
                    VOrderedLayout layout = (VOrderedLayout) Util.getLayout(VMaskedPopupCalendar.this);
                    for (int i = 0; i < layout.getWidgetCount(); i++) {
                        if (layout.getWidget(i) instanceof Paintable)
                            childrens.add((Paintable) layout.getWidget(i));
                    }
                    layout.requestLayout(childrens);
                }
            });
        }
        debug("VMaskedTextField created");
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        debug("updateFromUIDL: " + uidl);
        if (!(uidl.getBooleanAttribute("readonly"))) {
            String maskParam = uidl.getStringAttribute("mask");
            getImpl().setMask(maskParam == null ? "" : maskParam);
        }
        super.updateFromUIDL(uidl, client);
    }


    public VMaskedTextField getImpl() {
        return (VMaskedTextField) super.getImpl();
    }

    @Override
    protected TextBoxBase createImpl() {
        return new VMaskedTextField() {
            public void valueChange(boolean blurred) {
                String newText = getText();
                if (!prompting && newText != null
                        && !newText.equals(valueBeforeEdit)) {
                    if (validateText(newText)) {
                        if (!newText.toString().equals(nullRepresentation)) {
                            getElement().removeClassName(MASKED_FIELD_CLASS);
                        }
                        VMaskedPopupCalendar.this.onChange(null);
                        valueBeforeEdit = newText;
                    } else {
                        setText(valueBeforeEdit);
                    }
                }
            }
        };
    }
}
