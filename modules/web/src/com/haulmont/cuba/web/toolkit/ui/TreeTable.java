package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.data.TreeTableContainer;
import com.haulmont.cuba.web.toolkit.data.util.TreeTableContainerWrapper;
import com.itmill.toolkit.data.Container;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.data.util.HierarchicalContainer;
import com.itmill.toolkit.event.Action;
import com.itmill.toolkit.terminal.PaintException;
import com.itmill.toolkit.terminal.PaintTarget;
import com.itmill.toolkit.terminal.Resource;
import com.itmill.toolkit.ui.Component;

import java.util.*;

/**
 * User: Nikolay Gorodnov
 * Date: 03.06.2009
 */
public class TreeTable
        extends Table
        implements
        Container.Hierarchical,
        TreeTableContainer
{
    public TreeTable() {
        setRowHeaderMode(ROW_HEADER_MODE_HIDDEN);
    }

    public TreeTable(String caption) {
        this();
        setCaption(caption);
    }

    public TreeTable(String caption, Container dataSource) {
        this();
        setCaption(caption);
        setContainerDataSource(dataSource);
    }

    @Override
    protected void refreshRenderedCells() {
        if (getParent() == null) {
            return;
        }

        if (isContentRefreshesEnabled) {

            HashSet oldListenedProperties = listenedProperties;
            HashSet oldVisibleComponents = visibleComponents;

            // initialize the listener collections
            listenedProperties = new HashSet();
            visibleComponents = new HashSet();

            // Collects the basic facts about the table page
            final Object[] colids = getVisibleColumns();
            final int cols = colids.length;
            int firstIndex = getCurrentPageFirstItemIndex();
            int rows, totalRows;
            rows = totalRows = size();
            int pagelen;
            if (allowMultiStringCells) {
                pagelen = totalRows;
            } else {
                pagelen = getPageLength();
            }
            if (rows > 0 && firstIndex >= 0) {
                rows -= firstIndex;
            }
            if (pagelen > 0 && pagelen < rows) {
                rows = pagelen;
            }

            // If "to be painted next" variables are set, use them
            if (lastToBeRenderedInClient - firstToBeRenderedInClient > 0) {
                rows = lastToBeRenderedInClient - firstToBeRenderedInClient + 1;
            }
            Object id;
            if (firstToBeRenderedInClient >= 0) {
                if (firstToBeRenderedInClient < totalRows) {
                    firstIndex = firstToBeRenderedInClient;
                } else {
                    firstIndex = totalRows - 1;
                }
            } else {
                // initial load
                firstToBeRenderedInClient = firstIndex;
            }
            if (totalRows > 0) {
                if (rows + firstIndex > totalRows) {
                    rows = totalRows - firstIndex;
                }
            } else {
                rows = 0;
            }

            if (rows < 0) {
                rows = 0;
            }

            Object[][] cells = new Object[cols + CELL_FIRSTCOL][rows];
            if (rows == 0) {
                pageBuffer = cells;
                unregisterPropertiesAndComponents(oldListenedProperties,
                        oldVisibleComponents);
                return;
            }

            // Gets the first item id
            if (items instanceof Container.Indexed) {
                id = ((Container.Indexed) items).getIdByIndex(firstIndex);
            } else {
                id = ((Container.Ordered) items).firstItemId();
                for (int i = 0; i < firstIndex; i++) {
                    id = ((Container.Ordered) items).nextItemId(id);
                }
            }

            final int headmode = getRowHeaderMode();
            final boolean[] iscomponent = new boolean[cols];
            for (int i = 0; i < cols; i++) {
                iscomponent[i] = columnGenerators.containsKey(colids[i])
                        || Component.class.isAssignableFrom(getType(colids[i]));
            }
            int firstIndexNotInCache;
            if (pageBuffer != null && pageBuffer[CELL_ITEMID].length > 0) {
                firstIndexNotInCache = pageBufferFirstIndex
                        + pageBuffer[CELL_ITEMID].length;
            } else {
                firstIndexNotInCache = -1;
            }

            // Creates the page contents
            int filledRows = 0;
            for (int i = 0; i < rows && id != null; i++) {
                cells[CELL_ITEMID][i] = id;
                cells[CELL_KEY][i] = itemIdMapper.key(id);
                if (headmode != ROW_HEADER_MODE_HIDDEN) {
                    switch (headmode) {
                    case ROW_HEADER_MODE_INDEX:
                        cells[CELL_HEADER][i] = String.valueOf(i + firstIndex
                                + 1);
                        break;
                    default:
                        cells[CELL_HEADER][i] = getItemCaption(id);
                    }
                    cells[CELL_ICON][i] = getItemIcon(id);
                }

                if (cols > 0 && !((TreeTableContainer) items)
                        .isCaption(id))
                {
                    for (int j = 0; j < cols; j++) {
                        if (isColumnCollapsed(colids[j])) {
                            continue;
                        }
                        Property p = null;
                        Object value = "";
                        boolean isGenerated = columnGenerators
                                .containsKey(colids[j]);

                        if (!isGenerated) {
                            p = getContainerProperty(id, colids[j]);
                        }

                        // check in current pageBuffer already has row
                        int index = firstIndex + i;
                        if (p != null || isGenerated) {
                            if (p instanceof Property.ValueChangeNotifier) {
                                if (oldListenedProperties == null
                                        || !oldListenedProperties.contains(p)) {
                                    ((Property.ValueChangeNotifier) p)
                                            .addListener(this);
                                }
                                listenedProperties.add(p);
                            }
                            if (index < firstIndexNotInCache
                                    && index >= pageBufferFirstIndex) {
                                // we have data already in our cache,
                                // recycle it instead of fetching it via
                                // getValue/getPropertyValue
                                int indexInOldBuffer = index
                                        - pageBufferFirstIndex;
                                value = pageBuffer[CELL_FIRSTCOL + j][indexInOldBuffer];
                            } else {
                                if (isGenerated) {
                                    ColumnGenerator cg = (ColumnGenerator) columnGenerators
                                            .get(colids[j]);
                                    value = cg
                                            .generateCell(this, id, colids[j]);

                                } else if (iscomponent[j]) {
                                    value = p.getValue();
                                } else if (p != null) {
                                    value = getPropertyValue(id, colids[j], p);
                                } else {
                                    value = getPropertyValue(id, colids[j],
                                            null);
                                }
                            }
                        }

                        if (value instanceof Component) {
                            if (oldVisibleComponents == null
                                    || !oldVisibleComponents.contains(value)) {
                                ((Component) value).setParent(this);
                            }
                            visibleComponents.add(value);
                        }
                        cells[CELL_FIRSTCOL + j][i] = value;
                    }
                }

                id = ((Container.Ordered) items).nextItemId(id);

                filledRows++;
            }

            // Assures that all the rows of the cell-buffer are valid
            if (filledRows != cells[0].length) {
                final Object[][] temp = new Object[cells.length][filledRows];
                for (int i = 0; i < cells.length; i++) {
                    System.arraycopy(cells[i], 0, temp[i], 0, filledRows);
                }
                cells = temp;
            }

            pageBufferFirstIndex = firstIndex;

            // Saves the results to internal buffer
            pageBuffer = cells;

            unregisterPropertiesAndComponents(oldListenedProperties,
                    oldVisibleComponents);

            requestRepaint();
        }
    }

    @Override
    public void setContainerDataSource(Container newDataSource) {

        disableContentRefreshing();

        if (newDataSource == null) {
            newDataSource = new HierarchicalContainer();
        }

        super.setContainerDataSource(
                new TreeTableContainerWrapper(newDataSource));

        initComponent();

        enableContentRefreshing(true);

    }

/*
    @Override
    public void changeVariables(Object source, Map variables) {

        boolean clientNeedsContentRefresh = false;

        boolean needsResetPageBuffer = false;

        handleClickEvent(variables);

        disableContentRefreshing();

        if (!isSelectable() && variables.containsKey("selected")) {
            // Not-selectable is a special case, AbstractSelect does not support
            // TODO could be optimized.
            variables = new HashMap(variables);
            variables.remove("selected");
        }

        super.changeVariables(source, variables);

        // Page start index
        if (variables.containsKey("firstvisible")) {
            final Integer value = (Integer) variables.get("firstvisible");
            if (value != null) {
                setCurrentPageFirstItemIndex(value.intValue(), false);
            }
        }

        // Sets requested firstrow and rows for the next paint
        if (variables.containsKey("reqfirstrow")
                || variables.containsKey("reqrows")) {

            try {
                firstToBeRenderedInClient = ((Integer) variables
                        .get("firstToBeRendered")).intValue();
                lastToBeRenderedInClient = ((Integer) variables
                        .get("lastToBeRendered")).intValue();
            } catch (Exception e) {
                // FIXME: Handle exception
                e.printStackTrace();
            }

            // respect suggested rows only if table is not otherwise updated
            // (row caches emptied by other event)
            if (!containerChangeToBeRendered) {
                Integer value = (Integer) variables.get("reqfirstrow");
                if (value != null) {
                    reqFirstRowToPaint = value.intValue();
                }
                value = (Integer) variables.get("reqrows");
                if (value != null) {
                    reqRowsToPaint = value.intValue();
                    // sanity check
                    if (reqFirstRowToPaint + reqRowsToPaint > size()) {
                        reqRowsToPaint = size() - reqFirstRowToPaint;
                    }
                }
            }
            clientNeedsContentRefresh = true;
        }

        // Actions
        if (variables.containsKey("action")) {
            final StringTokenizer st = new StringTokenizer((String) variables
                    .get("action"), ",");
            if (st.countTokens() == 2) {
                final Object itemId = itemIdMapper.get(st.nextToken());
                final Action action = (Action) actionMapper.get(st.nextToken());
                if (action != null && containsId(itemId)
                        && actionHandlers != null) {
                    for (final Iterator i = actionHandlers.iterator(); i
                            .hasNext();) {
                        ((Action.Handler) i.next()).handleAction(action, this,
                                itemId);
                    }
                }
            }
        }

        if (!sortDisabled) {
            // Sorting
            boolean doSort = false;
            if (variables.containsKey("sortcolumn")) {
                final String colId = (String) variables.get("sortcolumn");
                if (colId != null && !"".equals(colId) && !"null".equals(colId)) {
                    final Object id = columnIdMap.get(colId);
                    setSortContainerPropertyId(id, false);
                    doSort = true;
                }
            }
            if (variables.containsKey("sortascending")) {
                final boolean state = ((Boolean) variables.get("sortascending"))
                        .booleanValue();
                if (state != sortAscending) {
                    setSortAscending(state, false);
                    doSort = true;
                }
            }
            if (doSort) {
                this.sort();
                resetPageBuffer();
            }
        }

        // Dynamic column hide/show and order
        // Update visible columns
        if (isColumnCollapsingAllowed()) {
            if (variables.containsKey("collapsedcolumns")) {
                try {
                    final Object[] ids = (Object[]) variables
                            .get("collapsedcolumns");
                    for (final Iterator it = visibleColumns.iterator(); it
                            .hasNext();) {
                        setColumnCollapsed(it.next(), false);
                    }
                    for (int i = 0; i < ids.length; i++) {
                        setColumnCollapsed(columnIdMap.get(ids[i].toString()),
                                true);
                    }
                } catch (final Exception e) {
                    // FIXME: Handle exception
                    e.printStackTrace();
                }
                clientNeedsContentRefresh = true;
            }
        }
        if (isColumnReorderingAllowed()) {
            if (variables.containsKey("columnorder")) {
                try {
                    final Object[] ids = (Object[]) variables
                            .get("columnorder");
                    final Object[] ordered = new Object[ids.length];
                    for (int i = 0; i < ids.length; i++) {
                        ordered[i] = columnIdMap.get(ids[i].toString());
                    }
                    setColumnOrder(ordered);
                } catch (final Exception e) {
                    // FIXME: Handle exception
                    e.printStackTrace();

                }
                clientNeedsContentRefresh = true;
            }
        }

        if (needsResetPageBuffer) {
            resetPageBuffer();
        }

        enableContentRefreshing(clientNeedsContentRefresh);
    }

*/
    @Override
    protected boolean changeVariables(Map variables) {
        boolean clientNeedsContentRefresh = super.changeVariables(variables);

        boolean needsResetPageBuffer = false;

        //expand selected row
        if (variables.containsKey("expand"))
        {
            String key = (String) variables.get("expand");
            Object itemId = itemIdMapper.get(key);
            setExpanded(itemId, false);

            needsResetPageBuffer = true;
            clientNeedsContentRefresh = true;
        }

        //collapse selected row
        if (variables.containsKey("collapse"))
        {
            String key = (String) variables.get("collapse");
            Object itemId = itemIdMapper.get(key);
            setCollapsed(itemId, false);

            needsResetPageBuffer = true;
            clientNeedsContentRefresh = true;
        }

        if (needsResetPageBuffer) {
            resetPageBuffer();
        }

        return clientNeedsContentRefresh;
    }


    @Override
    public void paintContent(PaintTarget target) throws PaintException {

        // The tab ordering number
        if (getTabIndex() > 0) {
            target.addAttribute("tabindex", getTabIndex());
        }

        // Initialize temps
        final Object[] colids = getVisibleColumns();
        final int cols = colids.length;
        final int first = getCurrentPageFirstItemIndex();
        int total = size();
        final int pagelen = getPageLength();
        final int colHeadMode = getColumnHeaderMode();
        final boolean colheads = colHeadMode != COLUMN_HEADER_MODE_HIDDEN;
        final boolean rowheads = getRowHeaderMode() != ROW_HEADER_MODE_HIDDEN;
        final Object[][] cells = getVisibleCells();
        int rows;
        if (reqRowsToPaint >= 0) {
            rows = reqRowsToPaint;
        } else {
            rows = cells[0].length;
            if (alwaysRecalculateColumnWidths /*|| recalcWidth*/) {
                // TODO experimental feature for now: tell the client to
                // recalculate column widths.
                // We'll only do this for paints that do not originate from
                // table scroll/cache requests (i.e when reqRowsToPaint<0)
                target.addAttribute("recalcWidths", true);
//                recalcWidth = false;
            }
        }

        if (!isNullSelectionAllowed() && getNullSelectionItemId() != null
                && containsId(getNullSelectionItemId())) {
            total--;
            rows--;
        }

        // selection support
        final List<String> selectedKeys = new LinkedList<String>();
        if (isMultiSelect()) {
            // only paint selections that are currently visible in the client
            @SuppressWarnings("unchecked")
            final Set<Object> sel = new HashSet<Object>((Set) getValue());
            Collection vids = getVisibleItemIds();
            for (final Object id : vids) {
                if (sel.contains(id)) {
                    selectedKeys.add(itemIdMapper.key(id));
                }
            }
        } else {
            Object value = getValue();
            if (value == null) {
                value = getNullSelectionItemId();
            }
            if (value != null) {
                selectedKeys.add(itemIdMapper.key(value));
            }
        }

        // Table attributes
        if (isSelectable()) {
            target.addAttribute("selectmode", (isMultiSelect() ? "multi"
                    : "single"));
        } else {
            target.addAttribute("selectmode", "none");
        }

        if (clickListenerCount > 0) {
            target.addAttribute("listenClicks", true);
        }

        target.addAttribute("cols", cols);
        target.addAttribute("rows", rows);

        target.addAttribute("firstrow",
                (reqFirstRowToPaint >= 0 ? reqFirstRowToPaint
                        : firstToBeRenderedInClient));
        target.addAttribute("totalrows", total);
        if (pagelen != 0 && !allowMultiStringCells) {
            target.addAttribute("pagelength", pagelen);
        }
        if (colheads) {
            target.addAttribute("colheaders", true);
        }
        if (rowheads) {
            target.addAttribute("rowheaders", true);
        }
        if (allowMultiStringCells) {
            target.addAttribute("multistring", true);
        }

        if (isSelectable() && !isMultiSelect() && !isNullSelectionAllowed()) {
            target.addAttribute("nullSelectionDisallowed", true);
        }

        // Visible column order
        final Collection sortables = getSortableContainerPropertyIds();
        final List<Object> visibleColOrder =
                new ArrayList<Object>(visibleColumns.size());
        for (final Object columnId : visibleColumns) {
            if (!isColumnCollapsed(columnId)) {
                visibleColOrder.add(columnIdMap.key(columnId));
            }
        }
        target.addAttribute("vcolorder", visibleColOrder.toArray());

        // Rows
        final Set<Action> actions = new LinkedHashSet<Action>();
        final boolean selectable = isSelectable();
        final boolean[] iscomponent = new boolean[visibleColumns.size()];
        int iscomponentIndex = 0;
        for (final Iterator it = visibleColumns.iterator(); it.hasNext()
                && iscomponentIndex < iscomponent.length;) {
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
        // cells array contains all that are supposed to be visible on client,
        // but we'll start from the one requested by client
        int start = 0;
        if (reqFirstRowToPaint != -1 && firstToBeRenderedInClient != -1) {
            start = reqFirstRowToPaint - firstToBeRenderedInClient;
        }
        int end = cells[0].length;
        if (reqRowsToPaint != -1) {
            end = start + reqRowsToPaint;
        }
        // sanity check
        if (lastToBeRenderedInClient != -1 && lastToBeRenderedInClient < end) {
            end = lastToBeRenderedInClient + 1;
        }
        if (start > cells[CELL_ITEMID].length || start < 0) {
            start = 0;
        }

        for (int i = start; i < end; i++) {
            final Object itemId = cells[CELL_ITEMID][i];

            if (!isNullSelectionAllowed() && getNullSelectionItemId() != null
                    && itemId == getNullSelectionItemId()) {
                // Remove null selection item if null selection is not allowed
                continue;
            }

            target.startTag("tr");

            // tr attributes
            if (rowheads) {
                if (cells[CELL_ICON][i] != null) {
                    target.addAttribute("icon", (Resource) cells[CELL_ICON][i]);
                }
                if (cells[CELL_HEADER][i] != null) {
                    target.addAttribute("caption",
                            (String) cells[CELL_HEADER][i]);
                }
            }
            target.addAttribute("key", Integer.parseInt(cells[CELL_KEY][i]
                    .toString()));
            if (actionHandlers != null || isSelectable()) {
                if (isSelected(itemId)) {
                    target.addAttribute("selected", true);
                }
            }

            // add tree table attributes
            int level;
            if ((level = getLevel(itemId)) > -1) {
                target.addAttribute("level", level);
            }

            final boolean allowChildren = areChildrenAllowed(itemId);
            if (allowChildren && hasChildren(itemId)) {
                target.addAttribute("children", getChildren(itemId).size());
                if (isExpanded(itemId)) {
                    target.addAttribute("expanded", true);
                }
            }

            boolean isCaption = ((TreeTableContainer) items).isCaption(itemId);

            if (isCaption) {
                target.addAttribute("rowCaption",
                        ((TreeTableContainer) items).getCaption(itemId));
                //todo remove all data for that row
            }

            // Actions
            if (actionHandlers != null) {
                final List<String> keys = new LinkedList<String>();
                for (final Object actionHandler : actionHandlers) {
                    final Action[] aa = ((Action.Handler) actionHandler)
                            .getActions(itemId, this);
                    if (aa != null) {
                        for (final Action action : aa) {
                            final String key = actionMapper.key(action);
                            actions.add(action);
                            keys.add(key);
                        }
                    }
                }
                target.addAttribute("al", keys.toArray());
            }

            /*
             * For each row, if a cellStyleGenerator is specified, get the
             * specific style for the cell, using null as propertyId. If there
             * is any, add it to the target.
             */
            if (cellStyleGenerator != null) {
                String rowStyle = cellStyleGenerator.getStyle(itemId, null);
                if (rowStyle != null && !rowStyle.equals("")) {
                    target.addAttribute("rowstyle", rowStyle);
                }
            }

            if (!isCaption) {
                // cells
                int currentColumn = 0;
                for (final Iterator it = visibleColumns.iterator(); it.hasNext(); currentColumn++) {
                    final Object columnId = it.next();
                    if (columnId == null || isColumnCollapsed(columnId)) {
                        continue;
                    }
                    /*
                    * For each cell, if a cellStyleGenerator is specified, get the
                    * specific style for the cell. If there is any, add it to the
                    * target.
                    */
                    if (cellStyleGenerator != null) {
                        String cellStyle = cellStyleGenerator.getStyle(itemId,
                                columnId);
                        if (cellStyle != null && !cellStyle.equals("")) {
                            target.addAttribute("style-"
                                    + columnIdMap.key(columnId), cellStyle);
                        }
                    }
                    if ((iscomponent[currentColumn] || isColumnEditable(columnId))
                            && Component.class.isInstance(cells[CELL_FIRSTCOL
                            + currentColumn][i])) {
                        final Component c = (Component) cells[CELL_FIRSTCOL
                                + currentColumn][i];
                        if (c == null) {
                            target.addText("");
                        } else {
                            c.paint(target);
                        }
                    } else {
                        target
                                .addText((String) cells[CELL_FIRSTCOL
                                        + currentColumn][i]);
                    }
                }
            }

            target.endTag("tr");
        }
        target.endTag("rows");

        // The select variable is only enabled if selectable
        if (selectable && selectedKeys.size() > 0) {
            target.addVariable(this, "selected", selectedKeys.toArray(new String[selectedKeys.size()]));
        }

        // The cursors are only shown on pageable table
        if (first != 0 || getPageLength() > 0) {
            target.addVariable(this, "firstvisible", first);
        }

        // Sorting
        if (getContainerDataSource() instanceof Container.Sortable) {
            target.addVariable(this, "sortcolumn", columnIdMap
                    .key(sortContainerPropertyId));
            target.addVariable(this, "sortascending", isSortAscending());
        }

        // Resets and paints "to be painted next" variables. Also reset
        // pageBuffer
        reqFirstRowToPaint = -1;
        reqRowsToPaint = -1;
        containerChangeToBeRendered = false;
        target.addVariable(this, "reqrows", reqRowsToPaint);
        target.addVariable(this, "reqfirstrow", reqFirstRowToPaint);

        // Actions
        if (!actions.isEmpty()) {
            target.addVariable(this, "action", "");
            target.startTag("actions");
            for (final Object anActionSet : actions) {
                final Action a = (Action) anActionSet;
                target.startTag("action");
                if (a.getCaption() != null) {
                    target.addAttribute("caption", a.getCaption());
                }
                if (a.getIcon() != null) {
                    target.addAttribute("icon", a.getIcon());
                }
                target.addAttribute("key", actionMapper.key(a));
                target.endTag("action");
            }
            target.endTag("actions");
        }
        if (isColumnCollapsingAllowed()) {
            final String[] colorder = new String[visibleColumns.size()];
            int i = 0;
            for (final Iterator it = visibleColumns.iterator(); it.hasNext()
                    && i < colorder.length;) {
                colorder[i++] = columnIdMap.key(it.next());
            }
            target.addVariable(this, "columnorder", colorder);
        }
        // Available columns
        if (isColumnCollapsingAllowed()) {
            final HashSet<Object> ccs = new HashSet<Object>();
            for (final Object o : visibleColumns) {
                if (isColumnCollapsed(o)) {
                    ccs.add(o);
                }
            }
            final String[] collapsedkeys = new String[ccs.size()];
            int nextColumn = 0;
            for (final Iterator it = visibleColumns.iterator(); it.hasNext()
                    && nextColumn < collapsedkeys.length;) {
                final Object columnId = it.next();
                if (isColumnCollapsed(columnId)) {
                    collapsedkeys[nextColumn++] = columnIdMap.key(columnId);
                }
            }
            target.addVariable(this, "collapsedcolumns", collapsedkeys);
        }
        target.startTag("visiblecolumns");
        for (final Object columnId : visibleColumns) {
            if (columnId != null) {
                target.startTag("column");
                target.addAttribute("cid", columnIdMap.key(columnId));
                final String head = getColumnHeader(columnId);
                target.addAttribute("caption", (head != null ? head : ""));
                if (isColumnCollapsed(columnId)) {
                    target.addAttribute("collapsed", true);
                }
                if (colheads) {
                    if (getColumnIcon(columnId) != null) {
                        target.addAttribute("icon", getColumnIcon(columnId));
                    }
                    if (sortables.contains(columnId)) {
                        target.addAttribute("sortable", true);
                    }
                }
                if (!ALIGN_LEFT.equals(getColumnAlignment(columnId))) {
                    target.addAttribute("align", getColumnAlignment(columnId));
                }
                if (getColumnWidth(columnId) > -1) {
                    target.addAttribute("width", String
                            .valueOf(getColumnWidth(columnId)));
                }

                target.endTag("column");
            }
        }
        target.endTag("visiblecolumns");
    }

    @Override
    public String getTag() {
        return "treetable";
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
        return ((Hierarchical) items).setParent(itemId, newParentId);
    }

    public boolean areChildrenAllowed(Object itemId) {
        return ((Hierarchical) items).areChildrenAllowed(itemId);
    }

    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed)
            throws UnsupportedOperationException
    {
        return ((Hierarchical) items).setChildrenAllowed(itemId, areChildrenAllowed);
    }

    public boolean isRoot(Object itemId) {
        return ((Hierarchical) items).isRoot(itemId);
    }

    public boolean hasChildren(Object itemId) {
        return ((Hierarchical) items).hasChildren(itemId);
    }

    public boolean isCaption(Object itemId) {
        return items instanceof TreeTableContainer
                && ((TreeTableContainer) items).isCaption(itemId);
    }

    public String getCaption(Object itemId) {
        return ((TreeTableContainer) items).getCaption(itemId);
    }

    public boolean setCaption(Object itemId, String caption) {
        return ((TreeTableContainer) items).setCaption(itemId, caption);
    }

    public int getLevel(Object itemId) {
        return ((TreeTableContainer) items).getLevel(itemId);
    }

    public boolean isExpanded(Object itemId) {
        return ((TreeTableContainerWrapper) items).isExpanded(itemId);
    }

    public void setExpanded(Object itemId) {
        setExpanded(itemId, true);
    }

    protected void setExpanded(Object itemId, boolean rerender) {
        if (!isExpanded(itemId)) {
            ((TreeTableContainerWrapper) items).setExpanded(itemId);
            if (rerender)  {
                resetPageBuffer();
                refreshRenderedCells();
                requestRepaint();
            }
        }
    }

    public void expandAll() {
        ((TreeTableContainerWrapper) items).expandAll();
        resetPageBuffer();
        refreshRenderedCells();
        requestRepaint();
    }

    public void collapseAll() {
        if (((TreeTableContainerWrapper) items).isAllCollapsed()) {
            return;
        }
        ((TreeTableContainerWrapper) items).collapseAll();

        resetPageBuffer();
        currentPageFirstItemId = null;
        currentPageFirstItemIndex = 0;

        refreshRenderedCells();
        requestRepaint();
    }

    public void setCollapsed(Object itemId) {
        setCollapsed(itemId, true);
    }

    protected void setCollapsed(Object itemId, boolean rerender) {
        if (isExpanded(itemId)) {
            ((TreeTableContainerWrapper) items).setCollapsed(itemId);
            if (rerender) {
                resetPageBuffer();
                refreshRenderedCells();
                requestRepaint();
            }
        }
    }
}
