/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.widgets.client.fieldgrouplayout;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Element;
import com.haulmont.cuba.web.widgets.CubaFieldGroupLayout;
import com.haulmont.cuba.web.widgets.client.caption.CubaCaptionWidget;
import com.haulmont.cuba.web.widgets.client.gridlayout.CubaGridLayoutConnector;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.VCaption;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.VGridLayout;
import com.vaadin.shared.ui.Connect;

import javax.annotation.Nullable;

@Connect(CubaFieldGroupLayout.class)
public class CubaFieldGroupLayoutConnector extends CubaGridLayoutConnector {

    protected static final String CAPTIONTEXT_STYLENAME = "v-captiontext";
    protected static final String ALIGN_RIGHT_STYLENAME = "v-align-right";

    protected boolean initialStateChangePerformed = false;

    @Override
    public CubaFieldGroupLayoutWidget getWidget() {
        return (CubaFieldGroupLayoutWidget) super.getWidget();
    }

    @Override
    public CubaFieldGroupLayoutState getState() {
        return (CubaFieldGroupLayoutState) super.getState();
    }

    @Override
    protected void setDefaultCaptionParameters(CubaCaptionWidget widget) {
        super.setDefaultCaptionParameters(widget);

        if (getState().useInlineCaption) {
            widget.setCaptionPlacedAfterComponentByDefault(false);
        }
    }

    @Override
    public void updateCaption(ComponentConnector childConnector) {
        super.updateCaption(childConnector);

        if (getState().useInlineCaption && initialStateChangePerformed) {
            updateCaptionSizes();
            updateCaptionAlignments();

            // always relayout after caption changes
            getLayoutManager().setNeedsLayout(this);
        }
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (getState().useInlineCaption) {
            updateCaptionSizes();
            updateCaptionAlignments();
        }

        if (stateChangeEvent.isInitialStateChange()) {
            this.initialStateChangePerformed = true;
        }
    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        // construct component cells with known caption alignment
        getWidget().useInlineCaption = getState().useInlineCaption;

        super.onConnectorHierarchyChange(event);

        if (getState().useInlineCaption && initialStateChangePerformed) {
            updateCaptionSizes();

            // always relayout after caption changes
            getLayoutManager().setNeedsLayout(this);
        }
    }

    protected void updateCaptionAlignments() {
        int index = 0;
        for (VGridLayout.Cell[] column : getWidget().getCellMatrix()) {
            if (column != null) {
                updateCaptionAlignments(index, column);
            }
            index++;
        }
    }

    protected void updateCaptionAlignments(int index, VGridLayout.Cell[] column) {
        CaptionAlignment alignment = getState().columnsCaptionAlignment;
        CaptionAlignment[] captionAlignments = getState().columnsCaptionAlignments;
        if (captionAlignments != null
                && index < captionAlignments.length
                && captionAlignments[index] != null) {
            alignment = captionAlignments[index];
        }

        for (VGridLayout.Cell cell : column) {
            if (cell != null && isCaptionInlineApplicable(cell)) {
                VCaption caption = cell.slot.getCaption();

                if (alignment == CaptionAlignment.RIGHT) {
                    caption.addStyleName(ALIGN_RIGHT_STYLENAME);
                } else {
                    caption.removeStyleName(ALIGN_RIGHT_STYLENAME);
                }
            }
        }
    }

    public void updateCaptionSizes() {
        int index = 0;
        for (VGridLayout.Cell[] column : getWidget().getCellMatrix()) {
            if (column != null) {
                updateCaptionSizes(index, column);
            }
            index++;
        }
    }

