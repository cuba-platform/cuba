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
import com.haulmont.cuba.web.gui.components.JavaScriptComponent.DependencyType;
import com.haulmont.cuba.web.widgets.client.fieldgrouplayout.CaptionAlignment;
import com.haulmont.cuba.web.widgets.client.popupview.PopupPosition;
import com.haulmont.cuba.web.widgets.client.resizabletextarea.ResizeDirection;
import com.haulmont.cuba.web.widgets.client.timefield.TimeMode;
import com.haulmont.cuba.web.widgets.client.timefield.TimeResolution;
import com.haulmont.cuba.web.widgets.data.AggregationContainer;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.Orientation;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.shared.ui.grid.GridStaticCellType;
import com.vaadin.shared.ui.grid.ScrollDestination;
import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.ui.Dependency;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.AbstractSelect;

import javax.annotation.Nullable;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.vaadin.v7.ui.AbstractTextField.TextChangeEventMode;

/**
 * Convenient class for methods that converts values from Vaadin to CUBA instances and vice versa.
 */
public final class WebWrapperUtils {

    public static final String AUTO_SIZE = "AUTO";

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

    public static ContentMode toContentMode(com.vaadin.shared.ui.ContentMode contentMode) {
        checkNotNullArgument(contentMode);

        switch (contentMode) {
            case TEXT:
                return ContentMode.TEXT;
            case PREFORMATTED:
                return ContentMode.PREFORMATTED;
            case HTML:
                return ContentMode.HTML;
            default:
                throw new IllegalArgumentException("Unknown content mode: " + contentMode);
        }
    }

