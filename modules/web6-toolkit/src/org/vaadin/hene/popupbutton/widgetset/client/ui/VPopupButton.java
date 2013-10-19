/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package org.vaadin.hene.popupbutton.widgetset.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.*;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.toolkit.gwt.client.ui.Table;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.RenderInformation.Size;
import com.vaadin.terminal.gwt.client.ui.VButton;
import com.vaadin.terminal.gwt.client.ui.VOverlay;
import com.vaadin.terminal.gwt.client.ui.richtextarea.VRichTextArea;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

// This class contains code from the VPopupView class.  

public class VPopupButton extends VButton implements Container,
		Iterable<Widget> {

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-popupbutton";

    public static final String POPUP_INDICATOR_CLASSNAME = "v-popup-indicator";
    private static final String CONSTRUCTED_POPUP_CLASSNAME = "popupList";

    private final LayoutPopup popup = new LayoutPopup();

	private boolean popupVisible = false;

    private boolean autoClose = false;

	public VPopupButton() {
		super();
		DivElement e = Document.get().createDivElement();
		e.setClassName(POPUP_INDICATOR_CLASSNAME);
		getElement().getFirstChildElement().appendChild(e);
	}

	/**
	 * Called whenever an update is received from the server
	 */
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        com.google.gwt.user.client.Element containerElement = popup.getContainerElement();
        containerElement.removeClassName(CONSTRUCTED_POPUP_CLASSNAME);

		super.updateFromUIDL(uidl, client);
		if (client.updateComponent(this, uidl, false)) {
			hidePopup();
			return;
		}
		addStyleName(CLASSNAME);

        autoClose = uidl.getBooleanVariable("autoClose");
		popupVisible = uidl.getBooleanVariable("popupVisible");
		if (popupVisible) {
			UIDL popupUIDL = uidl.getChildUIDL(0);
			popup.setVisible(false);
			popup.show();
			popup.updateFromUIDL(popupUIDL);
			showPopup();
		} else {
			hidePopup();
		}
	}

	@Override
	public void onBrowserEvent(Event event) {
		int type = event.getTypeInt();
		switch (type) {
		case Event.ONCLICK:
            Widget parent = this.getParent();
            if (parent instanceof Table.ITableBody.ITableRow) {
                parent.onBrowserEvent(event);    
            }
			updateState(true, false);
			break;
		}
		super.onBrowserEvent(event);
	}

	private void updateState(boolean visible, boolean immediate) {
		client.updateVariable(id, "popupVisible", visible, immediate);
	}

	private void showPopup() {
        Scheduler.get().scheduleDeferred(new Command() {
            public void execute() {
                int extra = 20;
                int left = getAbsoluteLeft();
                int top = getAbsoluteTop() + getOffsetHeight();
                int browserWindowWidth = Window.getClientWidth()
                        + Window.getScrollLeft();
                int browserWindowHeight = Window.getClientHeight()
                        + Window.getScrollTop();
                if (left + popup.getOffsetWidth() > browserWindowWidth - extra) {
                    left = getAbsoluteLeft()
                            - (popup.getOffsetWidth() - getOffsetWidth());
                }
                if (top + popup.getOffsetHeight() > browserWindowHeight - extra) {
                    top = getAbsoluteTop() - popup.getOffsetHeight() - 2;
                }

                adjustPopupWidth();

                popup.setPopupPosition(left, top);
                popup.setVisible(true);
            }
        });
	}

    /**
     * Expand items in popup after size calculation
     */
    private void adjustPopupWidth() {
        // calculate width for items
        com.google.gwt.user.client.Element containerElement = popup.getContainerElement();
        containerElement.addClassName(CONSTRUCTED_POPUP_CLASSNAME);
    }

	private void hidePopup() {
		popup.setVisible(false);
		popup.hide();
	}

	private static native void nativeBlur(Element e)
	/*-{
	    if (e && e.blur) {
	        e.blur();
	    }
	}-*/;

	private class LayoutPopup extends VOverlay {

		public static final String CLASSNAME = VPopupButton.CLASSNAME
				+ "-popup";

		private final Set<Element> activeChildren = new HashSet<Element>();

		private boolean hiding = false;

		public LayoutPopup() {
			super(false, false, true);
			setStyleName(CLASSNAME);
		}

		public void updateFromUIDL(final UIDL uidl) {
			if (Util.isCached(uidl.getChildUIDL(0))) {
				return;
			}

			Paintable newPopupComponent = client.getPaintable(uidl.getChildUIDL(0));
			if (!newPopupComponent.equals(getPaintable())) {
				if (getPaintable() != null) {
					client.unregisterPaintable(getPaintable());
				}
				setWidget((Widget) newPopupComponent);
			}
			getPaintable().updateFromUIDL(uidl.getChildUIDL(0), client);
		}

		private Paintable getPaintable() {
			return (Paintable) getWidget();
		}

		private VCaptionWrapper getCaptionWrapper() {
			if (getWidget() instanceof VCaptionWrapper) {
				return (VCaptionWrapper) getWidget();
			}
			return null;
		}

		@Override
		protected void onPreviewNativeEvent(NativePreviewEvent event) {
			Element target = Element
                    .as(event.getNativeEvent().getEventTarget());
            switch (event.getTypeInt()) {
                case Event.ONCLICK:
                    if (isOrHasChildOfButton(target)) {
                        updateState(false, true);
                    } else if (autoClose && target instanceof com.google.gwt.user.client.Element &&
                            Util.findWidget((com.google.gwt.user.client.Element) target,
                                    VButton.class) instanceof VButton) {
                        updateState(false, true);
                    }

                    break;
                case Event.ONMOUSEDOWN:
                    if (!isOrHasChildOfPopup(target)
                            && !isOrHasChildOfConsole(target)
                            && !isOrHasChildOfButton(target)) {
                        updateState(false, true);
                    }
                    break;
			case Event.ONKEYPRESS:
				if (isOrHasChildOfPopup(target)) {
					// Catch children that use keyboard, so we can unfocus them
					// when
					// hiding
					activeChildren.add(target);
				}
				break;
			default:
				break;
			}

			super.onPreviewNativeEvent(event);
		}

		private boolean isOrHasChildOfPopup(Element element) {
			return getElement().isOrHasChild(element);
		}

		private boolean isOrHasChildOfButton(Element element) {
			return VPopupButton.this.getElement().isOrHasChild(element);
		}

		private boolean isOrHasChildOfConsole(Element element) {
			Console console = ApplicationConnection.getConsole();
			return console instanceof VDebugConsole
					&& ((VDebugConsole) console).getElement().isOrHasChild(
							element);
		}

		/*
		 *
		 * We need a hack make popup act as a child of VPopupButton in Vaadin's
		 * component tree, but work in default GWT manner when closing or
		 * opening.
		 *
		 * (non-Javadoc)
		 *
		 * @see com.google.gwt.user.client.ui.Widget#getParent()
		 */
		@Override
		public Widget getParent() {
			if (!isAttached() || hiding) {
				return super.getParent();
			} else {
				return VPopupButton.this;
			}
		}

		@Override
		protected void onDetach() {
			super.onDetach();
			hiding = false;
		}

		public void hide(boolean autoClosed) {
			hiding = true;
			syncChildren();
			super.hide(autoClosed);
		}

		@Override
		public void show() {
			hiding = false;
			super.show();
		}

		/*-
		private void unregisterPaintables() {
			if (getPaintable() != null) {
				client.unregisterPaintable(getPaintable());
			}
		}*/

		/**
		 * Try to sync all known active child widgets to server
		 */
		private void syncChildren() {
			// Notify children with focus
			if ((getWidget() instanceof Focusable)) {
				((Focusable) getWidget()).setFocus(false);
			} else {
				checkForRTE(getWidget());
			}

			// Notify children that have used the keyboard
			for (Element e : activeChildren) {
				try {
					nativeBlur(e);
				} catch (Exception ignored) {
				}
			}
			activeChildren.clear();
		}

		private void checkForRTE(Widget popupComponentWidget2) {
			if (popupComponentWidget2 instanceof VRichTextArea) {
				((VRichTextArea) popupComponentWidget2)
						.synchronizeContentToServer();
			} else if (popupComponentWidget2 instanceof HasWidgets) {
				HasWidgets hw = (HasWidgets) popupComponentWidget2;
                for (Object aHw : hw)
                    checkForRTE((Widget) aHw);
			}
		}

		@Override
		public com.google.gwt.user.client.Element getContainerElement() {
			return super.getContainerElement();
		}

		@Override
		public void updateShadowSizeAndPosition() {
			super.updateShadowSizeAndPosition();
		}
	}

	public RenderSpace getAllocatedSpace(Widget child) {
		Size popupExtra = calculatePopupExtra();

		return new RenderSpace(RootPanel.get().getOffsetWidth()
				- popupExtra.getWidth(), RootPanel.get().getOffsetHeight()
				- popupExtra.getHeight());
	}

	/**
	 * Calculate extra space taken by the popup decorations
	 *
	 * @return Size
	 */
	protected Size calculatePopupExtra() {
		Element pe = popup.getElement();
		Element ipe = popup.getContainerElement();

		// border + padding
		int width = Util.getRequiredWidth(pe) - Util.getRequiredWidth(ipe);
		int height = Util.getRequiredHeight(pe) - Util.getRequiredHeight(ipe);

		return new Size(width, height);
	}

	public boolean hasChildComponent(Widget component) {
		if (popup.getWidget() != null) {
			return popup.getWidget() == component;
		} else {
			return false;
		}
	}

	public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
		if (!hasChildComponent(oldComponent)) {
			throw new IllegalArgumentException();
		}
		popup.setWidget(newComponent);
	}

	public boolean requestLayout(Set<Paintable> children) {
		popup.updateShadowSizeAndPosition();
		return true;
	}

	public void updateCaption(Paintable component, UIDL uidl) {
		if (VCaption.isNeeded(uidl)) {
			if (popup.getCaptionWrapper() != null) {
				popup.getCaptionWrapper().updateCaption(uidl);
			} else {
				VCaptionWrapper captionWrapper = new VCaptionWrapper(component,
						client);
				popup.setWidget(captionWrapper);
				captionWrapper.updateCaption(uidl);
			}
		} else {
			if (popup.getCaptionWrapper() != null) {
				popup.setWidget((Widget) popup.getCaptionWrapper()
						.getPaintable());
			}
		}
	}

	public Iterator<Widget> iterator() {
		return new Iterator<Widget>() {

			int pos = 0;

			public boolean hasNext() {
				// There is a child widget only if next() has not been called.
				return (pos == 0);
			}

			public Widget next() {
				// Next can be called only once to return the popup.
				if (pos != 0) {
					throw new NoSuchElementException();
				}
				pos++;
				return popup;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		hidePopup();
	}

    public boolean isOpened() {
        return popupVisible;
    }

    public void close() {
        hidePopup();
        updateState(false, true);
    }
}