/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.data.impl.GenericDataSupplier;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import com.haulmont.cuba.gui.xml.XmlInheritanceProcessor;
import com.haulmont.cuba.gui.xml.data.DsContextLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.gui.xml.layout.loaders.ComponentLoaderContext;
import com.haulmont.cuba.security.entity.PermissionType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * GenericUI class intended for creation and opening application screens.
 *
 * @author abramov
 * @version $Id$
 */
public abstract class WindowManager {

    /**
     * Constant that is passed to {@link Window#close(String)} and {@link Window#close(String, boolean)} methods when
     * the screen is closed by window manager. Propagated to {@link Window.CloseListener#windowClosed}.
     */
    public static final String MAIN_MENU_ACTION_ID = "mainMenu";

    /**
     * How to open a screen: {@link #NEW_TAB}, {@link #THIS_TAB}, {@link #DIALOG}, {@link #NEW_WINDOW}
     */
    public enum OpenType {
        /**
         * Open a screen in new tab of the main window.
         * <p/> In Web Client with <code>AppWindow.Mode.SINGLE</code> the new screen replaces current screen.
         */
        NEW_TAB,
        /**
         * Open a screen on top of the current tab screens stack.
         */
        THIS_TAB,
        /**
         * Open a screen as modal dialog.
         */
        DIALOG,
        /**
         * In Desktop Client open a screen in new main window, in Web Client the same as new {@link #NEW_TAB}
         */
        NEW_WINDOW
    }

    public interface WindowCloseListener {
        void onWindowClose(Window window, boolean anyOpenWindowExist);
    }

    protected DataSupplier defaultDataSupplier;

    protected Messages messages = AppBeans.get(Messages.NAME);

    protected Scripting scripting = AppBeans.get(Scripting.NAME);

    protected Resources resources = AppBeans.get(Resources.NAME);