    public static com.vaadin.shared.ui.ContentMode toVaadinContentMode(ContentMode contentMode) {
        checkNotNullArgument(contentMode);

        switch (contentMode) {
            case TEXT:
                return com.vaadin.shared.ui.ContentMode.TEXT;
            case PREFORMATTED:
                return com.vaadin.shared.ui.ContentMode.PREFORMATTED;
            case HTML:
                return com.vaadin.shared.ui.ContentMode.HTML;
            default:
                throw new IllegalArgumentException("Unknown content mode: " + contentMode);
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
            case BLUR:
                return TextInputField.TextChangeEventMode.BLUR;
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
            case BLUR:
                vMode = ValueChangeMode.BLUR;
                break;
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

    public static MouseEventDetails toMouseEventDetails(com.vaadin.shared.MouseEventDetails vMouseEventDetails) {
        checkNotNullArgument(vMouseEventDetails);

        MouseEventDetails mouseEventDetails = new MouseEventDetails();
        mouseEventDetails.setButton(toMouseButton(vMouseEventDetails.getButton()));
        mouseEventDetails.setClientX(vMouseEventDetails.getClientX());
        mouseEventDetails.setClientY(vMouseEventDetails.getClientY());
        mouseEventDetails.setAltKey(vMouseEventDetails.isAltKey());
        mouseEventDetails.setCtrlKey(vMouseEventDetails.isCtrlKey());
        mouseEventDetails.setMetaKey(vMouseEventDetails.isMetaKey());
        mouseEventDetails.setShiftKey(vMouseEventDetails.isShiftKey());
        mouseEventDetails.setDoubleClick(vMouseEventDetails.isDoubleClick());
        mouseEventDetails.setRelativeX(vMouseEventDetails.getRelativeX());
        mouseEventDetails.setRelativeY(vMouseEventDetails.getRelativeY());

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
        checkNotNullArgument(cellType);

        switch (cellType) {
            case HTML:
                return DataGridStaticCellType.HTML;
            case TEXT:
                return DataGridStaticCellType.TEXT;
            case WIDGET:
                return DataGridStaticCellType.COMPONENT;
            default:
                throw new UnsupportedOperationException("Unsupported GridStaticCellType");
        }
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

    public static com.vaadin.v7.ui.Table.Align convertColumnAlignment(com.haulmont.cuba.gui.components.Table.ColumnAlignment alignment) {
        if (alignment == null) {
            return null;
        }

        switch (alignment) {
            case LEFT:
                return com.vaadin.v7.ui.Table.Align.LEFT;
            case CENTER:
                return com.vaadin.v7.ui.Table.Align.CENTER;
            case RIGHT:
                return com.vaadin.v7.ui.Table.Align.RIGHT;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static AggregationContainer.Type convertAggregationType(AggregationInfo.Type function) {
        switch (function) {
            case COUNT:
                return AggregationContainer.Type.COUNT;
            case AVG:
                return AggregationContainer.Type.AVG;
            case MAX:
                return AggregationContainer.Type.MAX;
            case MIN:
                return AggregationContainer.Type.MIN;
            case SUM:
                return AggregationContainer.Type.SUM;
            case CUSTOM:
                return AggregationContainer.Type.CUSTOM;
            default:
                throw new IllegalArgumentException("Unknown function: " + function);
        }
    }

    public static DateResolution convertDateResolution(DatePicker.Resolution resolution) {
        switch (resolution) {
            case YEAR:
                return DateResolution.YEAR;
            case MONTH:
                return DateResolution.MONTH;
            case DAY:
            default:
                return DateResolution.DAY;
        }
    }

    public static DateResolution convertDateTimeResolution(DateField.Resolution resolution) {
        switch (resolution) {
            case YEAR:
                return DateResolution.YEAR;
            case MONTH:
                return DateResolution.MONTH;
            case DAY:
            case HOUR:
            case MIN:
            case SEC:
            default:
                return DateResolution.DAY;
        }
    }

    public static TimeResolution toVaadinTimeResolution(TimeField.Resolution resolution) {
        switch (resolution) {
            case SEC:
                return TimeResolution.SECOND;
            case HOUR:
                return TimeResolution.HOUR;
            case MIN:
            default:
                return TimeResolution.MINUTE;
        }
    }

    public static TimeResolution toVaadinTimeResolution(DateField.Resolution resolution) {
        switch (resolution) {
            case HOUR:
                return TimeResolution.HOUR;
            case MIN:
                return TimeResolution.MINUTE;
            case SEC:
                return TimeResolution.SECOND;
            default:
                throw new IllegalArgumentException("Can't be converted to TimeResolution: " + resolution);
        }
    }

    public static TimeField.Resolution fromVaadinTimeResolution(TimeResolution timeResolution) {
        switch (timeResolution) {
            case HOUR:
                return TimeField.Resolution.HOUR;
            case MINUTE:
                return TimeField.Resolution.MIN;
            case SECOND:
                return TimeField.Resolution.SEC;
            default:
                throw new IllegalArgumentException("Can't be converted to TimeField.Resolution: " + timeResolution);
        }
    }

    public static DataGrid.ColumnResizeMode convertToDataGridColumnResizeMode(ColumnResizeMode mode) {
        checkNotNullArgument(mode);

        switch (mode) {
            case ANIMATED:
                return DataGrid.ColumnResizeMode.ANIMATED;
            case SIMPLE:
                return DataGrid.ColumnResizeMode.SIMPLE;
            default:
                throw new IllegalArgumentException("Can't be converted to ColumnResizeMode: " + mode);
        }
    }

    public static ColumnResizeMode convertToGridColumnResizeMode(DataGrid.ColumnResizeMode mode) {
        checkNotNullArgument(mode);

        switch (mode) {
            case ANIMATED:
                return ColumnResizeMode.ANIMATED;
            case SIMPLE:
                return ColumnResizeMode.SIMPLE;
            default:
                throw new IllegalArgumentException("Can't be converted to ColumnResizeMode: " + mode);
        }
    }

    public static SortDirection convertToGridSortDirection(DataGrid.SortDirection sortDirection) {
        checkNotNullArgument(sortDirection);

        switch (sortDirection) {
            case ASCENDING:
                return SortDirection.ASCENDING;
            case DESCENDING:
                return SortDirection.DESCENDING;
            default:
                throw new IllegalArgumentException("Can't be converted to SortDirection: " + sortDirection);
        }
    }

    public static DataGrid.SortDirection convertToDataGridSortDirection(SortDirection sortDirection) {
        checkNotNullArgument(sortDirection);

        switch (sortDirection) {
            case ASCENDING:
                return DataGrid.SortDirection.ASCENDING;
            case DESCENDING:
                return DataGrid.SortDirection.DESCENDING;
            default:
                throw new IllegalArgumentException("Can't be converted to SortDirection: " + sortDirection);
        }
    }

    public static ScrollDestination convertToGridScrollDestination(DataGrid.ScrollDestination destination) {
        checkNotNullArgument(destination);

        switch (destination) {
            case ANY:
                return ScrollDestination.ANY;
            case START:
                return ScrollDestination.START;
            case MIDDLE:
                return ScrollDestination.MIDDLE;
            case END:
                return ScrollDestination.END;
            default:
                throw new IllegalArgumentException("Can't be converted to ScrollDestination: " + destination);
        }
    }

    public static HasOrientation.Orientation convertToOrientation(Orientation orientation) {
        checkNotNullArgument(orientation);

        switch (orientation) {
            case VERTICAL:
                return HasOrientation.Orientation.VERTICAL;
            case HORIZONTAL:
                return HasOrientation.Orientation.HORIZONTAL;
            default:
                throw new IllegalArgumentException("Can't be converted to HasOrientation.Orientation: " + orientation);
        }
    }

    public static Orientation convertToVaadinOrientation(HasOrientation.Orientation orientation) {
        checkNotNullArgument(orientation);

        switch (orientation) {
            case VERTICAL:
                return Orientation.VERTICAL;
            case HORIZONTAL:
                return Orientation.HORIZONTAL;
            default:
                throw new IllegalArgumentException("Can't be converted to Orientation: " + orientation);
        }
    }

    public static HasOrientation.Orientation fromVaadinSliderOrientation(SliderOrientation orientation) {
        checkNotNullArgument(orientation);

        switch (orientation) {
            case VERTICAL:
                return HasOrientation.Orientation.VERTICAL;
            case HORIZONTAL:
                return HasOrientation.Orientation.HORIZONTAL;
            default:
                throw new IllegalArgumentException("Can't be converted to HasOrientation.Orientation: " + orientation);
        }
    }

    public static SliderOrientation toVaadinSliderOrientation(HasOrientation.Orientation orientation) {
        checkNotNullArgument(orientation);

        switch (orientation) {
            case VERTICAL:
                return SliderOrientation.VERTICAL;
            case HORIZONTAL:
                return SliderOrientation.HORIZONTAL;
            default:
                throw new IllegalArgumentException("Can't be converted to SliderOrientation: " + orientation);
        }
    }

    public static CaptionAlignment toVaadinFieldGroupCaptionAlignment(Form.CaptionAlignment alignment) {
        checkNotNullArgument(alignment);

        switch (alignment) {
            case LEFT:
                return CaptionAlignment.LEFT;
            case RIGHT:
                return CaptionAlignment.RIGHT;
            default:
                throw new IllegalArgumentException("Can't be converted to CaptionAlignment " + alignment);
        }
    }

    public static Form.CaptionAlignment fromVaadinFieldGroupCaptionAlignment(CaptionAlignment alignment) {
        checkNotNullArgument(alignment);

        switch (alignment) {
            case LEFT:
                return Form.CaptionAlignment.LEFT;
            case RIGHT:
                return Form.CaptionAlignment.RIGHT;
            default:
                throw new IllegalArgumentException("Can't be converted to CaptionAlignment " + alignment);
        }
    }

    @Nullable
    public static DependencyType toDependencyType(Dependency.Type type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case JAVASCRIPT:
                return DependencyType.JAVASCRIPT;
            case STYLESHEET:
                return DependencyType.STYLESHEET;
            default:
                throw new IllegalArgumentException("Can't be converted to DependencyType: " + type);
        }
    }

    @Nullable
    public static Dependency.Type toVaadinDependencyType(DependencyType type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case JAVASCRIPT:
                return Dependency.Type.JAVASCRIPT;
            case STYLESHEET:
                return Dependency.Type.STYLESHEET;
            default:
                throw new IllegalArgumentException("Can't be converted to Dependency.Type: " + type);
        }
    }

    public static String fromVaadinSize(String size) {
        return Component.AUTO_SIZE.equalsIgnoreCase(size)
                ? AUTO_SIZE
                : size;
    }

    public static String toVaadinSize(String size) {
        return AUTO_SIZE.equalsIgnoreCase(size)
                ? Component.AUTO_SIZE
                : size;
    }

    @Nullable
    public static PopupPosition toVaadinPopupPosition(PopupView.PopupPosition popupPosition) {
        if (popupPosition == null) {
            return null;
        }

        for (PopupPosition position : PopupPosition.values()) {
            if (position.name().equals(popupPosition.name())) {
                return position;
            }
        }
        return null;
    }

    @Nullable
    public static PopupView.PopupPosition fromVaadinPopupPosition(PopupPosition popupPosition) {
        if (popupPosition == null) {
            return null;
        }

        for (PopupView.PopupPosition position : PopupView.PopupPosition.values()) {
            if (position.name().equals(popupPosition.name())) {
                return position;
            }
        }
        return null;
    }

    @Nullable
    public static TimeMode toVaadinTimeMode(TimeField.TimeMode timeMode) {
        for (TimeMode mode : TimeMode.values()) {
            if (mode.name().equals(timeMode.name())) {
                return mode;
            }
        }
        return null;
    }

    @Nullable
    public static TimeField.TimeMode fromVaadinTimeMode(TimeMode timeMode) {
        for (TimeField.TimeMode mode : TimeField.TimeMode.values()) {
            if (mode.name().equals(timeMode.name())) {
                return mode;
            }
        }
        return null;
    }
}