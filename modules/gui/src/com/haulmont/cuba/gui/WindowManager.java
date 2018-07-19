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
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger.LifeCycle;
import com.haulmont.cuba.gui.logging.UserActionsLogger;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import com.haulmont.cuba.gui.xml.data.DsContextLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.gui.xml.layout.ScreenXmlLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.ComponentLoaderContext;
import com.haulmont.cuba.security.entity.PermissionType;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * GenericUI class intended for creation and opening application screens.
 */
public abstract class WindowManager {
    private org.slf4j.Logger userActionsLog = LoggerFactory.getLogger(UserActionsLogger.class);

    /**
     * Constant that is passed to {@link Window#close(String)} and {@link Window#close(String, boolean)} methods when
     * the screen is closed by window manager. Propagated to {@link Window.CloseListener#windowClosed}.
     */
    public static final String MAIN_MENU_ACTION_ID = "mainMenu";

    /**
     * How to open a screen: {@link #NEW_TAB}, {@link #THIS_TAB}, {@link #DIALOG}, {@link #NEW_WINDOW}.
     * <br>
     * You can set additional parameters for window using builder style methods:
     * <pre>
     * openEditor("sales$Customer.edit", customer,
     *            OpenType.DIALOG.width(300).resizable(false), params);
     * </pre>
     */
    public final static class OpenType {
        /**
         * Open a screen in new tab of the main window.
         * <br> In Web Client with {@code AppWindow.Mode.SINGLE} the new screen replaces current screen.
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

        private Float width;
        private SizeUnit widthUnit;
        private Float height;
        private SizeUnit heightUnit;

        private Integer positionX;
        private Integer positionY;

        private Boolean resizable;
        private Boolean closeable;
        private Boolean modal;
        private Boolean closeOnClickOutside;
        private Boolean maximized;

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

        public SizeUnit getHeightUnit() {
            return heightUnit;
        }

        public OpenType setHeightUnit(SizeUnit heightUnit) {
            OpenType instance = getMutableInstance();
            instance.heightUnit = heightUnit;
            return instance;
        }

        public Float getHeight() {
            return height;
        }

        /**
         * @deprecated Use {@link #height(Float)} instead.
         */
        @Deprecated
        public OpenType height(Integer height) {
            return height(height.floatValue());
        }

        /**
         * @deprecated Use {@link #setHeight(Float)} instead.
         */
        @Deprecated
        public OpenType setHeight(Integer height) {
            return setHeight(height.floatValue());
        }

        public OpenType height(Float height) {
            OpenType instance = getMutableInstance();

            instance.height = height;
            return instance;
        }

        public OpenType setHeight(Float height) {
            OpenType instance = getMutableInstance();

            instance.height = height;
            return instance;
        }

        public OpenType height(String height) {
            return setHeight(height);
        }

        public OpenType setHeight(String height) {
            OpenType instance = getMutableInstance();

            SizeWithUnit size = SizeWithUnit.parseStringSize(height);

            instance.height = size.getSize();
            instance.heightUnit = size.getUnit();
            return instance;
        }

        public OpenType heightAuto() {
            OpenType instance = getMutableInstance();

            instance.height = -1.0f;
            instance.heightUnit = SizeUnit.PIXELS;
            return instance;
        }

        public SizeUnit getWidthUnit() {
            return widthUnit;
        }

        public OpenType setWidthUnit(SizeUnit widthUnit) {
            OpenType instance = getMutableInstance();
            instance.widthUnit = widthUnit;
            return instance;
        }

        public Float getWidth() {
            return width;
        }

        /**
         * @deprecated Use {@link #width(Float)} instead.
         */
        @Deprecated
        public OpenType width(Integer width) {
            return width(width.floatValue());
        }

        /**
         * @deprecated Use {@link #setWidth(Float)} instead.
         */
        @Deprecated
        public OpenType setWidth(Integer width) {
            return setWidth(width.floatValue());
        }

        public OpenType width(Float width) {
            OpenType instance = getMutableInstance();

            instance.width = width;
            return instance;
        }

        public OpenType setWidth(Float width) {
            OpenType instance = getMutableInstance();

            instance.width = width;
            return instance;
        }

        public OpenType width(String width) {
            return setWidth(width);
        }

        public OpenType setWidth(String width) {
            OpenType instance = getMutableInstance();

            SizeWithUnit size = SizeWithUnit.parseStringSize(width);

            instance.width = size.getSize();
            instance.widthUnit = size.getUnit();
            return instance;
        }

