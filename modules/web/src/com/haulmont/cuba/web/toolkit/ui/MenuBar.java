/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 27.01.2009 16:11:44
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.itmill.toolkit.data.Container;
import com.itmill.toolkit.data.util.ContainerHierarchicalWrapper;
import com.itmill.toolkit.data.util.IndexedContainer;
import com.itmill.toolkit.event.ItemClickEvent;
import com.itmill.toolkit.terminal.PaintException;
import com.itmill.toolkit.terminal.PaintTarget;
import com.itmill.toolkit.ui.AbstractSelect;

import java.util.*;

/**
 * @deprecated
 */
public class MenuBar extends AbstractSelect
        implements Container.Hierarchical, ItemClickEvent.ItemClickSource
{
    public static final String TAG_NAME = "menubar";

    private boolean vertical;
    private boolean autoOpen = false;
    private int clickListenerCount = 0;

    public MenuBar() {
        this(false);
    }

    public MenuBar(boolean vertical) {
        this(vertical, null);
    }

    public MenuBar(boolean vertical, Container dataSource) {
        setVertical(vertical);
        setContainerDataSource(dataSource);
    }

    public void paintContent(PaintTarget target) throws PaintException {

//        super.paintContent(target);

        if (isVertical()) {
            target.addAttribute("vertical", vertical);
        }
        if (isAutoOpen()) {
            target.addAttribute("autoOpen", autoOpen);
        }
        if (clickListenerCount > 0) {
            target.addAttribute("clickListen", true);
        }

        final Stack iteratorStack = new Stack();

        final Collection rootIds = rootItemIds();
        if (rootIds != null) {
            iteratorStack.push(rootIds.iterator());
        }

        while (!iteratorStack.isEmpty()) {

            final Iterator i = (Iterator) iteratorStack.peek();

            if (!i.hasNext())
            {
                iteratorStack.pop();

                if (!iteratorStack.isEmpty()) {
                    target.endTag("menu");
                }

            } else {

                Object itemId = i.next();

                final boolean allowChildren = areChildrenAllowed(itemId);
                if (allowChildren) {
                    target.startTag("menu");
                } else {
                    target.startTag("item");
                }

                final String key = itemIdMapper.key(itemId);
                target.addAttribute("key", key);
                target.addAttribute("caption", getItemCaption(itemId));

                if (hasChildren(itemId)
                        && areChildrenAllowed(itemId)) {
                    iteratorStack.push(getChildren(itemId).iterator());
                } else {
                    if (allowChildren) {
                        target.endTag("menu");
                    } else {
                        target.endTag("item");
                    }
                }
            }
        }
    }

    public void changeVariables(Object source, Map variables) {

        if (clickListenerCount > 0 && variables.containsKey("clickedKey")) {
            String key = (String) variables.get("clickedKey");

            Object id = itemIdMapper.get(key);
//            MouseEventDetails details = MouseEventDetails.deSerialize((String) variables
//                    .get("clickEvent"));
            fireEvent(new ItemClickEvent(this, getItem(id), id, null, null));
        }

//        if (variables.containsKey("selected")) {
//            variables = new HashMap(variables);
//            variables.remove("selected");
//        }

//        super.changeVariables(source, variables);
    }

    public void setContainerDataSource(Container newDataSource) {
        if (newDataSource == null) {
            newDataSource = new ContainerHierarchicalWrapper(
                    new IndexedContainer());
        }

        if (Container.Hierarchical.class.isAssignableFrom(newDataSource
                .getClass())) {
            super.setContainerDataSource(newDataSource);
        } else {
            super.setContainerDataSource(new ContainerHierarchicalWrapper(
                    newDataSource));
        }
    }

    public Collection getChildren(Object itemId) {
        return ((Hierarchical) items).getChildren(itemId);
    }

    public Object getParent(Object itemId) {
        return ((Hierarchical) items).getParent(itemId);
    }

    public Collection rootItemIds() {
        return ((Hierarchical) items).rootItemIds();
    }

    public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
        boolean success = ((Hierarchical) items).setParent(itemId, newParentId);
        if (success) {
            requestRepaint();
        }
        return success;
    }

    public boolean areChildrenAllowed(Object itemId) {
        return ((Hierarchical) items).areChildrenAllowed(itemId);
    }

    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed) throws UnsupportedOperationException {
        boolean success = ((Hierarchical) items).setChildrenAllowed(itemId, areChildrenAllowed);
        if (success) {
            requestRepaint();
        }
        return success;
    }

    public boolean isRoot(Object itemId) {
        return ((Hierarchical) items).isRoot(itemId);
    }

    public boolean hasChildren(Object itemId) {
        return ((Hierarchical) items).hasChildren(itemId);
    }

    public void addListener(ItemClickEvent.ItemClickListener listener) {
        addListener(ItemClickEvent.class, listener,
                ItemClickEvent.ITEM_CLICK_METHOD);
        clickListenerCount++;
        if (clickListenerCount == 1) {
            requestRepaint();
        }
    }

    public void removeListener(ItemClickEvent.ItemClickListener listener) {
        if (clickListenerCount > 0) {
            removeListener(ItemClickEvent.class, listener,
                    ItemClickEvent.ITEM_CLICK_METHOD);
            clickListenerCount--; //todo fix the Tree class. There was  clickListenerCount++;
            if (clickListenerCount == 0) {
                requestRepaint();
            }
        }
    }

    public boolean isAutoOpen() {
        return autoOpen;
    }

    public void setAutoOpen(boolean autoOpen) {
        this.autoOpen = autoOpen;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public String getTag() {
        return TAG_NAME;
    }
}
