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
 */

package com.haulmont.cuba.web.theme;

import com.vaadin.ui.Table;

public class HaloTheme {

    /**
     * Header style for main application headings. Can be combined with any other Label style.
     */
    public static final String LABEL_H1 = "h1";

    /**
     * Header style for different sections in the application. Can be combined with any other Label style.
     */
    public static final String LABEL_H2 = "h2";

    /**
     * Header style for different sub-sections in the application. Can be combined with any other Label style.
     */
    public static final String LABEL_H3 = "h3";

    /**
     * Header style for different sub-sections in the application. Can be combined with any other Label style.
     */
    public static final String LABEL_H4 = "h4";

    /**
     * A utility style that can be combined with the {@link #LABEL_H1}, {@link #LABEL_H2}, {@link #LABEL_H3} and
     * {@link #LABEL_H4} styles to remove the default margins from the header.
     */
    public static final String LABEL_NO_MARGIN = "no-margin";

    /**
     * Lighter font weight. Suitable for additional/supplementary UI text. Can be combined with any other Label style.
     */
    public static final String LABEL_LIGHT = "light";

    /**
     * Bolder font weight. Suitable for important/prominent UI text. Can be combined with any other Label style.
     */
    public static final String LABEL_BOLD = "bold";

    /**
     * Colored text. Can be combined with any other Label style.
     */
    public static final String LABEL_COLORED = "colored";

    /**
     * Success badge style. Adds a border around the label and an icon next to the text. Suitable for UI notifications
     * that need to in the direct context of some component. Can be combined with any other Label style.
     */
    public static final String LABEL_SUCCESS = "success";

    /**
     * Failure badge style. Adds a border around the label and an icon next to the text. Suitable for UI notifications
     * that need to in the direct context of some component. Can be combined with any other Label style.
     */
    public static final String LABEL_FAILURE = "failure";

    /**
     * Spinner style. Add this style name to an empty Label to create a spinner.
     *
     * <h4>Example</h4>
     *
     * <pre>
     * Label spinner = new Label();
     * spinner.addStyleName(ValoTheme.LABEL_SPINNER);
     * </pre>
     */
    public static final String LABEL_SPINNER = "spinner";

    /**
     * Primary action button (e.g. the button that should get activated when the user presses the <code>enter</code>
     * key in a form). Use sparingly, only one default button per view should be visible. Can be combined with any
     * other Button style.
     */
    public static final String BUTTON_PRIMARY = "primary";

    /**
     * A prominent button that can be used instead of the {@link #BUTTON_PRIMARY} for primary actions when
     * the action is considered <b>safe</b> for the user (i.e. does not cause any data loss or any other
     * irreversible action). Can be combined with any other Button style.
     */
    public static final String BUTTON_FRIENDLY = "friendly";

    /**
     * A prominent button that can be used when the action is considered <b>unsafe</b> for the user (i.e. it causes
     * data loss or some other irreversible action). Can be combined with any other Button style.
     */
    public static final String BUTTON_DANGER = "danger";

    /**
     * "Quiet" button, which looks like {@link com.vaadin.ui.themes.ValoTheme#BUTTON_BORDERLESS} until you
     * hover over it with the mouse. Can be combined with any other Button
     * style.
     */
    public static final String BUTTON_QUIET = "quiet";

    /**
     * Align the icon to the right side of the button caption. Can be combined with any other Button style.
     */
    public static final String BUTTON_ICON_ALIGN_RIGHT = "icon-align-right";

    /**
     * Stack the icon on top of the button caption. Can be combined with any other Button style.
     */
    public static final String BUTTON_ICON_ALIGN_TOP = "icon-align-top";

    /**
     * Only show the icon in the button, and size the button to a square shape.
     */
    public static final String BUTTON_ICON_ONLY = "icon-only";

    /**
     * Borderless button. Can be combined with any other Button style.
     */
    public static final String BUTTON_BORDERLESS = "borderless";

    /**
     * Borderless button with a colored caption text. Can be combined with any other Button style.
     */
    public static final String BUTTON_BORDERLESS_COLORED = "borderless-colored";

    /**
     * Removes the border and background from the text field. Can be combined with any other TextField style.
     */
    public static final String TEXTFIELD_BORDERLESS = "borderless";

    /**
     * Align the text inside the field to the right. Can be combined with any other TextField style.
     */
    public static final String TEXTFIELD_ALIGN_RIGHT = "align-right";

    /**
     * Align the text inside the field to center. Can be combined with any other TextField style.
     */
    public static final String TEXTFIELD_ALIGN_CENTER = "align-center";

    /**
     * Move the default caption icon inside the text field. Can be combined with any other TextField style.
     */
    public static final String TEXTFIELD_INLINE_ICON = "inline-icon";

    /**
     * Removes the border and background from the text area. Can be combined with any other TextArea style.
     */
    public static final String TEXTAREA_BORDERLESS = "borderless";

    /**
     * Align the text inside the area to the right. Can be combined with any other TextArea style.
     */
    public static final String TEXTAREA_ALIGN_RIGHT = "align-right";

    /**
     * Align the text inside the area to center. Can be combined with any other TextArea style.
     */
    public static final String TEXTAREA_ALIGN_CENTER = "align-center";

    /**
     * Removes the border and background from the date field. Can be combined with any other DateField style.
     */
    public static final String DATEFIELD_BORDERLESS = "borderless";

    /**
     * Removes the border and background from the combo box. Can be combined with any other ComboBox style.
     *
     * This style can also be used for {@link com.haulmont.cuba.gui.components.PickerField} and
     * {@link com.haulmont.cuba.gui.components.LookupPickerField}
     */
    public static final String LOOKUPFIELD_BORDERLESS = "borderless";

