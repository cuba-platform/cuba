/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 09.07.2009 17:19:11
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.data.Container;
import com.itmill.toolkit.ui.Component;
import com.itmill.toolkit.ui.Field;
import com.itmill.toolkit.terminal.PaintTarget;
import com.itmill.toolkit.terminal.PaintException;
import com.itmill.toolkit.terminal.Resource;
import com.itmill.toolkit.event.Action;

import java.util.*;

public class Table
        extends com.itmill.toolkit.ui.Table
{
    protected LinkedList<Object> editableColumns = null;

    public Object[] getEditableColumns() {
        if (editableColumns == null) {
            return null;
        }
        return editableColumns.toArray();
    }

    public void setEditableColumns(Object[] editableColumns) {
        if (editableColumns == null) {
            throw new NullPointerException("You cannot set null as editable columns");
        }

        if (this.editableColumns == null) {
            this.editableColumns = new LinkedList<Object>();
        } else {
            this.editableColumns.clear();
        }

        final Collection properties = getContainerPropertyIds();
        for (final Object editableColumn : editableColumns) {
            if (editableColumn == null) {
                throw new NullPointerException("Ids must be non-nulls");
            } else if (!properties.contains(editableColumn)
                    || columnGenerators.containsKey(editableColumn)) {
                throw new IllegalArgumentException(
                        "Ids must exist in the Container and it must be not a generated column, incorrect id: "
                                + editableColumn);
            }
            this.editableColumns.add(editableColumn);
        }

        resetPageBuffer();
        refreshRenderedCells();
    }

    @Override
    public void addGeneratedColumn(Object id, ColumnGenerator generatedColumn) {
        if (generatedColumn == null) {
            throw new IllegalArgumentException(
                    "Can not add null as a GeneratedColumn");
        }
        if (columnGenerators.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Can not add the same GeneratedColumn twice, id:" + id);
        } else {
            columnGenerators.put(id, generatedColumn);
            /*
             * add to visible column list unless already there (overriding
             * column from DS)
             */
            if (!visibleColumns.contains(id)) {
                visibleColumns.add(id);
            }

            if (editableColumns != null) {
                editableColumns.remove(id);
            }

            resetPageBuffer();
            refreshRenderedCells();
        }
    }

    @Override
    protected Object getPropertyValue(Object rowId, Object colId, Property property) {
        if (isColumnEditable(colId) && fieldFactory != null) {
            final Field f = fieldFactory.createField(getContainerDataSource(),
                    rowId, colId, this);
            if (f != null) {
                f.setPropertyDataSource(property);
                return f;
            }
        }

        return formatPropertyValue(rowId, colId, property);
    }

    protected boolean isColumnEditable(Object columnId) {
        return isEditable() &&
                editableColumns != null && editableColumns.contains(columnId);
    }

    @Override
    public boolean removeContainerProperty(Object propertyId)
            throws UnsupportedOperationException {
        if (editableColumns != null) {
            editableColumns.remove(propertyId);
        }
        return super.removeContainerProperty(propertyId);
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
            if (alwaysRecalculateColumnWidths) {
                // TODO experimental feature for now: tell the client to
                // recalculate column widths.
                // We'll only do this for paints that do not originate from
                // table scroll/cache requests (i.e when reqRowsToPaint<0)
                target.addAttribute("recalcWidths", true);
            }
        }

        if (!isNullSelectionAllowed() && getNullSelectionItemId() != null
                && containsId(getNullSelectionItemId())) {
            total--;
            rows--;
        }

        // selection support
        LinkedList selectedKeys = new LinkedList();
        if (isMultiSelect()) {
            // only paint selections that are currently visible in the client
            HashSet sel = new HashSet((Set) getValue());
            Collection vids = getVisibleItemIds();
            for (Iterator it = vids.iterator(); it.hasNext();) {
                Object id = it.next();
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

        if (!isNullSelectionAllowed()) {
            target.addAttribute("nullSelectionDisallowed", true);
        }

        // Visible column order
        final Collection sortables = getSortableContainerPropertyIds();
        final ArrayList visibleColOrder = new ArrayList();
        for (final Iterator it = visibleColumns.iterator(); it.hasNext();) {
            final Object columnId = it.next();
            if (!isColumnCollapsed(columnId)) {
                visibleColOrder.add(columnIdMap.key(columnId));
            }
        }
        target.addAttribute("vcolorder", visibleColOrder.toArray());

        // Rows
        final Set actionSet = new LinkedHashSet();
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

            // Actions
            if (actionHandlers != null) {
                final ArrayList keys = new ArrayList();
                for (final Iterator ahi = actionHandlers.iterator(); ahi
                        .hasNext();) {
                    final Action[] aa = ((Action.Handler) ahi.next())
                            .getActions(itemId, this);
                    if (aa != null) {
                        for (int ai = 0; ai < aa.length; ai++) {
                            final String key = actionMapper.key(aa[ai]);
                            actionSet.add(aa[ai]);
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

            target.endTag("tr");
        }
        target.endTag("rows");

        // The select variable is only enabled if selectable
        if (selectable && selectedKeys.size() > 0) {
            target.addVariable(this, "selected", (String[]) selectedKeys
                    .toArray(new String[selectedKeys.size()]));
        }

        // The cursors are only shown on pageable table
        if (first != 0 || getPageLength() > 0) {
            target.addVariable(this, "firstvisible", first);
        }

        // Sorting
        if (getContainerDataSource() instanceof Container.Sortable) {
            target.addVariable(this, "sortcolumn", columnIdMap
                    .key(getSortContainerPropertyId()));
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
        if (!actionSet.isEmpty()) {
            target.addVariable(this, "action", "");
            target.startTag("actions");
            for (final Iterator it = actionSet.iterator(); it.hasNext();) {
                final Action a = (Action) it.next();
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
        if (isColumnReorderingAllowed()) {
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
            final HashSet ccs = new HashSet();
            for (final Iterator i = visibleColumns.iterator(); i.hasNext();) {
                final Object o = i.next();
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
        int i = 0;
        for (final Iterator it = visibleColumns.iterator(); it.hasNext(); i++) {
            final Object columnId = it.next();
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

}
