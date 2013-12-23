/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.cuba.gui.ComponentPalette;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.loaders.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author abramov
 * @version $Id$
 */
public class LayoutLoaderConfig {

    private Map<String, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader>> loaders = new HashMap<>();

    private static LayoutLoaderConfig windowLoaders = new LayoutLoaderConfig();
    private static LayoutLoaderConfig editorLoaders = new LayoutLoaderConfig();
    private static LayoutLoaderConfig lookupLoaders = new LayoutLoaderConfig();
    private static LayoutLoaderConfig frameLoaders = new LayoutLoaderConfig();

    private static Map<String, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader>> customLoaders =
            new HashMap<>();

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
        config.register(BoxLayout.HBOX, HBoxLoader.class);
        config.register(BoxLayout.VBOX, VBoxLoader.class);
        config.register(GridLayout.NAME, GridLayoutLoader.class);
        config.register(ScrollBoxLayout.NAME, ScrollBoxLayoutLoader.class);
        config.register("scrollbox", ScrollBoxLayoutLoader.class); // for backward compatibility
        config.register(GroupBoxLayout.NAME, GroupBoxLayoutLoader.class);
        config.register(ToggleBoxLayout.NAME, ToggleBoxLayoutLoader.class);
        config.register("togglebox", ToggleBoxLayoutLoader.class); // for backward compatibility
        config.register(HtmlBoxLayout.NAME, HtmlBoxLayoutLoader.class);
        config.register("htmlbox", HtmlBoxLayoutLoader.class); // for backward compatibility
        config.register(FlowBoxLayout.NAME, FlowBoxLayoutLoader.class);
        config.register("flowbox", FlowBoxLayoutLoader.class); // for backward compatibility

        config.register(Button.NAME, ButtonLoader.class);
        config.register(LinkButton.NAME, ButtonLoader.class);
        config.register(CheckBox.NAME, AbstractFieldLoader.class);
        config.register(Label.NAME, LabelLoader.class);
        config.register(Link.NAME, LinkLoader.class);

        config.register(TextField.NAME, TextFieldLoader.class);
        config.register(MaskedField.NAME, MaskedFieldLoader.class);
        config.register(TextArea.NAME, ResizableTextFieldLoader.class);
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
        config.register(FileUploadField.NAME, FileUploadFieldLoader.class);
        config.register(FileMultiUploadField.NAME, FileUploadFieldLoader.class);
        config.register("multiupload", FileUploadFieldLoader.class); // for backward compatibility
        config.register(Table.NAME, TableLoader.class);
        config.register(TreeTable.NAME, TreeTableLoader.class);
        config.register(GroupTable.NAME, GroupTableLoader.class);
        config.register(IFrame.NAME, IFrameLoader.class);
        config.register(RuntimePropertiesFrame.NAME, RuntimePropertiesFrameLoader.class);
        config.register(SplitPanel.NAME, SplitPanelLoader.class);
        config.register(Tree.NAME, TreeLoader.class);
        config.register(TabSheet.NAME, TabSheetLoader.class);
        config.register("tabsheet", TabSheetLoader.class); // for backward compatibility
        config.register(Embedded.NAME, EmbeddedLoader.class);
        config.register(Filter.NAME, FilterLoader.class);
        config.register(AccessControl.NAME, AccessControlLoader.class);
        config.register(ButtonsPanel.NAME, ButtonsPanelLoader.class);
        config.register(ActionsField.NAME, ActionsFieldLoader.class);
        config.register(PopupButton.NAME, PopupButtonLoader.class);
        config.register(FieldGroup.NAME, FieldGroupLoader.class);
        config.register(TokenList.NAME, TokenListLoader.class);
        config.register(WidgetsTree.NAME, WidgetsTreeLoader.class);
        config.register(TwinColumn.NAME, TwinColumnLoader.class);
        config.register(ProgressBar.NAME, ProgressBarLoader.class);
        config.register(SearchField.NAME, SearchFieldLoader.class);
    }

    public static void registerLoader(String tagName, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> aClass) {
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

    public Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> getLoader(String name) {
        final Class<? extends ComponentLoader> loader = customLoaders.get(name);
        if (loader == null) {
            return loaders.get(name);
        }

        return loader;
    }

    protected void register(String name, Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> loader) {
        loaders.put(name, loader);
    }
}