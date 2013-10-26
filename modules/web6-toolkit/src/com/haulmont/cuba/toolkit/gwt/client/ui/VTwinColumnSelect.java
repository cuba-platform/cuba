/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import com.haulmont.cuba.toolkit.gwt.client.Tools;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.ui.VButton;
import com.vaadin.terminal.gwt.client.ui.VOptionGroupBase;

import java.util.*;

/**
 * @author gorodnov
 * @version $Id$
 */
public class VTwinColumnSelect extends VOptionGroupBase implements DoubleClickHandler {
    private static final String CLASSNAME = "twincolumn";

    private static final int DEFAULT_COLUMN_COUNT = 10;

    private final TwinColListBox options;

    private final TwinColListBox selections;

    private final VButton add;

    private final VButton remove;

    private FlowPanel buttons;

    private Panel panel;

    private boolean widthSet = false;

    private Map<String, UIDL> optionsUidl;

    private class TwinColListBox extends FocusPanel implements HasDoubleClickHandlers, ClickHandler,
            KeyDownHandler, KeyUpHandler {

        private FlowPanel container = new FlowPanel();

        private boolean enabled;

        private List<Option> options = new ArrayList<Option>();

        private Map<Option, RenderedOption> renderedOptions = new HashMap<Option, RenderedOption>();

        private int optionHeight = -1;

        private String height;
        private String width;

        private int visibleItems;

        private int borderPaddingsHeight = -1;
        private int borderPaddingsWidth = -1;

        private int decoBorderPaddingsHeight = -1;
        private int decoBorderPaddingsWidth = -1;

        private final static String DEFAULT_WIDTH = "10em";
        private final static int DEFAULT_ITEM_COUNT = 5;

        private boolean multipleSelect;

        private boolean cmdKeyPressed;

        class Option {
            String key;
            String caption;
            String desc;
            String icon;
            boolean selected;

            Option(String key, String caption, String desc, String icon) {
                this.key = key;
                this.caption = caption;
                this.desc = desc;
                this.icon = icon;
            }
        }

        class RenderedOption extends Widget implements HasClickHandlers {
            RenderedOption(String caption, String desc, String icon) {
                setElement(DOM.createDiv());

                DOM.setInnerHTML(getElement(), buildHtmlSnippet(caption, desc, icon));

                setStylePrimaryName(CLASSNAME + "-listBox-opt");
                if (icon != null && !"".equals(icon)) {
                    addStyleName("hasIcon");
                }

                Tools.textSelectionEnable(getElement(), false);
            }

            @Override
            protected void onDetach() {
                Tools.textSelectionEnable(getElement(), true);

                super.onDetach();
            }

            private String buildHtmlSnippet(String caption, String desc, String icon) {
                boolean hasIcon = icon != null && !"".equals(icon);

                final StringBuffer sb = new StringBuffer();
                if (hasIcon) {
                    sb.append("<img src=\"")
                            .append(icon)
                            .append("\" alt=\"\" class=\"v-icon\" />");
                }
                sb.append("<div class=\"").append(CLASSNAME).append("-label\"");
                if (desc != null) {
                    sb.append(" title=\"").append(Util.escapeHTML(desc)).append("\"");
                }
                sb.append(">")
                        .append(Util.escapeHTML(caption))
                        .append("</div>");
                if (hasIcon) {
                    sb.append("<div style=\"clear:both;width:0px;height:0px;\"></div>");
                }

                return sb.toString();
            }

            public HandlerRegistration addClickHandler(ClickHandler handler) {
                return addDomHandler(handler, ClickEvent.getType());
            }
        }

        TwinColListBox() {
            this(false);
        }

        TwinColListBox(boolean multipleSelect) {
            super();
            this.multipleSelect = multipleSelect;
            setWidget(container);
            setStylePrimaryName(CLASSNAME + "-listBox");
            container.setStylePrimaryName(CLASSNAME + "-listBox-deco");

            setVisibleItemCount(DEFAULT_ITEM_COUNT);
            setWidth(DEFAULT_WIDTH);

            addKeyDownHandler(this);
            addKeyUpHandler(this);
        }

        @Override
        protected void onAttach() {
            super.onAttach();
            setVisibleItemCount();
            if (width != null) {
                setWidth(width);
            }
            if (height != null) {
                setHeight(height);
            }
        }

        public void onClick(ClickEvent event) {
            if (event.getSource() instanceof RenderedOption) {
                ApplicationConnection.getConsole().log("Click to option >> multiselect: "
                        + isMultipleSelect() + "; " +
                        "commandKeyPressed: " + String.valueOf(cmdKeyPressed));
                RenderedOption renderedOption = (RenderedOption) event.getSource();
                int optionIndex = container.getWidgetIndex(renderedOption);
                if (!(isMultipleSelect() && cmdKeyPressed)) {
                    for (int index = 0; index < options.size(); index++) {
                        final Option opt = options.get(index);
                        if (opt.selected && index != optionIndex) {
                            setItemSelected(index, false);
                        }
                    }
                }
                if (!isItemSelected(optionIndex)) {
                    setItemSelected(optionIndex, true);
                }
                setFocus(true);
            }
        }

        public void onKeyDown(KeyDownEvent event) {
            if (isMultipleSelect() && event.getNativeKeyCode() == KeyCodes.KEY_CTRL) {
                ApplicationConnection.getConsole().log("Key down event: " + event.getNativeKeyCode());
                cmdKeyPressed = true;
            }
        }

        public void onKeyUp(KeyUpEvent event) {
            if (isMultipleSelect() && event.getNativeKeyCode() == KeyCodes.KEY_CTRL) {
                ApplicationConnection.getConsole().log("Key up event: " + event.getNativeKeyCode());
                cmdKeyPressed = false;
            }
        }

        void setVisibleItemCount(int itemCount) {
            assert itemCount > 0;
            visibleItems = itemCount;
            if (isAttached()) {
                setVisibleItemCount();
            }
        }

        public boolean isMultipleSelect() {
            return multipleSelect;
        }

        public void setMultipleSelect(boolean multipleSelect) {
            this.multipleSelect = multipleSelect;
            cmdKeyPressed = false;
        }

        private void setVisibleItemCount() {
            if (visibleItems > 0) {
                setHeight(visibleItemsHeight(visibleItems) + "px");
            }
        }

        private int visibleItemsHeight(int visibleItems) {
            return (getOptionHeight() * visibleItems) + getBorderPaddingsHeight() + getDecoBorderPaddingsHeight();
        }

        private int getBorderPaddingsHeight() {
            if (borderPaddingsHeight == -1) {
                detectBorderPaddings();
            }
            return borderPaddingsHeight;
        }

        private int getBorderPaddingsWidth() {
            if (borderPaddingsWidth == -1) {
                detectBorderPaddings();
            }
            return borderPaddingsWidth;
        }

        private int getDecoBorderPaddingsHeight() {
            if (decoBorderPaddingsHeight == -1) {
                detectBorderPaddings();
            }
            return decoBorderPaddingsHeight;
        }

        private int getDecoBorderPaddingsWidth() {
            if (decoBorderPaddingsWidth == -1) {
                detectBorderPaddings();
            }
            return decoBorderPaddingsWidth;
        }

        private void detectBorderPaddings() {
            if (!isAttached()) return;

            container.setWidth("0px");
            container.setHeight("0px");
            container.getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
            decoBorderPaddingsHeight = container.getOffsetHeight();
            decoBorderPaddingsWidth = container.getOffsetWidth();

            super.setWidth("0px");
            super.setHeight("0px");
            borderPaddingsHeight = getOffsetHeight();
            borderPaddingsWidth = getOffsetWidth();

            setWidth(width);
            setHeight(height);

            container.getElement().getStyle().setOverflow(Style.Overflow.VISIBLE);
            container.setWidth("");
            container.setHeight("");
        }

        @Override
        public void setWidth(String width) {
            if (width != null) {
                this.width = width;
                if ("".equals(width)) {
                    width = DEFAULT_WIDTH;
                }
                if (isAttached()) {
                    super.setWidth(width);
                    int w = getOffsetWidth() - getBorderPaddingsWidth() - getDecoBorderPaddingsWidth();
                    width = w + "px";
                }
                super.setWidth(width);
            }
        }

        public void setHeight(String height) {
            if (height != null) {
                this.height = height;
                if ("".equals(height)) {
                    height = visibleItemsHeight(DEFAULT_ITEM_COUNT) + "px";
                }
                if (isAttached()) {
                    super.setHeight(height);
                    int h = getOffsetHeight() - getBorderPaddingsHeight() - getDecoBorderPaddingsHeight();
                    height = h + "px";
                }
                super.setHeight(height);
            }
        }

        private int getOptionHeight() {
            if (optionHeight == -1) {
                addItem("ABC", "", "", null);
                optionHeight = (container.getOffsetHeight() - getDecoBorderPaddingsHeight()) / getItemCount();
                removeItem(getItemCount() - 1);
            }
            return optionHeight;
        }

        void clearOptions() {
            while (getItemCount() > 0) {
                removeItem(0);
            }
        }

        void addItem(String caption, String desc, String key, String icon) {
            Option opt = new Option(key, caption, desc, icon);
            options.add(opt);
            RenderedOption renderedOption = new RenderedOption(caption, desc, icon);
            renderedOption.addClickHandler(this);
            container.add(renderedOption);
            renderedOptions.put(opt, renderedOption);
        }

        void removeItem(int optionIndex) {
            assert optionIndex >= 0 && options.size() > optionIndex;
            final Option opt = options.remove(optionIndex);
            final RenderedOption renderedOption = renderedOptions.remove(opt);
            container.remove(renderedOption);
        }

        void setOptionClassName(int optionIndex, String className) {
            assert optionIndex >= 0 && options.size() > optionIndex;
            final Option opt = options.get(optionIndex);
            final RenderedOption renderedOption = renderedOptions.get(opt);
            renderedOption.addStyleName(className);
        }

        int getItemCount() {
            return options.size();
        }

        String getValue(int optionIndex) {
            assert optionIndex >= 0 && options.size() > optionIndex;
            Option opt = options.get(optionIndex);
            return opt.key;
        }

        String getItemIcon(int optionIndex) {
            assert optionIndex >= 0 && options.size() > optionIndex;
            Option opt = options.get(optionIndex);
            return opt.icon;
        }

        boolean isItemSelected(int optionIndex) {
            assert optionIndex >= 0 && options.size() > optionIndex;
            Option opt = options.get(optionIndex);
            return opt.selected;
        }

        void setItemSelected(int optionIndex, boolean selected) {
            assert optionIndex >= 0 && options.size() > optionIndex;
            Option opt = options.get(optionIndex);
            if (opt.selected != selected) {
                opt.selected = selected;
                RenderedOption renderedOption = renderedOptions.get(opt);
                if (selected) {
                    renderedOption.addStyleName("selected");
                } else {
                    renderedOption.removeStyleName("selected");
                }
            }
        }

        String getItemCaption(int optionIndex) {
            assert optionIndex >= 0 && options.size() > optionIndex;
            Option opt = options.get(optionIndex);
            return opt.caption;
        }

        String getItemDesc(int optionIndex) {
            assert optionIndex >= 0 && options.size() > optionIndex;
            Option opt = options.get(optionIndex);
            return opt.desc;
        }

        public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
            return addDomHandler(handler, DoubleClickEvent.getType());
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            if (enabled) {
                removeStyleName("disabled");
            } else {
                addStyleName("disabled");
            }
        }
    }

    public VTwinColumnSelect() {
        super(CLASSNAME);

        options = new TwinColListBox();
        options.addClickHandler(this);
        options.addDoubleClickHandler(this);

        selections = new TwinColListBox();
        selections.addClickHandler(this);
        selections.addDoubleClickHandler(this);

        options.addStyleName(CLASSNAME + "-options");
        addFocusHandler(options);

        selections.addStyleName(CLASSNAME + "-selections");
        addFocusHandler(selections);

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

    protected void addFocusHandler(final TwinColListBox listbox) {
        listbox.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                if (!isDisabled() && !isReadonly())
                    listbox.addStyleDependentName("focus");
                else
                    listbox.removeStyleDependentName("focus");
            }
        });
        listbox.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                listbox.removeStyleDependentName("focus");
            }
        });
    }

    @Override
    protected void buildOptions(UIDL uidl) {
        final boolean enabled = !isDisabled() && !isReadonly();
        options.setEnabled(enabled);
        selections.setEnabled(enabled);
        options.setMultipleSelect(isMultiselect());
        selections.setMultipleSelect(isMultiselect());
        add.setEnabled(enabled);
        remove.setEnabled(enabled);
        options.clearOptions();
        selections.clearOptions();
        int selectedOptions = 0;
        int availableOptions = 0;

        optionsUidl = new HashMap<String, UIDL>(uidl.getChildCount());

        for (final Iterator i = uidl.getChildIterator(); i.hasNext();) {
            final UIDL optionUidl = (UIDL) i.next();
            final String key = optionUidl.getStringAttribute("key");
            optionsUidl.put(key, optionUidl);
            if (optionUidl.hasAttribute("selected")) {
                String icon = null;
                if (optionUidl.hasAttribute("icon")) {
                    icon = client.translateVaadinUri(optionUidl.getStringAttribute("icon"));
                }
                selections.addItem(
                        optionUidl.getStringAttribute("caption"),
                        optionUidl.hasAttribute("desc") ? optionUidl.getStringAttribute("desc") : null,
                        key,
                        icon
                );
                if (optionUidl.hasAttribute("style")) {
                    selections.setOptionClassName(selectedOptions, optionUidl.getStringAttribute("style"));
                }
                selectedOptions++;
            } else {
                String icon = null;
                if (optionUidl.hasAttribute("icon")) {
                    icon = client.translateVaadinUri(optionUidl.getStringAttribute("icon"));
                }
                options.addItem(
                        optionUidl.getStringAttribute("caption"),
                        optionUidl.hasAttribute("desc") ? optionUidl.getStringAttribute("desc") : null,
                        key,
                        icon
                );
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
                final String value = selections.getValue(selectionIndex);
                options.addItem(
                        selections.getItemCaption(selectionIndex),
                        selections.getItemDesc(selectionIndex),
                        value,
                        selections.getItemIcon(selectionIndex)
                );
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
                final String value = options.getValue(optionIndex);
                selections.addItem(
                        options.getItemCaption(optionIndex),
                        options.getItemDesc(optionIndex),
                        value,
                        options.getItemIcon(optionIndex)
                );
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

    private void unselectAllItems(TwinColListBox list) {
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
        if (w < 0) {
            w = 0;
        }
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
