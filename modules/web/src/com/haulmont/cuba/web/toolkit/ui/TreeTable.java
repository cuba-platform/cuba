package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.toolkit.gwt.client.ui.IScrollTreeTable;
import com.haulmont.cuba.web.toolkit.data.AggregationContainer;
import com.haulmont.cuba.web.toolkit.data.TreeTableContainer;
import com.haulmont.cuba.web.toolkit.data.util.TreeTableContainerWrapper;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.Action;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Component;

import java.util.*;

/**
 * User: Nikolay Gorodnov
 * Date: 03.06.2009
 */

@SuppressWarnings("serial")
@ClientWidget(IScrollTreeTable.class)
public class TreeTable extends Table implements Container.Hierarchical, TreeTableContainer {

    /**
     * List of registered {@link TreeWillExpandListener} listeners
     */
    private List<TreeWillExpandListener> treeWillExpandListeners;

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
            int rows, totalRows;
            rows = totalRows = size();
            int pagelen;
            if (allowMultiStringCells) {
                pagelen = totalRows;
            } else {
                pagelen = getPageLength();
            }

            int firstIndex = pagingMode == PagingMode.PAGE
                    ? currentPageFirstItemIndex() : getCurrentPageFirstItemIndex();

            if (rows > 0 && firstIndex >= 0) {
                rows -= firstIndex;
            }

            if (pagelen > 0 && pagelen < rows) {
                rows = pagelen;
            }

