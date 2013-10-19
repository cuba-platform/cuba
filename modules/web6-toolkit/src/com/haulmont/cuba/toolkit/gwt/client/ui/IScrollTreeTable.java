/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.toolkit.gwt.client.Tools;
import com.vaadin.terminal.gwt.client.BrowserInfo;
import com.vaadin.terminal.gwt.client.RenderInformation;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.IScrollTable;

/**
 * User: Nikolay Gorodnov
 * Date: 03.06.2009
 */
public class IScrollTreeTable
        extends IScrollTable {

    private static int LEVEL_STEP_SIZE = 19;

    @Override
    public void enableBrowserIntelligence() {
        super.enableBrowserIntelligence();
        ((IScrollTreeTableBody) getBody()).enableBrowserIntelligence();
    }

    @Override
    public void disableBrowserIntelligence() {
        super.disableBrowserIntelligence();
        ((IScrollTreeTableBody) getBody()).disableBrowserIntelligence();
    }

    @Override
    protected IScrollTableBody createBody() {
        return new IScrollTreeTableBody();
    }

    public class IScrollTreeTableBody extends IScrollTableBody {

        protected int groupColIndex =
                showRowHeaders ? 1 : 0;

        @Override
        protected IScrollTreeTableRow[] createRowsArray(int rows) {
            return new IScrollTreeTableRow[rows];
        }

        @Override
        protected void addRowBeforeFirstRendered(IScrollTableRow row) {
            super.addRowBeforeFirstRendered(row);
            if (((IScrollTreeTableRow) row).isExpanded()) {
                row.addStyleName("v-expanded");
            } else if (((IScrollTreeTableRow) row).hasChildren()) {
                row.addStyleName("v-collapsed");
            }
        }

        @Override
        protected void addRow(IScrollTableRow row) {
            super.addRow(row);
            if (((IScrollTreeTableRow) row).isExpanded()) {
                row.addStyleName("v-expanded");
            } else if (((IScrollTreeTableRow) row).hasChildren()) {
                row.addStyleName("v-collapsed");
            }
        }

        protected IScrollTreeTableRow createRowInstance(UIDL uidl) {
            if (isCaptionRow(uidl)) {
                return new IScrollTreeTableCaptionRow(uidl, aligns);
            } else {
                return new IScrollTreeTableRow(uidl, aligns);
            }
        }

        @Override
        protected IScrollTableRow createRow(UIDL uidl) {
            final IScrollTreeTableRow row = createRowInstance(uidl);
            if (!isCaptionRow(uidl)) {
                final int cells = DOM.getChildCount(row.getElement());
                for (int i = 0; i < cells; i++) {
                    final int w = IScrollTreeTable.this
                            .getColWidth(getColKeyByIndex(i));
                    applyCellWidth(row, i, w);
                }
            }
            return row;
        }

        @Override
        public int getColWidth(int i) {
            if (initDone) {
                IScrollTreeTableRow row = null;
                for (Object o : renderedRows) {
                    if (!(o instanceof IScrollTreeTableCaptionRow)) {
                        row = (IScrollTreeTableRow) o;
                        break;
                    }
                }
                if (row != null) {
                    final Element e = DOM.getChild(row.getElement(), i);
                    return DOM.getElementPropertyInt(e, "offsetWidth");
                }
            }
            return 0;
        }

        @Override
        public void setColWidth(int colIndex, int w) {
            for (final Widget o : renderedRows) {
                if (o instanceof IScrollTreeTableCaptionRow) {
                    if (colIndex < groupColIndex) {
                        applyCellWidth((IScrollTreeTableRow) o, colIndex, w);
                    } else {
                        int rowWidth = scrollbarWidthReserved > 0
                                ? calculatedWidth - scrollbarWidthReserved : calculatedWidth;
                        applyCellWidth((IScrollTreeTableRow) o, groupColIndex, rowWidth);
                    }
                } else {
                    applyCellWidth((IScrollTreeTableRow) o, colIndex, w);
                }
            }
        }

        protected void disableBrowserIntelligence() {
            for (final Widget o : renderedRows) {
               ((IScrollTreeTableRow) o).disableBrowserIntelligence();
            }
        }

        protected void enableBrowserIntelligence() {
            for (final Widget o : renderedRows) {
               ((IScrollTreeTableRow) o).enableBrowserIntelligence();
            }
        }

        private void applyCellWidth(IScrollTreeTableRow row,
                                      int colIndex, int w) {
            final Element cell = DOM.getChild(row.getElement(),
                    colIndex);
            int innerWidth = w;
            if (colIndex == groupColIndex) {
                if (row.hasChildren()) {
                    innerWidth -= (row.getLevel() * LEVEL_STEP_SIZE);
                } else {
                    innerWidth -= ((row.getLevel() + 1) * LEVEL_STEP_SIZE);
                }
            }
            setWidthDependsOnStyle(DOM.getFirstChild(cell), innerWidth);
            DOM.setStyleAttribute(cell, "width", w + "px");
        }

        @Override
        protected void applyAlternatingRowColor(IScrollTableRow row,
                                                String style) {
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

                Tools.setStylePrimaryName(td, CLASSNAME + "-cell");
                if (allowMultiStingCells) {
                    Tools.addStyleName(td, CLASSNAME + "-cell-wrap");
                }

                final Element container = DOM.createDiv();
                Tools.setStylePrimaryName(container, CLASSNAME + "-caption-row-content");
                if (groupCell != null) {
                    final Element contentDiv = DOM.createDiv();
                    DOM.setStyleAttribute(container, "marginLeft", getLevel() * LEVEL_STEP_SIZE
                            + "px");
                    Tools.setStyleName(contentDiv, CLASSNAME + "-float");
                    DOM.setInnerText(contentDiv, uidl.getStringAttribute("rowCaption"));
                    DOM.appendChild(container, groupCell);
                    DOM.appendChild(container, contentDiv);
                } else {
                    DOM.setStyleAttribute(container, "marginLeft", (getLevel() + 1) * LEVEL_STEP_SIZE
                            + "px");
                    DOM.setInnerText(container, uidl.getStringAttribute("rowCaption"));
                }

                DOM.appendChild(td, container);
                DOM.appendChild(getElement(), td);
            }

            @Override
            public void moveCol(int oldIndex, int newIndex) {
            }
        }

        public class IScrollTreeTableRow extends IScrollTableRow {
            private boolean expanded;
            private int level = 0;

            protected Element groupCell = null;
            protected Element wrapCell = null;

            protected IScrollTreeTableRow() {
                super();
            }

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
                    groupCell = createGroupContainer();
                    if (uidl.hasAttribute("expanded")) {
                        expanded = true;
                    }
                }

                if (uidl.hasAttribute("level"))
                    level = uidl.getIntAttribute("level");
                else
                    level = 0;

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

                Tools.setStylePrimaryName(td, CLASSNAME + "-cell");
                initCellStylePaddingBorders(Tools.setStylePrimaryName(container, CLASSNAME + "-cell-content"));
                if (allowMultiStingCells) {
                    Tools.addStyleName(td, CLASSNAME + "-cell-wrap");
                }
                if (style != null && !style.equals("")) {
                    Tools.addStyleDependentName(td, style);
                    initCellStylePaddingBorders(Tools.addStyleDependentName(container, style));
                }

                Element contentDiv = container;

                if (col == groupColIndex) {
                    if (groupCell != null) {
                        contentDiv = DOM.createDiv();

                        DOM.setStyleAttribute(container, "marginLeft", getLevel() * LEVEL_STEP_SIZE
                                + "px");

                        Tools.setStyleName(contentDiv, CLASSNAME + "-float");
                        DOM.appendChild(container, groupCell);
                        DOM.appendChild(container, contentDiv);
                    } else {
                        DOM.setStyleAttribute(container, "marginLeft", (getLevel() + 1) * LEVEL_STEP_SIZE
                                + "px");
                    }
                }

                setCellText(contentDiv, text, textIsHTML);
                setCellAlignment(contentDiv, align);

                DOM.appendChild(td, container);
                DOM.appendChild(getElement(), td);

                tableCells.add(td);

                Tools.textSelectionEnable(td, textSelectionEnabled);
            }

            @Override
            public void addCell(Widget w, char align, String style, int col) {
                final Element td = DOM.createTD();
                final Element container = DOM.createDiv();

                Tools.setStylePrimaryName(td, CLASSNAME + "-cell");
                initCellStylePaddingBorders(Tools.setStylePrimaryName(container, CLASSNAME + "-cell-content"));
                if (allowMultiStingCells) {
                    Tools.addStyleName(td, CLASSNAME + "-cell-wrap");
                }
                if (style != null && !style.equals("")) {
                    Tools.addStyleDependentName(td, style);
                    initCellStylePaddingBorders(Tools.addStyleDependentName(container, style));
                }

                Element contentDiv = container;

                if (col == groupColIndex) {
                    int k;
                    if (groupCell != null) {
                        wrapCell = DOM.createDiv();

                        contentDiv = DOM.createDiv();
                        Tools.setStyleName(contentDiv, CLASSNAME + "-float");
                        DOM.appendChild(wrapCell, groupCell);
                        DOM.appendChild(wrapCell, contentDiv);

                        DOM.appendChild(container, wrapCell);

                        k = getLevel();

                    } else {
                        k = getLevel() + 1;
                    }
                    DOM.setStyleAttribute(container, "marginLeft", k * LEVEL_STEP_SIZE
                            + "px");
                }

                setCellAlignment(contentDiv, align);

                DOM.appendChild(td, container);
                DOM.appendChild(getElement(), td);

                if (BrowserInfo.get().getWebkitVersion() > 0) {
                    DOM.setElementPropertyBoolean(td, "__cell", true);
                    DOM.setElementPropertyBoolean(container, "__cell", true);
                }

                setCellWidget(contentDiv, w, col);
            }

            public void disableBrowserIntelligence() {
                if (wrapCell != null) {
                    DOM.setStyleAttribute(wrapCell, "width", 9000 + "px");
                }
            }

            public void enableBrowserIntelligence() {
                if (wrapCell != null) {
                    DOM.setStyleAttribute(wrapCell, "width", "");
                }
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
                Tools.setStyleName(groupContainer, CLASSNAME + "-group-cell");
                return groupContainer;
            }

            @Override
            public RenderSpace getAllocatedSpace(Widget child) {
                int w = 0;
                int i = getColIndexOf(child);
                HeaderCell headerCell = tHead.getHeaderCell(i);
                if (headerCell != null) {
                    RenderInformation.Size paddingBorders = getElementPaddingBorders(DOM.getParent(child.getElement()));
                    if (paddingBorders != null) {
                        if (initializedAndAttached) {
                            w = headerCell.getWidth() - paddingBorders.getWidth();
                        } else {
                            // header offset width is not absolutely correct value,
                            // but
                            // a best guess (expecting similar content in all
                            // columns ->
                            // if one component is relative width so are others)
                            w = headerCell.getOffsetWidth() - paddingBorders.getWidth();
                        }
                    }
                }

                if (i == groupColIndex) {
                    w -= (getLevel() + 1) * LEVEL_STEP_SIZE;
                }

                return new RenderSpace(w, getRowHeight());
            }

            @Override
            public void onBrowserEvent(Event event) {
                final Element targetElement = DOM.eventGetTarget(event);
                if (Tools.isCheckbox(targetElement) || Tools.isRadio(targetElement))
                    return;

                switch (DOM.eventGetType(event)) {
                    case Event.ONCLICK:
                        if (groupCell != null && DOM.eventGetTarget(event) == groupCell) {
                            if (BrowserInfo.get().getWebkitVersion() > 0) {
                                bodyContainer.setFocus(true);
                            }
                            handleClickEvent(event);
                            handleRowClick(event);
                        } else {
                            super.onBrowserEvent(event);
                        }
                        break;
                    case Event.ONDBLCLICK:
                        handleClickEvent(event);
                        break;
                    case Event.ONCONTEXTMENU:
                        if (selectMode > com.vaadin.terminal.gwt.client.ui.Table.SELECT_MODE_NONE) {
                            rowClick();
                        }
                        handleRowClick(event);
                        showContextMenu(event);
                        break;
                    default:
                        break;
                }
            }

            public boolean hasChildren() {
                return (groupCell != null);
            }

            @Override
            protected void handleRowClick(Event event) {
                if (groupCell != null
                        && DOM.eventGetTarget(event) == groupCell) {
                    if (isExpanded()) {
                        client.updateVariable(paintableId, "collapse", getKey(), true);
                    } else {
                        client.updateVariable(paintableId, "expand", getKey(), true);
                    }
                    DOM.eventCancelBubble(event, true);
                } else {
                    super.handleRowClick(event);
                }
            }
        }
    }
}
