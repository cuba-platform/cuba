/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component.Alignment;
import com.haulmont.cuba.gui.components.DataGrid.DataGridStaticCellType;
import com.haulmont.cuba.gui.components.LookupField.FilterMode;
import com.haulmont.cuba.web.widgets.client.resizabletextarea.ResizeDirection;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.shared.ui.grid.GridStaticCellType;
import com.vaadin.v7.ui.AbstractSelect;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.vaadin.v7.ui.AbstractTextField.TextChangeEventMode;

/**
 * Convenient class for methods that converts values from Vaadin to CUBA instances and vice versa.
 */
public final class WebWrapperUtils {
    private WebWrapperUtils() {
    }

    public static CaptionMode toCaptionMode(AbstractSelect.ItemCaptionMode captionMode) {
        if (captionMode == null) {
            return null;
        }

        switch (captionMode) {
            case ITEM:
                return CaptionMode.ITEM;
            case PROPERTY:
                return CaptionMode.PROPERTY;
            case EXPLICIT_DEFAULTS_ID:
                return CaptionMode.MAP_ENTRY;
            default:
                throw new UnsupportedOperationException("Unsupported Vaadin AbstractSelect.ItemCaptionMode");
        }
    }

    public static AbstractSelect.ItemCaptionMode toVaadinCaptionMode(CaptionMode captionMode) {
        if (captionMode == null) {
            return null;
        }

        switch (captionMode) {
            case ITEM:
                return AbstractSelect.ItemCaptionMode.ITEM;
            case PROPERTY:
                return AbstractSelect.ItemCaptionMode.PROPERTY;
            case MAP_ENTRY:
                return AbstractSelect.ItemCaptionMode.EXPLICIT_DEFAULTS_ID;
            default:
                throw new UnsupportedOperationException("Unsupported CaptionMode");
        }
    }

    public static FilteringMode toVaadinFilterMode(FilterMode filterMode) {
        if (filterMode == null) {
            return null;
        }

        switch (filterMode) {
            case NO:
                return FilteringMode.OFF;
            case STARTS_WITH:
                return FilteringMode.STARTSWITH;
            case CONTAINS:
                return FilteringMode.CONTAINS;
            default:
                throw new UnsupportedOperationException("Unsupported FilterMode");
        }
    }

    public static FilterMode toFilterMode(FilteringMode filterMode) {
        if (filterMode == null) {
            return null;
        }

        switch (filterMode) {
            case OFF:
                return FilterMode.NO;
            case CONTAINS:
                return FilterMode.CONTAINS;
            case STARTSWITH:
                return FilterMode.STARTS_WITH;
            default:
                throw new UnsupportedOperationException("Unsupported Vaadin FilteringMode");
        }
    }

    public static com.vaadin.ui.Alignment toVaadinAlignment(Alignment alignment) {
        if (alignment == null) {
            return null;
        }

        switch (alignment) {
            case TOP_LEFT:
                return com.vaadin.ui.Alignment.TOP_LEFT;
            case TOP_CENTER:
                return com.vaadin.ui.Alignment.TOP_CENTER;
            case TOP_RIGHT:
                return com.vaadin.ui.Alignment.TOP_RIGHT;
            case MIDDLE_LEFT:
                return com.vaadin.ui.Alignment.MIDDLE_LEFT;
            case MIDDLE_CENTER:
                return com.vaadin.ui.Alignment.MIDDLE_CENTER;
            case MIDDLE_RIGHT:
                return com.vaadin.ui.Alignment.MIDDLE_RIGHT;
            case BOTTOM_LEFT:
                return com.vaadin.ui.Alignment.BOTTOM_LEFT;
            case BOTTOM_CENTER:
                return com.vaadin.ui.Alignment.BOTTOM_CENTER;
            case BOTTOM_RIGHT:
                return com.vaadin.ui.Alignment.BOTTOM_RIGHT;
            default:
                throw new UnsupportedOperationException("Unsupported Alignment");
        }
    }

    public static TextInputField.TextChangeEventMode toTextChangeEventMode(ValueChangeMode mode) {
        if (mode == null) {
            return null;
        }

        switch (mode) {
            case EAGER:
                return TextInputField.TextChangeEventMode.EAGER;
            case LAZY:
                return TextInputField.TextChangeEventMode.LAZY;
            case TIMEOUT:
                return TextInputField.TextChangeEventMode.TIMEOUT;
            default:
                throw new UnsupportedOperationException("Unsupported Vaadin TextChangeEventMode");
        }
    }

    public static TextInputField.TextChangeEventMode toTextChangeEventMode(TextChangeEventMode mode) {
        if (mode == null) {
            return null;
        }

        switch (mode) {
            case EAGER:
                return TextInputField.TextChangeEventMode.EAGER;
            case LAZY:
                return TextInputField.TextChangeEventMode.LAZY;
            case TIMEOUT:
                return TextInputField.TextChangeEventMode.TIMEOUT;
            default:
                throw new UnsupportedOperationException("Unsupported Vaadin TextChangeEventMode");
        }
    }

    public static ValueChangeMode toVaadinValueChangeEventMode(TextInputField.TextChangeEventMode mode) {
        if (mode == null) {
            return null;
        }

        ValueChangeMode vMode;
        switch (mode) {
            case EAGER:
                vMode = ValueChangeMode.EAGER;
                break;
            case LAZY:
                vMode = ValueChangeMode.LAZY;
                break;
            case TIMEOUT:
                vMode = ValueChangeMode.TIMEOUT;
                break;
            default:
                throw new UnsupportedOperationException("Unsupported TextChangeEventMode");
        }

        return vMode;
    }

