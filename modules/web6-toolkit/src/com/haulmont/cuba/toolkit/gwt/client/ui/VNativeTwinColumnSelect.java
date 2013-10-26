/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.VButton;
import com.vaadin.terminal.gwt.client.ui.VOptionGroupBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author gorodnov
 * @version $Id$
 */
public class VNativeTwinColumnSelect extends VOptionGroupBase implements DoubleClickHandler {

    private static final String CLASSNAME = "v-select-twincol";

    private static final int VISIBLE_COUNT = 10;

    private static final int DEFAULT_COLUMN_COUNT = 10;

    private final NativeTwinColListBox options;

    private final NativeTwinColListBox selections;

    private final VButton add;

    private final VButton remove;

    private FlowPanel buttons;

    private Panel panel;

    private boolean widthSet = false;

    private Map<String, UIDL> optionsUidl;

    private class NativeTwinColListBox extends ListBox implements HasDoubleClickHandlers {
        public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
            return addDomHandler(handler, DoubleClickEvent.getType());
        }

        public void addOptionStyle(int index, String name, String value) {
            assert index > -1;
            assert getSelectElement().getOptions().getLength() > index;
            OptionElement option = getSelectElement().getOptions().getItem(index);
            option.getStyle().setProperty(name, value);
        }

        public void setOptionClassName(int index, String className) {
            assert index > -1;
            assert getSelectElement().getOptions().getLength() > index;
            OptionElement option = getSelectElement().getOptions().getItem(index);
            option.setClassName(className);
        }