        public OpenType widthAuto() {
            OpenType instance = getMutableInstance();

            instance.width = -1.0f;
            instance.widthUnit = SizeUnit.PIXELS;
            return instance;
        }

        public Integer getPositionX() {
            return positionX;
        }

        public OpenType setPositionX(Integer positionX) {
            OpenType instance = getMutableInstance();

            instance.positionX = positionX;
            return instance;
        }

        public OpenType positionX(Integer positionX) {
            OpenType instance = getMutableInstance();

            instance.positionX = positionX;
            return instance;
        }

        public Integer getPositionY() {
            return positionY;
        }

        public OpenType setPositionY(Integer positionY) {
            OpenType instance = getMutableInstance();

            instance.positionY = positionY;
            return instance;
        }

        public OpenType positionY(Integer positionY) {
            OpenType instance = getMutableInstance();

            instance.positionY = positionY;
            return instance;
        }

        public OpenType center() {
            OpenType instance = getMutableInstance();
            instance.positionX = null;
            instance.positionY = null;
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

        /**
         * @return true if a window can be closed by click on outside window area
         */
        public Boolean getCloseOnClickOutside() {
            return closeOnClickOutside;
        }

        /**
         * Set closeOnClickOutside to true if a window should be closed by click on outside window area.
         * It works when a window has a modal mode.
         */
        public OpenType closeOnClickOutside(Boolean closeOnClickOutside) {
            OpenType instance = getMutableInstance();

            instance.closeOnClickOutside = closeOnClickOutside;
            return instance;
        }

        /**
         * Set closeOnClickOutside to true if a window should be closed by click on outside window area.
         * It works when a window has a modal mode.
         */
        public OpenType setCloseOnClickOutside(Boolean closeOnClickOutside) {
            OpenType instance = getMutableInstance();

            instance.closeOnClickOutside = closeOnClickOutside;
            return instance;
        }

        /**
         * @return true if a window is maximized across the screen.
         */
        public Boolean getMaximized() {
            return maximized;
        }

        /**
         * Set maximized to true if a window should be maximized across the screen.
         */
        public OpenType maximized(Boolean maximized) {
            OpenType instance = getMutableInstance();

            instance.maximized = maximized;
            return instance;
        }

        /**
         * Set maximized to true if a window should be maximized across the screen.
         */
        public OpenType setMaximized(Boolean maximized) {
            OpenType instance = getMutableInstance();

            instance.maximized = maximized;
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
            openType.setHeightUnit(heightUnit);
            openType.setWidth(width);
            openType.setWidthUnit(widthUnit);
            openType.setCloseOnClickOutside(closeOnClickOutside);
            openType.setMaximized(maximized);
            openType.setPositionX(positionX);
            openType.setPositionY(positionY);

            return openType;
        }
    }

    public enum OpenMode {
        /**
         * Open a screen in new tab of the main window.
         * <br> In Web Client with {@code AppWindow.Mode.SINGLE} the new screen replaces current screen.
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

    protected Configuration configuration = AppBeans.get(Configuration.NAME);

    protected BackgroundWorker backgroundWorker = AppBeans.get(BackgroundWorker.NAME);

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);

    protected ScreenXmlLoader screenXmlLoader = AppBeans.get(ScreenXmlLoader.NAME);

    protected ScreenViewsLoader screenViewsLoader = AppBeans.get(ScreenViewsLoader.NAME);

    protected List<WindowCloseListener> listeners = new ArrayList<>();

    protected WindowManager() {
        defaultDataSupplier = new GenericDataSupplier();
    }

    public abstract Collection<Window> getOpenWindows();

    /**
     * Select tab with window in main tabsheet.
     */
    public abstract void selectWindowTab(Window window);

    /**
     * @deprecated Please use {@link Window#setCaption(String)} ()} and {@link Window#setDescription(String)} ()} methods.
     */
    @Deprecated
    public abstract void setWindowCaption(Window window, String caption, String description);

    protected Integer getHash(WindowInfo windowInfo, Map<String, Object> params) {
        return windowInfo.hashCode() + params.hashCode();
    }

