/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.data.ComponentSize;
import com.haulmont.cuba.desktop.gui.data.DesktopContainerHelper;
import com.haulmont.cuba.desktop.sys.DesktopWindowManager;
import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.settings.Settings;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopWindow implements Window, Component.Disposable,
        Component.Wrapper, Component.HasXmlDescriptor, WrappedWindow, DesktopContainer
{
    private static final long serialVersionUID = 1026363207247384464L;

    private boolean disposed = false;

    protected BoxLayoutAdapter layoutAdapter;
    protected JPanel panel;

    protected String id;

    protected Map<String, Component> componentByIds = new HashMap<String, Component>();
    protected Collection<Component> ownComponents = new HashSet<Component>();

    protected Map<String, Component> allComponents = new HashMap<String, Component>();

    protected DsContext dsContext;
    protected WindowContext context;
    protected String messagePack;
    protected Element xmlDescriptor;
    protected String caption;
    protected String description;
    protected Component expandedComponent;
    protected Map<Component, ComponentCaption> captions = new HashMap<Component, ComponentCaption>();
    protected Map<Component, JPanel> wrappers = new HashMap<Component, JPanel>();

    protected WindowDelegate delegate;

    protected DesktopFrameActionsHolder actionsHolder;

    private List<CloseListener> listeners = new ArrayList<CloseListener>();

    protected boolean forceClose;
    protected Runnable doAfterClose;

    protected List<Timer> timers = new ArrayList<Timer>();

    private Log log = LogFactory.getLog(DesktopWindow.class);

    private DesktopWindowManager windowManager;

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
        Component component = getComponent(componentId);
        if (component != null) {
            component.requestFocus();
        } else {
            log.error("Can't find focus component: " + componentId);
        }
    }

    @Override
    public Settings getSettings() {
        return delegate.getSettings();
    }

    @Override
    public boolean close(final String actionId) {
        WindowManager windowManager = getWindowManager();

        if (!forceClose && getDsContext() != null && getDsContext().isModified()) {
            windowManager.showOptionDialog(
                    MessageProvider.getMessage(AppConfig.getMessagesPack(), "closeUnsaved.caption"),
                    MessageProvider.getMessage(AppConfig.getMessagesPack(), "closeUnsaved"),
                    MessageType.WARNING,
                    new Action[]{
                            new DialogAction(DialogAction.Type.YES) {
                                public void actionPerform(Component component) {
                                    forceClose = true;
                                    close(actionId);
                                }

                            },
                            new DialogAction(DialogAction.Type.NO) {
                                public void actionPerform(Component component) {
                                    doAfterClose = null;
                                }
                            }
                    }
            );
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
            ((DesktopTimer) timer).startTimer();
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
    public void setContext(WindowContext ctx) {
        context = ctx;
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
    public String getMessage(String key) {
        if (messagePack == null)
            throw new IllegalStateException("MessagePack is not set");
        return MessageProvider.getMessage(messagePack, key);
    }

    @Override
    public void registerComponent(Component component) {
        if (component.getId() != null)
            allComponents.put(component.getId(), component);
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
        return delegate.<T>openWindow(windowAlias, openType, params);
    }

    @Override
    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        return delegate.<T>openWindow(windowAlias, openType);
    }

    @Override
    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        return delegate.<T>openEditor(windowAlias, item, openType, params, parentDs);
    }

    @Override
    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.<T>openEditor(windowAlias, item, openType, params);
    }

    @Override
    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        return delegate.<T>openEditor(windowAlias, item, openType, parentDs);
    }

    @Override
    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        return delegate.<T>openEditor(windowAlias, item, openType);
    }

    @Override
    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.<T>openLookup(windowAlias, handler, openType, params);
    }

    @Override
    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        return delegate.<T>openLookup(windowAlias, handler, openType);
    }

    @Override
    public <T extends IFrame> T openFrame(Component parent, String windowAlias) {
        return delegate.<T>openFrame(parent, windowAlias);
    }

    @Override
    public <T extends IFrame> T openFrame(Component parent, String windowAlias, Map<String, Object> params) {
        return delegate.<T>openFrame(parent, windowAlias, params);
    }

    @Override
    public void detachFrame(String caption) {
        throw new UnsupportedOperationException("Can not detach " + getClass());
    }

    @Override
    public void attachFrame() {
        throw new UnsupportedOperationException("Can not attach "+ getClass());
    }

    @Override
    public void addDetachListener(DetachListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeDetachListener(DetachListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDetached() {
        return false;
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
    public void add(Component component) {
        ComponentCaption caption = null;
        boolean haveDescription = false;
        if (DesktopContainerHelper.hasExternalCaption(component)) {
            caption = new ComponentCaption(component);
            captions.put(component, caption);
            getContainer().add(caption, layoutAdapter.getCaptionConstraints());
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
            getContainer().add(wrapper);
            wrappers.put(component, wrapper);
        } else {
            getContainer().add(composition, layoutAdapter.getConstraints(component));
        }
        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
            registerComponent(component);
        }
        ownComponents.add(component);

        DesktopContainerHelper.assignContainer(component, this);
    }

    @Override
    public void remove(Component component) {
        if (wrappers.containsKey(component)) {
            getContainer().remove(wrappers.get(component));
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
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    @Override
    public <T extends Component> T getComponent(String id) {
        final String[] elements = ValuePathHelper.parse(id);
        if (elements.length == 1) {
            return (T) allComponents.get(id);
        } else {
            Component frame = allComponents.get(elements[0]);
            if (frame != null && frame instanceof Container) {
                final List<String> subList = Arrays.asList(elements).subList(1, elements.length);
                String subPath = ValuePathHelper.format(subList.toArray(new String[subList.size()]));
                return (T) ((Container) frame).getComponent(subPath);
            } else
                return null;
        }
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
        return 0;
    }

    @Override
    public int getHeightUnits() {
        return 0;
    }

    @Override
    public void setHeight(String height) {
        int w = getContainer().getWidth();

        ComponentSize h = ComponentSize.parse(height);
        if (h.inPixels()) {
            Dimension dimension = new Dimension(w, (int) h.value);
            getContainer().setMinimumSize(dimension);
            getContainer().setPreferredSize(dimension);
        }
        else if (h.inPercents()) {
            // TODO determine height of main frame, and multiply by percents
            // such method is used in permission-show.xml
            int hValue = 400;
            Dimension dimension = new Dimension(w, hValue);
            getContainer().setMinimumSize(dimension);
            getContainer().setPreferredSize(dimension);
        }
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public int getWidthUnits() {
        return 0;
    }

    @Override
    public void setWidth(String width) {
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
        JComponent composition = DesktopComponentsHelper.getComposition(child);
        layoutAdapter.updateConstraints(composition, layoutAdapter.getConstraints(child));
        if (captions.containsKey(child)) {
            captions.get(child).update();
        }
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
                    errors.add(component, e.getMessage());
                }
            }
        }

        delegate.postValidate(errors);

        if (!errors.isEmpty()) {
            StringBuilder buffer = new StringBuilder();
            for (ValidationErrors.Item error : errors.getAll()) {
                buffer.append(error.description).append("<br/>");
            }
            showNotification(
                    MessageProvider.getMessage(AppConfig.getMessagesPack(), "validationFail.caption"),
                    buffer.toString(),
                    NotificationType.HUMANIZED
            );
            return false;
        }
        return true;
    }

    public static class Editor extends DesktopWindow implements Window.Editor {

        public Editor() {
            super();
            addAction(new AbstractShortcutAction("commitAndCloseAction",
                    new ShortcutAction.KeyCombination(ShortcutAction.Key.ENTER, ShortcutAction.Modifier.CTRL)) {
                @Override
                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                    commitAndClose();
                }
            });
        }

        @Override
        protected WindowDelegate createDelegate() {
            return new EditorWindowDelegate(this);
        }

        public Entity getItem() {
            return ((EditorWindowDelegate) delegate).getItem();
        }

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

        public void setParentDs(Datasource parentDs) {
            ((EditorWindowDelegate) delegate).setParentDs(parentDs);
        }

        protected Datasource getDatasource() {
            return delegate.getDatasource();
        }

        public boolean commit() {
            return commit(true);
        }

        public boolean commit(boolean validate) {
            if (validate && !getWrapper().validateAll())
                return false;

            return ((EditorWindowDelegate) delegate).commit(false);
        }

        public void commitAndClose() {
            if (!getWrapper().validateAll())
                return;

            if (((EditorWindowDelegate) delegate).commit(true))
                close(COMMIT_ACTION_ID);
        }

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
            super();
            addAction(new AbstractShortcutAction(LOOKUP_SELECTED_ACTION_ID,
                    new ShortcutAction.KeyCombination(ShortcutAction.Key.ENTER, ShortcutAction.Modifier.CTRL)) {
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
                        new AbstractAction(LOOKUP_ENTER_PRESSED_ACTION_ID) {
                            @Override
                            public void actionPerform(Component component) {
                                fireSelectAction();
                            }
                        });
                table.setItemClickAction(
                        new AbstractAction(LOOKUP_ITEM_CLICK_ACTION_ID) {
                            @Override
                            public void actionPerform(Component component) {
                                fireSelectAction();
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
            if (selectListener != null)
                selectListener.actionPerformed(null);
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

            JButton selectBtn = new JButton(MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Select"));
            selectBtn.setIcon(App.getInstance().getResources().getIcon("icons/ok.png"));
            selectBtn.addActionListener(selectListener);
            DesktopComponentsHelper.adjustSize(selectBtn);
            buttonsPanel.add(selectBtn);

            JButton cancelBtn = new JButton(MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Cancel"));
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
                if (validator != null && !validator.validate())
                    return;

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
