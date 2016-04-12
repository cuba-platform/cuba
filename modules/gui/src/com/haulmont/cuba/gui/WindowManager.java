/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui;

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.bali.util.Preconditions;
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
import com.haulmont.cuba.gui.xml.layout.ScreenXmlCache;
import com.haulmont.cuba.gui.xml.layout.loaders.ComponentLoaderContext;
import com.haulmont.cuba.security.entity.PermissionType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * GenericUI class intended for creation and opening application screens.
 */
public abstract class WindowManager {

    /**
     * Constant that is passed to {@link Window#close(String)} and {@link Window#close(String, boolean)} methods when
     * the screen is closed by window manager. Propagated to {@link Window.CloseListener#windowClosed}.
     */
    public static final String MAIN_MENU_ACTION_ID = "mainMenu";

    /**
     * How to open a screen: {@link #NEW_TAB}, {@link #THIS_TAB}, {@link #DIALOG}, {@link #NEW_WINDOW}.
     * <p/>
     * You can set additional parameters for window using builder style methods:
     * <pre>
     * openEditor("sales$Customer.edit", customer,
     *            OpenType.DIALOG.width(300).resizable(false), params);
     * </pre>
     */
    public final static class OpenType {
        /**
         * Open a screen in new tab of the main window.
         * <p/> In Web Client with <code>AppWindow.Mode.SINGLE</code> the new screen replaces current screen.
         */
        public static final OpenType NEW_TAB = new OpenType(OpenMode.NEW_TAB, false);

        /**
         * Open a screen on top of the current tab screens stack.
         */
        public static final OpenType THIS_TAB = new OpenType(OpenMode.THIS_TAB, false);

        /**
         * Open a screen as modal dialog.
         */
        public static final OpenType DIALOG = new OpenType(OpenMode.DIALOG, false);

        /**
         * In Desktop Client open a screen in new main window, in Web Client the same as new {@link #NEW_TAB}
         */
        public static final OpenType NEW_WINDOW = new OpenType(OpenMode.NEW_WINDOW, false);

        private OpenMode openMode;
        private boolean mutable = true;

        private Integer width;
        private Integer height;
        private Boolean resizable;
        private Boolean closeable;
        private Boolean modal;

        public OpenType(OpenMode openMode) {
            this.openMode = openMode;
        }

        private OpenType(OpenMode openMode, boolean mutable) {
            this.openMode = openMode;
            this.mutable = mutable;
        }

        public OpenMode getOpenMode() {
            return openMode;
        }

        public OpenType setOpenMode(OpenMode openMode) {
            OpenType instance = getMutableInstance();

            instance.openMode = openMode;
            return instance;
        }

        public Integer getHeight() {
            return height;
        }

        public OpenType height(Integer heightPx) {
            OpenType instance = getMutableInstance();

            instance.height = heightPx;
            return instance;
        }

        public OpenType setHeight(Integer heightPx) {
            OpenType instance = getMutableInstance();

            instance.height = heightPx;
            return instance;
        }

        public OpenType heightAuto() {
            OpenType instance = getMutableInstance();

            instance.height = -1;
            return instance;
        }

        public Integer getWidth() {
            return width;
        }

        public OpenType width(Integer widthPx) {
            OpenType instance = getMutableInstance();

            instance.width = widthPx;
            return instance;
        }

        public OpenType setWidth(Integer widthPx) {
            OpenType instance = getMutableInstance();

            instance.width = widthPx;
            return instance;
        }

        public OpenType widthAuto() {
            OpenType instance = getMutableInstance();

            instance.width = -1;
            return instance;
        }

        public Boolean getResizable() {
            return resizable;
        }

        public OpenType setResizable(Boolean resizable) {
            OpenType instance = getMutableInstance();

            instance.resizable = resizable;
            return instance;
        }

        public OpenType resizable(Boolean resizable) {
            OpenType instance = getMutableInstance();

            instance.resizable = resizable;
            return instance;
        }

        public Boolean getCloseable() {
            return closeable;
        }

