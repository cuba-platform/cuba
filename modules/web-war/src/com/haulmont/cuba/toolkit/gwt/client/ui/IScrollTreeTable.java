package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.itmill.toolkit.terminal.gwt.client.UIDL;
import com.itmill.toolkit.terminal.gwt.client.ui.IScrollTable;
import com.itmill.toolkit.terminal.gwt.client.ui.Table;

import java.util.Iterator;

/**
 * User: Nikolay Gorodnov
 * Date: 03.06.2009
 */
public class IScrollTreeTable
        extends IScrollTable {

    private static int LEVEL_STEP_SIZE = 19;

    @Override
    protected IScrollTableBody createBody() {
        return new IScrollTreeTableBody();
    }

    public class IScrollTreeTableBody extends IScrollTableBody {

        protected int groupColIndex =
                showRowHeaders ? 1 : 0;

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
                final IScrollTableRow row = createRowInstance((UIDL) it.next(),
                        aligns);
                addRow(row);
            }
            if (isAttached()) {
                fixSpacers();
            }
        }

        @Override
        protected void addRowBeforeFirstRendered(IScrollTableRow row) {
            super.addRowBeforeFirstRendered(row);
            if (((IScrollTreeTableRow) row).isExpanded()) {
                row.addStyleName("i-expanded");
            }
        }

        @Override
        protected void addRow(IScrollTableRow row) {
            super.addRow(row);
            if (((IScrollTreeTableRow) row).isExpanded()) {
                row.addStyleName("i-expanded");
            }
        }

        protected IScrollTreeTableRow createRowInstance(UIDL uidl, char[] aligns) {
            boolean isCaption = isCaptionRow(uidl);
            final IScrollTreeTableRow row;
            if (isCaption) {
                row = new IScrollTreeTableCaptionRow(uidl, aligns);
            } else {
                row = new IScrollTreeTableRow(uidl, aligns);
            }
            return row;
        }

        @Override
        protected IScrollTableRow createRow(UIDL uidl) {
            return createRowInstance(uidl, aligns);
        }

        @Override
        protected void applyAlternatingRowColor(IScrollTableRow row,
                    String style)
        {
            if (row instanceof IScrollTreeTableCaptionRow) {
                row.addStyleName(CLASSNAME + "-caption-row");
            } else {
                super.applyAlternatingRowColor(row, style);
            }
        }

        private boolean isCaptionRow(UIDL uidl) {
            return uidl.hasAttribute("rowCaption");
        }

        public class IScrollTreeTableCaptionRow extends IScrollTreeTableRow {
            public IScrollTreeTableCaptionRow(UIDL uidl, char[] aligns) {
                super(uidl, aligns);
            }

            @Override
            protected void addCells(UIDL uidl, int col) {
                int columnCount = IScrollTreeTable.this.tHead.getVisibleCellCount();

                final Element td = DOM.createTD();
                DOM.setElementAttribute(td, "colSpan", String.valueOf(columnCount));

                final Element container = DOM.createDiv();
                String className = CLASSNAME + "-caption-row-content";

                if (hasChildren) {
                    groupCell = createGroupContainer();
                    DOM.setStyleAttribute(groupCell, "marginLeft", getLevel() * LEVEL_STEP_SIZE
                            + "px");
                    DOM.appendChild(td, groupCell);
                    className += " " + CLASSNAME + "-float";
                    DOM.setElementProperty(container, "className", className);
                } else {
                    DOM.setStyleAttribute(container, "marginLeft", getLevel() * LEVEL_STEP_SIZE
                            + "px");
                }

                DOM.setElementProperty(container, "className", className);
                DOM.setInnerText(container, uidl.getStringAttribute("rowCaption"));
                DOM.appendChild(td, container);
                DOM.appendChild(getElement(), td);
            }
        }

        public class IScrollTreeTableRow
                extends IScrollTableBody.IScrollTableRow
        {
            protected boolean hasChildren;
            private boolean expanded;
            private int level;

            protected Element groupCell = null;

            public IScrollTreeTableRow(UIDL uidl, char[] aligns) {
                super(uidl.getIntAttribute("key"));

                String rowStyle = uidl.getStringAttribute("rowstyle");
                if (rowStyle != null) {
                    addStyleName(CLASSNAME + "-row-" + rowStyle);
                }

                tHead.getColumnAlignments();
                int col = 0;

                // row header
                if (showRowHeaders) {
                    addCell(buildCaptionHtmlSnippet(uidl), aligns[col], "", col,
                            true);
                    col++;
                }

                if (uidl.hasAttribute("al")) {
                    actionKeys = uidl.getStringArrayAttribute("al");
                }

                if (uidl.hasAttribute("children") && uidl.getIntAttribute("children") > 0) {
                    hasChildren = true;
                    if (uidl.hasAttribute("expanded")) {
                        expanded = true;
                    }
                }

                level = uidl.getIntAttribute("level");

                addCells(uidl, col);

                if (uidl.hasAttribute("selected") && !isSelected()) {
                    toggleSelection();
                }
            }

            @Override
            public void addCell(String text, char align, String style, int col, boolean textIsHTML) {
                // String only content is optimized by not using Label widget
                final Element td = DOM.createTD();
                final Element container = DOM.createDiv();
                String className = CLASSNAME + "-cell-content";
                String classNameExt = null;
                if (style != null && !style.equals("")) {
                    classNameExt = CLASSNAME + "-cell-content-" + style;
                }
                if (classNameExt != null) {
                    className += " " + classNameExt;
                    DOM.setElementProperty(td, "className", classNameExt + "-td");
                }

                DOM.setElementProperty(container, "className", className);
                if (textIsHTML) {
                    DOM.setInnerHTML(container, text);
                } else {
                    DOM.setInnerText(container, text);
                }
                if (align != ALIGN_LEFT) {
                    switch (align) {
                    case ALIGN_CENTER:
                        DOM.setStyleAttribute(container, "textAlign", "center");
                        break;
                    case ALIGN_RIGHT:
                    default:
                        DOM.setStyleAttribute(container, "textAlign", "right");
                        break;
                    }
                }

                if (col == groupColIndex) {
                    if (hasChildren) {
                        groupCell = createGroupContainer();
                        DOM.setStyleAttribute(groupCell, "marginLeft", getLevel() * LEVEL_STEP_SIZE
                                + "px");
                        DOM.appendChild(td, groupCell);
                        className += " " + CLASSNAME + "-float";
                        DOM.setElementProperty(container, "className", className);
                    } else {
                        DOM.setStyleAttribute(container, "marginLeft", (getLevel() + 1) * LEVEL_STEP_SIZE
                                + "px");
                    }
                }

                DOM.appendChild(td, container);
                DOM.appendChild(getElement(), td);
            }

            @Override
            public void addCell(Widget w, char align, String style, int col) {
                final Element td = DOM.createTD();
                final Element container = DOM.createDiv();
                String className = CLASSNAME + "-cell-content";
                String classNameExt = null;
                if (style != null && !style.equals("")) {
                    classNameExt = CLASSNAME + "-cell-content-" + style;
                }
                if (classNameExt != null) {
                    className += " " + classNameExt;
                    DOM.setElementProperty(td, "className", classNameExt + "-td");
                }
                DOM.setElementProperty(container, "className", className);
                if (align != ALIGN_LEFT) {
                    switch (align) {
                    case ALIGN_CENTER:
                        DOM.setStyleAttribute(container, "textAlign", "center");
                        break;
                    case ALIGN_RIGHT:
                    default:
                        DOM.setStyleAttribute(container, "textAlign", "right");
                        break;
                    }
                }

                if (col == groupColIndex) {
                    if (hasChildren) {
                        groupCell = createGroupContainer();
                        DOM.setStyleAttribute(groupCell, "marginLeft", getLevel() * LEVEL_STEP_SIZE
                                + "px");
                        DOM.appendChild(td, groupCell);
                        className += " " + CLASSNAME + "-float";
                        DOM.setElementProperty(container, "className", className);
                    } else {
                        DOM.setStyleAttribute(container, "marginLeft", (getLevel() + 1) * LEVEL_STEP_SIZE
                                + "px");
                    }
                }

                DOM.appendChild(td, container);
                DOM.appendChild(getElement(), td);
                w.removeFromParent();
                DOM.appendChild(container, w.getElement());
                adopt(w);
                childWidgets.add(w);
            }

            public boolean isExpanded() {
                return expanded;
            }

            public int getLevel() {
                return level;
            }

            protected Element createGroupContainer() {
                Element groupContainer = DOM.createDiv();
                DOM.setInnerHTML(groupContainer, "&nbsp;");
                DOM.setElementProperty(groupContainer, "className", CLASSNAME + "-group-cell");
                return groupContainer;
            }

            @Override
            public void onBrowserEvent(Event event) {
                final Element tdOrTr = DOM.getParent(DOM.eventGetTarget(event));
                if (getElement() == tdOrTr
                        || getElement() == tdOrTr.getParentElement()) {
                    switch (DOM.eventGetType(event)) {
                        case Event.ONCLICK:
                            handleClickEvent(event);
                            handleRowClick(event);
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

            protected void handleRowClick(Event event) {
                if (groupCell != null
                        && DOM.eventGetTarget(event) == groupCell) {
                    if (isExpanded()) {
                        client.updateVariable(paintableId, "collapse", getKey(), true);
                    } else {
                        client.updateVariable(paintableId, "expand", getKey(), true);
                    }
                    DOM.eventCancelBubble(event, true);
                } else if (selectMode > Table.SELECT_MODE_NONE) {
                    toggleSelection();
                    // Note: changing the immediateness of this might
                    // require changes to "clickEvent" immediateness
                    // also.
                    client.updateVariable(paintableId, "selected",
                            selectedRowKeys.toArray(), immediate);
                }
            }
        }
    }
}
