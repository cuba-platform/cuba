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
import com.itmill.toolkit.data.Item;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.data.util.ContainerHierarchicalWrapper;
import com.itmill.toolkit.data.util.IndexedContainer;
import com.itmill.toolkit.terminal.PaintException;
import com.itmill.toolkit.terminal.PaintTarget;
import com.itmill.toolkit.ui.*;

import java.util.*;

public class TreeTable
        extends TableSupport
        implements Container.Hierarchical, TreeTableContainer
{

    public static final String TAG_NAME = "treetable";

    private List<Object> visibleColumns = null;
    private final Map<Object, TableSupport.ColumnGenerator> columnGenerators = new LinkedHashMap<Object, TableSupport.ColumnGenerator>();
    private final Map<Object, String> columnHeaders = new HashMap<Object, String>();

    private Set<Component> visibleComponents = null;
    private Set<Property> listenedProperties = null;

    private final Set<Object> expanded = new HashSet<Object>();

    private boolean selectable = false;

    private boolean editable = false;

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

//        if (clickListenerCount > 0) {
        target.addAttribute("listenClicks", false);
//        }

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

        final Set<Component> oldVisibleComponents = visibleComponents;
        final Set<Property> oldListenedProperties = listenedProperties;

        visibleComponents = new HashSet<Component>();
        listenedProperties = new HashSet<Property>();

        final boolean[] iscomponent = new boolean[visibleColumns.size()];
        int iscomponentIndex = 0;
        for (final Iterator it = visibleColumns.iterator(); it.hasNext()
                && iscomponentIndex < iscomponent.length;)
        {
            final Object columnId = it.next();
            if (columnGenerators.containsKey(columnId)) {
                iscomponent[iscomponentIndex++] = true;
            } else {
                final Class colType = getType(columnId);
                iscomponent[iscomponentIndex++] = colType != null
                        && Component.class.isAssignableFrom(colType);
            }
        }
        
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
                    if (isSelected(itemId)) {
                        target.addAttribute("selected", true);
                    }

                    if (allowChildren) {
                        target.startTag("c");
                    }

                    int colIndex = 0;
                    for (final Object colId : visibleColumns) {
                        if (colId == null) {
                            continue;
                        }

                        Object value = "";
                        boolean isGenerated = columnGenerators.containsKey(colId);

                        Property p = null;
                        if (!isGenerated) {
                            p = getContainerProperty(itemId, colId);
                        }

                        if (p != null || isGenerated)
                        {
                            if (p instanceof Property.ValueChangeNotifier) {
                                if (oldListenedProperties == null
                                        || !oldListenedProperties.contains(p)) {
                                    ((Property.ValueChangeNotifier) p)
                                            .addListener(this);
                                }
                                listenedProperties.add(p);
                            }
                            if (isGenerated) {
                                final ColumnGenerator cg = columnGenerators.get(colId);
                                value = cg.generateCell(this, itemId, colId);
                            } else if (iscomponent[colIndex]) {
                                value = p.getValue();
                            } else if (p != null) {
                                value = getPropertyValue(itemId, colId, p);
                            } else {
                                value = getPropertyValue(itemId, colId, null);
                            }
                        }

                        if ((iscomponent[colIndex] || isEditable())
                                && Component.class.isInstance(value))
                        {
                            final Component c = (Component) value;
                            if (c == null) {
                                target.addText("");
                            } else {
                                c.paint(target);
                            }
                        } else {
                            target.addText((String) value);
                        }

                        if (value instanceof Component) {
                            if (oldVisibleComponents == null
                                    || !oldVisibleComponents.contains(value)) {
                                ((Component) value).setParent(this);
                            }
                            visibleComponents.add((Component) value);
                        }

                        colIndex++;
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

        unregisterComponentsAndProperties(
                oldVisibleComponents,
                oldListenedProperties
        );
    }

    protected Object getPropertyValue(Object rowId, Object colId,
            Property property) {
// todo uncomment when i will be impl editable cells       
//        if (isEditable() && fieldFactory != null) {
//            final Field f = fieldFactory.createField(getContainerDataSource(),
//                    rowId, colId, this);
//            if (f != null) {
//                f.setPropertyDataSource(property);
//                return f;
//            }
//        }

        return formatPropertyValue(rowId, colId, property);
    }

    protected String formatPropertyValue(Object rowId, Object colId,
            Property property) {
        if (property == null) {
            return "";
        }
        return property.toString();
    }

    private void unregisterComponentsAndProperties(
            Set<Component> oldVisibleComponents, Set<Property> oldListenedProperties) {
        if (oldVisibleComponents != null) {
            for (final Component c : oldVisibleComponents) {
                if (!visibleComponents.contains(c)) {
                    c.setParent(null);
                }
            }
        }

        if (oldListenedProperties != null) {
            for (final Property p : oldListenedProperties) {
                if (!listenedProperties.contains(p)
                        && (p instanceof ValueChangeNotifier))
                {
                   ((ValueChangeNotifier) p).removeListener(this);
                }
            }
        }
    }

    @Override
    public void setContainerDataSource(Container newDataSource) {
        setContainerDataSource(newDataSource, true);
    }

    private void setContainerDataSource(Container newDataSource, boolean rerender) {

        if (newDataSource == null) {
            newDataSource = new IndexedContainer();
        }

        // Assures that the data source is ordered by making unordered
        // containers ordered by wrapping them
        if (newDataSource instanceof Hierarchical) {
            super.setContainerDataSource(newDataSource);
        } else {
            super.setContainerDataSource(new ContainerHierarchicalWrapper(newDataSource));
        }
        //todo handle ordered and sortable containers

        // Resets column properties
//        if (collapsedColumns != null) {
//            collapsedColumns.clear();
//        }

        // columnGenerators 'override' properties, don't add the same id twice
        List<Object> col = new LinkedList<Object>();
        for (final Object id : getContainerPropertyIds())
        {
            if (columnGenerators == null || !columnGenerators.containsKey(id)) {
                col.add(id);
            }
        }
        // generators added last
        if (columnGenerators != null && columnGenerators.size() > 0) {
            col.addAll(columnGenerators.keySet());
        }

        setVisibleColumns(col.toArray(), false);

        // null value as we may not be sure that currently selected identifier
        // exits in new ds
        setValue(null);

        if (rerender) requestRepaint();
    }

    public Object addItem(Object[] cells, Object itemId)
            throws UnsupportedOperationException {

        // remove generated columns from the list of columns being assigned
        final LinkedList availableCols = new LinkedList();
        for (final Object id : visibleColumns) {
            if (!columnGenerators.containsKey(id)) {
                availableCols.add(id);
            }
        }
        // Checks that a correct number of cells are given
        if (cells.length != availableCols.size()) {
            return null;
        }

        // Creates new item
        Item item;
        if (itemId == null) {
            itemId = items.addItem();
            if (itemId == null) {
                return null;
            }
            item = items.getItem(itemId);
        } else {
            item = items.addItem(itemId);
        }
        if (item == null) {
            return null;
        }

        // Fills the item properties
        for (int i = 0; i < availableCols.size(); i++) {
            item.getItemProperty(availableCols.get(i)).setValue(cells[i]);
        }

        if (!(items instanceof Container.ItemSetChangeNotifier)) {
            requestRepaint();
        }

        return itemId;
    }


    private void handleClickEvent(Map variables) {
//        if (clickListenerCount > 0) {            //todo use it if it needed
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
        // Visible columns must exist
        if (columns == null) {
            throw new NullPointerException(
                    "Can not set visible columns to null value");
        }

        // Checks that the new visible columns contains no nulls and properties
        // exist
        final Collection properties = getContainerPropertyIds();
        for (final Object column : columns) {
            if (column == null) {
                throw new NullPointerException("Ids must be non-nulls");
            } else if (!properties.contains(column)
                    && columnGenerators != null && !columnGenerators.containsKey(column))
            {
                throw new IllegalArgumentException(
                        "Ids must exist in the Container or as a generated column , missing id: "
                                + column);
            }
        }

        // If this is called before the constructor is finished, it might be
        // uninitialized
        final LinkedList<Object> newVisibleColumns = new LinkedList<Object>();
        newVisibleColumns.addAll(Arrays.asList(columns));

        // Removes alignments, icons and headers from hidden columns
        if (visibleColumns != null) {
            for (final Object column : visibleColumns) {
                if (!newVisibleColumns.contains(column)) {
                    setColumnHeader(column, null, false);
//                            setColumnAlignment(col, null);
//                            setColumnIcon(col, null);
                }
            }

            visibleColumns.clear();
            visibleColumns.addAll(newVisibleColumns);

        } else {
            visibleColumns = newVisibleColumns;
        }


        if (rerender) requestRepaint();
    }

    public Object[] getVisibleColumns() {
        if (visibleColumns == null) {
            return new Object[0];
        }
        return visibleColumns.toArray();
    }

    public void addGeneratedColumn(Object columnId, TableSupport.ColumnGenerator columnGenerator) {
        addGeneratedColumn(columnId, columnGenerator, true);
    }

    private void addGeneratedColumn(Object columnId, TableSupport.ColumnGenerator columnGenerator, boolean rerender) {
        if (columnGenerator == null) {
            throw new IllegalArgumentException(
                    "Can not add null as a GeneratedColumn");
        }
        if (columnGenerators.containsKey(columnId)) {
            throw new IllegalArgumentException(
                    "Can not add the same GeneratedColumn twice, id:" + columnId);
        } else {
            columnGenerators.put(columnId, columnGenerator);
            /*
             * add to visible column list unless already there (overriding
             * column from DS)
             */
            if (!visibleColumns.contains(columnId)) {
                visibleColumns.add(columnId);
            }
            if (rerender) requestRepaint();
        }
    }

    public boolean removeGeneratedColumn(Object columnId) {
        return removeGeneratedColumn(columnId, true);
    }

    private boolean removeGeneratedColumn(Object columnId, boolean rerender) {
        if (columnGenerators.containsKey(columnId)) {
            columnGenerators.remove(columnId);
            if (!items.getContainerPropertyIds().contains(columnId)) {
                visibleColumns.remove(columnId);
            }
            if (rerender) requestRepaint();
            return true;
        } else {
            return false;
        }
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
        requestRepaint();
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        requestRepaint();
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
            if (rerender) requestRepaint();
        }
    }

    @Override
    public void attach() {
        super.attach();

        if (visibleComponents != null) {
            for (final Component component : visibleComponents) {
                component.attach();
            }
        }
    }

    @Override
    public void detach() {
        super.detach();

        if (visibleComponents != null) {
            for (final Component component : visibleComponents) {
                component.detach();
            }
        }
    }

    public String getTag()
    {
        return TAG_NAME;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (getParent() == null || getParent().isEnabled()) {
            requestRepaintAll();
        }
    }

    public void requestRepaintAll() {
        requestRepaint();
        if (visibleComponents != null) {
            for (final Component c : visibleComponents) {
                if (c instanceof Form) {
                    // Form has children in layout, but is not
                    // ComponentContainer
                    c.requestRepaint();
                    ((Form) c).getLayout().requestRepaintAll();
                } else if (c instanceof Table) {
                    ((Table) c).requestRepaintAll();
                } else if (c instanceof ComponentContainer) {
                    ((ComponentContainer) c).requestRepaintAll();
                } else {
                    c.requestRepaint();
                }
            }
        }
    }
}
