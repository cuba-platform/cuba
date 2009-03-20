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

import com.itmill.toolkit.ui.AbstractComponent;
import com.itmill.toolkit.ui.AbstractSelect;
import com.itmill.toolkit.data.Container;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.terminal.PaintTarget;
import com.itmill.toolkit.terminal.PaintException;
import com.haulmont.cuba.web.toolkit.data.TreeTableContainer;

import java.util.*;

public class TreeTable extends AbstractComponent {

    public static final String TAG_NAME = "treetable";

    private final List<TableBody> tableBodies = new LinkedList<TableBody>();

    private final List visibleColumns = new LinkedList();
    private final Map<Object, String> columnHeaders = new HashMap<Object, String>();

    public TreeTable() {
    }

    public TableBody addTableBody(Container dataSource)
    {
        TableBody tableBody = new TableBody(dataSource);
        tableBodies.add(tableBody);
        return tableBody;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException
    {
        Object[] colIds = getVisibleColumns();
        int cols = colIds.length;

        target.addAttribute("selectmode", "none");
        target.addAttribute("cols", cols);

        paintColumns(target);

        paintBodies(target);
    }

    @Override
    public void changeVariables(Object source, Map variables)
    {
        if (variables.containsKey("expand"))
        {
            String key = (String) variables.get("expand");
            final TableBody tableBody = getTableBody(getBodyKey(key));
            Object itemId = tableBody.getItemIdByKey(getRowKey(key));
            tableBody.setExpanded(itemId, false);
        }
        if (variables.containsKey("collapse")) 
        {
            String key = (String) variables.get("collapse");
            final TableBody tableBody = getTableBody(getBodyKey(key));
            Object itemId = tableBody.getItemIdByKey(getRowKey(key));
            tableBody.setCollapsed(itemId, false);
        }
        requestRepaint();
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

    protected void paintBodies(PaintTarget target) throws PaintException {
        target.startTag("tbodies");
        for (final TableBody tableBody : tableBodies) {
            tableBody.paintContent(target);
        }
        target.endTag("tbodies");
    }

    public class TableBody extends AbstractSelect
            implements TreeTableContainer
    {
        private final Set<Object> expanded = new HashSet<Object>();

        public TableBody(Container dataSource) {
            super(null, dataSource);
        }

        @Override
        public void paintContent(PaintTarget target) throws PaintException {
            target.startTag("tbody");

            final String bodyKey = String.valueOf(TreeTable.this.tableBodies.indexOf(this));

            target.addAttribute("key", bodyKey);
            if (getCaption() != null && getCaption().trim().length() > 0) {
                target.addAttribute("caption", getCaption());
            }
            target.addAttribute("rows", getContainerDataSource().size());

            final Stack<Iterator> iteratorStack = new Stack<Iterator>();

            final Collection rootIds = rootItemIds();
            if (rootIds != null) {
                iteratorStack.push(rootIds.iterator());
            }

//            int level = 0;

            while (!iteratorStack.isEmpty())
            {
                final Iterator it = iteratorStack.peek();

                if (!it.hasNext())
                {
                    iteratorStack.pop();

//                    --level;

                    if (!iteratorStack.isEmpty())
                    {
                        target.endTag("gr");
                    }

                } else {

                    Object itemId = it.next();

                    boolean allowChildren = areChildrenAllowed(itemId);
                    if (allowChildren)
                    {
                        target.startTag("gr");
                        if (isGroupCaption(itemId)) {
                            target.addAttribute("caption", getItemCaption(itemId));
                        }
                    }
                    else {
                        target.startTag("tr");
                    }

                    final String key = itemIdMapper.key(itemId);
                    target.addAttribute("key", bodyKey + ":"+ key);
//                    target.addAttribute("level", level);

                    if (allowChildren) {
                        target.addAttribute("expanded", expanded.contains(itemId));
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

                    if (hasChildren(itemId) && allowChildren && expanded.contains(itemId))
                    {
//                        level++;

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

            target.endTag("tbody");
        }

        @Override
        public void changeVariables(Object source, Map variables) {
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
                TreeTable.this.requestRepaint();
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
                TreeTable.this.requestRepaint();
            }
            return success;
        }

        public boolean isRoot(Object itemId) {
            return ((Hierarchical) items).isRoot(itemId);
        }

        public boolean hasChildren(Object itemId) {
            return ((Hierarchical) items).hasChildren(itemId);
        }

        @Override
        public void requestRepaint() {
            TreeTable.this.requestRepaint();
        }

        public boolean isGroupCaption(Object itemId) {
            return ((TreeTableContainer) items).isGroupCaption(itemId);
        }

        public String getGroupCaption(Object itemId) {
            return ((TreeTableContainer) items).getGroupCaption(itemId);
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

        Object getItemIdByKey(String key) {
            return itemIdMapper.get(key);
        }
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
    }

    public String getColumnHeader(Object propertyId) {
        String header = columnHeaders.get(propertyId);
        if (header == null) {
            header = String.valueOf(propertyId);
        }
        return header;
    }

    private TableBody getTableBody(int index)
    {
        if (tableBodies.isEmpty()) {
            throw new IllegalStateException("Table sections list is empty!");
        }
        if (index < 0 || index > tableBodies.size() - 1) {
            throw new IllegalArgumentException("Index out of bound of the list");
        }
        return tableBodies.get(index);
    }

    public String getTag()
    {
        return TAG_NAME;
    }

    private int getBodyKey(String clientKey) {
        int index;
        if (clientKey != null && (index = clientKey.indexOf(":")) != -1) {
            try {
                return Integer.parseInt(clientKey.substring(0, index));
            } catch (NumberFormatException e) {
                //ignore body
            }
        }
        throw new IllegalArgumentException(String.format("Illegal key: %s", clientKey));
    }

    private String getRowKey(String clientKey) {
        int index;
        if (clientKey != null && (index = clientKey.indexOf(":")) != -1) {
            return clientKey.substring(index + 1);
        }
        throw new IllegalArgumentException(String.format("Illegal key: %s", clientKey));
    }
}