    protected Security security = AppBeans.get(Security.NAME);

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);

    private DialogParams dialogParams;

    protected List<WindowCloseListener> listeners = new ArrayList<>();

    protected WindowManager() {
        dialogParams = createDialogParams();
        defaultDataSupplier = new GenericDataSupplier();
    }

    public abstract Collection<Window> getOpenWindows();

    /**
     * Select tab with window in main tabsheet.
     */
    public abstract void selectWindowTab(Window window);

    public abstract void setWindowCaption(Window window, String caption, String description);

    protected Integer getHash(WindowInfo windowInfo, Map<String, Object> params) {
        return windowInfo.hashCode() + params.hashCode();
    }

    protected Window createWindow(WindowInfo windowInfo, OpenType openType, Map<String, Object> params,
                                  LayoutLoaderConfig layoutConfig) {
        checkPermission(windowInfo);

        StopWatch loadDescriptorWatch = new Log4JStopWatch(windowInfo.getId() + "#" +
                UIPerformanceLogger.LifeCycle.LOAD_DESCRIPTOR,
                Logger.getLogger(UIPerformanceLogger.class));

        String templatePath = windowInfo.getTemplate();

        InputStream stream = resources.getResourceAsStream(templatePath);
        if (stream == null) {
            throw new DevelopmentException("Template is not found", "Path", templatePath);
        }

        StopWatch xmlLoadWatch = new Log4JStopWatch(windowInfo.getId() + "#" +
                UIPerformanceLogger.LifeCycle.XML,
                Logger.getLogger(UIPerformanceLogger.class));
        Document document = null;
        try {
            document = LayoutLoader.parseDescriptor(stream, params);
        } finally {
            IOUtils.closeQuietly(stream);
        }
        XmlInheritanceProcessor processor = new XmlInheritanceProcessor(document, params);
        Element element = processor.getResultRoot();

        xmlLoadWatch.stop();

        preloadMainScreenClass(element);//try to load main screen class to resolve dynamic compilation dependencies issues

        WindowCreationHelper.deployViews(element);

        final DsContext dsContext = loadDsContext(element);
        final ComponentLoaderContext componentLoaderContext = new ComponentLoaderContext(dsContext, params);
        componentLoaderContext.setFullFrameId(windowInfo.getId());
        componentLoaderContext.setCurrentIFrameId(windowInfo.getId());

        final Window window = loadLayout(windowInfo.getTemplate(), element, componentLoaderContext, layoutConfig);

        window.setId(windowInfo.getId());

        initDatasources(window, dsContext, params);

        WindowContext windowContext = new WindowContextImpl(window, openType, params);
        window.setContext(windowContext);
        dsContext.setFrameContext(windowContext);

        window.setWindowManager(this);

        loadDescriptorWatch.stop();

        final Window windowWrapper = wrapByCustomClass(window, element, params);
        componentLoaderContext.setFrame(windowWrapper);
        componentLoaderContext.executePostInitTasks();

        Configuration configuration = AppBeans.get(Configuration.NAME);
        if (configuration.getConfig(GlobalConfig.class).getTestMode()) {
            initDebugIds(window);
        }

        StopWatch uiPermissionsWatch = new Log4JStopWatch(windowInfo.getId() + "#" +
                UIPerformanceLogger.LifeCycle.UI_PERMISSIONS,
                Logger.getLogger(UIPerformanceLogger.class));

        // apply ui permissions
        WindowCreationHelper.applyUiPermissions(window);

        uiPermissionsWatch.stop();

        return windowWrapper;
    }

    private void preloadMainScreenClass(Element element) {
        final String screenClass = element.attributeValue("class");
        if (!StringUtils.isBlank(screenClass)) {
            scripting.loadClass(screenClass);
        }
    }

    protected void initDebugIds(final IFrame frame) {
    }

    private void checkPermission(WindowInfo windowInfo) {
        boolean permitted = security.isScreenPermitted(windowInfo.getId());
        if (!permitted)
            throw new AccessDeniedException(PermissionType.SCREEN, windowInfo.getId());
    }

    protected void initDatasources(final Window window, DsContext dsContext, Map<String, Object> params) {
        window.setDsContext(dsContext);

        for (Datasource ds : dsContext.getAll()) {
            if (Datasource.State.NOT_INITIALIZED.equals(ds.getState()) && ds instanceof DatasourceImplementation) {
                ((DatasourceImplementation) ds).initialized();
            }
        }
    }

    protected Window loadLayout(String descriptorPath, Element rootElement, ComponentLoader.Context context, LayoutLoaderConfig layoutConfig) {
        final LayoutLoader layoutLoader = new LayoutLoader(context, AppConfig.getFactory(), layoutConfig);
        layoutLoader.setLocale(getLocale());
        if (!StringUtils.isEmpty(descriptorPath)) {
            if (descriptorPath.contains("/")) {
                descriptorPath = StringUtils.substring(descriptorPath, 0, descriptorPath.lastIndexOf("/"));
            }

            String path = descriptorPath.replaceAll("/", ".");
            int start = path.startsWith(".") ? 1 : 0;
            path = path.substring(start);

            layoutLoader.setMessagesPack(path);
        }

        final Window window = layoutLoader.loadComponent(rootElement, null);
        return window;
    }

    protected DsContext loadDsContext(Element element) {
        DataSupplier dataSupplier;

        String dataSupplierClass = element.attributeValue("dataSupplier");
        if (StringUtils.isEmpty(dataSupplierClass)) {
            dataSupplier = defaultDataSupplier;
        } else {
            Class<Object> aClass = ReflectionHelper.getClass(dataSupplierClass);
            try {
                dataSupplier = (DataSupplier) aClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        DsContext dsContext = new DsContextLoader(dataSupplier).loadDatasources(element.element("dsContext"), null);
        return dsContext;
    }

    protected Window createWindow(WindowInfo windowInfo, Map params) {
        final Window window;
        try {
            window = (Window) windowInfo.getScreenClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        window.setId(windowInfo.getId());
        window.setWindowManager(this);

        init(window, params);

        StopWatch uiPermissionsWatch = new Log4JStopWatch(windowInfo.getId() + "#" +
                UIPerformanceLogger.LifeCycle.UI_PERMISSIONS,
                Logger.getLogger(UIPerformanceLogger.class));

        // apply ui permissions
        WindowCreationHelper.applyUiPermissions(window);

        uiPermissionsWatch.stop();

        return window;
    }

    protected Window createWindowByScreenClass(WindowInfo windowInfo, Map<String, Object> params) {
        Class screenClass = windowInfo.getScreenClass();

        Class[] paramTypes = ReflectionHelper.getParamTypes(params);
        Constructor constructor = null;
        try {
            constructor = screenClass.getConstructor(paramTypes);
        } catch (NoSuchMethodException e) {
            //
        }

        Object obj;
        try {
            if (constructor == null) {
                obj = screenClass.newInstance();
            } else {
                obj = constructor.newInstance(params);
            }
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if (obj instanceof Callable) {
            try {
                Window window = ((Callable<Window>) obj).call();
                return window;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (obj instanceof Runnable) {
            ((Runnable) obj).run();
            return null;
        } else
            throw new IllegalStateException("Screen class must be an instance of Callable<Window> or Runnable");
    }

    public boolean windowExist(WindowInfo windowInfo, Map<String, Object> params) {
        return (getWindow(getHash(windowInfo, params)) != null);
    }

    public Window openWindow(WindowInfo windowInfo, WindowManager.OpenType openType, Map<String, Object> params) {
        if (params == null) {
            params = Collections.emptyMap();
        }

        checkCanOpenWindow(windowInfo, openType, params);
        Integer hashCode = getHash(windowInfo, params);
        params = createParametersMap(windowInfo, params);
        String template = windowInfo.getTemplate();

        Window window;

        if (template != null) {
            window = createWindow(windowInfo, openType, params, LayoutLoaderConfig.getWindowLoaders());
            String caption = loadCaption(window, params);
            String description = loadDescription(window, params);
            if (openType == OpenType.NEW_TAB) {
                putToWindowMap(window, hashCode);
            }
            showWindow(window, caption, description, openType, windowInfo.getMultipleOpen());
            return window;
        } else {
            Class screenClass = windowInfo.getScreenClass();
            if (screenClass != null) {
                window = createWindowByScreenClass(windowInfo, params);
                if (openType == OpenType.NEW_TAB) {
                    putToWindowMap(window, hashCode);
                }
                return window;
            } else
                return null;
        }
    }

    public Window openWindow(WindowInfo windowInfo, OpenType openType) {
        return openWindow(windowInfo, openType, Collections.<String, Object>emptyMap());
    }

    protected abstract void putToWindowMap(Window window, Integer hashCode);

    protected abstract Window getWindow(Integer hashCode);

    protected abstract void checkCanOpenWindow(WindowInfo windowInfo, OpenType openType, Map<String, Object> params);

    protected String loadCaption(Window window, Map<String, Object> params) {
        String caption = window.getCaption();
        if (!StringUtils.isEmpty(caption)) {
            caption = TemplateHelper.processTemplate(caption, params);
        } else {
            caption = WindowParams.CAPTION.getString(params);
            if (StringUtils.isEmpty(caption)) {
                String msgPack = window.getMessagesPack();
                if (msgPack != null) {
                    caption = messages.getMessage(msgPack, "caption");
                    if (!"caption".equals(caption)) {
                        caption = TemplateHelper.processTemplate(caption, params);
                    }
                }
            } else {
                caption = TemplateHelper.processTemplate(caption, params);
            }
        }
        window.setCaption(caption);

        return caption;
    }

    protected String loadDescription(Window window, Map<String, Object> params) {
        String description = window.getDescription();
        if (!StringUtils.isEmpty(description)) {
            return TemplateHelper.processTemplate(description, params);
        } else {
            description = WindowParams.DESCRIPTION.getString(params);
            if (StringUtils.isEmpty(description)) {
                description = null;
            } else {
                description = TemplateHelper.processTemplate(description, params);
            }
        }
        window.setDescription(description);

        return description;
    }

    public Window.Editor openEditor(WindowInfo windowInfo, Entity item, OpenType openType,
                                           Datasource parentDs) {
        return openEditor(windowInfo, item, openType, Collections.<String, Object>emptyMap(), parentDs);
    }

    public Window.Editor openEditor(WindowInfo windowInfo, Entity item, OpenType openType) {
        return openEditor(windowInfo, item, openType, Collections.<String, Object>emptyMap());
    }

    public Window.Editor openEditor(WindowInfo windowInfo, Entity item, OpenType openType, Map<String, Object> params) {
        return openEditor(windowInfo, item, openType, params, null);
    }

    public Window.Editor openEditor(WindowInfo windowInfo, Entity item,
                                           OpenType openType, Map<String, Object> params,
                                           Datasource parentDs) {
        if (params == null) {
            params = Collections.emptyMap();
        }

        checkCanOpenWindow(windowInfo, openType, params);

        Integer hashCode = getHash(windowInfo, params);
        String template = windowInfo.getTemplate();
        Window window = getWindow(hashCode);
        if (window != null) {
            params = createParametersMap(windowInfo, params);
            String caption = loadCaption(window, params);
            String description = loadDescription(window, params);

            showWindow(window, caption, description, openType, false);
            return (Window.Editor) window;
        }

        params = createParametersMap(windowInfo, params);
        WindowParams.ITEM.set(params, item instanceof Datasource ? ((Datasource) item).getItem() : item);

        if (template != null) {
            window = createWindow(windowInfo, openType, params, LayoutLoaderConfig.getEditorLoaders());
        } else {
            Class windowClass = windowInfo.getScreenClass();
            if (windowClass != null) {
                window = createWindow(windowInfo, params);
                if (!(window instanceof Window.Editor)) {
                    throw new IllegalStateException(
                            String.format("Class %s does't implement Window.Editor interface", windowClass));
                }
            } else {
                throw new IllegalStateException("Invalid WindowInfo: " + windowInfo);
            }
        }
        ((Window.Editor) window).setParentDs(parentDs);

        StopWatch setItemWatch = new Log4JStopWatch(windowInfo.getId() + "#" +
                UIPerformanceLogger.LifeCycle.SET_ITEM,
                Logger.getLogger(UIPerformanceLogger.class));

        ((Window.Editor) window).setItem(item);

        setItemWatch.stop();

        final String caption = loadCaption(window, params);
        final String description = loadDescription(window, params);
        showWindow(window, caption, description, openType, false);

        return (Window.Editor) window;
    }

    public Window.Lookup openLookup(WindowInfo windowInfo, Window.Lookup.Handler handler,
                                           OpenType openType, Map<String, Object> params) {
        if (params == null) {
            params = Collections.emptyMap();
        }

        checkCanOpenWindow(windowInfo, openType, params);

        params = createParametersMap(windowInfo, params);

        String template = windowInfo.getTemplate();
        Window window;

        if (template != null) {
            window = createWindow(windowInfo, openType, params, LayoutLoaderConfig.getLookupLoaders());

            final Element element = ((Component.HasXmlDescriptor) window).getXmlDescriptor();
            final String lookupComponent = element.attributeValue("lookupComponent");
            if (!StringUtils.isEmpty(lookupComponent)) {
                final Component component = window.getComponent(lookupComponent);
                ((Window.Lookup) window).setLookupComponent(component);
            }
        } else {
            Class windowClass = windowInfo.getScreenClass();
            if (windowClass != null) {
                window = createWindow(windowInfo, params);
                if (!(window instanceof Window.Lookup)) {
                    throw new IllegalStateException(
                            String.format("Class %s does't implement Window.Lookup interface", windowClass));
                }
            } else {
                throw new IllegalStateException("Invalid WindowInfo: " + windowInfo);
            }
        }

        ((Window.Lookup) window).setLookupHandler(handler);

        final String caption = loadCaption(window, params);
        final String description = loadDescription(window, params);

        showWindow(window, caption, description, openType, false);

        return (Window.Lookup) window;
    }

    public Window.Lookup openLookup(WindowInfo windowInfo, Window.Lookup.Handler handler, OpenType openType) {
        return openLookup(windowInfo, handler, openType, Collections.<String, Object>emptyMap());
    }

    public IFrame openFrame(IFrame parentFrame, Component parent, WindowInfo windowInfo) {
        return openFrame(parentFrame, parent, windowInfo, Collections.<String, Object>emptyMap());
    }

    public IFrame openFrame(IFrame parentFrame, Component parent, WindowInfo windowInfo,
                                          Map<String, Object> params) {
        if (params == null) {
            params = Collections.emptyMap();
        }

        //Parameters can be useful later
        params = createParametersMap(windowInfo, params);

        String src = windowInfo.getTemplate();

        ComponentLoaderContext context = new ComponentLoaderContext(parentFrame.getDsContext(), params);
        context.setFullFrameId(windowInfo.getId());

        final LayoutLoader loader =
                new LayoutLoader(context, AppConfig.getFactory(), LayoutLoaderConfig.getFrameLoaders());
        loader.setLocale(getLocale());
        loader.setMessagesPack(parentFrame.getMessagesPack());

        InputStream stream = resources.getResourceAsStream(src);
        if (stream == null) {
            throw new GuiDevelopmentException("Template is not found", context.getFullFrameId(), "Path", src);
        }

        StopWatch loadDescriptorWatch = new Log4JStopWatch(windowInfo.getId() + "#" +
                UIPerformanceLogger.LifeCycle.LOAD_DESCRIPTOR,
                Logger.getLogger(UIPerformanceLogger.class));

        final IFrame component;
        try {
            context.setCurrentIFrameId(windowInfo.getId());
            component = (IFrame) loader.loadComponent(stream, parent, context.getParams());
        } finally {
            IOUtils.closeQuietly(stream);
        }
        if (component.getMessagesPack() == null) {
            component.setMessagesPack(parentFrame.getMessagesPack());
        }

        component.setId(windowInfo.getId());
        component.setFrame(parentFrame);
        context.setFrame(component);
        context.executePostInitTasks();

        loadDescriptorWatch.stop();

        if (parent != null)
            showFrame(parent, component);

        initDebugIds(component);

        return component;
    }

    protected Map<String, Object> createParametersMap(WindowInfo windowInfo, Map<String, Object> params) {
        final Map<String, Object> map = new HashMap<>(params.size());

        final Element element = windowInfo.getDescriptor();
        if (element != null) {
            Element paramsElement = element.element("params") != null ? element.element("params") : element;
            if (paramsElement != null) {
                @SuppressWarnings({"unchecked"})
                final List<Element> paramElements = paramsElement.elements("param");
                for (Element paramElement : paramElements) {
                    final String name = paramElement.attributeValue("name");
                    final String value = paramElement.attributeValue("value");
                    if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                        Boolean booleanValue = Boolean.valueOf(value);
                        map.put(name, booleanValue);
                    } else {
                        map.put(name, value);
                    }
                }
            }
        }

        map.putAll(params);

        return map;
    }

    protected DialogParams createDialogParams() {
        return new DialogParams();
    }

    public DialogParams getDialogParams() {
        return dialogParams;
    }

    protected void fireListeners(Window window, boolean anyOpenWindowExist) {
        for (WindowCloseListener wcl : listeners) {
            wcl.onWindowClose(window, anyOpenWindowExist);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected abstract void showWindow(Window window, String caption, OpenType openType, boolean multipleOpen);

    protected abstract void showWindow(Window window, String caption, String description, OpenType openType, boolean multipleOpen);

    protected abstract void showFrame(Component parent, IFrame frame);

    protected Settings getSettingsImpl(String id) {
        return new SettingsImpl(id);
    }

    protected void afterShowWindow(Window window) {
        if (!WindowParams.DISABLE_APPLY_SETTINGS.getBool(window.getContext())) {
            window.applySettings(getSettingsImpl(window.getId()));
        }
        if (!WindowParams.DISABLE_RESUME_SUSPENDED.getBool(window.getContext())) {
            ((DsContextImplementation) window.getDsContext()).resumeSuspended();
        }

        if (window instanceof AbstractWindow) {
            StopWatch readyStopWatch = new Log4JStopWatch(window.getId() + "#" +
                    UIPerformanceLogger.LifeCycle.READY,
                    Logger.getLogger(UIPerformanceLogger.class));

            ((AbstractWindow) window).ready();

            readyStopWatch.stop();
        }
    }

    public abstract void close(Window window);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected Locale getLocale() {
        return userSessionSource.getUserSession().getLocale();
    }

    protected Window wrapByCustomClass(Window window, Element element, Map<String, Object> params) {
        final String screenClass = element.attributeValue("class");
        if (!StringUtils.isBlank(screenClass)) {
            Class<Window> aClass = scripting.loadClass(screenClass);
            if (aClass == null) {
                String msg = messages.getMainMessage("unableToLoadControllerClass");
                throw new GuiDevelopmentException(msg, window.getId());
            }
            Window wrappingWindow = ((WrappedWindow) window).wrapBy(aClass);

            if (wrappingWindow instanceof AbstractWindow) {
                Element companionsElem = element.element("companions");
                if (companionsElem != null) {
                    StopWatch companionStopWatch = new Log4JStopWatch(window.getId() + "#" +
                            UIPerformanceLogger.LifeCycle.COMPANION,
                            Logger.getLogger(UIPerformanceLogger.class));

                    initCompanion(companionsElem, (AbstractWindow) wrappingWindow);

                    companionStopWatch.stop();
                }
            }

            StopWatch injectStopWatch = new Log4JStopWatch(window.getId() + "#" +
                    UIPerformanceLogger.LifeCycle.INJECTION,
                    Logger.getLogger(UIPerformanceLogger.class));

            ControllerDependencyInjector dependencyInjector = new ControllerDependencyInjector(wrappingWindow, params);
            dependencyInjector.inject();

            injectStopWatch.stop();

            init(wrappingWindow, params);

            return wrappingWindow;
        } else {
            throw new GuiDevelopmentException("'class' attribute is not defined in XML descriptor", window.getId());
        }
    }

    protected void init(Window window, Map<String, Object> params) {
        if (window instanceof AbstractWindow) {
            StopWatch initStopWatch = new Log4JStopWatch(window.getId() +
                    "#" + UIPerformanceLogger.LifeCycle.INIT,
                    Logger.getLogger(UIPerformanceLogger.class));

            ((AbstractWindow) window).init(params);

            initStopWatch.stop();
        }
    }

    protected void initCompanion(Element companionsElem, AbstractWindow window) {
        Element element = companionsElem.element(AppConfig.getClientType().toString().toLowerCase());
        if (element != null) {
            String className = element.attributeValue("class");
            if (!StringUtils.isBlank(className)) {
                Class aClass = scripting.loadClass(className);
                if (aClass == null)
                    throw new IllegalStateException("Class " + className + " is not found");
                Object companion;
                try {
                    companion = aClass.newInstance();
                    window.setCompanion(companion);

                    CompanionDependencyInjector cdi = new CompanionDependencyInjector(window, companion);
                    cdi.inject();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Show notification. <br/>
     * Supports line breaks (<code>\n</code>).
     *
     * @param caption text
     * @param type    defines how to display the notification.
     *                Don't forget to escape data from the database in case of <code>*_HTML</code> types!
     */
    public abstract void showNotification(String caption, IFrame.NotificationType type);

    /**
     * Show notification with caption description. <br/>
     * Supports line breaks (<code>\n</code>).
     *
     * @param caption     caption
     * @param description text
     * @param type        defines how to display the notification.
     *                    Don't forget to escape data from the database in case of <code>*_HTML</code> types!
     */
    public abstract void showNotification(String caption, String description, IFrame.NotificationType type);

    /**
     * Show message dialog with title and message. <br/>
     * Supports line breaks (<code>\n</code>) for non HTML messageType.
     *
     * @param title       dialog title
     * @param message     text
     * @param messageType defines how to display the dialog.
     *                    Don't forget to escape data from the database in case of <code>*_HTML</code> types!
     */
    public abstract void showMessageDialog(String title, String message, IFrame.MessageType messageType);

    /**
     * Show options dialog with title and message. <br/>
     * Supports line breaks (<code>\n</code>) for non HTML messageType.
     *
     * @param title       dialog title
     * @param message     text
     * @param messageType defines how to display the dialog.
     *                    Don't forget to escape data from the database in case of <code>*_HTML</code> types!
     * @param actions     available actions
     */
    public abstract void showOptionDialog(String title, String message, IFrame.MessageType messageType, Action[] actions);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Open a web page in browser.
     * @param url       URL of the page
     * @param params    optional parameters.
     * <p/>The following parameters are recognized by Web client:
     * <ul>
     * <li/> <code>target</code> - String value used as the target name in a
     * window.open call in the client. This means that special values such as
     * "_blank", "_self", "_top", "_parent" have special meaning. If not specified, "_blank" is used.
     * <li/> <code>width</code> - Integer value specifying the width of the browser window in pixels
     * <li/> <code>height</code> - Integer value specifying the height of the browser window in pixels
     * <li/> <code>border</code> - String value specifying the border style of the window of the browser window.
     * Possible values are "DEFAULT", "MINIMAL", "NONE".
     * </ul>
     * Desktop client doesn't support any parameters and just ignores them.
     */
    public abstract void showWebPage(String url, @Nullable Map<String, Object> params);
}