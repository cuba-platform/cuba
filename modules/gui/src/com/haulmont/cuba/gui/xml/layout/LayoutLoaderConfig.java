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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LayoutLoaderConfig {

    private static Map<String, Class<? extends ComponentLoader>> loaders = new ConcurrentHashMap<>();

    private static Class<? extends WindowLoader> windowLoader = WindowLoader.class;
    private static Class<? extends FrameLoader> frameLoader = FrameLoader.class;
    private static Class<? extends WindowLoader.Editor> editorLoader = WindowLoader.Editor.class;
    private static Class<? extends WindowLoader.Lookup> lookupLoader = WindowLoader.Lookup.class;

    private static LayoutLoaderConfig llc = new LayoutLoaderConfig();

    static {
        loaders.put(HBoxLayout.NAME, HBoxLayoutLoader.class);
        loaders.put(VBoxLayout.NAME, VBoxLayoutLoader.class);
        loaders.put(GridLayout.NAME, GridLayoutLoader.class);
        loaders.put(ScrollBoxLayout.NAME, ScrollBoxLayoutLoader.class);
        loaders.put(GroupBoxLayout.NAME, GroupBoxLayoutLoader.class);
        loaders.put(HtmlBoxLayout.NAME, HtmlBoxLayoutLoader.class);
        loaders.put(FlowBoxLayout.NAME, FlowBoxLayoutLoader.class);
        loaders.put(CssLayout.NAME, CssLayoutLoader.class);

        loaders.put(Button.NAME, ButtonLoader.class);
        loaders.put(LinkButton.NAME, LinkButtonLoader.class);
        loaders.put(CheckBox.NAME, CheckBoxLoader.class);
        loaders.put(Label.NAME, LabelLoader.class);
        loaders.put(Link.NAME, LinkLoader.class);

        loaders.put(TextField.NAME, TextFieldLoader.class);
        loaders.put(MaskedField.NAME, MaskedFieldLoader.class);
        loaders.put(TextArea.NAME, ResizableTextAreaLoader.class);
        loaders.put(SourceCodeEditor.NAME, SourceCodeEditorLoader.class);
        loaders.put(PasswordField.NAME, PasswordFieldLoader.class);
        loaders.put(RichTextArea.NAME, RichTextAreaLoader.class);

        loaders.put(DateField.NAME, DateFieldLoader.class);
        loaders.put(TimeField.NAME, TimeFieldLoader.class);
        loaders.put(DatePicker.NAME, DatePickerLoader.class);
        loaders.put(LookupField.NAME, LookupFieldLoader.class);
        loaders.put(SuggestionField.NAME, SuggestionFieldLoader.class);
        loaders.put(SuggestionPickerField.NAME, SuggestionPickerFieldLoader.class);
        loaders.put(PickerField.NAME, PickerFieldLoader.class);
        loaders.put(ColorPicker.NAME, ColorPickerLoader.class);
        loaders.put(LookupPickerField.NAME, LookupPickerFieldLoader.class);
        loaders.put(SearchPickerField.NAME, SearchPickerFieldLoader.class);
        loaders.put(OptionsGroup.NAME, OptionsGroupLoader.class);
        loaders.put(OptionsList.NAME, OptionsListLoader.class);
        loaders.put(FileUploadField.NAME, FileUploadFieldLoader.class);
        loaders.put(FileMultiUploadField.NAME, FileMultiUploadFieldLoader.class);
        loaders.put(CurrencyField.NAME, CurrencyFieldLoader.class);

        loaders.put(Table.NAME, TableLoader.class);
        loaders.put(TreeTable.NAME, TreeTableLoader.class);
        loaders.put(GroupTable.NAME, GroupTableLoader.class);
        loaders.put(DataGrid.NAME, DataGridLoader.class);

        loaders.put(Calendar.NAME, CalendarLoader.class);

        loaders.put(Frame.NAME, FrameComponentLoader.class);
        loaders.put("iframe", FrameComponentLoader.class); // for backward compatibility
        loaders.put(RuntimePropertiesFrame.NAME, RuntimePropertiesFrameLoader.class);
        loaders.put(SplitPanel.NAME, SplitPanelLoader.class);
        loaders.put(Tree.NAME, TreeLoader.class);
        loaders.put(TabSheet.NAME, TabSheetLoader.class);
        loaders.put(Accordion.NAME, AccordionLoader.class);
        loaders.put(Embedded.NAME, EmbeddedLoader.class);
        loaders.put(Image.NAME, ImageLoader.class);
        loaders.put(BrowserFrame.NAME, BrowserFrameLoader.class);
        loaders.put(Filter.NAME, FilterLoader.class);
        loaders.put(ButtonsPanel.NAME, ButtonsPanelLoader.class);
        loaders.put(PopupButton.NAME, PopupButtonLoader.class);
        loaders.put(PopupView.NAME, PopupViewLoader.class);
        loaders.put(FieldGroup.NAME, FieldGroupLoader.class);
        loaders.put(TokenList.NAME, TokenListLoader.class);
        loaders.put(WidgetsTree.NAME, WidgetsTreeLoader.class);
        loaders.put(TwinColumn.NAME, TwinColumnLoader.class);
        loaders.put(ProgressBar.NAME, ProgressBarLoader.class);
        loaders.put(SearchField.NAME, SearchFieldLoader.class);
        loaders.put(RelatedEntities.NAME, RelatedEntitiesLoader.class);
        loaders.put(BulkEditor.NAME, BulkEditorLoader.class);
        loaders.put(CapsLockIndicator.NAME, CapsLockIndicatorLoader.class);

        /* Main window components */

        loaders.put(AppMenu.NAME, AppMenuLoader.class);
        loaders.put(AppWorkArea.NAME, AppWorkAreaLoader.class);
        loaders.put(LogoutButton.NAME, LogoutButtonLoader.class);
        loaders.put(NewWindowButton.NAME, NewWindowButtonLoader.class);
        loaders.put(UserIndicator.NAME, UserIndicatorLoader.class);
        loaders.put(FoldersPane.NAME, FoldersPaneLoader.class);
        loaders.put(FtsField.NAME, FtsFieldLoader.class);
        loaders.put(TimeZoneIndicator.NAME, TimeZoneIndicatorLoader.class);
        loaders.put(SideMenu.NAME, SideMenuLoader.class);
    }

    public static void registerLoader(String tagName, Class<? extends ComponentLoader> aClass) {
        loaders.put(tagName, aClass);
    }

    /**
     * @deprecated Use {@link com.haulmont.cuba.gui.xml.layout.ExternalUIComponentsSource} or app-components mechanism
     */
    @Deprecated
    public static void registerLoaders(ComponentPalette... palettes) {
        for (ComponentPalette palette : palettes) {
            Map<String, Class<? extends ComponentLoader>> paletteLoaders = palette.getLoaders();
            for (Map.Entry<String, Class<? extends ComponentLoader>> loaderEntry : paletteLoaders.entrySet()) {
                loaders.put(loaderEntry.getKey(), loaderEntry.getValue());
            }
        }
    }

    public static LayoutLoaderConfig getWindowLoaders() {
        llc.register("window", windowLoader);
        return llc;
    }

    public static LayoutLoaderConfig getEditorLoaders() {
        llc.register("window", editorLoader);
        return llc;
    }

    public static LayoutLoaderConfig getFrameLoaders() {
        llc.register("window", frameLoader);
        return llc;
    }

    public static LayoutLoaderConfig getLookupLoaders() {
        llc.register("window", lookupLoader);
        return llc;
    }

    public Class<? extends ComponentLoader> getLoader(String name) {
        return loaders.get(name);
    }

    public static void registerWindowLoader(Class<? extends WindowLoader> loader) {
        windowLoader = loader;
    }

    public static void registerFrameLoader(Class<? extends FrameLoader> loader) {
        frameLoader = loader;
    }

    public static void registerEditorLoader(Class<? extends WindowLoader.Editor> loader) {
        editorLoader = loader;
    }

    public static void registerLookupLoader(Class<? extends WindowLoader.Lookup> loader) {
        lookupLoader = loader;
    }

    protected void register(String tagName, Class<? extends ComponentLoader> loaderClass) {
        loaders.put(tagName, loaderClass);
    }
}