/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 30.06.2010 16:57:32
 *
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.VGridLayout;
import com.vaadin.terminal.gwt.client.ui.layout.CellBasedLayout;
import com.vaadin.terminal.gwt.client.ui.layout.ChildComponentContainer;

public class VFieldGroupLayout extends VGridLayout {

    private boolean verticalCaption = false;

    protected ChildComponentContainer createComponentContainer(Paintable paintable) {
        return new FieldGroupComponentContainer((Widget) paintable,
                CellBasedLayout.ORIENTATION_VERTICAL);
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {

        if (uidl.hasAttribute("verticalCaption")) {
            verticalCaption = uidl.getBooleanAttribute("verticalCaption");
        }

        super.updateFromUIDL(uidl, client);

        if (cells != null) {
            for (final VGridLayout.Cell[] cells : this.cells) {
                for (final Cell cell : cells) {
                    if (cell != null
                            && cell.cc != null
                            && (cell.hasRelativeHeight() || cell
                            .hasRelativeWidth())) {
                        client.handleComponentRelativeSize(cell.cc.getWidget());
                    }
                }
            }
        }
    }

    @Override
    public RenderSpace getAllocatedSpace(Widget child) {
        if (verticalCaption) {
            return super.getAllocatedSpace(child);
        } else {
            RenderSpace cellSpace = super.getAllocatedSpace(child);
            Cell cell = paintableToCell.get(child);
            return new RenderSpace(
                    cellSpace.getWidth() - captionWidths[cell.getCol()] - spacingPixelsHorizontal,
                    cellSpace.getHeight()
            );
        }
    }

    @Override
    protected void expandColumns() {
        if (!"".equals(width)) {
            int usedSpace = minColumnWidths[0];
            for (int i = 1; i < minColumnWidths.length; i++) {
                usedSpace += spacingPixelsHorizontal + minColumnWidths[i];
            }
            canvas.setWidth("");
            int availableSpace = canvas.getOffsetWidth();
            int excessSpace = availableSpace - usedSpace;
            int distributed = 0;
            if (excessSpace > 0) {
                for (int i = 0; i < columnWidths.length; i++) {
                    int ew = excessSpace * colExpandRatioArray[i] / 1000;
                    columnWidths[i] = minColumnWidths[i] + ew;
                    distributed += ew;
                }
                excessSpace -= distributed;
                int c = 0;
                while (excessSpace > 0) {
                    columnWidths[c % columnWidths.length]++;
                    excessSpace--;
                    c++;
                }
            }

            if (!verticalCaption) {
                for (int i = 0; i < minColumnWidths.length; i++) {
                    int w = minColumnWidths[i] + captionWidths[i] + spacingPixelsHorizontal;
                    if (columnWidths[i] < w) {
                        columnWidths[i] = w;
                    }
                }
            }
        }
    }

    @Override
    protected void layoutCells() {
        int x = 0;
        int maxHeight = 0;
        for (int i = 0; i < cells.length; i++) {
            int y = 0;
            for (int j = 0; j < cells[i].length; j++) {
                Cell cell = cells[i][j];
                if (!(cell == null || cell.cc == null || !cell.cc.isAttached())) {
                    if (!cell.cc.isVisible()) continue;
                    cell.layout(x, y);
                    y += rowHeights[j] + spacingPixelsVertical;
                }
            }
            x += columnWidths[i] + spacingPixelsHorizontal;
            maxHeight = Math.max(maxHeight, y);
        }

        if ("".equals(width)) {
            canvas.setWidth((x - spacingPixelsHorizontal) + "px");
        } else {
            // main element defines width
            canvas.setWidth("");
        }
        int canvasHeight;
        if ("".equals(height)) {
            canvasHeight = maxHeight - spacingPixelsVertical;
        } else {
            canvasHeight = getOffsetHeight() - marginTopAndBottom;
        }
        canvas.setHeight(canvasHeight + "px");
    }

    private class FieldGroupComponentContainer extends ChildComponentContainer {
        protected FieldGroupComponentContainer(Widget widget, int orientation) {
            super(widget, orientation);
        }

        @Override
        public int getCaptionHeightAboveComponent() {
            if (verticalCaption) {
                return super.getCaptionHeightAboveComponent();
            } else {
                return 0;
            }
        }

        protected void setCaption(VCaption newCaption) {
            if (newCaption != null) {
                newCaption.removeFromParent();
            }

            if (caption != null && newCaption != caption) {
                remove(caption);
            }

            caption = newCaption;

            if (caption != null) {
                if (verticalCaption) {
                    if (caption.shouldBePlacedAfterComponent()) {
                        Util.setFloat(caption.getElement(), "left");
                        containerDIV.appendChild(caption.getElement());
                    } else {
                        Util.setFloat(caption.getElement(), "");
                        containerDIV.insertBefore(caption.getElement(), widgetDIV);
                    }
                } else {
                    Util.setFloat(caption.getElement(), "left");
                    containerDIV.insertBefore(caption.getElement(), widgetDIV);
                }

                adopt(caption);
            }
        }

        @Override
        public void updateCaptionSize() {
            captionWidth = 0;
            captionHeight = 0;

            if (caption != null) {
                captionWidth = caption.getRenderedWidth();
                captionHeight = caption.getHeight();
                captionRequiredWidth = caption.getRequiredWidth();

                if (!verticalCaption) {
                    Cell cell = paintableToCell.get((Paintable) widget);
                    int maxWidth = VFieldGroupLayout.this.captionWidths[cell.getCol()];
                    VFieldGroupLayout.this.captionWidths[cell.getCol()] = Math.max(maxWidth, captionWidth);
                }
            }
        }

        public void updateContainerDOMSize() {
            int width = contSize.getWidth();
            int height = contSize.getHeight() - alignmentTopOffset;
            if (width < 0) {
                width = 0;
            }
            if (height < 0) {
                height = 0;
            }

            setWidth(width + "px");
            setHeight(height + "px");

            // Also update caption max width
            if (caption != null) {
                if (verticalCaption) {
                    if (caption.shouldBePlacedAfterComponent()) {
                        caption.setMaxWidth(captionWidth);
                    } else {
                        caption.setMaxWidth(width);
                    }
                } else {
                    Cell cell = paintableToCell.get((Paintable) widget);
                    int maxWidth = VFieldGroupLayout.this.captionWidths[cell.getCol()];
                    caption.setMaxWidth(maxWidth + spacingPixelsHorizontal);
                }

                captionWidth = caption.getRenderedWidth();

                // Remove initial height
                caption.setHeight("");
            }
        }
    }
}
