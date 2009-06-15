package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.itmill.toolkit.terminal.gwt.client.UIDL;
import com.itmill.toolkit.terminal.gwt.client.ui.IScrollTable;
import com.itmill.toolkit.terminal.gwt.client.ui.Table;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;

import java.util.Iterator;

/**
 * User: Nikolay Gorodnov
 * Date: 03.06.2009
 */
public class IScrollTreeTable
        extends IScrollTable
{
    @Override
    protected IScrollTableBody createBody() {
        return new IScrollTreeTableBody();
    }

    public class IScrollTreeTableBody extends IScrollTableBody
    {
        @Override
        public void renderRows(UIDL rowData, int firstIndex, int rows) {
            // FIXME REVIEW
            aligns = tHead.getColumnAlignments();
            final Iterator it = rowData.getChildIterator();
            if (firstIndex == lastRendered + 1) {
                while (it.hasNext()) {
                    final IScrollTableRow row = createRow((UIDL) it.next());
                    addRow(row);
                    lastRendered++;
                }
                fixSpacers();
            } else if (firstIndex + rows == firstRendered) {
                final IScrollTableRow[] rowArray = new IScrollTreeTableRow[rows];
                int i = rows;
                while (it.hasNext()) {
                    i--;
                    rowArray[i] = createRow((UIDL) it.next());
                }
                for (i = 0; i < rows; i++) {
                    addRowBeforeFirstRendered(rowArray[i]);
                    firstRendered--;
                }
            } else {
                // completely new set of rows
                while (lastRendered + 1 > firstRendered) {
                    unlinkRow(false);
                }
                final IScrollTableRow row = createRow((UIDL) it.next());
                firstRendered = firstIndex;
                lastRendered = firstIndex - 1;
                addRow(row);
                lastRendered++;
                setContainerHeight();
                fixSpacers();
                while (it.hasNext()) {
                    addRow(createRow((UIDL) it.next()));
                    lastRendered++;
                }
                fixSpacers();
            }
            // this may be a new set of rows due content change,
            // ensure we have proper cache rows
            int reactFirstRow = (int) (firstRowInViewPort - pageLength
                    * CACHE_REACT_RATE);
            int reactLastRow = (int) (firstRowInViewPort + pageLength + pageLength
                    * CACHE_REACT_RATE);
            if (reactFirstRow < 0) {
                reactFirstRow = 0;
            }
            if (reactLastRow > totalRows) {
                reactLastRow = totalRows - 1;
            }
            if (lastRendered < reactLastRow) {
                // get some cache rows below visible area
                rowRequestHandler.setReqFirstRow(lastRendered + 1);
                rowRequestHandler.setReqRows(reactLastRow - lastRendered - 1);
                rowRequestHandler.deferRowFetch(1);
            } else if (IScrollTreeTable.this.tBody.getFirstRendered() > reactFirstRow) {
                /*
                 * Branch for fetching cache above visible area.
                 *
                 * If cache needed for both before and after visible area, this
                 * will be rendered after-cache is reveived and rendered. So in
                 * some rare situations table may take two cache visits to
                 * server.
                 */
                rowRequestHandler.setReqFirstRow(reactFirstRow);
                rowRequestHandler.setReqRows(firstRendered - reactFirstRow);
                rowRequestHandler.deferRowFetch(1);
            }
        }

        @Override
        public void renderInitialRows(UIDL rowData, int firstIndex, int rows) {
            firstRendered = firstIndex;
            lastRendered = firstIndex + rows - 1;
            final Iterator it = rowData.getChildIterator();
            aligns = tHead.getColumnAlignments();
            while (it.hasNext()) {
                final IScrollTableRow row = new IScrollTreeTableRow((UIDL) it
                        .next(), aligns);
                addRow(row);
            }
            if (isAttached()) {
                fixSpacers();
            }
        }

        @Override
        protected IScrollTableRow createRow(UIDL uidl) {
            final IScrollTableRow row = new IScrollTreeTableRow(uidl, aligns);
            final int cells = DOM.getChildCount(row.getElement());
            for (int i = 0; i < cells; i++) {
                final Element cell = DOM.getChild(row.getElement(), i);
                final int w = IScrollTreeTable.this
                        .getColWidth(getColKeyByIndex(i));
                DOM.setStyleAttribute(DOM.getFirstChild(cell), "width",
                        (w - CELL_CONTENT_PADDING) + "px");
                DOM.setStyleAttribute(cell, "width", w + "px");
            }
            return row;
        }

        public class IScrollTreeTableRow extends IScrollTableBody.IScrollTableRow
        {
            private boolean hasChildren;
            private boolean expanded;

            public IScrollTreeTableRow(UIDL uidl, char[] aligns) {
                super(uidl, aligns);

                if (uidl.hasAttribute("children") && uidl.getIntAttribute("children") > 0) {
                    hasChildren = true;
                    if (uidl.hasAttribute("expanded")) {
                        expanded = true;
                    }
                }
            }

            @Override
            public void onBrowserEvent(Event event) {
                final Element tdOrTr = DOM.getParent(DOM.eventGetTarget(event));
                if (getElement() == tdOrTr
                        || getElement() == tdOrTr.getParentElement()) {
                    switch (DOM.eventGetType(event)) {
                    case Event.ONCLICK:
                        handleClickEvent(event);
                        if (selectMode > Table.SELECT_MODE_NONE) {
                            toggleSelection();
                            // Note: changing the immediateness of this might
                            // require changes to "clickEvent" immediateness
                            // also.
                            client.updateVariable(paintableId, "selected",
                                    selectedRowKeys.toArray(), immediate);
                        } else if (hasChildren) {         //todo
                            if (expanded) {
                                client.updateVariable(paintableId, "collapse", getKey(), true);
                            } else {
                                client.updateVariable(paintableId, "expand", getKey(), true);
                            }
                            DOM.eventCancelBubble(event, true);

//                            Window.alert(expanded ? "Collapse" : "Expand");
                        }
                        break;
                    case Event.ONDBLCLICK:
                        handleClickEvent(event);
                        break;
                    case Event.ONCONTEXTMENU:
                        showContextMenu(event);
                        break;
                    default:
                        break;
                    }
                }
            }
        }
    }

}
