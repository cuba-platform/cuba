/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;
import com.haulmont.cuba.toolkit.gwt.client.HasIndicator;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.Field;
import com.vaadin.terminal.gwt.client.ui.ShortcutActionHandler;

import java.util.Iterator;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class VIndicatedTextField extends Composite implements Paintable, Field,
        ChangeHandler, FocusHandler, BlurHandler, HasIndicator {

    /**
     * The input node CSS classname.
     */
    public static final String CLASSNAME = "v-textfield-i";
    /**
     * This CSS classname is added to the input node on hover.
     */
    public static final String CLASSNAME_FOCUS = "focus";

    protected String id;

    protected ApplicationConnection client;

    private String valueBeforeEdit = null;

    private boolean immediate = false;
    private int maxLength = -1;

    private static final String CLASSNAME_PROMPT = "prompt";
    private static final String ATTR_INPUTPROMPT = "prompt";
    private String inputPrompt = null;
    private boolean prompting = false;

    private int textboxPadding = -1;
    private int componentPadding = -1;
    private String width = null;
    private int horizPaddingAndBorder = 2;

    protected ShortcutActionHandler shortcutHandler;

    private final FlowPanel panel = new FlowPanel();

    private final TextBox tb = new TextBox() {
        @Override
        public void onBrowserEvent(Event event) {
            super.onBrowserEvent(event);
            if (client != null) {
                client.handleTooltipEvent(event, VIndicatedTextField.this);
            }
        }
    };

    private final HTML indicator = new HTML("") {
        @Override
        public void onBrowserEvent(Event event) {
            super.onBrowserEvent(event);
            if (client != null) {
                client.handleTooltipEvent(event, VIndicatedTextField.this);
            }
        }
    };

    public VIndicatedTextField() {
        tb.sinkEvents(VTooltip.TOOLTIP_EVENTS);
        indicator.sinkEvents(VTooltip.TOOLTIP_EVENTS);
        indicator.setStyleName(CLASSNAME + "-indicator");
        panel.add(tb);
        panel.add(indicator);

        initWidget(panel);
        setStyleName(CLASSNAME);
        tb.setStyleName(CLASSNAME + "-input");
        tb.addFocusHandler(this);
        tb.addBlurHandler(this);
        tb.addChangeHandler(this);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        final int type = DOM.eventGetType(event);
        if (type == Event.ONKEYDOWN && shortcutHandler != null) {
            shortcutHandler.handleKeyboardEvent(event);
            return;
        }
        if (client != null) {
            client.handleTooltipEvent(event, this);
        }
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        this.client = client;
        id = uidl.getId();

        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        if (uidl.getBooleanAttribute("readonly")) {
            tb.setReadOnly(true);
        }
        else {
            tb.setReadOnly(false);
        }

        inputPrompt = uidl.getStringAttribute(ATTR_INPUTPROMPT);

        setMaxLength(uidl.hasAttribute("maxLength") ? uidl
                .getIntAttribute("maxLength") : -1);

        immediate = uidl.getBooleanAttribute("immediate");

        if (uidl.hasAttribute("cols")) {
            setColumns(new Integer(uidl.getStringAttribute("cols")).intValue());
        }

        String text = uidl.hasVariable("text") ? uidl.getStringVariable("text")
                : null;
        setPrompting(inputPrompt != null && focusedTextField != this
                && (text == null || text.equals("")));
        if (prompting) {
            setTbText(inputPrompt);
            addStyleDependentName(CLASSNAME_PROMPT);
        }
        else {
            setTbText(text);
            removeStyleDependentName(CLASSNAME_PROMPT);
        }
        valueBeforeEdit = uidl.getStringVariable("text");

        for (final Iterator it = uidl.getChildIterator(); it.hasNext();) {
            final UIDL data = (UIDL) it.next();
            if (data.getTag().equals("actions")) {
                if (shortcutHandler == null) {
                    shortcutHandler = new ShortcutActionHandler(id, client);
                }
                shortcutHandler.updateActionMap(data);
            }
        }

        updateRootWidth();
    }

    private void setTbText(final String txt) {
        if (BrowserInfo.get().isGecko()) {
            /*
             * Gecko is really sluggish when updating input attached to dom.
             * Some optimizations seems to work much better in Gecko if we
             * update the actual content lazily when the rest of the DOM has
             * stabilized. In tests, about ten times better performance is
             * achieved with this optimization. See for eg. #2898
             */
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    tb.setText(txt);
                }
            });
        } else {
            tb.setText(txt);
        }
    }

    private void setMaxLength(int newMaxLength) {
        if (newMaxLength > 0) {
            maxLength = newMaxLength;
            if (tb.getElement().getTagName().toLowerCase().equals("textarea")) {
                // NOP no maxlength property for textarea
            } else {
                tb.getElement().setPropertyInt("maxLength", maxLength);
            }
        } else if (maxLength != -1) {
            if (tb.getElement().getTagName().toLowerCase().equals("textarea")) {
                // NOP no maxlength property for textarea
            } else {
                tb.getElement().setAttribute("maxlength", "");
            }
            maxLength = -1;
        }

    }

    protected int getMaxLength() {
        return maxLength;
    }

    @Override
    public void onChange(ChangeEvent event) {
        valueChange(false);
    }

    /**
     * Called when the field value might have changed and/or the field was
     * blurred. These are combined so the blur event is sent in the same batch
     * as a possible value change event (these are often connected).
     *
     * @param blurred
     *            true if the field was blurred
     */
    public void valueChange(boolean blurred) {
        if (client != null && id != null) {
            boolean sendBlurEvent = false;
            boolean sendValueChange = false;

            if (blurred && client.hasEventListeners(this, EventId.BLUR)) {
                sendBlurEvent = true;
                client.updateVariable(id, EventId.BLUR, "", false);
            }

            String newText = tb.getText();
            if (!prompting && newText != null
                    && !newText.equals(valueBeforeEdit)) {
                sendValueChange = immediate;
                client.indicateMyRequest(this);
                client.updateVariable(id, "text", tb.getText(), false);
                valueBeforeEdit = newText;
            }

            if (sendBlurEvent || sendValueChange) {
                client.sendPendingVariableChanges();
            }
        }
    }

    /*
     * Shows loading indicator (animated gif) at time from change request sent to upcoming uidl changes
     */
    @Override
    public void showLoadingIndicator(boolean show) {
        String stylename = show ? CLASSNAME + "-indicator-active" : CLASSNAME + "-indicator";
        indicator.setStyleName(stylename);
    }

    private static VIndicatedTextField focusedTextField;

    public static void flushChangesFromFocusedTextField() {
        if (focusedTextField != null) {
            focusedTextField.onChange(null);
        }
    }

    @Override
    public void onFocus(FocusEvent event) {
        addStyleDependentName(CLASSNAME_FOCUS);
        if (prompting) {
            tb.setText("");
            removeStyleDependentName(CLASSNAME_PROMPT);
            setPrompting(false);
            if (BrowserInfo.get().isIE6()) {
                // IE6 does not show the cursor when tabbing into the field
                tb.setCursorPos(0);
            }
        }
        focusedTextField = this;
        if (client.hasEventListeners(this, EventId.FOCUS)) {
            client.updateVariable(client.getPid(this), EventId.FOCUS, "", true);
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        removeStyleDependentName(CLASSNAME_FOCUS);
        focusedTextField = null;
        String text = tb.getText();
        setPrompting(inputPrompt != null && (text == null || "".equals(text)));
        if (prompting) {
            setTbText(inputPrompt);
            addStyleDependentName(CLASSNAME_PROMPT);
        }
        valueChange(true);
    }

    private void setPrompting(boolean prompting) {
        this.prompting = prompting;
    }

    public void setColumns(int columns) {
        setColumns(tb.getElement(), columns);
    }

    private native void setColumns(Element e, int c)
    /*-{
    try {
    	switch(e.tagName.toLowerCase()) {
    		case "input":
    			//e.size = c;
    			e.style.width = c+"em";
    			break;
    		case "textarea":
    			//e.cols = c;
    			e.style.width = c+"em";
    			break;
    		default:;
    	}
    } catch (e) {}
    }-*/;

    @Override
    public void setWidth(String width) {
        if (width == null || width.equals("")) {
            this.width = null;
        } else {
            this.width = width;
        }
        horizPaddingAndBorder = Util.setWidthExcludingPaddingAndBorder(this,
                width, horizPaddingAndBorder);
        updateRootWidth();
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        Util.setHeightExcludingPaddingAndBorder(tb, height, 3);
    }


    private void updateRootWidth() {
        if (width == null) {
            /*
             * When the width is not specified we must specify width for root
             * div so the popupopener won't wrap to the next line and also so
             * the size of the combobox won't change over time.
             */
            int tbWidth = Util.getRequiredWidth(tb);
            int openerWidth = Util.getRequiredWidth(indicator);

            int w = tbWidth + openerWidth;
                /*
                 * Firefox3 has its own way of doing rendering so we need to
                 * specify the width for the TextField to make sure it actually
                 * is rendered as wide as FF3 says it is
                 */
            tb.setWidth((tbWidth - getTextboxPadding()) + "px");
            super.setWidth((w) + "px");
            // Freeze the initial width, so that it won't change even if the
            // icon size changes
            width = w + "px";
        } else {
            /*
             * When the width is specified we also want to explicitly specify
             * widths for textbox and popupopener
             */
            setTextboxWidth(getMainWidth() - getComponentPadding());
        }
    }

    private void setTextboxWidth(int componentWidth) {
        int padding = getTextboxPadding();
        int popupOpenerWidth = Util.getRequiredWidth(indicator);
        int textboxWidth = componentWidth - padding - popupOpenerWidth;
        if (textboxWidth < 0) {
            textboxWidth = 0;
        }
        tb.setWidth(textboxWidth + "px");
    }

    private int getTextboxPadding() {
        if (textboxPadding < 0) {
            textboxPadding = Util.measureHorizontalPaddingAndBorder(tb
                    .getElement(), 4);
        }
        return textboxPadding;
    }

    private int getComponentPadding() {
        if (componentPadding < 0) {
            componentPadding = Util.measureHorizontalPaddingAndBorder(
                    getElement(), 3);
        }
        return componentPadding;
    }

    private int getMainWidth() {
        int componentWidth;
        if (BrowserInfo.get().isIE6()) {
            // Required in IE when textfield is wider than this.width
            DOM.setStyleAttribute(getElement(), "overflow", "hidden");
            componentWidth = getOffsetWidth();
            DOM.setStyleAttribute(getElement(), "overflow", "");
        } else {
            componentWidth = getOffsetWidth();
        }
        return componentWidth;
    }
}