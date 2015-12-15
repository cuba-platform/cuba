/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload;

import com.google.gwt.aria.client.Id;
import com.google.gwt.aria.client.RelevantValue;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ComputedStyle;
import com.vaadin.client.Focusable;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.*;
import com.vaadin.client.ui.aria.AriaHelper;

import static com.vaadin.client.WidgetUtil.isFocusedElementEditable;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaFileUploadProgressWindow extends VOverlay implements KeyDownHandler, KeyUpHandler,
                                                                      Focusable {

    public static final String CLASSNAME = "v-window";

    private static final String MODAL_WINDOW_OPEN_CLASSNAME = "v-modal-window-open";

    public static final int Z_INDEX = 15000;

    protected Element contents;

    protected Element header;

    protected Element footer;

    protected Element resizeBox;

    protected SimpleFocusablePanel contentPanel;

    protected boolean dragging;

    protected int startX;

    protected int startY;

    protected int origX;

    protected int origY;

    protected boolean resizing;

    protected int origW;

    protected int origH;

    protected Element closeBox;

    protected boolean vaadinModality = false;

    protected boolean resizable = true;

    protected boolean draggable = false;

    protected Element modalityCurtain;
    protected Element draggingCurtain;
    protected Element resizingCurtain;

    protected Element headerText;

    protected boolean closable = true;

    protected Element topTabStop;
    protected Element bottomTabStop;

    protected Event.NativePreviewHandler topEventBlocker;
    protected Event.NativePreviewHandler bottomEventBlocker;

    protected HandlerRegistration topBlockerRegistration;
    protected HandlerRegistration bottomBlockerRegistration;

    // Prevents leaving the window with the Tab key when true
    protected boolean doTabStop;

    protected Element wrapper;

    protected boolean visibilityChangesDisabled;

    protected CloseListener closeListener;

    protected VLabel currentFileLabel;
    protected VButton cancelButton;
    protected VProgressBar progressBar;

    public CubaFileUploadProgressWindow() {
        super(false, true); // no autohide, modal
        // Different style of shadow for windows

        Roles.getDialogRole().set(getElement());
        Roles.getDialogRole().setAriaRelevantProperty(getElement(),
                RelevantValue.ADDITIONS);

        constructDOM();
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        /*
         * When this window gets reattached, set the tabstop to the previous
         * state.
         */
        setTabStopEnabled(doTabStop);

        // Fix for #14413. Any pseudo elements inside these elements are not
        // visible on initial render unless we shake the DOM.
        if (BrowserInfo.get().isIE8()) {
            closeBox.getStyle().setDisplay(Style.Display.NONE);
            Scheduler.get().scheduleFinally(new Command() {
                @Override
                public void execute() {
                    closeBox.getStyle().clearDisplay();
                }
            });
        }
    }

    @Override
    protected void onDetach() {
        super.onDetach();

        removeTabBlockHandlers();
    }

    private void addTabBlockHandlers() {
        if (topBlockerRegistration == null) {
            topBlockerRegistration = Event
                    .addNativePreviewHandler(topEventBlocker);
            bottomBlockerRegistration = Event
                    .addNativePreviewHandler(bottomEventBlocker);
        }
    }

    private void removeTabBlockHandlers() {
        if (topBlockerRegistration != null) {
            topBlockerRegistration.removeHandler();
            topBlockerRegistration = null;

            bottomBlockerRegistration.removeHandler();
            bottomBlockerRegistration = null;
        }
    }

    @Override
    protected void setZIndex(int zIndex) {
        super.setZIndex(zIndex);
        if (vaadinModality) {
            getModalityCurtain().getStyle().setZIndex(zIndex);
        }
    }

    protected Element getModalityCurtain() {
        if (modalityCurtain == null) {
            modalityCurtain = DOM.createDiv();
            modalityCurtain.setClassName(CLASSNAME + "-modalitycurtain");
        }
        return modalityCurtain;
    }

    protected void constructDOM() {
        setStyleName(CLASSNAME);

        topTabStop = DOM.createDiv();
        topTabStop.setTabIndex(0);

        header = DOM.createDiv();
        header.setClassName(CLASSNAME + "-outerheader");

        headerText = DOM.createDiv();
        headerText.setClassName(CLASSNAME + "-header");

        contents = DOM.createDiv();
        contents.setClassName(CLASSNAME + "-contents");

        footer = DOM.createDiv();
        footer.setClassName(CLASSNAME + "-footer");

        resizeBox = DOM.createDiv();
        resizeBox.setClassName(CLASSNAME + "-resizebox");

        closeBox = DOM.createDiv();
        closeBox.setClassName(CLASSNAME + "-closebox");
        closeBox.setTabIndex(0);

        DOM.appendChild(footer, resizeBox);

        bottomTabStop = DOM.createDiv();
        bottomTabStop.setTabIndex(0);

        wrapper = DOM.createDiv();
        wrapper.setClassName(CLASSNAME + "-wrap");

        DOM.appendChild(wrapper, topTabStop);
        DOM.appendChild(wrapper, header);
        DOM.appendChild(header, closeBox);
        DOM.appendChild(header, headerText);
        DOM.appendChild(wrapper, contents);
        DOM.appendChild(wrapper, footer);
        DOM.appendChild(wrapper, bottomTabStop);
        DOM.appendChild(super.getContainerElement(), wrapper);

        sinkEvents(Event.ONDBLCLICK | Event.MOUSEEVENTS | Event.TOUCHEVENTS
                | Event.ONCLICK | Event.ONLOSECAPTURE);

        currentFileLabel = new VLabel();
        currentFileLabel.addStyleName("upload-file-label");
        currentFileLabel.setWidth("100%");

        progressBar = new VProgressBar();
        progressBar.addStyleName("upload-progressbar");
        progressBar.setIndeterminate(false);
        progressBar.setState(0);
        progressBar.setWidth("100%");

        cancelButton = new VButton();
        cancelButton.addStyleName("upload-cancel-button");
        cancelButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                closeWindow();
            }
        });

        contentPanel = new SimpleFocusablePanel();
        contentPanel.setStyleName("content-pane");
        contentPanel.addKeyDownHandler(this);
        contentPanel.addKeyUpHandler(this);

        setWidget(contentPanel);

        final FlowPanel verticalPanel = new FlowPanel();
        verticalPanel.setStyleName("vertical-panel");
        verticalPanel.addStyleName("v-widget");
        verticalPanel.addStyleName("v-has-width");
        verticalPanel.addStyleName("v-has-height");

        verticalPanel.add(currentFileLabel);
        verticalPanel.add(progressBar);
        verticalPanel.add(cancelButton);

        contentPanel.setWidget(verticalPanel);

        // do some calculations for window layout
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                Style contentStyle = contents.getStyle();

                ComputedStyle headerCs = new ComputedStyle(header);
                String headerHeight = headerCs.getProperty("height");
                contentStyle.setProperty("paddingTop", headerHeight);
                contentStyle.setProperty("marginTop", "-" + headerHeight);

                ComputedStyle footerCs = new ComputedStyle(footer);
                String footerHeight = footerCs.getProperty("height");
                contentStyle.setProperty("paddingBottom", footerHeight);
                contentStyle.setProperty("marginBottom", "-" + footerHeight);
            }
        });

        // Make the closebox accessible for assistive devices
        Roles.getButtonRole().set(closeBox);
        Roles.getButtonRole().setAriaLabelProperty(closeBox, "close button");

        // Provide the title to assistive devices
        AriaHelper.ensureHasId(headerText);
        Roles.getDialogRole().setAriaLabelledbyProperty(getElement(),
                Id.of(headerText));

        // Handlers to Prevent tab to leave the window
        // and backspace to cause browser navigation
        topEventBlocker = new Event.NativePreviewHandler() {
            @Override
            public void onPreviewNativeEvent(Event.NativePreviewEvent event) {
                NativeEvent nativeEvent = event.getNativeEvent();
                if (nativeEvent.getEventTarget().cast() == topTabStop
                        && nativeEvent.getKeyCode() == KeyCodes.KEY_TAB
                        && nativeEvent.getShiftKey()) {
                    nativeEvent.preventDefault();
                }
                if (nativeEvent.getEventTarget().cast() == topTabStop
                        && nativeEvent.getKeyCode() == KeyCodes.KEY_BACKSPACE) {
                    nativeEvent.preventDefault();
                }
            }
        };

        bottomEventBlocker = new Event.NativePreviewHandler() {
            @Override
            public void onPreviewNativeEvent(Event.NativePreviewEvent event) {
                NativeEvent nativeEvent = event.getNativeEvent();
                if (nativeEvent.getEventTarget().cast() == bottomTabStop
                        && nativeEvent.getKeyCode() == KeyCodes.KEY_TAB
                        && !nativeEvent.getShiftKey()) {
                    nativeEvent.preventDefault();
                }
                if (nativeEvent.getEventTarget().cast() == bottomTabStop
                        && nativeEvent.getKeyCode() == KeyCodes.KEY_BACKSPACE) {
                    nativeEvent.preventDefault();
                }
            }
        };
    }

    @Override
    public void setVisible(boolean visible) {
        /*
         * Visibility with VWindow works differently than with other Paintables
         * in Vaadin. Invisible VWindows are not attached to DOM at all. Flag is
         * used to avoid visibility call from
         * ApplicationConnection.updateComponent();
         */
        if (!visibilityChangesDisabled) {
            super.setVisible(visible);
        }

        if (visible
                && BrowserInfo.get().requiresPositionAbsoluteOverflowAutoFix()) {

            /*
             * Shake up the DOM a bit to make the window shed unnecessary
             * scrollbars and resize correctly afterwards. The version fixing
             * ticket #11994 which was changing the size to 110% was replaced
             * with this due to ticket #12943
             */
            WidgetUtil
                    .runWebkitOverflowAutoFix(contents.getFirstChildElement());
        }
    }

    public void setDraggable(boolean draggable) {
        if (this.draggable == draggable) {
            return;
        }

        this.draggable = draggable;

        setCursorProperties();
    }

    private void setCursorProperties() {
        if (!draggable) {
            header.getStyle().setProperty("cursor", "default");
            footer.getStyle().setProperty("cursor", "default");
        } else {
            header.getStyle().setProperty("cursor", "");
            footer.getStyle().setProperty("cursor", "");
        }
    }

    /**
     * Sets the closable state of the window. Additionally hides/shows the close
     * button according to the new state.
     *
     * @param closable true if the window can be closed by the user
     */
    public void setClosable(boolean closable) {
        if (this.closable == closable) {
            return;
        }

        this.closable = closable;
        if (closable) {
            closeBox.setClassName(CLASSNAME + "-closebox");
        } else {
            closeBox.setClassName(CLASSNAME + "-closebox " + CLASSNAME + "-closebox-disabled");
        }
    }

    /**
     * Returns the closable state of the sub window. If the sub window is
     * closable a decoration (typically an X) is shown to the user. By clicking
     * on the X the user can close the window.
     *
     * @return true if the sub window is closable
     */
    protected boolean isClosable() {
        return closable;
    }

    public void setCancelButtonCaption(String cancelButtonCaption) {
        cancelButton.setText(cancelButtonCaption);
    }

    public void setCurrentFileName(String currentFileName) {
        currentFileLabel.setText(currentFileName);
    }

    public String getCurrentFileName() {
        return currentFileLabel.getText();
    }

    public void setProgress(float state) {
        progressBar.setState(state);
    }

    @Override
    public void show() {
        if (vaadinModality) {
            showModalityCurtain();
        }
        super.show();
    }

    @Override
    public void hide() {

        /*
         * If the window has a RichTextArea and the RTA is focused at the time
         * of hiding in IE8 only the window will have some problems returning
         * the focus to the correct place. Curiously the focus will be returned
         * correctly if clicking on the "close" button in the window header but
         * closing the window from a button for example in the window will fail.
         * Symptom described in #10776
         *
         * The problematic part is that for the focus to be returned correctly
         * an input element needs to be focused in the root panel. Focusing some
         * other element apparently won't work.
         */
        if (BrowserInfo.get().isIE8()) {
            fixIE8FocusCaptureIssue();
        }

        if (vaadinModality) {
            hideModalityCurtain();
        }
        super.hide();
    }

    private void fixIE8FocusCaptureIssue() {
        Element e = DOM.createInputText();
        Style elemStyle = e.getStyle();
        elemStyle.setPosition(Style.Position.ABSOLUTE);
        elemStyle.setTop(-10, Style.Unit.PX);
        elemStyle.setWidth(0, Style.Unit.PX);
        elemStyle.setHeight(0, Style.Unit.PX);

        contentPanel.getElement().appendChild(e);
        e.focus();
        contentPanel.getElement().removeChild(e);
    }

    
    public void setVaadinModality(boolean modality) {
        vaadinModality = modality;
        if (vaadinModality) {
            if (isAttached()) {
                showModalityCurtain();
            }
            addTabBlockHandlers();
        } else {
            if (modalityCurtain != null) {
                if (isAttached()) {
                    hideModalityCurtain();
                }
                modalityCurtain = null;
            }
            if (!doTabStop) {
                removeTabBlockHandlers();
            }
        }
    }

    private void showModalityCurtain() {
        getModalityCurtain().getStyle().setZIndex(Z_INDEX);

        if (isShowing()) {
            getOverlayContainer().insertBefore(getModalityCurtain(),
                    getElement());
        } else {
            getOverlayContainer().appendChild(getModalityCurtain());
        }

        Document.get().getBody().addClassName(MODAL_WINDOW_OPEN_CLASSNAME);
    }

    private void hideModalityCurtain() {
        Document.get().getBody().removeClassName(MODAL_WINDOW_OPEN_CLASSNAME);

        modalityCurtain.removeFromParent();

        if (BrowserInfo.get().isIE()) {
            // IE leaks memory in certain cases unless we release the reference
            // (#9197)
            modalityCurtain = null;
        }
    }

    /*
     * Shows an empty div on top of all other content; used when moving, so that
     * iframes (etc) do not steal event.
     */
    private void showDraggingCurtain() {
        getElement().getParentElement().insertBefore(getDraggingCurtain(),
                getElement());
    }

    private void hideDraggingCurtain() {
        if (draggingCurtain != null) {
            draggingCurtain.removeFromParent();
        }
    }

    /*
     * Shows an empty div on top of all other content; used when resizing, so
     * that iframes (etc) do not steal event.
     */
    private void showResizingCurtain() {
        getElement().getParentElement().insertBefore(getResizingCurtain(),
                getElement());
    }

    private void hideResizingCurtain() {
        if (resizingCurtain != null) {
            resizingCurtain.removeFromParent();
        }
    }

    private Element getDraggingCurtain() {
        if (draggingCurtain == null) {
            draggingCurtain = createCurtain();
            draggingCurtain.setClassName(CLASSNAME + "-draggingCurtain");
        }

        return draggingCurtain;
    }

    private Element getResizingCurtain() {
        if (resizingCurtain == null) {
            resizingCurtain = createCurtain();
            resizingCurtain.setClassName(CLASSNAME + "-resizingCurtain");
        }

        return resizingCurtain;
    }

    private Element createCurtain() {
        Element curtain = DOM.createDiv();

        curtain.getStyle().setPosition(Style.Position.ABSOLUTE);
        curtain.getStyle().setTop(0, Style.Unit.PX);
        curtain.getStyle().setLeft(0, Style.Unit.PX);
        curtain.getStyle().setWidth(100, Style.Unit.PCT);
        curtain.getStyle().setHeight(100, Style.Unit.PCT);
        curtain.getStyle().setZIndex(VOverlay.Z_INDEX);

        return curtain;
    }

    /** INTERNAL. May be removed or replaced in the future. */
    public void setResizable(boolean resizability) {
        resizable = resizability;
        if (resizability) {
            footer.setClassName(CLASSNAME + "-footer");
            resizeBox.setClassName(CLASSNAME + "-resizebox");
        } else {
            footer.setClassName(CLASSNAME + "-footer " + CLASSNAME + "-footer-noresize");
            resizeBox.setClassName(CLASSNAME + "-resizebox " + CLASSNAME + "-resizebox-disabled");
        }
    }

    @Override
    public void setPopupPosition(int left, int top) {
        if (top < 0) {
            // ensure window is not moved out of browser window from top of the
            // screen
            top = 0;
        }
        super.setPopupPosition(left, top);
    }

    public void setCaption(String c) {
        setCaption(c, false);
    }

    public void setCaption(String c, boolean asHtml) {
        String html;
        if (asHtml) {
            html = c == null ? "" : c;
        } else {
            html = WidgetUtil.escapeHTML(c);
        }

        headerText.setInnerHTML(html);
    }

    @Override
    protected com.google.gwt.user.client.Element getContainerElement() {
        // in GWT 1.5 this method is used in PopupPanel constructor
        if (contents == null) {
            return super.getContainerElement();
        }
        return DOM.asOld(contents);
    }

    private Event headerDragPending;

    @Override
    public void onBrowserEvent(final Event event) {
        boolean bubble = true;

        final int type = event.getTypeInt();

        final Element target = DOM.eventGetTarget(event);

        if (resizing || resizeBox == target) {
            onResizeEvent(event);
            bubble = false;
        } else if (isClosable() && target == closeBox) {
            if (type == Event.ONCLICK) {
                closeWindow();
            }
            bubble = false;
        } else if (header.isOrHasChild(target) && !dragging) {
            // dblclick handled in connector
            if (type != Event.ONDBLCLICK && draggable) {
                if (type == Event.ONMOUSEDOWN) {
                    /**
                     * Prevents accidental selection of window caption or
                     * content. (#12726)
                     */
                    event.preventDefault();

                    headerDragPending = event;
                } else if (type == Event.ONMOUSEMOVE
                        && headerDragPending != null) {
                    // ie won't work unless this is set here
                    dragging = true;
                    onDragEvent(headerDragPending);
                    onDragEvent(event);
                    headerDragPending = null;
                } else {
                    headerDragPending = null;
                }
                bubble = false;
            }
        } else if (dragging || !contents.isOrHasChild(target)) {
            onDragEvent(event);
            bubble = false;
        }
        /*
         * If clicking on other than the content, move focus to the window.
         * After that this windows e.g. gets all keyboard shortcuts.
         */
        if (type == Event.ONMOUSEDOWN
                && !contentPanel.getElement().isOrHasChild(target)
                && target != closeBox) {
            contentPanel.focus();
        }

        if (!bubble) {
            event.stopPropagation();
        } else {
            // Super.onBrowserEvent takes care of Handlers added by the
            // ClickEventHandler
            super.onBrowserEvent(event);
        }
    }

    protected void closeWindow() {
        hide();

        if (closeListener != null) {
            closeListener.onClose();
        }
    }

    private void onResizeEvent(Event event) {
        if (resizable && WidgetUtil.isTouchEventOrLeftMouseButton(event)) {
            switch (event.getTypeInt()) {
                case Event.ONMOUSEDOWN:
                case Event.ONTOUCHSTART:
                    showResizingCurtain();
                    if (BrowserInfo.get().isIE()) {
                        resizeBox.getStyle().setVisibility(Style.Visibility.HIDDEN);
                    }
                    resizing = true;
                    startX = WidgetUtil.getTouchOrMouseClientX(event);
                    startY = WidgetUtil.getTouchOrMouseClientY(event);
                    origW = getElement().getOffsetWidth();
                    origH = getElement().getOffsetHeight();
                    DOM.setCapture(getElement());
                    event.preventDefault();
                    break;
                case Event.ONMOUSEUP:
                case Event.ONTOUCHEND:
                    setSize(event);
                case Event.ONTOUCHCANCEL:
                    DOM.releaseCapture(getElement());
                case Event.ONLOSECAPTURE:
                    hideResizingCurtain();
                    if (BrowserInfo.get().isIE()) {
                        resizeBox.getStyle().clearVisibility();
                    }
                    resizing = false;
                    break;
                case Event.ONMOUSEMOVE:
                case Event.ONTOUCHMOVE:
                    if (resizing) {
                        setSize(event);
                        event.preventDefault();
                    }
                    break;
                default:
                    event.preventDefault();
                    break;
            }
        }
    }

    private boolean cursorInsideBrowserContentArea(Event event) {
        if (event.getClientX() < 0 || event.getClientY() < 0) {
            // Outside to the left or above
            return false;
        }

        if (event.getClientX() > Window.getClientWidth()
                || event.getClientY() > Window.getClientHeight()) {
            // Outside to the right or below
            return false;
        }

        return true;
    }

    private void setSize(Event event) {
        if (!cursorInsideBrowserContentArea(event)) {
            // Only drag while cursor is inside the browser client area
            return;
        }

        int w = WidgetUtil.getTouchOrMouseClientX(event) - startX + origW;
        int h = WidgetUtil.getTouchOrMouseClientY(event) - startY + origH;

        w = Math.max(w, getMinWidth());
        h = Math.max(h, getMinHeight());

        setWidth(w + "px");
        setHeight(h + "px");
    }

    private int getMinHeight() {
        return getPixelValue(getElement().getStyle().getProperty("minHeight"));
    }

    private int getMinWidth() {
        return getPixelValue(getElement().getStyle().getProperty("minWidth"));
    }

    private static int getPixelValue(String size) {
        if (size == null || !size.endsWith("px")) {
            return -1;
        } else {
            return Integer.parseInt(size.substring(0, size.length() - 2));
        }
    }

    @Override
    public void setWidth(String width) {
        // Override PopupPanel which sets the width to the contents
        getElement().getStyle().setProperty("width", width);
        // Update v-has-width in case undefined window is resized
        setStyleName("v-has-width", width != null && width.length() > 0);
    }

    @Override
    public void setHeight(String height) {
        // Override PopupPanel which sets the height to the contents
        getElement().getStyle().setProperty("height", height);
        // Update v-has-height in case undefined window is resized
        setStyleName("v-has-height", height != null && height.length() > 0);
    }

    private void onDragEvent(Event event) {
        if (!WidgetUtil.isTouchEventOrLeftMouseButton(event)) {
            return;
        }

        switch (DOM.eventGetType(event)) {
            case Event.ONTOUCHSTART:
                if (event.getTouches().length() > 1) {
                    return;
                }
            case Event.ONMOUSEDOWN:
                beginMovingWindow(event);
                break;
            case Event.ONMOUSEUP:
            case Event.ONTOUCHEND:
            case Event.ONTOUCHCANCEL:
            case Event.ONLOSECAPTURE:
                stopMovingWindow();
                break;
            case Event.ONMOUSEMOVE:
            case Event.ONTOUCHMOVE:
                moveWindow(event);
                break;
            default:
                break;
        }
    }

    private void moveWindow(Event event) {
        if (dragging) {
            if (cursorInsideBrowserContentArea(event)) {
                // Only drag while cursor is inside the browser client area
                final int x = WidgetUtil.getTouchOrMouseClientX(event) - startX
                        + origX;
                final int y = WidgetUtil.getTouchOrMouseClientY(event) - startY
                        + origY;
                setPopupPosition(x, y);
            }

            event.preventDefault();
        }
    }

    private void beginMovingWindow(Event event) {
        if (draggable) {
            showDraggingCurtain();
            dragging = true;
            startX = WidgetUtil.getTouchOrMouseClientX(event);
            startY = WidgetUtil.getTouchOrMouseClientY(event);
            origX = getElement().getAbsoluteLeft();
            origY = getElement().getAbsoluteTop();
            DOM.setCapture(getElement());

            event.preventDefault();
        }
    }

    private void stopMovingWindow() {
        dragging = false;
        hideDraggingCurtain();
        DOM.releaseCapture(getElement());
    }

    @Override
    protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
        if (dragging) {
            Event e = Event.as(event.getNativeEvent());
            onDragEvent(e);
            event.cancel();
        }
    }

    @Override
    public void addStyleDependentName(String styleSuffix) {
        // VWindow's getStyleElement() does not return the same element as
        // getElement(), so we need to override this.
        setStyleName(getElement(), getStylePrimaryName() + "-" + styleSuffix,
                true);
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (vaadinModality
                && event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE
                && !isFocusedElementEditable()) {
            event.preventDefault();
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (isClosable() && event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
            closeWindow();
        }
    }

    @Override
    public void focus() {
        contentPanel.focus();
    }

    /**
     * Registers the handlers that prevent to leave the window using the
     * Tab-key.
     * <p>
     * The value of the parameter doTabStop is stored and used for non-modal
     * windows. For modal windows, the handlers are always registered, while
     * preserving the stored value.
     *
     * @param doTabStop
     *            true to prevent leaving the window, false to allow leaving the
     *            window for non modal windows
     */
    public void setTabStopEnabled(boolean doTabStop) {
        this.doTabStop = doTabStop;

        if (doTabStop || vaadinModality) {
            addTabBlockHandlers();
        } else {
            removeTabBlockHandlers();
        }
    }

    public void setCloseListener(CloseListener closeListener) {
        this.closeListener = closeListener;
    }

    public interface CloseListener {
        void onClose();
    }
}