        private SelectElement getSelectElement() {
            return getElement().cast();
        }
    }

    public VNativeTwinColumnSelect() {
        super(CLASSNAME);

        options = new NativeTwinColListBox();
        options.addClickHandler(this);
        options.addDoubleClickHandler(this);

        selections = new NativeTwinColListBox();
        selections.addClickHandler(this);
        selections.addDoubleClickHandler(this);

        options.setVisibleItemCount(VISIBLE_COUNT);
        selections.setVisibleItemCount(VISIBLE_COUNT);
        options.setStyleName(CLASSNAME + "-options");
        selections.setStyleName(CLASSNAME + "-selections");
        buttons = new FlowPanel();
        buttons.setStyleName(CLASSNAME + "-buttons");
        add = new VButton();
        add.setText(">>");
        add.addClickHandler(this);
        remove = new VButton();
        remove.setText("<<");
        remove.addClickHandler(this);
        panel = ((Panel) optionsContainer);
        panel.add(options);
        buttons.add(add);
        final HTML br = new HTML("<span/>");
        br.setStyleName(CLASSNAME + "-deco");
        buttons.add(br);
        buttons.add(remove);
        panel.add(buttons);
        panel.add(selections);
    }

    @Override
    protected void buildOptions(UIDL uidl) {
        final boolean enabled = !isDisabled() && !isReadonly();
        options.setMultipleSelect(isMultiselect());
        selections.setMultipleSelect(isMultiselect());
        options.setEnabled(enabled);
        selections.setEnabled(enabled);
        add.setEnabled(enabled);
        remove.setEnabled(enabled);
        options.clear();
        selections.clear();
        int selectedOptions = 0;
        int availableOptions = 0;

        optionsUidl = new HashMap<String, UIDL>(uidl.getChildCount());

        for (final Iterator i = uidl.getChildIterator(); i.hasNext();) {
            final UIDL optionUidl = (UIDL) i.next();
            final String key = optionUidl.getStringAttribute("key");
            optionsUidl.put(key, optionUidl);
            if (optionUidl.hasAttribute("selected")) {
                selections.addItem(optionUidl.getStringAttribute("caption"),
                        key);
                if (optionUidl.hasAttribute("style")) {
                    selections.setOptionClassName(selectedOptions, optionUidl.getStringAttribute("style"));
                }
                selectedOptions++;
            } else {
                options.addItem(optionUidl.getStringAttribute("caption"),
                        key);
                if (optionUidl.hasAttribute("style")) {
                    options.setOptionClassName(availableOptions, optionUidl.getStringAttribute("style"));
                }
                availableOptions++;
            }
        }

        int cols = -1;
        if (getColumns() > 0) {
            cols = getColumns();
        } else if (!widthSet) {
            cols = DEFAULT_COLUMN_COUNT;
        }

        if (cols >= 0) {
            options.setWidth(cols + "em");
            selections.setWidth(cols + "em");
//            buttons.setWidth("3.5em");
            optionsContainer.setWidth((2 * cols + 4) + "em");
        }
        if (getRows() > 0) {
            options.setVisibleItemCount(getRows());
            selections.setVisibleItemCount(getRows());

        }
    }

    @Override
    protected String[] getSelectedItems() {
        final ArrayList<String> selectedItemKeys = new ArrayList<String>();
        for (int i = 0; i < selections.getItemCount(); i++) {
            selectedItemKeys.add(selections.getValue(i));
        }
        return selectedItemKeys.toArray(new String[selectedItemKeys.size()]);
    }

    private boolean[] getItemsToAdd() {
        final boolean[] selectedIndexes = new boolean[options.getItemCount()];
        for (int i = 0; i < options.getItemCount(); i++) {
            if (options.isItemSelected(i)) {
                selectedIndexes[i] = true;
            } else {
                selectedIndexes[i] = false;
            }
        }
        return selectedIndexes;
    }

    private boolean[] getItemsToRemove() {
        final boolean[] selectedIndexes = new boolean[selections.getItemCount()];
        for (int i = 0; i < selections.getItemCount(); i++) {
            if (selections.isItemSelected(i)) {
                selectedIndexes[i] = true;
            } else {
                selectedIndexes[i] = false;
            }
        }
        return selectedIndexes;
    }

    public void onDoubleClick(DoubleClickEvent event) {
        if (event.getSource() == options) {
            unselectAllItems(selections);
            selectItems();
        } else if (event.getSource() == selections) {
            unselectAllItems(options);
            unselectItems();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        super.onClick(event);
        if (event.getSource() == add) {
            selectItems();
        } else if (event.getSource() == remove) {
            unselectItems();
        } else if (event.getSource() == options) {
            // unselect all in other list, to avoid mistakes (i.e wrong button)
            unselectAllItems(selections);
        } else if (event.getSource() == selections) {
            // unselect all in other list, to avoid mistakes (i.e wrong button)
            unselectAllItems(options);
        }
    }

    private void unselectItems() {
        final boolean[] sel = getItemsToRemove();
        for (int i = 0; i < sel.length; i++) {
            if (sel[i]) {
                final int selectionIndex = i
                        - (sel.length - selections.getItemCount());
                selectedKeys.remove(selections.getValue(selectionIndex));

                // Move selection to another column
                final String text = selections.getItemText(selectionIndex);
                final String value = selections.getValue(selectionIndex);
                options.addItem(text, value);
                UIDL optionUidl = optionsUidl.get(value);
                if (optionUidl.hasAttribute("style")) {
                    options.setOptionClassName(options.getItemCount() - 1, optionUidl.getStringAttribute("style"));
                }
                options.setItemSelected(options.getItemCount() - 1, true);
                selections.removeItem(selectionIndex);
            }
        }
        client.updateVariable(id, "selected", selectedKeys
                .toArray(new String[selectedKeys.size()]), isImmediate());
    }

    private void selectItems() {
        final boolean[] sel = getItemsToAdd();
        for (int i = 0; i < sel.length; i++) {
            if (sel[i]) {
                final int optionIndex = i
                        - (sel.length - options.getItemCount());
                selectedKeys.add(options.getValue(optionIndex));

                // Move selection to another column
                final String text = options.getItemText(optionIndex);
                final String value = options.getValue(optionIndex);
                selections.addItem(text, value);
                UIDL optionUidl = optionsUidl.get(value);
                if (optionUidl.hasAttribute("style")) {
                    selections.setOptionClassName(selections.getItemCount() - 1, optionUidl.getStringAttribute("style"));
                }
                selections.setItemSelected(selections.getItemCount() - 1,
                        true);
                options.removeItem(optionIndex);
            }
        }
        client.updateVariable(id, "selected", selectedKeys
                .toArray(new String[selectedKeys.size()]), isImmediate());
    }

    private void unselectAllItems(ListBox list) {
        final int c = list.getItemCount();
        for (int i = 0; i < c; i++) {
            list.setItemSelected(i, false);
        }
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        if ("".equals(height)) {
            options.setHeight("");
            selections.setHeight("");
        } else {
            setFullHeightInternals();
        }
    }

    private void setFullHeightInternals() {
        options.setHeight("100%");
        selections.setHeight("100%");
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        if (!"".equals(width) && width != null) {
            setRelativeInternalWidths();
        }
    }

    private void setRelativeInternalWidths() {
        DOM.setStyleAttribute(getElement(), "position", "relative");
        int buttonsWidth = buttons.getOffsetWidth();
        int w = (getOffsetWidth() - buttonsWidth) / 2;
        if (w < 0) { w = 0; }
        options.setWidth(w + "px");
        selections.setWidth(w + "px");
        widthSet = true;
    }

    @Override
    protected void setTabIndex(int tabIndex) {
        options.setTabIndex(tabIndex);
        selections.setTabIndex(tabIndex);
        add.setTabIndex(tabIndex);
        remove.setTabIndex(tabIndex);
    }

    public void focus() {
        options.setFocus(true);
    }
}
