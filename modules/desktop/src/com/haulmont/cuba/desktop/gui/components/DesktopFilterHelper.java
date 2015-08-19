/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.FilterHelper;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.security.entity.FilterEntity;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.UUID;

/**
 * @author gorbunkov
 * @version $Id$
 */
@ManagedBean(FilterHelper.NAME)
public class DesktopFilterHelper implements FilterHelper {

    private Log log = LogFactory.getLog(DesktopFilterHelper.class);

    @Inject
    protected DesktopFilterDragAndDropSupport dragAndDropSupport;

    @Override
    public void setLookupNullSelectionAllowed(LookupField lookupField, boolean value) {
        lookupField.setRequired(!value);
    }

    @Override
    public void setLookupTextInputAllowed(LookupField lookupField, boolean value) {
        //do nothing
    }

    @Override
    public AbstractSearchFolder saveFolder(AbstractSearchFolder folder) {
        return null;
    }

    @Override
    public void openFolderEditWindow(boolean isAppFolder, AbstractSearchFolder folder, Presentations presentations, Runnable commitHandler) {

    }

    @Override
    public boolean isFolderActionsEnabled() {
        return false;
    }

    @Override
    public void initConditionsDragAndDrop(Tree tree, ConditionsTree conditions) {
        dragAndDropSupport.initDragAndDrop(tree, conditions);
    }

    @Override
    public Object getFoldersPane() {
        return null;
    }

    @Override
    public void removeFolderFromFoldersPane(Folder folder) {

    }

    @Override
    public boolean isTableActionsEnabled() {
        return false;
    }

    @Override
    public void initTableFtsTooltips(Table table, Map<UUID, String> tooltips) {
    }

    @Override
    public void removeTableFtsTooltips(Table table) {

    }

    @Override
    public void setFieldReadOnlyFocusable(com.haulmont.cuba.gui.components.TextField textField, boolean readOnlyFocusable) {
        //do nothing
    }

    @Override
    public void setComponentFocusable(Component component, boolean focusable) {
        JComponent dComponent = DesktopComponentsHelper.unwrap(component);
        dComponent.setFocusable(focusable);
    }

    @Override
    public void setLookupCaptions(LookupField lookupField, Map<Object, String> captions) {
        ((DesktopLookupField) lookupField).setCaptionFormatter(new FilterEntityCaptionFormatter(captions));
    }

    @Override
    public void addTextChangeListener(TextField textField, final TextChangeListener listener) {
        final JTextField dTextField = (JTextField) DesktopComponentsHelper.unwrap(textField);
        dTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                listener.textChanged(dTextField.getText());
            }
        });
    }

    @Override
    public void addShortcutListener(TextField textField, final ShortcutListener listener) {
        final JTextField dTextField = (JTextField) DesktopComponentsHelper.unwrap(textField);
        final KeyStroke keyStroke = DesktopComponentsHelper.convertKeyCombination(listener.getKeyCombination());
        dTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (ObjectUtils.equals(e.getKeyCode(), keyStroke.getKeyCode()) &&
                        ObjectUtils.equals(e.getModifiers(), keyStroke.getModifiers())) {
                    listener.handleShortcutPressed();
                }
            }
        });
    }

    protected class FilterEntityCaptionFormatter implements DesktopAbstractOptionsField.CaptionFormatter<FilterEntity> {

        protected Map<Object, String> captions;

        public FilterEntityCaptionFormatter(Map<Object, String> captions) {
            this.captions = captions;
        }

        @Override
        public String formatValue(FilterEntity value) {
            return captions.get(value);
        }
    }

    @Override
    public void setLookupFieldPageLength(LookupField lookupField, int pageLength) {
    }
}