    protected Window createWindow(WindowInfo windowInfo, OpenType openType, Map<String, Object> params,
                                  LayoutLoaderConfig layoutConfig, boolean topLevel) {
        if (!topLevel) {
            checkPermission(windowInfo);
        }

        StopWatch loadDescriptorWatch = new Slf4JStopWatch(windowInfo.getId() + "#" +
                LifeCycle.LOAD,
                LoggerFactory.getLogger(UIPerformanceLogger.class));

        Element element = screenXmlLoader.load(windowInfo.getTemplate(), windowInfo.getId(), params);

        preloadMainScreenClass(element);//try to load main screen class to resolve dynamic compilation dependencies issues

        ComponentLoaderContext componentLoaderContext = new ComponentLoaderContext(params);
        componentLoaderContext.setFullFrameId(windowInfo.getId());
        componentLoaderContext.setCurrentFrameId(windowInfo.getId());

        ComponentLoader windowLoader = createLayout(windowInfo, element, componentLoaderContext, layoutConfig);
        Window clientSpecificWindow = (Window) windowLoader.getResultComponent();
        Window windowWrapper = wrapByCustomClass(clientSpecificWindow, element);

        screenViewsLoader.deployViews(element);

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

        if (configuration.getConfig(GlobalConfig.class).getPerformanceTestMode()) {
            initDebugIds(clientSpecificWindow);
        }

        StopWatch uiPermissionsWatch = new Slf4JStopWatch(windowInfo.getId() + "#" +
                LifeCycle.UI_PERMISSIONS,
                LoggerFactory.getLogger(UIPerformanceLogger.class));

        // apply ui permissions
        WindowCreationHelper.applyUiPermissions(clientSpecificWindow);

        uiPermissionsWatch.stop();

        return windowWrapper;
    }

    protected void preloadMainScreenClass(Element element) {
        String screenClass = element.attributeValue("class");
        if (!StringUtils.isBlank(screenClass)) {
            scripting.loadClass(screenClass);
        }
    }

    protected void initDebugIds(Frame frame) {
    }

    protected void checkPermission(WindowInfo windowInfo) {
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
        DsContext dsContext = new DsContextLoader(dataSupplier).loadDatasources(element.element("dsContext"), null, null);
        return dsContext;
    }

    protected Window createWindow(WindowInfo windowInfo, Map<String, Object> params) {
        Window window;
        try {
            window = (Window) windowInfo.getScreenClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate window class", e);
        }

        window.setId(windowInfo.getId());
        window.setWindowManager(this);

        init(window, params);

        StopWatch uiPermissionsWatch = new Slf4JStopWatch(windowInfo.getId() + "#" +
                LifeCycle.UI_PERMISSIONS,
                LoggerFactory.getLogger(UIPerformanceLogger.class));

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
                Callable callable = (Callable) obj;
                Window window = (Window) callable.call();
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
            window = createWindow(windowInfo, openType, params, LayoutLoaderConfig.getWindowLoaders(), false);
            String caption = loadCaption(window, params);
            String description = loadDescription(window, params);
            if (isOpenAsNewTab(openType)) {
                putToWindowMap(window, hashCode);
            }
            showWindow(window, caption, description, openType, windowInfo.getMultipleOpen());
            userActionsLog.trace("Window {} was opened", windowInfo.getId());
            return window;
        } else {
            Class screenClass = windowInfo.getScreenClass();
            if (screenClass != null) {
                window = createWindowByScreenClass(windowInfo, params);
                if (isOpenAsNewTab(openType)) {
                    putToWindowMap(window, hashCode);
                }
                userActionsLog.trace("Window {} was opened", windowInfo.getId());
                return window;
            } else
                return null;
        }
    }

    protected boolean isOpenAsNewTab(OpenType openType) {
        // todo check only if there are no opened tabbed windows
        if (getOpenWindows().isEmpty()
                && openType.getOpenMode() == OpenMode.THIS_TAB) {
            return true;
        }

        return openType.getOpenMode() == OpenMode.NEW_TAB;
    }

    public Window openWindow(WindowInfo windowInfo, OpenType openType) {
        return openWindow(windowInfo, openType, Collections.emptyMap());
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
        return openEditor(windowInfo, item, openType, Collections.emptyMap(), parentDs);
    }

