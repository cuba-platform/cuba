/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui;

import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.gui.ComponentPalette;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.mainwindow.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.*;
import com.haulmont.cuba.web.gui.components.mainwindow.*;

import javax.annotation.ManagedBean;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(ComponentsFactory.NAME)
public class WebComponentsFactory implements ComponentsFactory {

    private static Map<String, Class<? extends Component>> classes = new HashMap<>();

    private static Map<Class, String> names = new ConcurrentHashMap<>();

    static {
        classes.put(Window.NAME, WebWindow.class);
        classes.put(Window.Editor.NAME, WebWindow.Editor.class);
        classes.put(Window.Lookup.NAME, WebWindow.Lookup.class);

        classes.put(HBoxLayout.NAME, WebHBoxLayout.class);
        classes.put(VBoxLayout.NAME, WebVBoxLayout.class);
        classes.put(GridLayout.NAME, WebGridLayout.class);
        classes.put(ScrollBoxLayout.NAME, WebScrollBoxLayout.class);
        classes.put(HtmlBoxLayout.NAME, WebHtmlBoxLayout.class);
        classes.put(FlowBoxLayout.NAME, WebFlowBoxLayout.class);

        classes.put(Button.NAME, WebButton.class);
        classes.put(LinkButton.NAME, WebLinkButton.class);
        classes.put(Label.NAME, WebLabel.class);
        classes.put(Link.NAME, WebLink.class);
        classes.put(CheckBox.NAME, WebCheckBox.class);
        classes.put(GroupBoxLayout.NAME, WebGroupBox.class);
        classes.put(SourceCodeEditor.NAME, WebSourceCodeEditor.class);
        classes.put(TextField.NAME, WebTextField.class);
        classes.put(PasswordField.NAME, WebPasswordField.class);
        // Use resizable text area instead of text area
        classes.put(ResizableTextArea.NAME, WebResizableTextArea.class);
        classes.put(RichTextArea.NAME, WebRichTextArea.class);
        classes.put(MaskedField.NAME, WebMaskedField.class);

        classes.put(Frame.NAME, WebFrame.class);
        classes.put(Table.NAME, WebTable.class);
        classes.put(TreeTable.NAME, WebTreeTable.class);
        classes.put(GroupTable.NAME, WebGroupTable.class);
        classes.put(DateField.NAME, WebDateField.class);
        classes.put(TimeField.NAME, WebTimeField.class);
        classes.put(LookupField.NAME, WebLookupField.class);
        classes.put(SearchField.NAME, WebSearchField.class);
        classes.put(PickerField.NAME, WebPickerField.class);
        classes.put(LookupPickerField.NAME, WebLookupPickerField.class);
        classes.put(SearchPickerField.NAME, WebSearchPickerField.class);
        classes.put(OptionsGroup.NAME, WebOptionsGroup.class);
        classes.put(FileUploadField.NAME, WebFileUploadField.class);
        classes.put(FileMultiUploadField.NAME, WebFileMultiUploadField.class);
        classes.put(SplitPanel.NAME, WebSplitPanel.class);
        classes.put(Tree.NAME, WebTree.class);
        classes.put(TabSheet.NAME, WebTabSheet.class);
        classes.put(Embedded.NAME, WebEmbedded.class);
        classes.put(Filter.NAME, WebFilter.class);
        classes.put(ButtonsPanel.NAME, WebButtonsPanel.class);
        classes.put(PopupButton.NAME, WebPopupButton.class);

        classes.put(FieldGroup.NAME, WebFieldGroup.class);
        classes.put(TokenList.NAME, WebTokenList.class);
        classes.put(WidgetsTree.NAME, WebWidgetsTree.class);
        classes.put(TwinColumn.NAME, WebTwinColumn.class);
        classes.put(ProgressBar.NAME, WebProgressBar.class);
        classes.put(RowsCount.NAME, WebRowsCount.class);
        classes.put(RelatedEntities.NAME, WebRelatedEntities.class);
        classes.put(BulkEditor.NAME, WebBulkEditor.class);

        classes.put(EntityLinkField.NAME, WebEntityLinkField.class);

        /* Main window components */

        classes.put(AppMenu.NAME, WebAppMenu.class);
        classes.put(AppWorkArea.NAME, WebAppWorkArea.class);
        classes.put(LogoutButton.NAME, WebLogoutButton.class);
        classes.put(NewWindowButton.NAME, WebNewWindowButton.class);
        classes.put(UserIndicator.NAME, WebUserIndicator.class);
        classes.put(FoldersPane.NAME, WebFoldersPane.class);
        classes.put(FtsField.NAME, WebFtsField.class);
        classes.put(TimeZoneIndicator.NAME, WebTimeZoneIndicator.class);
    }

    public static void registerComponent(String element, Class<? extends Component> componentClass) {
        classes.put(element, componentClass);
    }

    public static void registerComponents(ComponentPalette... palettes) {
        for (ComponentPalette palette : palettes) {
            Map<String, Class<? extends Component>> loaders = palette.getComponents();
            for (Map.Entry<String, Class<? extends Component>> loaderEntry : loaders.entrySet()) {
                classes.put(loaderEntry.getKey(), loaderEntry.getValue());
            }
        }
    }

    @Override
    public Component createComponent(String name) {
        final Class<? extends Component> componentClass = classes.get(name);
        if (componentClass == null) {
            throw new IllegalStateException(String.format("Can't find component class for '%s'", name));
        }
        try {
            return componentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Error creating the '" + name + "' component instance", e);
        }
    }

    @Override
    public <T extends Component> T createComponent(Class<T> type) {
        String name = names.get(type);
        if (name == null) {
            java.lang.reflect.Field nameField;
            try {
                nameField = type.getField("NAME");
                name = (String) nameField.get(null);
            } catch (NoSuchFieldException | IllegalAccessException ignore) {
            }
            if (name == null)
                throw new DevelopmentException(String.format("Class '%s' doesn't have NAME field", type.getName()));
            else
                names.put(type, name);
        }
        return type.cast(createComponent(name));
    }

    @Override
    public Timer createTimer() {
        return new WebTimer();
    }
}