    public static TextChangeEventMode toVaadinTextChangeEventMode(TextInputField.TextChangeEventMode mode) {
        if (mode == null) {
            return null;
        }

        TextChangeEventMode vMode;
        switch (mode) {
            case EAGER:
                vMode = TextChangeEventMode.EAGER;
                break;
            case LAZY:
                vMode = TextChangeEventMode.LAZY;
                break;
            case TIMEOUT:
                vMode = TextChangeEventMode.TIMEOUT;
                break;
            default:
                throw new UnsupportedOperationException("Unsupported TextChangeEventMode");
        }

        return vMode;
    }

    public static MouseEventDetails toMouseEventDetails(MouseEvents.ClickEvent event) {
        checkNotNullArgument(event);

        MouseEventDetails mouseEventDetails = new MouseEventDetails();
        mouseEventDetails.setButton(toMouseButton(event.getButton()));
        mouseEventDetails.setClientX(event.getClientX());
        mouseEventDetails.setClientY(event.getClientY());
        mouseEventDetails.setAltKey(event.isAltKey());
        mouseEventDetails.setCtrlKey(event.isCtrlKey());
        mouseEventDetails.setMetaKey(event.isMetaKey());
        mouseEventDetails.setShiftKey(event.isShiftKey());
        mouseEventDetails.setDoubleClick(event.isDoubleClick());
        mouseEventDetails.setRelativeX(event.getRelativeX());
        mouseEventDetails.setRelativeY(event.getRelativeY());

        return mouseEventDetails;
    }

    public static MouseEventDetails.MouseButton toMouseButton(com.vaadin.shared.MouseEventDetails.MouseButton mouseButton) {
        if (mouseButton == null) {
            return null;
        }

        switch (mouseButton) {
            case LEFT:
                return MouseEventDetails.MouseButton.LEFT;
            case MIDDLE:
                return MouseEventDetails.MouseButton.MIDDLE;
            case RIGHT:
                return MouseEventDetails.MouseButton.RIGHT;
            default:
                throw new UnsupportedOperationException("Unsupported Vaadin MouseButton");
        }
    }

    public static DataGridStaticCellType toDataGridStaticCellType(GridStaticCellType cellType) {
        DataGridStaticCellType type;
        switch (cellType) {
            case HTML:
                type = DataGridStaticCellType.HTML;
                break;
            case TEXT:
                type = DataGridStaticCellType.TEXT;
                break;
            case WIDGET:
                type = DataGridStaticCellType.COMPONENT;
                break;
            default:
                throw new UnsupportedOperationException("Unsupported GridStaticCellType");
        }

        return type;
    }

    public static ResizeDirection toVaadinResizeDirection(ResizableTextArea.ResizeDirection direction) {
        switch (direction) {
            case BOTH:
                return ResizeDirection.BOTH;
            case VERTICAL:
                return ResizeDirection.VERTICAL;
            case HORIZONTAL:
                return ResizeDirection.HORIZONTAL;
            case NONE:
                return ResizeDirection.NONE;
            default:
                throw new IllegalArgumentException("Unknown direction: " + direction);
        }
    }

    public static ResizableTextArea.ResizeDirection toResizeDirection(ResizeDirection direction) {
        switch (direction) {
            case BOTH:
                return ResizableTextArea.ResizeDirection.BOTH;
            case VERTICAL:
                return ResizableTextArea.ResizeDirection.VERTICAL;
            case HORIZONTAL:
                return ResizableTextArea.ResizeDirection.HORIZONTAL;
            case NONE:
                return ResizableTextArea.ResizeDirection.NONE;
            default:
                throw new IllegalArgumentException("Unknown direction: " + direction);
        }
    }

    public static Sizeable.Unit toVaadinUnit(SizeUnit sizeUnit) {
        checkNotNullArgument(sizeUnit);

        switch (sizeUnit) {
            case PIXELS:
                return Sizeable.Unit.PIXELS;
            case PERCENTAGE:
                return Sizeable.Unit.PERCENTAGE;
            default:
                throw new UnsupportedOperationException("Unsupported Size Unit");
        }
    }

    public static SizeUnit toSizeUnit(Sizeable.Unit units) {
        checkNotNullArgument(units);

        switch (units) {
            case PIXELS:
                return SizeUnit.PIXELS;
            case PERCENTAGE:
                return SizeUnit.PERCENTAGE;
            default:
                throw new UnsupportedOperationException("Unsupported Size Unit");
        }
    }

    public static PopupButton.PopupOpenDirection toPopupOpenDirection(com.vaadin.ui.Alignment alignment) {
        checkNotNullArgument(alignment);

        if (alignment == com.vaadin.ui.Alignment.BOTTOM_LEFT)
            return PopupButton.PopupOpenDirection.BOTTOM_LEFT;

        if (alignment == com.vaadin.ui.Alignment.BOTTOM_RIGHT)
            return PopupButton.PopupOpenDirection.BOTTOM_RIGHT;

        if (alignment == com.vaadin.ui.Alignment.BOTTOM_CENTER)
            return PopupButton.PopupOpenDirection.BOTTOM_CENTER;

        throw new UnsupportedOperationException("Unsupported alignment");
    }

    public static com.vaadin.ui.Alignment toVaadinAlignment(PopupButton.PopupOpenDirection direction) {
        checkNotNullArgument(direction);

        switch (direction) {
            case BOTTOM_CENTER:
                return com.vaadin.ui.Alignment.BOTTOM_CENTER;
            case BOTTOM_RIGHT:
                return com.vaadin.ui.Alignment.BOTTOM_RIGHT;
            case BOTTOM_LEFT:
                return com.vaadin.ui.Alignment.BOTTOM_LEFT;
            default:
                throw new UnsupportedOperationException();
        }
    }
}