    public Window.Editor openEditor(WindowInfo windowInfo, Entity item, OpenType openType) {
        return openEditor(windowInfo, item, openType, Collections.emptyMap());
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

        if (openType.getOpenMode() != OpenMode.DIALOG) {
            Window existingWindow = getWindow(hashCode);
            if (existingWindow != null) {
                params = createParametersMap(windowInfo, params);
                String caption = loadCaption(existingWindow, params);
                String description = loadDescription(existingWindow, params);

                showWindow(existingWindow, caption, description, openType, false);
                return (Window.Editor) existingWindow;
            }
        }

        params = createParametersMap(windowInfo, params);
        WindowParams.ITEM.set(params, item instanceof Datasource ? ((Datasource) item).getItem() : item);

        Window window;
        if (template != null) {
            window = createWindow(windowInfo, openType, params, LayoutLoaderConfig.getEditorLoaders(), false);
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

        StopWatch setItemWatch = new Slf4JStopWatch(windowInfo.getId() + "#" +
                LifeCycle.SET_ITEM,
                LoggerFactory.getLogger(UIPerformanceLogger.class));

        ((Window.Editor) window).setItem(item);

        setItemWatch.stop();

        String caption = loadCaption(window, params);
        String description = loadDescription(window, params);
        showWindow(window, caption, description, openType, false);

        userActionsLog.trace("Editor {} was opened", windowInfo.getId());

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
            window = createWindow(windowInfo, openType, params, LayoutLoaderConfig.getLookupLoaders(), false);

            ((Window.Lookup) window).initLookupLayout();

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

        userActionsLog.trace("Lookup {} was opened", windowInfo.getId());

        return (Window.Lookup) window;
    }

    public Window.Lookup openLookup(WindowInfo windowInfo, Window.Lookup.Handler handler, OpenType openType) {
        return openLookup(windowInfo, handler, openType, Collections.emptyMap());
    }

    public Frame openFrame(Frame parentFrame, Component parent, WindowInfo windowInfo) {
        return openFrame(parentFrame, parent, windowInfo, Collections.emptyMap());
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

        StopWatch loadDescriptorWatch = new Slf4JStopWatch(windowInfo.getId() + "#" +
                LifeCycle.LOAD,
                LoggerFactory.getLogger(UIPerformanceLogger.class));

        Frame component;
        String frameId = id != null ? id : windowInfo.getId();

        Pair<ComponentLoader, Element> loaderElementPair = loader.createFrameComponent(src, frameId, context.getParams());
        component = (Frame) loaderElementPair.getFirst().getResultComponent();

        if (parent != null) {
            showFrame(parent, component);
        } else {
            component.setFrame(parentFrame);
        }

        loaderElementPair.getFirst().loadComponent();

        if (component.getMessagesPack() == null) {
            component.setMessagesPack(parentFrame.getMessagesPack());
        }

        context.executeInjectTasks();
        context.setFrame(component);
        context.executePostWrapTasks();

        // init of frame
        context.executeInitTasks();

        context.executePostInitTasks();

        loadDescriptorWatch.stop();

        initDebugIds(component);

        userActionsLog.trace("Frame {} was opened", windowInfo.getId());

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

    protected void fireListeners(Window window, boolean anyOpenWindowExist) {
        for (WindowCloseListener wcl : listeners) {
            wcl.onWindowClose(window, anyOpenWindowExist);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected abstract void showWindow(Window window, String caption, OpenType openType, boolean multipleOpen);

    protected abstract void showWindow(Window window, String caption, String description, OpenType openType, boolean multipleOpen);

    protected abstract void showFrame(Component parent, Frame frame);

    protected OpenType overrideOpenTypeParams(OpenType mutableOpenType, DialogOptions dialogOptions) {
        if (BooleanUtils.isTrue(dialogOptions.getForceDialog())) {
            mutableOpenType.setOpenMode(OpenMode.DIALOG);
        }

        if (dialogOptions.getHeight() != null) {
            mutableOpenType.setHeight(dialogOptions.getHeight());
        }

        if (dialogOptions.getHeightUnit() != null) {
            mutableOpenType.setHeightUnit(dialogOptions.getHeightUnit());
        }

        if (dialogOptions.getWidth() != null) {
            mutableOpenType.setWidth(dialogOptions.getWidth());
        }

        if (dialogOptions.getWidthUnit() != null) {
            mutableOpenType.setWidthUnit(dialogOptions.getWidthUnit());
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

        if(dialogOptions.getCloseOnClickOutside() != null){
            mutableOpenType.setCloseOnClickOutside(dialogOptions.getCloseOnClickOutside());
        }

        if (dialogOptions.getMaximized() != null) {
            mutableOpenType.setMaximized(dialogOptions.getMaximized());
        }

        if (dialogOptions.getPositionX() != null) {
            mutableOpenType.setPositionX(dialogOptions.getPositionX());
        }

        if (dialogOptions.getPositionY() != null) {
            mutableOpenType.setPositionY(dialogOptions.getPositionY());
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
            AbstractWindow abstractWindow = (AbstractWindow) window;

            if (abstractWindow.isAttributeAccessControlEnabled()) {
                AttributeAccessSupport attributeAccessSupport = AppBeans.get(AttributeAccessSupport.NAME);
                attributeAccessSupport.applyAttributeAccess(abstractWindow, false);
            }

            StopWatch readyStopWatch = new Slf4JStopWatch(window.getId() + "#" +
                    LifeCycle.READY,
                    LoggerFactory.getLogger(UIPerformanceLogger.class));

            abstractWindow.ready();

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
                StopWatch companionStopWatch = new Slf4JStopWatch(wrappingWindow.getId() + "#" +
                        LifeCycle.COMPANION,
                        LoggerFactory.getLogger(UIPerformanceLogger.class));

                initCompanion(companionsElem, (AbstractWindow) wrappingWindow);

                companionStopWatch.stop();
            }
        }

        StopWatch injectStopWatch = new Slf4JStopWatch(wrappingWindow.getId() + "#" +
                LifeCycle.INJECTION,
                LoggerFactory.getLogger(UIPerformanceLogger.class));

        ControllerDependencyInjector dependencyInjector =
                AppBeans.getPrototype(ControllerDependencyInjector.NAME, wrappingWindow, params);
        dependencyInjector.inject();

        injectStopWatch.stop();

        context.executeInjectTasks();
        context.executePostWrapTasks();

        init(wrappingWindow, params);

        context.executeInitTasks();
    }

    protected void init(Window window, Map<String, Object> params) {
        if (window instanceof AbstractWindow) {
            StopWatch initStopWatch = new Slf4JStopWatch(window.getId() +
                    "#" + LifeCycle.INIT,
                    LoggerFactory.getLogger(UIPerformanceLogger.class));

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

    /**
     * Opens default screen. Implemented only for the web module.
     * <p>
     * Default screen can be defined with the {@code cuba.web.defaultScreenId} application property.
     */
    public abstract void openDefaultScreen();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Show notification with {@link Frame.NotificationType#HUMANIZED}. <br>
     * Supports line breaks ({@code \n}).
     *
     * @param caption text
     */
    public abstract void showNotification(String caption);

    /**
     * Show notification. <br>
     * Supports line breaks ({@code \n}).
     *
     * @param caption text
     * @param type    defines how to display the notification.
     *                Don't forget to escape data from the database in case of {@code *_HTML} types!
     */
    public abstract void showNotification(String caption, Frame.NotificationType type);

    /**
     * Show notification with caption description. <br>
     * Supports line breaks ({@code \n}).
     *
     * @param caption     caption
     * @param description text
     * @param type        defines how to display the notification.
     *                    Don't forget to escape data from the database in case of {@code *_HTML} types!
     */
    public abstract void showNotification(String caption, String description, Frame.NotificationType type);

    /**
     * Show message dialog with title and message. <br>
     * Supports line breaks ({@code \n}) for non HTML messageType.
     *
     * @param title       dialog title
     * @param message     text
     * @param messageType defines how to display the dialog.
     *                    Don't forget to escape data from the database in case of {@code *_HTML} types!
     */
    public abstract void showMessageDialog(String title, String message, Frame.MessageType messageType);

    /**
     * Show options dialog with title and message. <br>
     * Supports line breaks ({@code \n}) for non HTML messageType.
     *
     * @param title       dialog title
     * @param message     text
     * @param messageType defines how to display the dialog.
     *                    Don't forget to escape data from the database in case of {@code *_HTML} types!
     * @param actions     available actions
     */
    public abstract void showOptionDialog(String title, String message, Frame.MessageType messageType, Action[] actions);

    /**
     * Shows exception dialog with default caption, message and displays stacktrace of given throwable.
     *
     * @param throwable throwable
     */
    public abstract void showExceptionDialog(Throwable throwable);

    /**
     * Shows exception dialog with given caption, message and displays stacktrace of given throwable.
     *
     * @param throwable throwable
     * @param caption   dialog caption
     * @param message   dialog message
     */
    public abstract void showExceptionDialog(Throwable throwable, @Nullable String caption, @Nullable String message);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Open a web page in browser.
     * @param url       URL of the page
     * @param params    optional parameters.
     * <br>The following parameters are recognized by Web client:
     * <ul>
     * <li>{@code target} - String value used as the target name in a
     * window.open call in the client. This means that special values such as
     * "_blank", "_self", "_top", "_parent" have special meaning. If not specified, "_blank" is used.</li>
     * <li> {@code width} - Integer value specifying the width of the browser window in pixels</li>
     * <li> {@code height} - Integer value specifying the height of the browser window in pixels</li>
     * <li> {@code border} - String value specifying the border style of the window of the browser window.
     * Possible values are "DEFAULT", "MINIMAL", "NONE".</li>
     * </ul>
     * Desktop client doesn't support any parameters and just ignores them.
     */
    public abstract void showWebPage(String url, @Nullable Map<String, Object> params);
}