/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.cuba.gui.ComponentPalette;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.mainwindow.*;
import com.haulmont.cuba.gui.xml.layout.loaders.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author abramov
 * @version $Id$
 */
public class LayoutLoaderConfig {

    private Map<String, Class<? extends ComponentLoader>> loaders = new HashMap<>();

    private static LayoutLoaderConfig windowLoaders = new LayoutLoaderConfig();
    private static LayoutLoaderConfig editorLoaders = new LayoutLoaderConfig();
    private static LayoutLoaderConfig lookupLoaders = new LayoutLoaderConfig();
    private static LayoutLoaderConfig frameLoaders = new LayoutLoaderConfig();

    private static Map<String, Class<? extends ComponentLoader>> customLoaders = new HashMap<>();

    static {
        windowLoaders.register("window", WindowLoader.class);
        registerComponents(windowLoaders);

        editorLoaders.register("window", WindowLoader.Editor.class);
        registerComponents(editorLoaders);

        lookupLoaders.register("window", WindowLoader.Lookup.class);
        registerComponents(lookupLoaders);

        frameLoaders.register("window", FrameLoader.class);
        registerComponents(frameLoaders);
    }

    private static void registerComponents(LayoutLoaderConfig config) {
        config.register(HBoxLayout.NAME, HBoxLayoutLoader.class);
        config.register(VBoxLayout.NAME, VBoxLayoutLoader.class);
        config.register(GridLayout.NAME, GridLayoutLoader.class);
        config.register(ScrollBoxLayout.NAME, ScrollBoxLayoutLoader.class);
        config.register(GroupBoxLayout.NAME, GroupBoxLayoutLoader.class);
        config.register(HtmlBoxLayout.NAME, HtmlBoxLayoutLoader.class);
        config.register(FlowBoxLayout.NAME, FlowBoxLayoutLoader.class);

        config.register(Button.NAME, ButtonLoader.class);
        config.register(LinkButton.NAME, LinkButtonLoader.class);
        config.register(CheckBox.NAME, CheckBoxLoader.class);
        config.register(Label.NAME, LabelLoader.class);
        config.register(Link.NAME, LinkLoader.class);

        config.register(TextField.NAME, TextFieldLoader.class);
        config.register(MaskedField.NAME, MaskedFieldLoader.class);
        config.register(TextArea.NAME, ResizableTextAreaLoader.class);
        config.register(SourceCodeEditor.NAME, SourceCodeEditorLoader.class);
        config.register(PasswordField.NAME, PasswordFieldLoader.class);
        config.register(RichTextArea.NAME, RichTextAreaLoader.class);

        config.register(DateField.NAME, DateFieldLoader.class);
        config.register(TimeField.NAME, TimeFieldLoader.class);
        config.register(LookupField.NAME, LookupFieldLoader.class);
        config.register(PickerField.NAME, PickerFieldLoader.class);
        config.register(LookupPickerField.NAME, LookupPickerFieldLoader.class);
        config.register(SearchPickerField.NAME, SearchPickerFieldLoader.class);
        config.register(OptionsGroup.NAME, OptionsGroupLoader.class);
        config.register(OptionsList.NAME, OptionsListLoader.class);
        config.register(FileUploadField.NAME, FileUploadFieldLoader.class);
        config.register(FileMultiUploadField.NAME, FileMultiUploadFieldLoader.class);

        config.register(Table.NAME, TableLoader.class);
        config.register(TreeTable.NAME, TreeTableLoader.class);
        config.register(GroupTable.NAME, GroupTableLoader.class);

        config.register(Frame.NAME, FrameComponentLoader.class);
        config.register("iframe", FrameComponentLoader.class); // for backward compatibility
        config.register(RuntimePropertiesFrame.NAME, RuntimePropertiesFrameLoader.class);
        config.register(SplitPanel.NAME, SplitPanelLoader.class);
        config.register(Tree.NAME, TreeLoader.class);
        config.register(TabSheet.NAME, TabSheetLoader.class);
        config.register(Embedded.NAME, EmbeddedLoader.class);
        config.register(Filter.NAME, FilterLoader.class);
        config.register(ButtonsPanel.NAME, ButtonsPanelLoader.class);
        config.register(PopupButton.NAME, PopupButtonLoader.class);
        config.register(FieldGroup.NAME, FieldGroupLoader.class);
        config.register(TokenList.NAME, TokenListLoader.class);
        config.register(WidgetsTree.NAME, WidgetsTreeLoader.class);
        config.register(TwinColumn.NAME, TwinColumnLoader.class);
        config.register(ProgressBar.NAME, ProgressBarLoader.class);
        config.register(SearchField.NAME, SearchFieldLoader.class);
        config.register(RelatedEntities.NAME, RelatedEntitiesLoader.class);
        config.register(BulkEditor.NAME, BulkEditorLoader.class);

        /* Main window components */

        config.register(AppMenu.NAME, AppMenuLoader.class);
        config.register(AppWorkArea.NAME, AppWorkAreaLoader.class);
        config.register(LogoutButton.NAME, LogoutButtonLoader.class);
        config.register(NewWindowButton.NAME, NewWindowButtonLoader.class);
        config.register(UserIndicator.NAME, UserIndicatorLoader.class);
        config.register(FoldersPane.NAME, FoldersPaneLoader.class);
        config.register(FtsField.NAME, FtsFieldLoader.class);
        config.register(TimeZoneIndicator.NAME, TimeZoneIndicatorLoader.class);
    }

    public static void registerLoader(String tagName, Class<? extends ComponentLoader> aClass) {
        customLoaders.put(tagName, aClass);
    }

    public static void registerLoaders(ComponentPalette... palettes) {
        for (ComponentPalette palette : palettes) {
            Map<String, Class<? extends ComponentLoader>> loaders = palette.getLoaders();
            for (Map.Entry<String, Class<? extends ComponentLoader>> loaderEntry : loaders.entrySet()) {
                customLoaders.put(loaderEntry.getKey(), loaderEntry.getValue());
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

    public Class<? extends ComponentLoader> getLoader(String name) {
        final Class<? extends ComponentLoader> loader = customLoaders.get(name);
        if (loader == null) {
            return loaders.get(name);
        }

        return loader;
    }

    protected void register(String name, Class<? extends ComponentLoader> loader) {
        loaders.put(name, loader);
    }
}