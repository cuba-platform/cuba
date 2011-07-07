/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.data.ComponentSize;
import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.WindowContext;
import com.haulmont.cuba.gui.settings.Settings;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.text.StrBuilder;
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
public class DesktopWindow implements Window, Component.Wrapper, Component.HasXmlDescriptor, WrappedWindow, DesktopContainer
{
    private static final long serialVersionUID = 1026363207247384464L;

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

    protected List<com.haulmont.cuba.gui.components.Action> actionsOrder = new LinkedList<com.haulmont.cuba.gui.components.Action>();

    protected WindowDelegate delegate;

    private List<CloseListener> listeners = new ArrayList<CloseListener>();

    protected boolean forceClose;
    protected Runnable doAfterClose;

    private Log log = LogFactory.getLog(DesktopWindow.class);

    public DesktopWindow() {
        initLayout();
        delegate = createDelegate();
    }

    protected void initLayout() {
        panel = new JPanel();
        layoutAdapter = BoxLayoutAdapter.create(panel);
        layoutAdapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.Y);
        layoutAdapter.setMargin(true);
    }

    protected WindowDelegate createDelegate() {
        return new WindowDelegate(this, App.getInstance().getWindowManager());
    }

    public Element getXmlDescriptor() {
        return xmlDescriptor;
    }

    public void setXmlDescriptor(Element element) {
        xmlDescriptor = element;
    }

    public void addListener(CloseListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeListener(CloseListener listener) {
        listeners.remove(listener);
    }

    public void applySettings(Settings settings) {
        delegate.applySettings(settings);
    }

    public void saveSettings() {
        delegate.saveSettings();
    }

    @Override
    public void setFocusComponent(String componentId) {
        getComponent(componentId).requestFocus();
    }

    public Settings getSettings() {
        return delegate.getSettings();
    }

    public boolean close(final String actionId) {
        WindowManager windowManager = App.getInstance().getWindowManager();

        if (!forceClose && getDsContext() != null && getDsContext().isModified()) {
            windowManager.showOptionDialog(
                    MessageProvider.getMessage(AppConfig.getMessagesPack(), "closeUnsaved.caption"),
                    MessageProvider.getMessage(AppConfig.getMessagesPack(), "closeUnsaved"),
                    MessageType.WARNING,
                    new Action[]{
                            new AbstractAction(MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Yes")) {
                                public void actionPerform(Component component) {
                                    forceClose = true;
                                    close(actionId);
                                }

                                @Override
                                public String getIcon() {
                                    return "icons/ok.png";
                                }
                            },
                            new AbstractAction(MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.No")) {
                                public void actionPerform(Component component) {
                                    doAfterClose = null;
                                }

                                @Override
                                public String getIcon() {
                                    return "icons/cancel.png";
                                }
                            }
                    }
            );
            return false;
        }

        saveSettings();

        windowManager.close(this);
        boolean res = onClose(actionId);
        if (res && doAfterClose != null) {
            doAfterClose.run();
        }
        return res;
    }

    public boolean close(String actionId, boolean force) {
        forceClose = force;
        return close(actionId);
    }

    public void closeAndRun(String actionId, Runnable runnable) {
        this.doAfterClose = runnable;
        close(actionId);
    }

    public void addTimer(Timer timer) {
    }

    public Timer getTimer(String id) {
        return null;
    }

    public void addAction(Action action) {
        actionsOrder.add(action);
    }

    public void removeAction(Action action) {
        actionsOrder.remove(action);
    }

    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actionsOrder);
    }

    public Action getAction(String id) {
        for (com.haulmont.cuba.gui.components.Action action : getActions()) {
            if (ObjectUtils.equals(action.getId(), id)) {
                return action;
            }
        }
        return null;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WindowContext getContext() {
        return context;
    }

    public void setContext(WindowContext ctx) {
        context = ctx;
    }

    public DsContext getDsContext() {
        return dsContext;
    }

    public void setDsContext(DsContext dsContext) {
        this.dsContext = dsContext;
    }

    public String getMessagesPack() {
        return messagePack;
    }

    public void setMessagesPack(String name) {
        messagePack = name;
    }

    public String getMessage(String key) {
        if (messagePack == null)
            throw new IllegalStateException("MessagePack is not set");
        return MessageProvider.getMessage(messagePack, key);
    }

    public void registerComponent(Component component) {
        if (component.getId() != null)
            allComponents.put(component.getId(), component);
    }

    public DialogParams getDialogParams() {
        return App.getInstance().getWindowManager().getDialogParams();
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.<T>openWindow(windowAlias, openType, params);
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        return delegate.<T>openWindow(windowAlias, openType);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        return delegate.<T>openEditor(windowAlias, item, openType, params, parentDs);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.<T>openEditor(windowAlias, item, openType, params);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        return delegate.<T>openEditor(windowAlias, item, openType, parentDs);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        return delegate.<T>openEditor(windowAlias, item, openType);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        return delegate.<T>openLookup(windowAlias, handler, openType, params);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        return delegate.<T>openLookup(windowAlias, handler, openType);
    }

    public <T extends IFrame> T openFrame(Component parent, String windowAlias) {
        return delegate.<T>openFrame(parent, windowAlias);
    }

    public <T extends IFrame> T openFrame(Component parent, String windowAlias, Map<String, Object> params) {
        return delegate.<T>openFrame(parent, windowAlias, params);
    }

    public void showMessageDialog(String title, String message, MessageType messageType) {
        App.getInstance().getWindowManager().showMessageDialog(title, message, messageType);
    }

    public void showOptionDialog(String title, String message, MessageType messageType, Action[] actions) {
        App.getInstance().getWindowManager().showOptionDialog(title, message, messageType, actions);
    }

    public void showOptionDialog(String title, String message, MessageType messageType, java.util.List<Action> actions) {
        App.getInstance().getWindowManager().showOptionDialog(title, message, messageType, actions.toArray(new Action[actions.size()]));
    }

    public void showNotification(String caption, NotificationType type) {
        App.getInstance().getWindowManager().showNotification(caption, type);
    }

    public void showNotification(String caption, String description, NotificationType type) {
        App.getInstance().getWindowManager().showNotification(caption, description, type);
    }

    public <A extends IFrame> A getFrame() {
        return (A) this;
    }

    public void setFrame(IFrame frame) {
        throw new UnsupportedOperationException();
    }

    public void expand(Component component, String height, String width) {
        JComponent composition = DesktopComponentsHelper.getComposition(component);
        layoutAdapter.expand(composition, height, width);
    }

    public void expand(Component component) {
        expand(component, "", "");
    }

    public void add(Component component) {
        JComponent composition = DesktopComponentsHelper.getComposition(component);
        getContainer().add(composition, layoutAdapter.getConstraints(component));
        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
            registerComponent(component);
        }
        ownComponents.add(component);

        if (component instanceof DesktopComponent) {
            ((DesktopComponent) component).setContainer(this);
        }
    }

    public void remove(Component component) {
        getContainer().remove(DesktopComponentsHelper.getComposition(component));
        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
        ownComponents.remove(component);

        if (component instanceof DesktopComponent) {
            ((DesktopComponent) component).setContainer(null);
        }
    }

    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

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

    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    public void expandLayout(boolean expandLayout) {
    }

    public <T> T getComponent() {
        return (T) panel;
    }

    public JComponent getComposition() {
        return panel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDebugId() {
        return null;
    }

    public void setDebugId(String id) {
    }

    public boolean isEnabled() {
        return panel.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        panel.setEnabled(enabled);
    }

    public boolean isVisible() {
        return true;
    }

    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException();
    }

    public void requestFocus() {
    }

    public float getHeight() {
        return 0;
    }

    public int getHeightUnits() {
        return 0;
    }

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

    public float getWidth() {
        return 0;
    }

    public int getWidthUnits() {
        return 0;
    }

    public void setWidth(String width) {
    }

    public Alignment getAlignment() {
        return null;
    }

    public void setAlignment(Alignment alignment) {
    }

    public String getStyleName() {
        return null;
    }

    public void setStyleName(String name) {
    }

    public void setMargin(boolean enable) {
        layoutAdapter.setMargin(enable);
    }

    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        layoutAdapter.setMargin(topEnable, rightEnable, bottomEnable, leftEnable);
    }

    public void setSpacing(boolean enabled) {
        layoutAdapter.setSpacing(enabled);
    }

    public Window wrapBy(Class<Window> wrapperClass) {
        return delegate.wrapBy(wrapperClass);
    }

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
        if (!ownComponents.contains(child)) {
            throw new UnsupportedOperationException("It's not a child");
        }
        JComponent composition = DesktopComponentsHelper.getComposition(child);
        layoutAdapter.updateConstraints(composition, layoutAdapter.getConstraints(child));
    }

    public static class Editor extends DesktopWindow implements Window.Editor {

        private static final long serialVersionUID = -7042930104147784581L;

        private Log log = LogFactory.getLog(DesktopWindow.Editor.class);

        @Override
        protected WindowDelegate createDelegate() {
            return new EditorWindowDelegate(this, App.getInstance().getWindowManager());
        }

        public Entity getItem() {
            return ((EditorWindowDelegate) delegate).getItem();
        }

        public void setItem(Entity item) {
            ((EditorWindowDelegate) delegate).setItem(item);
        }

        public boolean isValid() {
            Collection<Component> components = ComponentsHelper.getComponents(this);
            for (Component component : components) {
                if (component instanceof Field) {
                    if (!((Field) component).isValid())
                        return false;
                }
            }
            return true;
        }

        public void validate() throws ValidationException {
            Collection<Component> components = DesktopComponentsHelper.getComponents(this);
            for (Component component : components) {
                if (component instanceof Field) {
                    ((Field) component).validate();
                }
            }
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
            if (validate && !((Window.Editor)getWrapper()).validateOnCommit())
                return false;

            ((EditorWindowDelegate) delegate).commit();
            return true;
        }

        public boolean validateOnCommit() {
            List<String> problems = new ArrayList<String>();

            Collection<Component> components = DesktopComponentsHelper.getComponents(this);
            for (Component component : components) {
                if (component instanceof Field) {
                    try {
                        ((Field) component).validate();
                    } catch (ValidationException e) {
                        log.warn("Validation failed", e);
                        problems.add(e.getMessage());
                    }
                }
            }
            if (!problems.isEmpty()) {
                String text = new StrBuilder().appendWithSeparators(problems, "<br/>").toString();
                showNotification(
                        MessageProvider.getMessage(AppConfig.getMessagesPack(), "validationFail.caption"),
                        text,
                        NotificationType.HUMANIZED
                );
                return false;
            }
            return true;
        }

        public void commitAndClose() {
            if (commit()) {
                close(COMMIT_ACTION_ID);
            }
        }

        public boolean isLocked() {
            return ((EditorWindowDelegate) delegate).isLocked();
        }

    }

    public static class Lookup extends DesktopWindow implements Window.Lookup {

        private Component lookupComponent;
        private Handler handler;
        private Validator validator;

        private JPanel container;

        @Override
        public Component getLookupComponent() {
            return lookupComponent;
        }

        @Override
        public void setLookupComponent(Component lookupComponent) {
            this.lookupComponent = lookupComponent;
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

            JButton selectBtn = new JButton(MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Select"));
            selectBtn.setIcon(App.getInstance().getResources().getIcon("icons/ok.png"));
            selectBtn.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (validator != null && !validator.validate())
                                return;

                            Collection selected;
                            if (lookupComponent instanceof com.haulmont.cuba.gui.components.Table ) {
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
                            handler.handleLookup(selected);
                        }
                    }
            );
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
    }
}
