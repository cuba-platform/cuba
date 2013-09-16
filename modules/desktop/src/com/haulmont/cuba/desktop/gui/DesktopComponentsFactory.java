/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui;

import com.haulmont.cuba.desktop.gui.components.*;
import com.haulmont.cuba.desktop.gui.components.filter.DesktopFilter;
import com.haulmont.cuba.gui.ComponentPalette;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.annotation.ManagedBean;
import java.util.HashMap;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(ComponentsFactory.NAME)
public class DesktopComponentsFactory implements ComponentsFactory {

    private static Map<String, Class<? extends Component>> classes = new HashMap<>();

    static {
        classes.put(Window.NAME, DesktopWindow.class);
        classes.put(Window.Editor.NAME, DesktopWindow.Editor.class);
        classes.put(Window.Lookup.NAME, DesktopWindow.Lookup.class);

        classes.put(IFrame.NAME, DesktopFrame.class);
        classes.put(BoxLayout.HBOX, DesktopHBox.class);
        classes.put(BoxLayout.VBOX, DesktopVBox.class);
        classes.put(GridLayout.NAME, DesktopGridLayout.class);
        classes.put(ScrollBoxLayout.NAME, DesktopScrollBoxLayout.class);
        classes.put(SplitPanel.NAME, DesktopSplitPanel.class);

        classes.put(Button.NAME, DesktopButton.class);
        classes.put(LinkButton.NAME, DesktopLinkButton.class);
        classes.put(Label.NAME, DesktopLabel.class);
        classes.put(CheckBox.NAME, DesktopCheckBox.class);

        //Use resizable text area instead of text field
        classes.put(ResizableTextArea.NAME, DesktopResizableTextArea.class);
        classes.put(AutoCompleteTextField.NAME, DesktopAutoCompleteTextField.class);
        classes.put(PasswordField.NAME, DesktopPasswordField.class);
        classes.put(TextField.NAME, DesktopTextField.class);

        classes.put(DateField.NAME, DesktopDateField.class);
        classes.put(Table.NAME, DesktopTable.class);
        classes.put(GroupTable.NAME, DesktopGroupTable.class);
        classes.put(Tree.NAME, DesktopTree.class);
        classes.put(TreeTable.NAME, DesktopTreeTable.class);
        classes.put(ButtonsPanel.NAME, DesktopButtonsPanel.class);
        classes.put(Filter.NAME, DesktopFilter.class);
        classes.put(FieldGroup.NAME, DesktopFieldGroup.class);
        classes.put(RowsCount.NAME, DesktopRowsCount.class);
        classes.put(PopupButton.NAME, DesktopPopupButton.class);
        classes.put(LookupField.NAME, DesktopLookupField.class);
        classes.put(PickerField.NAME, DesktopPickerField.class);
        classes.put(LookupPickerField.NAME, DesktopLookupPickerField.class);
        classes.put(OptionsGroup.NAME, DesktopOptionsGroup.class);
        classes.put(FileUploadField.NAME, DesktopFileUploadField.class);
        classes.put(FileMultiUploadField.NAME, DesktopFileMultiUploadField.class);
        classes.put(TabSheet.NAME, DesktopTabSheet.class);
        classes.put(Embedded.NAME, DesktopEmbedded.class);
        classes.put(WidgetsTree.NAME, DesktopWidgetsTree.class);
        classes.put(GroupBoxLayout.NAME, DesktopGroupBox.class);
        classes.put(ProgressBar.NAME, DesktopProgressBar.class);
        classes.put(TimeField.NAME, DesktopTimeField.class);
        classes.put(TokenList.NAME, DesktopTokenList.class);
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
    public <T extends Component> T createComponent(String name) {
        final Class<Component> componentClass = (Class<Component>) classes.get(name);
        if (componentClass == null) {
            throw new IllegalStateException(String.format("Can't find component class for '%s'", name));
        }

        try {
            return (T) componentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Timer createTimer() {
        return new DesktopTimer();
    }
}