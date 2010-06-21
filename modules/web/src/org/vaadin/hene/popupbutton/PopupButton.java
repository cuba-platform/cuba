package org.vaadin.hene.popupbutton;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.*;
import org.vaadin.hene.popupbutton.client.ui.VPopupButton;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * Server side component for the VPopupButton widget.
 */
// This class contains code from AbstractComponentContainer
@SuppressWarnings("serial")
@ClientWidget(VPopupButton.class)
public class PopupButton extends Button implements ComponentContainer {

	private static final Method COMPONENT_ATTACHED_METHOD;

	private static final Method COMPONENT_DETACHED_METHOD;

	static {
		try {
			COMPONENT_ATTACHED_METHOD = ComponentAttachListener.class
					.getDeclaredMethod("componentAttachedToContainer",
							new Class[] { ComponentAttachEvent.class });
			COMPONENT_DETACHED_METHOD = ComponentDetachListener.class
					.getDeclaredMethod("componentDetachedFromContainer",
							new Class[] { ComponentDetachEvent.class });
		} catch (final NoSuchMethodException e) {
			// This should never happen
			throw new RuntimeException(
					"Internal error finding methods in PopupButton");
		}
	}

	private Component component;

	private boolean popupVisible = false;

    private boolean autoClose = true;

    public PopupButton(String caption) {
		super(caption);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.Button#paintContent(com.vaadin.terminal.PaintTarget)
	 */
	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
        target.addVariable(this, "autoClose", autoClose);
		target.addVariable(this, "popupVisible", popupVisible);
		if (popupVisible) {
			if (component == null) {
				throw new IllegalStateException(
						"component cannot be null. Use addComponent to set the component.");
			}
			target.startTag("component");
			component.paint(target);
			target.endTag("component");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.Button#changeVariables(java.lang.Object,
	 * java.util.Map)
	 */
	@Override
	public void changeVariables(Object source, Map variables) {
		super.changeVariables(source, variables);
		if (variables.containsKey("popupVisible")) {
			popupVisible = ((Boolean) variables.get("popupVisible"))
					.booleanValue();
			requestRepaint();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.ui.ComponentContainer#addComponent(com.vaadin.ui.Component)
	 */
	public void addComponent(Component c) {
		component = c;
		requestRepaint();

		if (c instanceof ComponentContainer) {
			// Make sure we're not adding the component inside it's own content
			for (Component parent = this; parent != null; parent = parent
					.getParent()) {
				if (parent == c) {
					throw new IllegalArgumentException(
							"Component cannot be added inside it's own content");
				}
			}
		}

		if (c.getParent() != null) {
			// If the component already has a parent, try to remove it
			ComponentContainer oldParent = (ComponentContainer) c.getParent();
			oldParent.removeComponent(c);

		}

		c.setParent(this);
		fireEvent(new ComponentAttachEvent(this, component));
	}

	public void addListener(ComponentAttachListener listener) {
		addListener(ComponentAttachEvent.class, listener,
				COMPONENT_ATTACHED_METHOD);

	}

	public void addListener(ComponentDetachListener listener) {
		addListener(ComponentDetachEvent.class, listener,
				COMPONENT_DETACHED_METHOD);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.ComponentContainer#getComponentIterator()
	 */
	public Iterator<Component> getComponentIterator() {
		return new Iterator<Component>() {

			private boolean first = component == null;

			public boolean hasNext() {
				return !first;
			}

			public Component next() {
				if (!first) {
					first = true;
					return component;
				} else {
					return null;
				}
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * Not supported in this implementation.
	 *
	 * @see com.vaadin.ui.ComponentContainer#moveComponentsFrom(com.vaadin.ui.
	 *      ComponentContainer)
	 */
	public void moveComponentsFrom(ComponentContainer source) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.ComponentContainer#removeAllComponents()
	 */
	public void removeAllComponents() {
		if (component != null) {
			removeComponent(component);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.ui.ComponentContainer#removeComponent(com.vaadin.ui.Component)
	 */
	public void removeComponent(Component c) {
		if (c.getParent() == this) {
			c.setParent(null);
			fireEvent(new ComponentDetachEvent(this, c));
		}
		component = null;
		requestRepaint();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seecom.vaadin.ui.ComponentContainer#removeListener(com.vaadin.ui.
	 * ComponentContainer.ComponentAttachListener)
	 */
	public void removeListener(ComponentAttachListener listener) {
		removeListener(ComponentAttachEvent.class, listener,
				COMPONENT_ATTACHED_METHOD);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seecom.vaadin.ui.ComponentContainer#removeListener(com.vaadin.ui.
	 * ComponentContainer.ComponentDetachListener)
	 */
	public void removeListener(ComponentDetachListener listener) {
		removeListener(ComponentDetachEvent.class, listener,
				COMPONENT_DETACHED_METHOD);
	}

	/**
	 *
	 * Not supported in this implementation.
	 *
	 * @see com.vaadin.ui.ComponentContainer#replaceComponent(com.vaadin.ui.Component,
	 *      com.vaadin.ui.Component)
	 */
	public void replaceComponent(Component oldComponent, Component newComponent) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.ComponentContainer#requestRepaintAll()
	 */
	public void requestRepaintAll() {
		requestRepaint();
		if (component != null) {
			if (component instanceof Form) {
				// Form has children in layout, but is not ComponentContainer
				component.requestRepaint();
				((Form) component).getLayout().requestRepaintAll();
			} else if (component instanceof Table) {
				((Table) component).requestRepaintAll();
			} else if (component instanceof ComponentContainer) {
				((ComponentContainer) component).requestRepaintAll();
			} else {
				component.requestRepaint();
			}
		}
	}

	/**
	 * Shows or hides popup.
	 *
	 * @param popupVisible
	 *            if true, popup is set to visible, otherwise popup is hidden.
	 */
	public void setPopupVisible(boolean popupVisible) {
		this.popupVisible = popupVisible;
		requestRepaint();
	}

	/**
	 * Checks if the popup is visible.
	 *
	 * @return true, if popup is visible, false otherwise.
	 */
	public boolean isPopupVisible() {
		return popupVisible;
	}

	/**
	 * Set the content component of the popup.
	 *
	 * @param component
	 *            the component to be displayed in the popup.
	 */
	public void setComponent(Component component) {
		addComponent(component);
	}

    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
        requestRepaint();
    }

    public boolean isAutoClose() {
        return autoClose;
    }
}