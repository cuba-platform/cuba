/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 03.08.2010 17:09:11
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.toolkit.gwt.client.ui.VWidgetsTree;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Component;

import java.io.Serializable;
import java.util.LinkedList;

@SuppressWarnings("serial")
@ClientWidget(VWidgetsTree.class)
public class WidgetsTree extends com.vaadin.ui.Tree {

    private WidgetBuilder widgetBuilder;

    @Override
    protected void paintItem(
            PaintTarget target,
            Object itemId,
            LinkedList<String> selectedKeys,
            LinkedList<String> expandedKeys
    ) throws PaintException {
        if (widgetBuilder != null) {
            Component c = widgetBuilder.buildWidget(this, itemId, areChildrenAllowed(itemId));
            if (c != null) {
                final String key = itemIdMapper.key(itemId);
                target.addAttribute("key", key);
/*
                if (isSelected(itemId)) {
                    target.addAttribute("selected", true);
                    selectedKeys.add(key);
                }
*/
                if (areChildrenAllowed(itemId) && hasChildren(itemId)) {
                    target.addAttribute("hasChildren", true);
                }
                if (areChildrenAllowed(itemId) && isExpanded(itemId)) {
                    target.addAttribute("expanded", true);
                    expandedKeys.add(key);
                }
                target.addAttribute("hasWidget", true);
                
                target.startTag("widget");
                c.setParent(this);
                c.requestRepaint();
                c.paint(target);
                target.endTag("widget");
                return;
            }
        }
        super.paintItem(target, itemId, selectedKeys, expandedKeys);
    }

    public WidgetBuilder getWidgetBuilder() {
        return widgetBuilder;
    }

    public void setWidgetBuilder(WidgetBuilder widgetBuilder) {
        this.widgetBuilder = widgetBuilder;
    }

    public interface WidgetBuilder extends Serializable {
        Component buildWidget(WidgetsTree source, Object itemId, boolean leaf);
    }

}