    /**
     * Align the text inside the combo box to the right. Can be combined with any other TextField style.
     *
     * This style can also be used for {@link com.haulmont.cuba.gui.components.PickerField} and
     * {@link com.haulmont.cuba.gui.components.LookupPickerField}
     */
    public static final String LOOKUPFIELD_ALIGN_RIGHT = "align-right";

    /**
     * Align the text inside the combo box to center. Can be combined with any other TextField style.
     *
     * This style can also be used for {@link com.haulmont.cuba.gui.components.PickerField} and
     * {@link com.haulmont.cuba.gui.components.LookupPickerField}
     */
    public static final String LOOKUPFIELD_ALIGN_CENTER = "align-center";

    /**
     * Remove the alternating row colors. Can be combined with any other Table/TreeTable style.
     */
    public static final String TABLE_NO_STRIPES = "no-stripes";

    /**
     * Remove the vertical divider lines between the table columns. Can be combined with any other
     * Table/TreeTable style.
     */
    public static final String TABLE_NO_VERTICAL_LINES = "no-vertical-lines";

    /**
     * Remove the horizontal divider lines between the table rows. Can be combined with any other Table/TreeTable style.
     */
    public static final String TABLE_NO_HORIZONTAL_LINES = "no-horizontal-lines";

    /**
     * Hide the table column headers (effectively the same as {@link Table.ColumnHeaderMode#HIDDEN}). Can be combined
     * with any other Table/TreeTable style.
     */
    public static final String TABLE_NO_HEADER = "no-header";

    /**
     * Remove the outer border of the table. Can be combined with any other Table/TreeTable style.
     */
    public static final String TABLE_BORDERLESS = "borderless";

    /**
     * Reduce the white space inside the table cells. Can be combined with any other Table/TreeTable style.
     */
    public static final String TABLE_COMPACT = "compact";

    /**
     * Small font size and reduced the white space inside the table cells. Can be combined with any other
     * Table/TreeTable style.
     */
    public static final String TABLE_SMALL = "small";

    /**
     * Make the progress bar indicator appear as a dot which progresses over the progress bar track (instead of
     * a growing bar).
     */
    public static final String PROGRESSBAR_POINT = "point";

    /**
     * Make the split handle wider.
     */
    public static final String SPLITPANEL_LARGE = "large";

    /**
     * Adds a border around the whole component as well as around individual tabs in the tab bar. Can be combined
     * with any other TabSheet style.
     */
    public static final String TABSHEET_FRAMED = "framed";

    /**
     * Center the tabs inside the tab bar. Works best if all the tabs fit completely in the tab bar (i.e. no tab bar
     * scrolling). Can be combined with any other TabSheet style.
     */
    public static final String TABSHEET_CENTERED_TABS = "centered-tabs";

    /**
     * Add a small amount of padding around the tabs in the tab bar, so that they don't touch the outer edges of the
     * component. Can be combined with any other TabSheet style.
     */
    public static final String TABSHEET_PADDED_TABBAR = "padded-tabbar";

    /**
     * Give equal amount of space to all tabs in the tab bar (.i.e expand ratio == 1 for all tabs). The tab captions
     * will be truncated if they do not fit in to the tab. Tab scrolling will be disabled when this style is applied
     * (all tabs will be visible at the same time). Can be combined with any other TabSheet style.
     */
    public static final String TABSHEET_EQUAL_WIDTH_TABS = "equal-width-tabs";

    /**
     * Reduce the whitespace around the tabs in the tab bar. Can be combined with any other TabSheet style.
     */
    public static final String TABSHEET_COMPACT_TABBAR = "compact-tabbar";

    /**
     * Display tab icons on top of the tab captions (by default the icons are place on the left side of the caption).
     * Can be combined with any other TabSheet style.
     */
    public static final String TABSHEET_ICONS_ON_TOP = "icons-on-top";

    /**
     * Only the selected tab has the close button visible. Does not prevent closing the tab programmatically,
     * it only hides the button from the end user. Can be combined with any other TabSheet style.
     */
    public static final String TABSHEET_ONLY_SELECTED_TAB_IS_CLOSABLE = "only-selected-closable";

    /**
     * Remove the outer border from the accordion. Can be combined with any other Accordion style.
     */
    public static final String ACCORDION_BORDERLESS = "borderless";

    /**
     * Remove borders and the background color of the GroupBox. Can be combined with any other GroupBox style.
     * <p>
     * Notice that you should enable flag "showAsPanel".
     */
    public static final String GROUPBOX_PANEL_BORDERLESS = "borderless";

    /**
     * Make any layout inside of another layout with {@link #LAYOUT_CARD} or {@link #LAYOUT_WELL} stylename looks like
     * caption of parent layout.
     */
    public static final String LAYOUT_HEADER = "v-panel-caption";

    /**
     * Make a layout look like a card. Add an additional <code>v-panel-caption</code> style name to any layout
     * inside the card layout to make it look like a layout header.
     */
    public static final String LAYOUT_CARD = "card";

    /**
     * Inset layout style. Add an additional <code>v-panel-caption</code> style name to any layout inside
     * the card layout to make it look like a layout header.
     */
    public static final String LAYOUT_WELL = "well";

    /**
     * Add this style name to a CssLayout to create a grouped set of components, i.e. a row of components
     * which are joined seamlessly together.
     * <p>
     * <h4>Example</h4>
     * <p>
     * <pre>
     * CssLayout group = new CssLayout();
     * group.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
     *
     * TextField field = new TextField();
     * group.addComponent(field);
     *
     * Button button = new Button(&quot;Action&quot;);
     * group.addComponent(button);
     * </pre>
     */
    public static final String CSSLAYOUT_COMPONENT_GROUP = "v-component-group";
}