        public OpenType closeable(Boolean closeable) {
            OpenType instance = getMutableInstance();

            instance.closeable = closeable;
            return instance;
        }

        public OpenType setCloseable(Boolean closeable) {
            OpenType instance = getMutableInstance();

            instance.closeable = closeable;
            return instance;
        }

        public Boolean getModal() {
            return modal;
        }

        public OpenType modal(Boolean modal) {
            OpenType instance = getMutableInstance();

            instance.modal = modal;
            return instance;
        }

        public OpenType setModal(Boolean modal) {
            OpenType instance = getMutableInstance();

            instance.modal = modal;
            return instance;
        }

        private OpenType getMutableInstance() {
            if (!mutable) {
                return copy();
            }

            return this;
        }

        public static OpenType valueOf(String openTypeString) {
            Preconditions.checkNotNullArgument(openTypeString, "openTypeString should not be null");

            switch (openTypeString) {
                case "NEW_TAB":
                    return NEW_TAB;

                case "THIS_TAB":
                    return THIS_TAB;

                case "DIALOG":
                    return DIALOG;

                case "NEW_WINDOW":
                    return NEW_WINDOW;

                default:
                    throw new IllegalArgumentException("Unable to parse OpenType");
            }
        }

        public OpenType copy() {
            OpenType openType = new OpenType(openMode);

            openType.setModal(modal);
            openType.setResizable(resizable);
            openType.setCloseable(closeable);
            openType.setHeight(height);
            openType.setWidth(width);

            return openType;
        }
    }

    public enum OpenMode {
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

    protected ScreenXmlCache screenXmlCache = AppBeans.get(ScreenXmlCache.class);

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
                UIPerformanceLogger.LifeCycle.LOAD,
                Logger.getLogger(UIPerformanceLogger.class));

        String templatePath = windowInfo.getTemplate();

        InputStream stream = resources.getResourceAsStream(templatePath);
        if (stream == null) {
            throw new DevelopmentException("Template is not found", "Path", templatePath);
        }

        String template;
        try {
            template = IOUtils.toString(stream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read screen template");
        } finally {
            IOUtils.closeQuietly(stream);
        }

        StopWatch xmlLoadWatch = new Log4JStopWatch(windowInfo.getId() + "#" +
                UIPerformanceLogger.LifeCycle.XML,
                Logger.getLogger(UIPerformanceLogger.class));

        Document document = screenXmlCache.get(template);
        if (document == null) {
            Document originalDocument = LayoutLoader.parseDescriptor(template);

            XmlInheritanceProcessor processor = new XmlInheritanceProcessor(originalDocument, params);
            Element resultRoot = processor.getResultRoot();

            document = resultRoot.getDocument();

            screenXmlCache.put(template, document);
        }

        Element element = document.getRootElement();

        xmlLoadWatch.stop();

        preloadMainScreenClass(element);//try to load main screen class to resolve dynamic compilation dependencies issues

        ComponentLoaderContext componentLoaderContext = new ComponentLoaderContext(params);
        componentLoaderContext.setFullFrameId(windowInfo.getId());
        componentLoaderContext.setCurrentFrameId(windowInfo.getId());

        ComponentLoader windowLoader = createLayout(windowInfo, element, componentLoaderContext, layoutConfig);
        Window clientSpecificWindow = (Window) windowLoader.getResultComponent();
        Window windowWrapper = wrapByCustomClass(clientSpecificWindow, element);

        WindowCreationHelper.deployViews(element);

        DsContext dsContext = loadDsContext(element);
        initDatasources(clientSpecificWindow, dsContext, params);

        componentLoaderContext.setDsContext(dsContext);

        WindowContext windowContext = new WindowContextImpl(clientSpecificWindow, openType, params);
        clientSpecificWindow.setContext(windowContext);
        dsContext.setFrameContext(windowContext);

        //noinspection unchecked
        windowLoader.loadComponent();

        clientSpecificWindow.setWindowManager(this);

        loadDescriptorWatch.stop();

        initWrapperFrame(windowWrapper, componentLoaderContext, element, params);

        componentLoaderContext.setFrame(windowWrapper);
        componentLoaderContext.executePostInitTasks();

        Configuration configuration = AppBeans.get(Configuration.NAME);
        if (configuration.getConfig(GlobalConfig.class).getTestMode()) {
            initDebugIds(clientSpecificWindow);
        }

        StopWatch uiPermissionsWatch = new Log4JStopWatch(windowInfo.getId() + "#" +
                UIPerformanceLogger.LifeCycle.UI_PERMISSIONS,
                Logger.getLogger(UIPerformanceLogger.class));

        // apply ui permissions
        WindowCreationHelper.applyUiPermissions(clientSpecificWindow);

        uiPermissionsWatch.stop();

        return windowWrapper;
    }