            if (pagingMode == PagingMode.SCROLLING) {
                // If "to be painted next" variables are set, use them
                if (lastToBeRenderedInClient - firstToBeRenderedInClient > 0) {
                    rows = lastToBeRenderedInClient - firstToBeRenderedInClient + 1;
                }
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

            if (pagingMode == PagingMode.PAGE) {
                pagesCount = totalRows % pagelen == 0
                        ? totalRows / pagelen
                        : totalRows / pagelen + 1;
                if (currentPage > pagesCount) currentPage = pagesCount;
            }

            Object id;
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
                        .isCaption(id)) {
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
                                    ColumnGenerator cg = columnGenerators.get(colids[j]);
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
                            visibleComponents.add((Component) value);
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

    @Override
    protected boolean changeVariables(Map<String, Object> variables) {
        boolean clientNeedsContentRefresh = super.changeVariables(variables);

        boolean needsResetPageBuffer = false;

        //expand selected row
        if (variables.containsKey("expand")) {
            String key = (String) variables.get("expand");
            Object itemId = itemIdMapper.get(key);
            setExpanded(itemId, false);

            needsResetPageBuffer = true;
            clientNeedsContentRefresh = true;
        }

        //collapse selected row
        if (variables.containsKey("collapse")) {
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

        if (getDragMode() != TableDragMode.NONE) {
            target.addAttribute("dragmode", getDragMode().ordinal());
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

        if (isTextSelectionEnabled()) {
            target.addAttribute("textSelection", true);
        }

        target.addAttribute("cols", cols);
        target.addAttribute("rows", rows);

        target.addAttribute("firstrow",
                (reqFirstRowToPaint >= 0 ? reqFirstRowToPaint
                        : firstToBeRenderedInClient));
        target.addAttribute("totalrows", total);
        if (pagelen > 0 && !allowMultiStringCells) {
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

        if (isStoreColWidth()) {
            target.addAttribute("storeColWidth", true);
        }

        if (pagingMode == PagingMode.PAGE) {
            target.addAttribute("pagescount", pagesCount);
            target.addAttribute("curpage", currentPage);
            paintPaging(target);
        }

        target.addAttribute("pagingMode", pagingMode.name());

        // Visible column order
        final List<Object> visibleColOrder = new ArrayList<Object>(visibleColumns.size());
        for (final Object columnId : visibleColumns) {
            if (!isColumnCollapsed(columnId)) {
                visibleColOrder.add(columnIdMap.key(columnId));
            }
        }
        target.addAttribute("vcolorder", visibleColOrder.toArray());

//        paintActionButtons(target);

        if (items instanceof AggregationContainer && isAggregatable()
                && !((AggregationContainer) items).getAggregationPropertyIds().isEmpty()) {
            paintAggregationRow(target, ((AggregationContainer) items).aggregate(new Context(items.getItemIds())));
        }

        // Rows
        final Set<Action> actions = new LinkedHashSet<Action>();
        final boolean selectable = isSelectable();
        final boolean[] iscomponent = new boolean[visibleColumns.size()];
        int iscomponentIndex = 0;
        for (final Iterator it = visibleColumns.iterator(); it.hasNext()
                && iscomponentIndex < iscomponent.length; ) {
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
        // trying to fix ArrayIndexOutOfBoundsException
        if (end > cells[CELL_ITEMID].length) {
            end = cells[CELL_ITEMID].length;
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
                target.addAttribute("children", getChildrenCount(itemId));
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
            paintRowActions(target, actions, itemId);

            paintCellStyleGenerator(target, itemId);

            if (!isCaption) {
                // cells
                int currentColumn = 0;
                for (final Iterator it = visibleColumns.iterator(); it.hasNext(); currentColumn++) {
                    final Object columnId = it.next();
                    paintCell(target, itemId, columnId, cells[CELL_FIRSTCOL + currentColumn][i],
                            iscomponent[currentColumn]);
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

        if (isEnableCancelSorting()) {
            target.addVariable(this, "enableCancelSorting", true);
        }

        // Resets and paints "to be painted next" variables. Also reset
        // pageBuffer
        reqFirstRowToPaint = -1;
        reqRowsToPaint = -1;
        containerChangeToBeRendered = false;
        target.addVariable(this, "reqrows", reqRowsToPaint);
        target.addVariable(this, "reqfirstrow", reqFirstRowToPaint);

        // Actions
        paintActions(target, actions);

        if (isColumnCollapsingAllowed()) {
            final String[] colorder = new String[visibleColumns.size()];
            int i = 0;
            for (final Iterator it = visibleColumns.iterator(); it.hasNext()
                    && i < colorder.length; ) {
                colorder[i++] = columnIdMap.key(it.next());
            }
            target.addVariable(this, "columnorder", colorder);
        }

        paintCollapsedColumns(target);

        paintVisibleColumns(target, colheads);

        if (getDropHandler() != null) {
            getDropHandler().getAcceptCriterion().paint(target);
        }
    }

    protected Set<Object> getItemIdsInRange(Object startItemId, final int length) {
        Set<Object> rootIds = super.getItemIdsInRange(startItemId, length);
        Set<Object> ids = new HashSet<Object>(rootIds);
        for (Object itemId : rootIds) {

            if (!isExpanded(itemId) && hasChildren(itemId)) {
                Collection<?> itemIds = getChildren(itemId);
                ids.addAll(itemIds);

                for (Object childItemId : itemIds) {
                    if (!isExpanded(childItemId) && hasChildren(childItemId)) {
                        setExpanded(childItemId, true);
                    }
                }

                setExpanded(itemId, true);
            }
        }
        return ids;
    }

    protected int getChildrenCount(Object itemId) {
        return getChildren(itemId).size();
    }

    @Override
    public Collection getChildren(Object itemId) {
        return ((Hierarchical) items).getChildren(itemId);
    }

    @Override
    public Object getParent(Object itemId) {
        return ((Hierarchical) items).getParent(itemId);
    }

    @Override
    public Collection rootItemIds() {
        return ((Hierarchical) items).rootItemIds();
    }

    @Override
    public boolean setParent(Object itemId, Object newParentId)
            throws UnsupportedOperationException {
        return ((Hierarchical) items).setParent(itemId, newParentId);
    }

    @Override
    public boolean areChildrenAllowed(Object itemId) {
        return ((Hierarchical) items).areChildrenAllowed(itemId);
    }

    @Override
    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed)
            throws UnsupportedOperationException {
        return ((Hierarchical) items).setChildrenAllowed(itemId, areChildrenAllowed);
    }

    @Override
    public boolean isRoot(Object itemId) {
        return ((Hierarchical) items).isRoot(itemId);
    }

    @Override
    public boolean hasChildren(Object itemId) {
        return ((Hierarchical) items).hasChildren(itemId);
    }

    @Override
    public boolean isCaption(Object itemId) {
        return items instanceof TreeTableContainer
                && ((TreeTableContainer) items).isCaption(itemId);
    }

    @Override
    public String getCaption(Object itemId) {
        return ((TreeTableContainer) items).getCaption(itemId);
    }

    @Override
    public boolean setCaption(Object itemId, String caption) {
        return ((TreeTableContainer) items).setCaption(itemId, caption);
    }

    @Override
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
            // fire the event
            fireTreeWillExpandEvent(itemId, true);

            ((TreeTableContainerWrapper) items).setExpanded(itemId);
            if (rerender) {
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
            // fire the event
            fireTreeWillExpandEvent(itemId, false);

            ((TreeTableContainerWrapper) items).setCollapsed(itemId);
            if (rerender) {
                resetPageBuffer();
                refreshRenderedCells();
                requestRepaint();
            }
        }
    }

    /**
     * register a TreeWillExpandListener
     */
    public void addTreeWillExpandListener(TreeWillExpandListener listener) {
        if(treeWillExpandListeners == null)
            treeWillExpandListeners = new ArrayList<TreeWillExpandListener>();

        treeWillExpandListeners.add(listener);
    }

    /**
     * unregister a TreeWillExpandListener
     */
    public void removeTreeWillExpandListener(TreeWillExpandListener listener) {
        if(treeWillExpandListeners != null) {
            treeWillExpandListeners.remove(listener);

            if(treeWillExpandListeners.isEmpty())
                treeWillExpandListeners = null;
        }
    }

    /**
     * fire TreeWillExpand event for the listeners
     */
    private void fireTreeWillExpandEvent(Object itemId, boolean expand) {
        if(treeWillExpandListeners != null)
            for(TreeWillExpandListener listener : treeWillExpandListeners)
                listener.treeWillExpand(itemId, expand);
    }

    /**
     * Listeners that are invoked when a tree item will
     * be expanded or collapsed
     */
    public interface TreeWillExpandListener {

        /**
         * @param itemId tree item id
         * @param expand if <code>true</code> expand, <code>false</code> collapse
         */
        public void treeWillExpand(Object itemId, boolean expand);
    }
}
