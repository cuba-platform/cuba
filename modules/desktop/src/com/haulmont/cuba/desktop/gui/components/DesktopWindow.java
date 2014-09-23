/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.data.ComponentSize;
import com.haulmont.cuba.desktop.gui.data.DesktopContainerHelper;
import com.haulmont.cuba.desktop.gui.data.TreeModelAdapter;
import com.haulmont.cuba.desktop.sys.DesktopWindowManager;
import com.haulmont.cuba.desktop.sys.DialogWindow;
import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.desktop.sys.vcl.FocusableComponent;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.settings.Settings;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopWindow implements Window, Component.Disposable,
        Component.Wrapper, Component.HasXmlDescriptor, WrappedWindow, DesktopContainer {

    protected Log log = LogFactory.getLog(getClass());

    protected boolean disposed = false;

    protected BoxLayoutAdapter layoutAdapter;
    protected JPanel panel;

    protected String id;

    protected Map<String, Component> componentByIds = new HashMap<>();
    protected Collection<Component> ownComponents = new LinkedHashSet<>();

    protected Map<String, Component> allComponents = new HashMap<>();

    protected DsContext dsContext;
    protected WindowContext context;
    protected String messagePack;
    protected String focusComponentId;
    protected Element xmlDescriptor;
    protected String caption;
    protected String description;
    protected Component expandedComponent;
    protected Map<Component, ComponentCaption> captions = new HashMap<>();
    protected Map<Component, Pair<JPanel, BoxLayoutAdapter>> wrappers = new HashMap<>();

    protected WindowDelegate delegate;

    protected DesktopFrameActionsHolder actionsHolder;

    protected List<CloseListener> listeners = new ArrayList<>();

    protected boolean forceClose;
    protected Runnable doAfterClose;

    protected List<Timer> timers = new ArrayList<>();

    protected DesktopWindowManager windowManager;

    protected Configuration configuration = AppBeans.get(Configuration.NAME);
    protected Messages messages = AppBeans.get(Messages.NAME);

    protected ComponentSize widthSize;
    protected ComponentSize heightSize;

    protected boolean scheduledRepaint = false;

    public DesktopWindow() {
        initLayout();
        delegate = createDelegate();
        actionsHolder = new DesktopFrameActionsHolder(this, panel);
    }

    protected void initLayout() {
        panel = new JPanel();
        layoutAdapter = BoxLayoutAdapter.create(panel);
        layoutAdapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.Y);
        layoutAdapter.setMargin(true);
    }

    protected WindowDelegate createDelegate() {
        return new WindowDelegate(this);
    }

    @Override
    public Element getXmlDescriptor() {
        return xmlDescriptor;
    }

    @Override
    public void setXmlDescriptor(Element element) {
        xmlDescriptor = element;
    }

    @Override
    public void addListener(CloseListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeListener(CloseListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void applySettings(Settings settings) {
        delegate.applySettings(settings);
    }

    @Override
    public void saveSettings() {
        delegate.saveSettings();
    }

    @Override
    public void setFocusComponent(String componentId) {
        this.focusComponentId = componentId;
        if (componentId != null) {
            Component component = getComponent(componentId);
            if (component != null) {
                component.requestFocus();
            } else {
                log.error("Can't find focus component: " + componentId);
            }
        } else {
            findAndFocusChildComponent();
        }
    }

    public boolean findAndFocusChildComponent() {
        final java.awt.Component focusComponent = getComponentToFocus(getContainer());
        if (focusComponent != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    focusComponent.requestFocus();
                }
            });
            return true;
        }
        return false;
    }

    //todo devyatkin find another way to get component to focus
    private java.awt.Component getComponentToFocus(java.awt.Container component) {
        if (component.isFocusable() && component.isEnabled()
                && DesktopComponentsHelper.isRecursivelyVisible(component)) {
            if (component instanceof JComboBox
                    || component instanceof JCheckBox
                    || component instanceof JTable
                    || component instanceof JTree) {
                return component;
            } else if (component instanceof JTextComponent && ((JTextComponent) component).isEditable()) {
                return component;
            }
        }
        for (java.awt.Component child : component.getComponents()) {
            if (child instanceof JTabbedPane) {
                // #PL-3176
                // we don't know about selected tab after request
                // may be focused component lays on not selected tab
                continue;
            }
            if (child instanceof java.awt.Container) {
                java.awt.Component result = getComponentToFocus((java.awt.Container) child);
                if (result != null) {
                    return result;
                }
            } else {
                return child;
            }
        }
        return null;
    }

    @Override
    public String getFocusComponent() {
        return focusComponentId;
    }

    @Override
    public Settings getSettings() {
        return delegate.getSettings();
    }

    @Override
    public boolean close(final String actionId) {
        if (!forceClose) {
            if (!delegate.preClose(actionId))
                return false;
        }

        if (!forceClose && isModified()) {
            final Committable committable = (getWrapper() instanceof Committable) ? (Committable) getWrapper() :
                        (this instanceof Committable) ? (Committable) this : null;
            if ((committable != null) && configuration.getConfig(ClientConfig.class).getUseSaveConfirmation()) {
                windowManager.showOptionDialog(
                        messages.getMainMessage("closeUnsaved.caption"),
                        messages.getMainMessage("saveUnsaved"),
                        MessageType.WARNING,
                        new Action[]{
                                new DialogAction(DialogAction.Type.OK) {
                                    @Override
                                    public String getCaption() {
                                        return messages.getMainMessage("closeUnsaved.save");
                                    }
                                    @Override
                                    public void actionPerform(Component component) {
                                        committable.commitAndClose();
                                    }
                                },
                                new AbstractAction("discard") {
                                    @Override
                                    public String getCaption() {
                                        return messages.getMainMessage("closeUnsaved.discard");
                                    }
                                    @Override
                                    public String getIcon() {
                                        return "icons/cancel.png";
                                    }
                                    @Override
                                    public void actionPerform(Component component) {
                                        close(actionId, true);
                                    }
                                },
                                new DialogAction(DialogAction.Type.CANCEL) {
                                    @Override
                                    public String getIcon() {
                                        return null;
                                    }
                                    @Override
                                    public void actionPerform(Component component) {
                                        doAfterClose = null;
                                    }
                                }
                        }
                );
            } else {
                windowManager.showOptionDialog(
                        messages.getMessage(AppConfig.getMessagesPack(), "closeUnsaved.caption"),
                        messages.getMessage(AppConfig.getMessagesPack(), "closeUnsaved"),
                        MessageType.WARNING,
                        new Action[]{
                                new DialogAction(DialogAction.Type.YES) {
                                    @Override
                                    public void actionPerform(Component component) {
                                        forceClose = true;
                                        close(actionId);
                                    }

                                },
                                new DialogAction(DialogAction.Type.NO) {
                                    @Override
                                    public void actionPerform(Component component) {
                                        doAfterClose = null;
                                    }
                                }
                        }
                );
            }
            return false;
        }

        if (delegate.getWrapper() != null)
            delegate.getWrapper().saveSettings();
        else
            saveSettings();

        delegate.disposeComponents();

        windowManager.close(this);
        boolean res = onClose(actionId);
        if (res && doAfterClose != null) {
            doAfterClose.run();
        }

        stopTimers();

        return res;
    }

    protected boolean isModified() {
        return getDsContext() != null && getDsContext().isModified();
    }

    private void stopTimers() {
        // hard stop timers
        for (Timer timer : timers) {
            ((DesktopTimer)timer).disposeTimer();
        }
    }

    @Override
    public boolean close(String actionId, boolean force) {
        forceClose = force;
        return close(actionId);
    }

    @Override
    public void closeAndRun(String actionId, Runnable runnable) {
        this.doAfterClose = runnable;
        close(actionId);
    }

    @Override
    public void addTimer(Timer timer) {
        if (timer instanceof DesktopTimer) {
            timers.add(timer);
        }
    }

    @Override
    public Timer getTimer(String id) {
        if (id == null)
            throw new IllegalArgumentException("id is null");

        for (Timer timer : timers) {
            if (id.equals(timer.getId()))
                return timer;
        }
        return null;
    }

    @Override
    public void addAction(final Action action) {
        actionsHolder.addAction(action);
    }

    @Override
    public void removeAction(Action action) {
        actionsHolder.removeAction(action);
    }

    @Override
    public void removeAction(String id) {
        actionsHolder.removeAction(id);
    }

    @Override
    public void removeAllActions() {
        actionsHolder.removeAllActions();
    }

    @Override
    public Collection<Action> getActions() {
        return actionsHolder.getActions();
    }

    @Override
    public Action getAction(String id) {
        return actionsHolder.getAction(id);
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public WindowContext getContext() {
        return context;
    }

    @Override
    public void setContext(FrameContext ctx) {
        context = (WindowContext) ctx;
    }

    @Override
    public DsContext getDsContext() {
        return dsContext;
    }

    @Override
    public void setDsContext(DsContext dsContext) {
        this.dsContext = dsContext;
    }

    @Override
    public String getMessagesPack() {
        return messagePack;
    }

    @Override
    public void setMessagesPack(String name) {
        messagePack = name;
    }

    @Override
    public void registerComponent(Component component) {
        if (component.getId() != null)
            allComponents.put(component.getId(), component);
    }

    @Nullable
    @Override
    public Component getRegisteredComponent(String id) {
        return allComponents.get(id);
    }

    @Override
    public boolean isValid() {
        return delegate.isValid();
    }

    @Override
    public void validate() throws ValidationException {
        delegate.validate();
    }

    @Override
    public DialogParams getDialogParams() {
        return getWindowManager().getDialogParams();
    }

    @Override
    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.openWindow(windowAlias, openType, params);
    }

    @Override
    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        return delegate.openWindow(windowAlias, openType);
    }

    @Override
    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        return delegate.openEditor(windowAlias, item, openType, params, parentDs);
    }

    @Override
    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.openEditor(windowAlias, item, openType, params);
    }

    @Override
    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        return delegate.openEditor(windowAlias, item, openType, parentDs);
    }

    @Override
    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        return delegate.openEditor(windowAlias, item, openType);
    }

    @Override
    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.openLookup(windowAlias, handler, openType, params);
    }

    @Override
    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        return delegate.openLookup(windowAlias, handler, openType);
    }

    @Override
    public <T extends IFrame> T openFrame(Component parent, String windowAlias) {
        return delegate.openFrame(parent, windowAlias);
    }

    @Override
    public <T extends IFrame> T openFrame(Component parent, String windowAlias, Map<String, Object> params) {
        return delegate.openFrame(parent, windowAlias, params);
    }

    @Override
    public DesktopWindowManager getWindowManager() {
        return windowManager;
    }

    @Override
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = (DesktopWindowManager) windowManager;
    }

    @Override
    public void showMessageDialog(String title, String message, MessageType messageType) {
        getWindowManager().showMessageDialog(title, message, messageType);
    }

    @Override
    public void showOptionDialog(String title, String message, MessageType messageType, Action[] actions) {
        getWindowManager().showOptionDialog(title, message, messageType, actions);
    }

    @Override
    public void showOptionDialog(String title, String message, MessageType messageType, java.util.List<Action> actions) {
        getWindowManager().showOptionDialog(title, message, messageType, actions.toArray(new Action[actions.size()]));
    }

    @Override
    public void showNotification(String caption, NotificationType type) {
        getWindowManager().showNotification(caption, type);
    }

    @Override
    public void showNotification(String caption, String description, NotificationType type) {
        getWindowManager().showNotification(caption, description, type);
    }

    @Override
    public void showWebPage(String url, @Nullable Map<String, Object> params) {
        getWindowManager().showWebPage(url, params);
    }

    @Override
    public <A extends IFrame> A getFrame() {
        return (A) this;
    }

    @Override
    public void setFrame(IFrame frame) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void expand(Component component, String height, String width) {
        if (expandedComponent != null && expandedComponent instanceof DesktopComponent) {
            ((DesktopComponent) expandedComponent).setExpanded(false);
        }

        // only Y direction
        if (StringUtils.isEmpty(height) || "-1px".equals(height) || height.endsWith("%")) {
            component.setHeight("100%");
        }

        JComponent composition = DesktopComponentsHelper.getComposition(component);
        layoutAdapter.expand(composition, height, width);

        if (component instanceof DesktopComponent) {
            ((DesktopComponent) component).setExpanded(true);
        }

        expandedComponent = component;
    }

    @Override
    public void expand(Component component) {
        expand(component, "", "");
    }

    @Override
    public boolean isExpanded(Component component) {
        return expandedComponent == component;
    }

    protected void requestRepaint() {
        if (!scheduledRepaint) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    panel.revalidate();
                    panel.repaint();

                    java.awt.Container container = panel.getTopLevelAncestor();
                    if (container instanceof DialogWindow) {
                        DialogWindow dialog = (DialogWindow) container;
                        if (!dialog.isResizable() && getHeight() <= 0) {
                            dialog.pack();
                        }
                    }

                    scheduledRepaint = false;
                }
            });

            scheduledRepaint = true;
        }
    }

    @Override
    public void add(Component component) {
        ComponentCaption caption = null;
        boolean haveDescription = false;
        if (DesktopContainerHelper.hasExternalCaption(component)) {
            caption = new ComponentCaption(component);
            captions.put(component, caption);
            getContainer().add(caption, layoutAdapter.getCaptionConstraints(component));
        } else if (DesktopContainerHelper.hasExternalDescription(component)) {
            caption = new ComponentCaption(component);
            captions.put(component, caption);
            haveDescription = true;
        }

        JComponent composition = DesktopComponentsHelper.getComposition(component);
         //if component have description without caption, we need to wrap
        // component to view Description button horizontally after component
        if (haveDescription) {
            JPanel wrapper = new JPanel();
            BoxLayoutAdapter adapter = BoxLayoutAdapter.create(wrapper);
            adapter.setExpandLayout(true);
            adapter.setSpacing(false);
            adapter.setMargin(false);
            wrapper.add(composition);
            wrapper.add(caption, new CC().alignY("top"));
            getContainer().add(wrapper, layoutAdapter.getConstraints(component));
            wrappers.put(component, new Pair<>(wrapper, adapter));
        } else {
            getContainer().add(composition, layoutAdapter.getConstraints(component));
        }
        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
            registerComponent(component);
        }
        ownComponents.add(component);

        DesktopContainerHelper.assignContainer(component, this);

        requestRepaint();
    }

    @Override
    public void remove(Component component) {
        if (wrappers.containsKey(component)) {
            getContainer().remove(wrappers.get(component).getFirst());
            wrappers.remove(component);
        } else {
            getContainer().remove(DesktopComponentsHelper.getComposition(component));
        }
        getContainer().validate();
        if (captions.containsKey(component)) {
            getContainer().remove(captions.get(component));
            captions.remove(component);
        }
        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
        ownComponents.remove(component);

        DesktopContainerHelper.assignContainer(component, null);
        if (expandedComponent == component) {
            expandedComponent = null;
        }

        requestRepaint();
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        //noinspection unchecked
        return (T) componentByIds.get(id);
    }

    @Override
    public <T extends Component> T getComponent(String id) {
        return ComponentsHelper.getWindowComponent(this, id);
    }

    @Nonnull
    @Override
    public <T extends Component> T getComponentNN(String id) {
        T component = getComponent(id);
        if (component == null) {
            throw new IllegalArgumentException(String.format("Not found component with id '%s'", id));
        }
        return component;
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    @Override
    public <T> T getComponent() {
        return (T) panel;
    }

    @Override
    public JComponent getComposition() {
        return panel;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDebugId() {
        return null;
    }

    @Override
    public void setDebugId(String id) {
    }

    @Override
    public boolean isEnabled() {
        return panel.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        panel.setEnabled(enabled);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void requestFocus() {
    }

    @Override
    public float getHeight() {
        return heightSize != null ? heightSize.value : -1;
    }

    @Override
    public int getHeightUnits() {
        return heightSize != null ? heightSize.unit : 0;
    }

    @Override
    public void setHeight(String height) {
        int w = getContainer().getWidth();

        ComponentSize h = ComponentSize.parse(height);
        if (h.inPixels()) {
            Dimension dimension = new Dimension(w, (int) h.value);
            getContainer().setMinimumSize(dimension);
            getContainer().setPreferredSize(dimension);
        } else if (h.inPercents()) {
            // TODO determine height of main frame, and multiply by percents
            // such method is used in permission-show.xml
            int hValue = 400;
            Dimension dimension = new Dimension(w, hValue);
            getContainer().setMinimumSize(dimension);
            getContainer().setPreferredSize(dimension);
        }

        heightSize = ComponentSize.parse(height);
    }

    @Override
    public float getWidth() {
        return widthSize != null ? widthSize.value : -1;
    }

    @Override
    public int getWidthUnits() {
        return widthSize != null ? widthSize.unit : 0;
    }

    @Override
    public void setWidth(String width) {
        widthSize = ComponentSize.parse(width);
    }

    @Override
    public Alignment getAlignment() {
        return null;
    }

    @Override
    public void setAlignment(Alignment alignment) {
    }

    @Override
    public String getStyleName() {
        return null;
    }

    @Override
    public void setStyleName(String name) {
    }

    @Override
    public void setMargin(boolean enable) {
        layoutAdapter.setMargin(enable);
    }

    @Override
    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        layoutAdapter.setMargin(topEnable, rightEnable, bottomEnable, leftEnable);
    }

    @Override
    public void setSpacing(boolean enabled) {
        layoutAdapter.setSpacing(enabled);
    }

    @Override
    public Window wrapBy(Class<Window> wrapperClass) {
        return delegate.wrapBy(wrapperClass);
    }

    @Override
    public Window getWrapper() {
        return delegate.getWrapper();
    }

    protected boolean onClose(String actionId) {
        fireWindowClosed(actionId);
        return true;
    }

    protected void fireWindowClosed(String actionId) {
        for (Object listener : listeners) {
            if (listener instanceof CloseListener) {
                ((CloseListener) listener).windowClosed(actionId);
            }
        }
    }

    protected JComponent getContainer() {
        return panel;
    }

    @Override
    public void updateComponent(Component child) {
        JComponent composition;
        if (wrappers.containsKey(child)) {
            composition = wrappers.get(child).getFirst();
        } else {
            composition = DesktopComponentsHelper.getComposition(child);
        }
        layoutAdapter.updateConstraints(composition, layoutAdapter.getConstraints(child));
        if (captions.containsKey(child)) {
            ComponentCaption caption = captions.get(child);
            caption.update();
            BoxLayoutAdapter adapterForCaption = layoutAdapter;
            if (wrappers.containsKey(child)) {
                adapterForCaption = wrappers.get(child).getSecond();
            }
            adapterForCaption.updateConstraints(caption, adapterForCaption.getCaptionConstraints(child));
        }
        requestRepaint();
    }

    @Override
    public void dispose() {
        // hard stop timers
        stopTimers();
        disposed = true;
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    @Override
    public boolean validate(List<Validatable> fields) {
        ValidationErrors errors = new ValidationErrors();

        for (Validatable field : fields) {
            try {
                field.validate();
            } catch (ValidationException e) {
                if (log.isTraceEnabled())
                    log.trace("Validation failed", e);
                else if (log.isDebugEnabled())
                    log.debug("Validation failed: " + e);

                ComponentsHelper.fillErrorMessages(field, e, errors);
            }
        }

        return handleValidationErrors(errors);
    }

    @Override
    public boolean validateAll() {
        ValidationErrors errors = new ValidationErrors();

        Collection<Component> components = ComponentsHelper.getComponents(this);
        for (Component component : components) {
            if (component instanceof Validatable) {
                try {
                    ((Validatable) component).validate();
                } catch (ValidationException e) {
                    if (log.isTraceEnabled())
                        log.trace("Validation failed", e);
                    else if (log.isDebugEnabled())
                        log.debug("Validation failed: " + e);

                    ComponentsHelper.fillErrorMessages((Validatable) component, e, errors);
                }
            }
        }

        return handleValidationErrors(errors);
    }

    protected boolean handleValidationErrors(ValidationErrors errors) {
        delegate.postValidate(errors);

        if (errors.isEmpty())
            return true;

        showValidationErrors(errors);

        focusProblemComponent(errors);

        return false;
    }

    protected void focusProblemComponent(ValidationErrors errors) {
        Component component = null;
        if (!errors.getAll().isEmpty()) {
            component = errors.getAll().iterator().next().component;
        }

        if (component != null) {
            try {
                JComponent jComponent = DesktopComponentsHelper.unwrap(component);
                java.awt.Component c = jComponent;
                java.awt.Component prevC = null;
                while (c != null) {
                    if (c instanceof JTabbedPane && !((JTabbedPane) c).getSelectedComponent().equals(prevC)) {
                        ((JTabbedPane) c).setSelectedComponent(prevC);
                        break;
                    }
                    prevC = c;
                    c = c.getParent();
                }

                if (jComponent instanceof FocusableComponent) {
                    ((FocusableComponent) jComponent).focus();
                } else {
                    // focus first up component
                    c = jComponent;
                    while (c != null) {
                        if (c.isFocusable()) {
                            final java.awt.Component focusComponent = c;
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    focusComponent.requestFocusInWindow();
                                    focusComponent.requestFocus();
                                }
                            });
                            break;
                        }
                        c = c.getParent();
                    }
                }
            } catch (Exception e) {
                log.warn("Error while validation handling ", e);
            }
        }
    }

    protected void showValidationErrors(ValidationErrors errors) {
        StringBuilder buffer = new StringBuilder();
        for (ValidationErrors.Item error : errors.getAll()) {
            buffer.append(error.description).append("\n");
        }
        showNotification(
                messages.getMainMessage("validationFail.caption"),
                buffer.toString(),
                NotificationType.HUMANIZED
        );
    }

    public static class Editor extends DesktopWindow implements Window.Editor {

        @Override
        protected WindowDelegate createDelegate() {
            return new EditorWindowDelegate(this);
        }

        @Override
        public Entity getItem() {
            return ((EditorWindowDelegate) delegate).getItem();
        }

        @Override
        public void setItem(Entity item) {
            ((EditorWindowDelegate) delegate).setItem(item);
        }

        @Override
        public boolean onClose(String actionId) {
            releaseLock();
            return super.onClose(actionId);
        }

        public void releaseLock() {
            ((EditorWindowDelegate) delegate).releaseLock();
        }

        @Nullable
        @Override
        public Datasource getParentDs() {
            return ((EditorWindowDelegate) delegate).getParentDs();
        }

        @Override
        public void setParentDs(Datasource parentDs) {
            ((EditorWindowDelegate) delegate).setParentDs(parentDs);
        }

        protected Datasource getDatasource() {
            return delegate.getDatasource();
        }

        @Override
        public boolean isModified() {
            return ((EditorWindowDelegate) delegate).isModified();
        }

        @Override
        public boolean commit() {
            return commit(true);
        }

        @Override
        public boolean commit(boolean validate) {
            if (validate && !getWrapper().validateAll())
                return false;

            return ((EditorWindowDelegate) delegate).commit(false);
        }

        @Override
        public void commitAndClose() {
            if (!getWrapper().validateAll())
                return;

            if (((EditorWindowDelegate) delegate).commit(true))
                close(COMMIT_ACTION_ID);
        }

        @Override
        public boolean isLocked() {
            return ((EditorWindowDelegate) delegate).isLocked();
        }
    }

    public static class Lookup extends DesktopWindow implements Window.Lookup {

        private Component lookupComponent;
        private Handler handler;
        private Validator validator;
        private SelectListener selectListener;

        private JPanel container;

        public Lookup() {
            Configuration configuration = AppBeans.get(Configuration.NAME);
            ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);

            addAction(new AbstractAction(WindowDelegate.LOOKUP_SELECTED_ACTION_ID, clientConfig.getCommitShortcut()) {
                @Override
                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                    fireSelectAction();
                }
            });
        }

        @Override
        public Component getLookupComponent() {
            return lookupComponent;
        }

        @Override
        public void setLookupComponent(Component lookupComponent) {
            this.lookupComponent = lookupComponent;
            if (lookupComponent instanceof com.haulmont.cuba.gui.components.Table) {
                com.haulmont.cuba.gui.components.Table table = (com.haulmont.cuba.gui.components.Table) lookupComponent;
                table.setEnterPressAction(
                        new AbstractAction(WindowDelegate.LOOKUP_ENTER_PRESSED_ACTION_ID) {
                            @Override
                            public void actionPerform(Component component) {
                                fireSelectAction();
                            }
                        });
                table.setItemClickAction(
                        new AbstractAction(WindowDelegate.LOOKUP_ITEM_CLICK_ACTION_ID) {
                            @Override
                            public void actionPerform(Component component) {
                                fireSelectAction();
                            }
                        });
            } else if (lookupComponent instanceof Tree) {
                final Tree tree = (Tree) lookupComponent;
                final JTree treeComponent = (JTree) DesktopComponentsHelper.unwrap(tree);
                treeComponent.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            int rowForLocation = treeComponent.getRowForLocation(e.getX(), e.getY());
                            TreePath pathForLocation = treeComponent.getPathForRow(rowForLocation);
                            if (pathForLocation != null) {
                                CollectionDatasource treeCds = tree.getDatasource();
                                if (treeCds != null) {
                                    TreeModelAdapter.Node treeItem = (TreeModelAdapter.Node) pathForLocation.getLastPathComponent();
                                    if (treeItem != null) {
                                        treeCds.setItem(treeItem.getEntity());
                                        fireSelectAction();
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }

        @Override
        public Handler getLookupHandler() {
            return handler;
        }

        @Override
        public void setLookupHandler(Handler handler) {
            this.handler = handler;
        }

        @Override
        public Validator getLookupValidator() {
            return validator;
        }

        @Override
        public void setLookupValidator(Validator validator) {
            this.validator = validator;
        }

        protected void fireSelectAction() {
            if (selectListener != null) {
                selectListener.actionPerformed(null);
            }
        }

        @Override
        protected void initLayout() {
            panel = new JPanel();
            panel.setLayout(
                    new MigLayout(
                            "flowy, fillx, ins 0" + (LayoutAdapter.isDebug() ? ", debug" : ""),
                            "",
                            "[]0[]")
            );

            container = new JPanel();
            layoutAdapter = BoxLayoutAdapter.create(container);
            layoutAdapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.Y);
            layoutAdapter.setMargin(true);

            panel.add(container, "grow, height 100%, width 100%");

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(
                    new MigLayout("ins 0 n n n" + (LayoutAdapter.isDebug() ? ", debug" : ""))
            );

            selectListener = new SelectListener();

            JButton selectBtn = new JButton(messages.getMessage(AppConfig.getMessagesPack(), "actions.Select"));
            selectBtn.setIcon(App.getInstance().getResources().getIcon("icons/ok.png"));
            selectBtn.addActionListener(selectListener);
            DesktopComponentsHelper.adjustSize(selectBtn);
            buttonsPanel.add(selectBtn);

            JButton cancelBtn = new JButton(messages.getMessage(AppConfig.getMessagesPack(), "actions.Cancel"));
            cancelBtn.setIcon(App.getInstance().getResources().getIcon("icons/cancel.png"));
            cancelBtn.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            close("cancel");
                        }
                    }
            );
            DesktopComponentsHelper.adjustSize(cancelBtn);
            buttonsPanel.add(cancelBtn);

            panel.add(buttonsPanel);
        }

        @Override
        protected JComponent getContainer() {
            return container;
        }

        private class SelectListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getLookupValidator() != null && !getLookupValidator().validate()) {
                    return;
                }

                Collection selected;
                if (lookupComponent instanceof com.haulmont.cuba.gui.components.Table) {
                    selected = ((com.haulmont.cuba.gui.components.Table) lookupComponent).getSelected();
                } else if (lookupComponent instanceof com.haulmont.cuba.gui.components.Tree) {
                    selected = ((com.haulmont.cuba.gui.components.Tree) lookupComponent).getSelected();
                } else if (lookupComponent instanceof LookupField) {
                    selected = Collections.singleton(((LookupField) lookupComponent).getValue());
                } else if (lookupComponent instanceof PickerField) {
                    selected = Collections.singleton(((PickerField) lookupComponent).getValue());
                } else if (lookupComponent instanceof OptionsGroup) {
                    final OptionsGroup optionsGroup = (OptionsGroup) lookupComponent;
                    selected = optionsGroup.getValue();
                } else {
                    throw new UnsupportedOperationException();
                }
                close("select");
                for (Object obj : selected) {
                    if (obj instanceof Instance) {
                        ((Instance) obj).removeAllListeners();
                    }
                }
                handler.handleLookup(selected);
            }
        }
    }
}