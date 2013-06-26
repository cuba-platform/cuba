/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.table;

import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.VScrollTable;

import java.util.Iterator;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaScrollTableWidget extends VScrollTable implements ShortcutActionHandler.ShortcutActionHandlerOwner {

    protected static final String WIDGET_CELL_CLASSNAME = "widget-container";

    protected ShortcutActionHandler shortcutHandler;

    protected CubaScrollTableWidget() {
        // handle shortcuts
        DOM.sinkEvents(getElement(), Event.ONKEYDOWN);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final int type = DOM.eventGetType(event);
        if (type == Event.ONKEYDOWN && shortcutHandler != null) {
            shortcutHandler.handleKeyboardEvent(event);
        }
    }

    public void setShortcutActionHandler(ShortcutActionHandler handler){
        this.shortcutHandler = handler;
    }

    @Override
    public ShortcutActionHandler getShortcutActionHandler() {
        return shortcutHandler;
    }

    @Override
    protected VScrollTableBody createScrollBody() {
        return new CubaScrollTableBody();
    }

    @Override
    public void updateActionMap(UIDL mainUidl) {
        UIDL actionsUidl = mainUidl.getChildByTagName("actions");
        if (actionsUidl == null) {
            return;
        }

        final Iterator<?> it = actionsUidl.getChildIterator();
        while (it.hasNext()) {
            final UIDL action = (UIDL) it.next();
            final String key = action.getStringAttribute("key");
            final String caption = action.getStringAttribute("caption");
            if (!action.hasAttribute("kc")) {
                actionMap.put(key + "_c", caption);
                if (action.hasAttribute("icon")) {
                    // TODO need some uri handling ??
                    actionMap.put(key + "_i", client.translateVaadinUri(action
                            .getStringAttribute("icon")));
                } else {
                    actionMap.remove(key + "_i");
                }
            }
        }
    }

    protected class CubaScrollTableBody extends VScrollTableBody {

        protected VScrollTableRow createRow(UIDL uidl, char[] aligns2) {
            if (uidl.hasAttribute("gen_html")) {
                // This is a generated row.
                return new VScrollTableGeneratedRow(uidl, aligns2);
            }
            return new CubaScrollTableRow(uidl, aligns2);
        }

        protected class CubaScrollTableRow extends VScrollTableRow {

            public CubaScrollTableRow(UIDL uidl, char[] aligns) {
                super(uidl, aligns);
            }

            @Override
            protected void initCellWithWidget(Widget w, char align,
                                              String style, boolean sorted, TableCellElement td) {
                super.initCellWithWidget(w, align, style, sorted, td);

                td.getFirstChildElement().addClassName(WIDGET_CELL_CLASSNAME);
            }

            @Override
            protected void updateCellStyleNames(TableCellElement td, String primaryStyleName) {
                Element container = td.getFirstChild().cast();
                boolean isWidget = container.getClassName() != null
                        && container.getClassName().contains(WIDGET_CELL_CLASSNAME);

                super.updateCellStyleNames(td, primaryStyleName);

                if (isWidget)
                    container.addClassName(WIDGET_CELL_CLASSNAME);
            }
        }
    }
}
