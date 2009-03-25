/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 24.02.2009 18:39:12
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.data.TreeTableContainer;
import com.itmill.toolkit.data.Container;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.terminal.PaintException;
import com.itmill.toolkit.terminal.PaintTarget;
import com.itmill.toolkit.ui.AbstractSelect;

import java.util.*;

public class TreeTable
        extends AbstractSelect
        implements Container.Hierarchical, TreeTableContainer
{

    public static final String TAG_NAME = "treetable";

    private final List visibleColumns = new LinkedList();
    private final Map<Object, String> columnHeaders = new HashMap<Object, String>();

    private final Set<Object> expanded = new HashSet<Object>();

    private boolean selectable = false;

    public TreeTable(Container dataSource) {
        super(null, dataSource);
    }

    @Override
    public void changeVariables(Object source, Map variables)
    {
        boolean needRepaint = false;

        handleClickEvent(variables);

        if (!isSelectable() && variables.containsKey("selected")) {
            variables = new HashMap(variables);
            variables.remove("selected");
        }

        super.changeVariables(source, variables);

        if (variables.containsKey("expand"))
        {
            String key = (String) variables.get("expand");
            Object itemId = itemIdMapper.get(key);
            setExpanded(itemId, false);

            needRepaint = true;
        }

        if (variables.containsKey("collapse"))
        {
            String key = (String) variables.get("collapse");
            Object itemId = itemIdMapper.get(key);
            setCollapsed(itemId, false);

            needRepaint = true;
        }

        if (needRepaint) requestRepaint();
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException
    {
        Object[] colIds = getVisibleColumns();
        int cols = colIds.length;

        target.addAttribute("cols", cols);
        target.addAttribute("rows", size());

        if (isSelectable()) {
            target.addAttribute("selectmode", (isMultiSelect() ? "multi"
                    : "single"));
        } else {
            target.addAttribute("selectmode", "none");
        }

        paintColumns(target);
        
        paintRows(target);
    }

    protected void paintColumns(PaintTarget target) throws PaintException {
        target.startTag("visiblecolumns");
        for (final Object columnId : visibleColumns) {
            if (columnId != null) {
                target.startTag("column");
                target.addAttribute("cid", String.valueOf(columnId));
                final String head = getColumnHeader(columnId);
                target.addAttribute("caption", (head != null ? head : ""));

                target.endTag("column");
            }
        }
        target.endTag("visiblecolumns");
    }

    protected void paintRows(PaintTarget target) throws PaintException {
        target.startTag("rows");

        final Stack<Iterator> iteratorStack = new Stack<Iterator>();

        final Collection rootIds = rootItemIds();
        if (rootIds != null) {
            iteratorStack.push(rootIds.iterator());
        }

        int level = 0;

        while (!iteratorStack.isEmpty())
        {
            final Iterator it = iteratorStack.peek();

            if (!it.hasNext())
            {
                iteratorStack.pop();

                --level;

                if (!iteratorStack.isEmpty())
                {
                    target.endTag("gr");
                }

            } else {

                Object itemId = it.next();

                boolean allowChildren = areChildrenAllowed(itemId);
                if (allowChildren) {
                    target.startTag("gr");
                } else {
                    target.startTag("tr");
                }

                final String key = itemIdMapper.key(itemId);
                target.addAttribute("key", key);
                target.addAttribute("level", level);

                if (allowChildren) {
                    target.addAttribute("expanded", expanded.contains(itemId));
                }

                if (hasCaption(itemId))
                {
                    target.addAttribute("caption", getCaption(itemId));
                } 
                else {
                    if (allowChildren) {
                        target.startTag("c");
                    }

                    for (final Object colId : visibleColumns) {
                        if (colId == null) {
                            continue;
                        }
                        Object value = "";
                        final Property p = getContainerProperty(itemId, colId);
                        if (p != null) {
                            value = p.getValue();
                        }

                        target.addText((String) value);
                    }

                    if (allowChildren) {
                        target.endTag("c");
                    }
                }


                if (allowChildren && hasChildren(itemId) && expanded.contains(itemId))
                {
                    level++;

                    iteratorStack.push(getChildren(itemId).iterator());
                }
                else {
                    if (allowChildren) {
                        target.endTag("gr");
                    } else {
                        target.endTag("tr");
                    }
                }
            }
        }

        target.endTag("rows");
    }

    private void handleClickEvent(Map variables) {
//        if (clickListenerCount > 0) {
//            if (variables.containsKey("clickEvent")) {
//                String key = (String) variables.get("clickedKey");
//                Object itemId = itemIdMapper.get(key);
//                Object propertyId = null;
//                String colkey = (String) variables.get("clickedColKey");
//                // click is not necessary on a property
//                if (colkey != null) {
//                    propertyId = columnIdMap.get(colkey);
//                }
//                MouseEventDetails evt = MouseEventDetails
//                        .deSerialize((String) variables.get("clickEvent"));
//                fireEvent(new ItemClickEvent(this, getItem(itemId), itemId,
//                        propertyId, evt));
//            }
//        }
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

    public boolean setParent(Object itemId, Object newParentId)
            throws UnsupportedOperationException
    {
        boolean success = ((Hierarchical) items).setParent(itemId, newParentId);
        if (success) {
            requestRepaint();
        }
        return success;
    }

    public boolean areChildrenAllowed(Object itemId) {
        return ((Hierarchical) items).areChildrenAllowed(itemId);
    }

    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed)
            throws UnsupportedOperationException
    {
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

    public boolean hasCaption(Object itemId) {
        return items instanceof TreeTableContainer
                && ((TreeTableContainer) items).hasCaption(itemId);
    }

    public String getCaption(Object itemId) {
        if (!(items instanceof TreeTableContainer)) {
            throw new IllegalStateException("The data source container is not an instance of TreeTableContainer");
        }
        return ((TreeTableContainer) items).getCaption(itemId);
    }

    public void setCaption(Object itemId, String caption) {
        if (!(items instanceof TreeTableContainer)) {
            throw new IllegalStateException("The data source container is not an instance of TreeTableContainer");
        }
        ((TreeTableContainer) items).setCaption(itemId, caption);
    }

    public void setVisibleColumns(Object[] columns) {
        setVisibleColumns(columns, true);
    }

    private void setVisibleColumns(Object[] columns, boolean rerender) {
        if (columns == null) {
            throw new NullPointerException();
        }
        visibleColumns.clear();

        visibleColumns.addAll(Arrays.asList(columns));

        if (rerender) requestRepaint();
    }

    public Object[] getVisibleColumns() {
        if (visibleColumns == null) {
            return new Object[0];
        }
        return visibleColumns.toArray();
    }

    public void setColumnHeader(Object propertyId, String header) {
        setColumnHeader(propertyId, header, true);
    }

    private void setColumnHeader(Object propertyId, String header, boolean rerender) {
        if (header == null) {
            columnHeaders.remove(propertyId);
            return;
        }
        columnHeaders.put(propertyId, header);

        if (rerender) requestRepaint();
    }

    public String getColumnHeader(Object propertyId) {
        String header = columnHeaders.get(propertyId);
        if (header == null) {
            header = String.valueOf(propertyId);
        }
        return header;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public boolean isExpanded(Object itemId) {
        return expanded.contains(itemId);
    }

    public void setExpanded(Object itemId) {
        setExpanded(itemId, true);
    }

    protected void setExpanded(Object itemId, boolean rerender) {
        if (!isExpanded(itemId)) {
            expanded.add(itemId);
            if (rerender) requestRepaint();
        }
    }

    public void setCollapsed(Object itemId) {
        setCollapsed(itemId, true);
    }

    protected void setCollapsed(Object itemId, boolean rerender) {
        if (isExpanded(itemId)) {
            expanded.remove(itemId);
            if (rerender)requestRepaint();
        }
    }

    public String getTag()
    {
        return TAG_NAME;
    }
}
