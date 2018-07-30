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
package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.cuba.gui.ComponentPalette;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.mainwindow.*;
import com.haulmont.cuba.gui.xml.layout.loaders.*;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LayoutLoaderConfig {

    private static final String WINDOW_TOP_LEVEL_TAG = "window";

    private static LayoutLoaderConfig sharedLoaders = new LayoutLoaderConfig();

    private static LayoutLoaderConfig windowLoaders = new LayoutLoaderConfig(sharedLoaders);
    private static LayoutLoaderConfig frameLoaders = new LayoutLoaderConfig(sharedLoaders);
    private static LayoutLoaderConfig lookupLoaders = new LayoutLoaderConfig(sharedLoaders);
    private static LayoutLoaderConfig editorLoaders = new LayoutLoaderConfig(sharedLoaders);

    private LayoutLoaderConfig parent;
    private Map<String, Class<? extends ComponentLoader>> loaders = new ConcurrentHashMap<>();

    public LayoutLoaderConfig() {
    }

    public LayoutLoaderConfig(LayoutLoaderConfig parent) {
        this.parent = parent;
    }

    static {
        sharedLoaders.register(HBoxLayout.NAME, HBoxLayoutLoader.class);
        sharedLoaders.register(VBoxLayout.NAME, VBoxLayoutLoader.class);
        sharedLoaders.register(GridLayout.NAME, GridLayoutLoader.class);
        sharedLoaders.register(ScrollBoxLayout.NAME, ScrollBoxLayoutLoader.class);
        sharedLoaders.register(GroupBoxLayout.NAME, GroupBoxLayoutLoader.class);
        sharedLoaders.register(HtmlBoxLayout.NAME, HtmlBoxLayoutLoader.class);
        sharedLoaders.register(FlowBoxLayout.NAME, FlowBoxLayoutLoader.class);
        sharedLoaders.register(CssLayout.NAME, CssLayoutLoader.class);

        sharedLoaders.register(Button.NAME, ButtonLoader.class);
        sharedLoaders.register(LinkButton.NAME, LinkButtonLoader.class);
        sharedLoaders.register(CheckBox.NAME, CheckBoxLoader.class);
        sharedLoaders.register(Label.NAME, LabelLoader.class);
        sharedLoaders.register(Link.NAME, LinkLoader.class);

        sharedLoaders.register(TextField.NAME, TextFieldLoader.class);
        sharedLoaders.register(MaskedField.NAME, MaskedFieldLoader.class);
        sharedLoaders.register(TextArea.NAME, ResizableTextAreaLoader.class);
        sharedLoaders.register(SourceCodeEditor.NAME, SourceCodeEditorLoader.class);
        sharedLoaders.register(PasswordField.NAME, PasswordFieldLoader.class);
        sharedLoaders.register(RichTextArea.NAME, RichTextAreaLoader.class);

        sharedLoaders.register(DateField.NAME, DateFieldLoader.class);
        sharedLoaders.register(TimeField.NAME, TimeFieldLoader.class);
        sharedLoaders.register(DatePicker.NAME, DatePickerLoader.class);
        sharedLoaders.register(LookupField.NAME, LookupFieldLoader.class);
        sharedLoaders.register(SuggestionField.NAME, SuggestionFieldLoader.class);
        sharedLoaders.register(SuggestionPickerField.NAME, SuggestionPickerFieldLoader.class);
        sharedLoaders.register(PickerField.NAME, PickerFieldLoader.class);
        sharedLoaders.register(ColorPicker.NAME, ColorPickerLoader.class);
        sharedLoaders.register(LookupPickerField.NAME, LookupPickerFieldLoader.class);
        sharedLoaders.register(SearchPickerField.NAME, SearchPickerFieldLoader.class);
        sharedLoaders.register(OptionsGroup.NAME, OptionsGroupLoader.class);
        sharedLoaders.register(CheckBoxGroup.NAME, CheckBoxGroupLoader.class);
        sharedLoaders.register(RadioButtonGroup.NAME, RadioButtonGroupLoader.class);
        sharedLoaders.register(OptionsList.NAME, OptionsListLoader.class);
        sharedLoaders.register(FileUploadField.NAME, FileUploadFieldLoader.class);
        sharedLoaders.register(FileMultiUploadField.NAME, FileMultiUploadFieldLoader.class);
        sharedLoaders.register(CurrencyField.NAME, CurrencyFieldLoader.class);

        sharedLoaders.register(Table.NAME, TableLoader.class);
        sharedLoaders.register(TreeTable.NAME, TreeTableLoader.class);
        sharedLoaders.register(GroupTable.NAME, GroupTableLoader.class);
        sharedLoaders.register(DataGrid.NAME, DataGridLoader.class);
        sharedLoaders.register(TreeDataGrid.NAME, TreeDataGridLoader.class);

        sharedLoaders.register(Calendar.NAME, CalendarLoader.class);

        sharedLoaders.register(Frame.NAME, FrameComponentLoader.class);
        sharedLoaders.register("iframe", FrameComponentLoader.class); // for backward compatibility
        sharedLoaders.register(RuntimePropertiesFrame.NAME, RuntimePropertiesFrameLoader.class);
        sharedLoaders.register(SplitPanel.NAME, SplitPanelLoader.class);
        sharedLoaders.register(Tree.NAME, TreeLoader.class);
        sharedLoaders.register(TabSheet.NAME, TabSheetLoader.class);
        sharedLoaders.register(Accordion.NAME, AccordionLoader.class);
        sharedLoaders.register(Embedded.NAME, EmbeddedLoader.class);
        sharedLoaders.register(Image.NAME, ImageLoader.class);
        sharedLoaders.register(BrowserFrame.NAME, BrowserFrameLoader.class);
        sharedLoaders.register(Filter.NAME, FilterLoader.class);
        sharedLoaders.register(ButtonsPanel.NAME, ButtonsPanelLoader.class);
        sharedLoaders.register(PopupButton.NAME, PopupButtonLoader.class);
        sharedLoaders.register(PopupView.NAME, PopupViewLoader.class);
        sharedLoaders.register(FieldGroup.NAME, FieldGroupLoader.class);
        sharedLoaders.register(TokenList.NAME, TokenListLoader.class);
        sharedLoaders.register(WidgetsTree.NAME, WidgetsTreeLoader.class);
        sharedLoaders.register(TwinColumn.NAME, TwinColumnLoader.class);
        sharedLoaders.register(ProgressBar.NAME, ProgressBarLoader.class);
        sharedLoaders.register(SearchField.NAME, SearchFieldLoader.class);
        sharedLoaders.register(RelatedEntities.NAME, RelatedEntitiesLoader.class);
        sharedLoaders.register(BulkEditor.NAME, BulkEditorLoader.class);
        sharedLoaders.register(CapsLockIndicator.NAME, CapsLockIndicatorLoader.class);

        /* Main window components */

        sharedLoaders.register(AppMenu.NAME, AppMenuLoader.class);
        sharedLoaders.register(AppWorkArea.NAME, AppWorkAreaLoader.class);
        sharedLoaders.register(LogoutButton.NAME, LogoutButtonLoader.class);
        sharedLoaders.register(NewWindowButton.NAME, NewWindowButtonLoader.class);
        sharedLoaders.register(UserIndicator.NAME, UserIndicatorLoader.class);
        sharedLoaders.register(FoldersPane.NAME, FoldersPaneLoader.class);
        sharedLoaders.register(FtsField.NAME, FtsFieldLoader.class);
        sharedLoaders.register(TimeZoneIndicator.NAME, TimeZoneIndicatorLoader.class);
        sharedLoaders.register(SideMenu.NAME, SideMenuLoader.class);

        // windows

        frameLoaders.register(WINDOW_TOP_LEVEL_TAG, FrameLoader.class);
        windowLoaders.register(WINDOW_TOP_LEVEL_TAG, WindowLoader.class);
        lookupLoaders.register(WINDOW_TOP_LEVEL_TAG, WindowLoader.Lookup.class);
        editorLoaders.register(WINDOW_TOP_LEVEL_TAG, WindowLoader.Editor.class);
    }

    public static void registerLoader(String tagName, Class<? extends ComponentLoader> aClass) {
        sharedLoaders.register(tagName, aClass);
    }

    /**
     * @deprecated Use {@link com.haulmont.cuba.gui.xml.layout.ExternalUIComponentsSource} or app-components mechanism
     */
    @Deprecated
    public static void registerLoaders(ComponentPalette... palettes) {
        for (ComponentPalette palette : palettes) {
            Map<String, Class<? extends ComponentLoader>> paletteLoaders = palette.getLoaders();
            for (Map.Entry<String, Class<? extends ComponentLoader>> loaderEntry : paletteLoaders.entrySet()) {
                sharedLoaders.register(loaderEntry.getKey(), loaderEntry.getValue());
            }
        }
    }

    public static LayoutLoaderConfig getWindowLoaders() {
        return windowLoaders;
    }

    public static LayoutLoaderConfig getEditorLoaders() {
        return editorLoaders;
    }

    public static LayoutLoaderConfig getFrameLoaders() {
        return frameLoaders;
    }

    public static LayoutLoaderConfig getLookupLoaders() {
        return lookupLoaders;
    }

    @Nullable
    public Class<? extends ComponentLoader> getLoader(String name) {
        Class<? extends ComponentLoader> loader = loaders.get(name);
        if (loader != null) {
            return loader;
        }

        if (parent != null) {
            return parent.getLoader(name);
        }

        return null;
    }

    public static void registerWindowLoader(Class<? extends WindowLoader> loader) {
        windowLoaders.register(WINDOW_TOP_LEVEL_TAG, loader);
    }

    public static void registerFrameLoader(Class<? extends FrameLoader> loader) {
        frameLoaders.register(WINDOW_TOP_LEVEL_TAG, loader);
    }

    public static void registerEditorLoader(Class<? extends WindowLoader.Editor> loader) {
        editorLoaders.register(WINDOW_TOP_LEVEL_TAG, loader);
    }

    public static void registerLookupLoader(Class<? extends WindowLoader.Lookup> loader) {
        lookupLoaders.register(WINDOW_TOP_LEVEL_TAG, loader);
    }

    protected void register(String tagName, Class<? extends ComponentLoader> loaderClass) {
        loaders.put(tagName, loaderClass);
    }
}