    protected void updateCaptionSizes(int index, VGridLayout.Cell[] column) {
        // reset indicators width
        resetIndicatorsWidth(column);

        int fixedCaptionWidth = -1;
        if (getState().fieldCaptionWidth > 0) {
            fixedCaptionWidth = getState().fieldCaptionWidth;
        }
        int[] columnCaptionWidth = getState().columnFieldCaptionWidth;
        if (columnCaptionWidth != null
                && index < columnCaptionWidth.length
                && columnCaptionWidth[index] > 0) {
            fixedCaptionWidth = columnCaptionWidth[index];
        }

        int maxIndicatorsWidth = 0;
        int maxCaptionWidth = 0;

        // calculate max widths
        for (VGridLayout.Cell cell : column) {
            if (cell != null && isCaptionInlineApplicable(cell)) {
                if (fixedCaptionWidth == -1) {
                    maxCaptionWidth = Math.max(maxCaptionWidth, getMaxCaptionWidth(cell));
                }

                maxIndicatorsWidth = Math.max(maxIndicatorsWidth, ((CubaFieldGroupLayoutComponentSlot) cell.slot).getIndicatorsWidth());
            }
        }

        if (fixedCaptionWidth > 0) {
            maxCaptionWidth = fixedCaptionWidth;
        }

        // apply max widths
        for (VGridLayout.Cell cell : column) {
            if (cell != null && isCaptionInlineApplicable(cell)) {
                cell.slot.getCaption().setWidth(maxCaptionWidth + "px");

                // Set fixed width to the inner textElement in order to have line wrapping
                if (cell.slot.getCaption() instanceof CubaCaptionWidget) {
                    Element textElement = ((CubaCaptionWidget) cell.slot.getCaption()).getTextElement();
                    textElement.getStyle().setProperty("width", maxCaptionWidth + "px");
                }

                if (cell.slot.isRelativeWidth()) {
                    ((CubaFieldGroupLayoutComponentSlot) cell.slot).setIndicatorsWidth(maxIndicatorsWidth + "px");
                }
            }
        }
    }

    protected int getMaxCaptionWidth(VGridLayout.Cell cell) {
        VCaption caption = cell.slot.getCaption();
        Element captionElement = caption.getElement();
        com.google.gwt.dom.client.Element childElement = findCaptionTextChildElement(captionElement);
        if (childElement != null) {
            // In some cases, e.g. by changing CheckBox 'captionManagedByLayout' attribute,
            // a slot has no Caption at first iteration of 'maxCaptionWidth' calculation,
            // as the result, for the second and so forth iterations, when the rest of
            // components get their captions, 'caption.getRenderedWidth()' returns an incorrect value.
            // In order to calculate the really required caption width, regardless of the 'white-space' mode,
            // we need to explicitly set it to 'nowrap'. After calculation, it's reverted back.
            Style style = childElement.getStyle();
            String prevWhiteSpace = style.getWhiteSpace();
            style.setWhiteSpace(Style.WhiteSpace.NOWRAP);
            int renderedWidth = caption.getRenderedWidth();

            if (prevWhiteSpace != null && !prevWhiteSpace.isEmpty()) {
                style.setWhiteSpace(Style.WhiteSpace.valueOf(prevWhiteSpace));
            } else {
                style.clearWhiteSpace();
            }

            return renderedWidth;
        }

        return caption.getRenderedWidth();
    }

    @Nullable
    protected com.google.gwt.dom.client.Element findCaptionTextChildElement(Element captionElement) {
        NodeList<Node> childNodes = captionElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.getItem(i);
            if (child instanceof com.google.gwt.dom.client.Element
                && ((com.google.gwt.dom.client.Element) child).getClassName().contains(CAPTIONTEXT_STYLENAME)) {
                return (com.google.gwt.dom.client.Element) child;
            }
        }

        return null;
    }

    protected void resetIndicatorsWidth(VGridLayout.Cell[] column) {
        for (VGridLayout.Cell cell : column) {
            if (cell != null && isCaptionInlineApplicable(cell)) {
                cell.slot.getCaption().getElement().getStyle().clearWidth();

                ((CubaFieldGroupLayoutComponentSlot) cell.slot).resetIndicatorsWidth();
            }
        }
    }

    protected boolean isCaptionInlineApplicable(VGridLayout.Cell cell) {
        return cell.slot != null && cell.slot.getCaption() != null;
    }
}