    private void preloadMainScreenClass(Element element) {
        String screenClass = element.attributeValue("class");
        if (!StringUtils.isBlank(screenClass)) {
            scripting.loadClass(screenClass);
        }
    }

    protected void initDebugIds(Frame frame) {
    }

    private void checkPermission(WindowInfo windowInfo) {
        boolean permitted = security.isScreenPermitted(windowInfo.getId());
        if (!permitted) {
            throw new AccessDeniedException(PermissionType.SCREEN, windowInfo.getId());
        }
    }

    protected void initDatasources(Window window, DsContext dsContext, Map<String, Object> params) {
        window.setDsContext(dsContext);

        for (Datasource ds : dsContext.getAll()) {
            if (Datasource.State.NOT_INITIALIZED.equals(ds.getState()) && ds instanceof DatasourceImplementation) {
                ((DatasourceImplementation) ds).initialized();
            }
        }
    }

    protected ComponentLoader createLayout(WindowInfo windowInfo, Element rootElement,
                                           ComponentLoader.Context context, LayoutLoaderConfig layoutConfig) {
        String descriptorPath = windowInfo.getTemplate();

        LayoutLoader layoutLoader = new LayoutLoader(context, AppConfig.getFactory(), layoutConfig);
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
        //noinspection UnnecessaryLocalVariable
        ComponentLoader windowLoader = layoutLoader.createWindow(rootElement, windowInfo.getId());
        return windowLoader;
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
                throw new RuntimeException("Unable to create data supplier for screen", e);
            }
        }

        //noinspection UnnecessaryLocalVariable
        DsContext dsContext = new DsContextLoader(dataSupplier).loadDatasources(element.element("dsContext"), null);
        return dsContext;
    }

    protected Window createWindow(WindowInfo windowInfo, Map params) {
        Window window;
        try {
            window = (Window) windowInfo.getScreenClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate window class", e);
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
            throw new RuntimeException("Unable to instantiate window class", e);
        }

        if (obj instanceof Callable) {
            try {
                Window window = ((Callable<Window>) obj).call();
                return window;
            } catch (Exception e) {
                throw new RuntimeException("Unable to instantiate window class", e);
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

    public Window openWindow(WindowInfo windowInfo, OpenType openType, Map<String, Object> params) {
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
            if (openType.getOpenMode() == OpenMode.NEW_TAB) {
                putToWindowMap(window, hashCode);
            }
            showWindow(window, caption, description, openType, windowInfo.getMultipleOpen());
            return window;
        } else {
            Class screenClass = windowInfo.getScreenClass();
            if (screenClass != null) {
                window = createWindowByScreenClass(windowInfo, params);
                if (openType.getOpenMode() == OpenMode.NEW_TAB) {
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

        String caption = loadCaption(window, params);
        String description = loadDescription(window, params);
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

            Element element = ((Component.HasXmlDescriptor) window).getXmlDescriptor();
            String lookupComponent = element.attributeValue("lookupComponent");
            if (!StringUtils.isEmpty(lookupComponent)) {
                Component component = window.getComponent(lookupComponent);
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

        String caption = loadCaption(window, params);
        String description = loadDescription(window, params);

        showWindow(window, caption, description, openType, false);

        return (Window.Lookup) window;
    }

    public Window.Lookup openLookup(WindowInfo windowInfo, Window.Lookup.Handler handler, OpenType openType) {
        return openLookup(windowInfo, handler, openType, Collections.<String, Object>emptyMap());
    }

    public Frame openFrame(Frame parentFrame, Component parent, WindowInfo windowInfo) {
        return openFrame(parentFrame, parent, windowInfo, Collections.<String, Object>emptyMap());
    }

    public Frame openFrame(Frame parentFrame, Component parent, WindowInfo windowInfo, Map<String, Object> params) {
        return openFrame(parentFrame, parent, null, windowInfo, params);
    }

    public Frame openFrame(Frame parentFrame, Component parent, @Nullable String id,
                           WindowInfo windowInfo, Map<String, Object> params) {
        if (params == null) {
            params = Collections.emptyMap();
        }

        // Parameters can be useful later
        params = createParametersMap(windowInfo, params);

        String src = windowInfo.getTemplate();

        ComponentLoaderContext context = new ComponentLoaderContext(params);
        context.setDsContext(parentFrame.getDsContext());
        context.setFullFrameId(windowInfo.getId());
        context.setCurrentFrameId(windowInfo.getId());

        LayoutLoader loader = new LayoutLoader(context, AppConfig.getFactory(), LayoutLoaderConfig.getFrameLoaders());
        loader.setLocale(getLocale());
        loader.setMessagesPack(parentFrame.getMessagesPack());

        InputStream stream = resources.getResourceAsStream(src);
        if (stream == null) {
            throw new GuiDevelopmentException("Template is not found", context.getFullFrameId(), "Path", src);
        }

        StopWatch loadDescriptorWatch = new Log4JStopWatch(windowInfo.getId() + "#" +
                UIPerformanceLogger.LifeCycle.LOAD,
                Logger.getLogger(UIPerformanceLogger.class));

        Frame component;
        String frameId = id != null ? id : windowInfo.getId();
        try {
            Pair<ComponentLoader, Element> loaderElementPair = loader.createFrameComponent(stream, frameId, context.getParams());
            component = (Frame) loaderElementPair.getFirst().getResultComponent();

            if (parent != null) {
                showFrame(parent, component);
            } else {
                component.setFrame(parentFrame);
            }

            loaderElementPair.getFirst().loadComponent();
        } finally {
            IOUtils.closeQuietly(stream);
        }

        if (component.getMessagesPack() == null) {
            component.setMessagesPack(parentFrame.getMessagesPack());
        }

        context.executeInjectTasks();
        context.setFrame(component);
        context.executePostInitTasks();

        loadDescriptorWatch.stop();

        initDebugIds(component);

        return component;
    }

    protected Map<String, Object> createParametersMap(WindowInfo windowInfo, Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>(params.size());

        Element element = windowInfo.getDescriptor();
        if (element != null) {
            Element paramsElement = element.element("params") != null ? element.element("params") : element;
            if (paramsElement != null) {
                @SuppressWarnings({"unchecked"})
                List<Element> paramElements = paramsElement.elements("param");
                for (Element paramElement : paramElements) {
                    String name = paramElement.attributeValue("name");
                    String value = paramElement.attributeValue("value");
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

    @Deprecated
    protected DialogParams createDialogParams() {
        return new DialogParams();
    }

    @Deprecated
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

    protected abstract void showFrame(Component parent, Frame frame);

    @Deprecated
    protected void copyDialogParamsToOpenType(OpenType mutableOpenType) {
        DialogParams dialogParams = getDialogParams();
        if (dialogParams.getCloseable() != null && mutableOpenType.getCloseable() == null) {
            mutableOpenType.closeable(dialogParams.getCloseable());
        }
        if (dialogParams.getModal() != null && mutableOpenType.getModal() == null) {
            mutableOpenType.setModal(dialogParams.getModal());
        }
        if (dialogParams.getResizable() != null && mutableOpenType.getResizable() == null) {
            mutableOpenType.setResizable(dialogParams.getResizable());
        }
        if (dialogParams.getWidth() != null && mutableOpenType.getWidth() == null) {
            mutableOpenType.setWidth(dialogParams.getWidth());
        }
        if (dialogParams.getHeight() != null && mutableOpenType.getHeight() == null) {
            mutableOpenType.setHeight(dialogParams.getHeight());
        }
    }

    protected OpenType overrideOpenTypeParams(OpenType mutableOpenType, DialogOptions dialogOptions) {
        if (BooleanUtils.isTrue(dialogOptions.getForceDialog())) {
            mutableOpenType.setOpenMode(OpenMode.DIALOG);
        }

        if (dialogOptions.getHeight() != null) {
            mutableOpenType.setHeight(dialogOptions.getHeight());
        }

        if (dialogOptions.getWidth() != null) {
            mutableOpenType.setWidth(dialogOptions.getWidth());
        }

        if (dialogOptions.getResizable() != null) {
            mutableOpenType.setResizable(dialogOptions.getResizable());
        }

        if (dialogOptions.getCloseable() != null) {
            mutableOpenType.setCloseable(dialogOptions.getCloseable());
        }

        if (dialogOptions.getModal() != null) {
            mutableOpenType.setModal(dialogOptions.getModal());
        }

        return mutableOpenType;
    }

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

    protected Window wrapByCustomClass(Frame window, Element element) {
        String screenClass = element.attributeValue("class");
        if (StringUtils.isBlank(screenClass)) {
            throw new GuiDevelopmentException("'class' attribute is not defined in XML descriptor", window.getId());
        }

        Class<?> aClass = scripting.loadClass(screenClass);
        if (aClass == null) {
            throw new GuiDevelopmentException("Unable to load controller class", window.getId());
        }
        //noinspection UnnecessaryLocalVariable
        Window wrappingWindow = ((WrappedWindow) window).wrapBy(aClass);
        return wrappingWindow;
    }

    protected void initWrapperFrame(Window wrappingWindow, ComponentLoaderContext context, Element element,
                                    Map<String, Object> params) {
        if (wrappingWindow instanceof AbstractWindow) {
            Element companionsElem = element.element("companions");
            if (companionsElem != null) {
                StopWatch companionStopWatch = new Log4JStopWatch(wrappingWindow.getId() + "#" +
                        UIPerformanceLogger.LifeCycle.COMPANION,
                        Logger.getLogger(UIPerformanceLogger.class));

                initCompanion(companionsElem, (AbstractWindow) wrappingWindow);

                companionStopWatch.stop();
            }
        }

        StopWatch injectStopWatch = new Log4JStopWatch(wrappingWindow.getId() + "#" +
                UIPerformanceLogger.LifeCycle.INJECTION,
                Logger.getLogger(UIPerformanceLogger.class));

        ControllerDependencyInjector dependencyInjector = new ControllerDependencyInjector(wrappingWindow, params);
        dependencyInjector.inject();

        injectStopWatch.stop();

        context.executeInjectTasks();

        init(wrappingWindow, params);
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
                Class aClass = scripting.loadClassNN(className);
                Object companion;
                try {
                    companion = aClass.newInstance();
                    window.setCompanion(companion);

                    CompanionDependencyInjector cdi = new CompanionDependencyInjector(window, companion);
                    cdi.inject();
                } catch (Exception e) {
                    throw new RuntimeException("Unable to init Companion", e);
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
    public abstract void showNotification(String caption, Frame.NotificationType type);

    /**
     * Show notification with caption description. <br/>
     * Supports line breaks (<code>\n</code>).
     *
     * @param caption     caption
     * @param description text
     * @param type        defines how to display the notification.
     *                    Don't forget to escape data from the database in case of <code>*_HTML</code> types!
     */
    public abstract void showNotification(String caption, String description, Frame.NotificationType type);

    /**
     * Show message dialog with title and message. <br/>
     * Supports line breaks (<code>\n</code>) for non HTML messageType.
     *
     * @param title       dialog title
     * @param message     text
     * @param messageType defines how to display the dialog.
     *                    Don't forget to escape data from the database in case of <code>*_HTML</code> types!
     */
    public abstract void showMessageDialog(String title, String message, Frame.MessageType messageType);

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
    public abstract void showOptionDialog(String title, String message, Frame.MessageType messageType, Action[] actions);

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