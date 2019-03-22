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

package com.haulmont.cuba.gui.sys;

import com.google.common.base.Strings;
import com.haulmont.cuba.client.ClientConfiguration;
import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.sys.ValuePathHelper;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.events.sys.UiEventListenerMethodAdapter;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.DataLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.sys.UiControllerReflectionInspector.AnnotatedMethod;
import com.haulmont.cuba.gui.sys.UiControllerReflectionInspector.InjectElement;
import com.haulmont.cuba.gui.sys.UiControllerReflectionInspector.ScreenIntrospectionData;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.reflect.Proxy.newProxyInstance;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

/**
 * Wires {@link Inject}, {@link Named}, {@link WindowParam} fields/setters and {@link EventListener} methods.
 */
@org.springframework.stereotype.Component(UiControllerDependencyInjector.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UiControllerDependencyInjector {

    private static final Logger log = LoggerFactory.getLogger(UiControllerDependencyInjector.class);

    public static final String NAME = "cuba_UiControllerDependencyInjector";

    protected FrameOwner frameOwner;
    protected ScreenOptions options;

    protected BeanLocator beanLocator;
    protected UiControllerReflectionInspector reflectionInspector;

    public UiControllerDependencyInjector(FrameOwner frameOwner, ScreenOptions options) {
        this.frameOwner = frameOwner;
        this.options = options;
    }

    @Inject
    public void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    @Inject
    public void setReflectionInspector(UiControllerReflectionInspector reflectionInspector) {
        this.reflectionInspector = reflectionInspector;
    }

    public void inject() {
        ScreenIntrospectionData screenIntrospectionData =
                reflectionInspector.getScreenIntrospectionData(frameOwner.getClass());

        injectValues(frameOwner, screenIntrospectionData);

        initSubscribeListeners(frameOwner, screenIntrospectionData);

        initInstallMethods(frameOwner, screenIntrospectionData);

        initUiEventListeners(frameOwner, screenIntrospectionData);
    }

    protected void initInstallMethods(FrameOwner frameOwner, ScreenIntrospectionData screenIntrospectionData) {
        List<AnnotatedMethod<Install>> installMethods = screenIntrospectionData.getInstallMethods();

        for (AnnotatedMethod<Install> annotatedMethod : installMethods) {
            Install annotation = annotatedMethod.getAnnotation();

            Frame frame = UiControllerUtils.getFrame(frameOwner);

            Object targetInstance = getInstallTargetInstance(frameOwner, annotation, frame);

            if (targetInstance == null) {
                if (annotation.required()) {
                    throw new DevelopmentException(
                            String.format("Unable to find @Install target for method %s in %s",
                                    annotatedMethod.getMethod(), frameOwner.getClass()));
                }

                log.trace("Skip @Install method {} of {} : it is not required and target not found",
                        annotatedMethod.getMethod().getName(), frameOwner.getClass());

                continue;
            }

            Class<?> instanceClass = targetInstance.getClass();
            Method installMethod = annotatedMethod.getMethod();

            MethodHandle targetSetterMethod = getInstallTargetSetterMethod(annotation, frame, instanceClass, installMethod);
            Class<?> targetParameterType = targetSetterMethod.type().parameterList().get(1);

            Object handler = null;
            if (targetInstance instanceof InstallTargetHandler) {
                handler = ((InstallTargetHandler) targetInstance).createInstallHandler(targetParameterType,
                        frameOwner, installMethod);
            }

            if (handler == null) {
                handler = createInstallHandler(frameOwner, installMethod, targetParameterType);
            }

            try {
                targetSetterMethod.invoke(targetInstance, handler);
            } catch (Error e) {
                throw e;
            } catch (Throwable e) {
                throw new RuntimeException("Unable to set declarative @Install handler for " + installMethod, e);
            }
        }
    }

    protected MethodHandle getInstallTargetSetterMethod(Install annotation, Frame frame, Class<?> instanceClass,
                                                        Method provideMethod) {
        String subjectProperty;
        if (Strings.isNullOrEmpty(annotation.subject()) && annotation.type() == Object.class) {
            InstallSubject installSubjectAnnotation = findMergedAnnotation(instanceClass, InstallSubject.class);
            if (installSubjectAnnotation != null) {
                subjectProperty = installSubjectAnnotation.value();
            } else {
                throw new DevelopmentException(
                        String.format("Unable to determine @Install subject of %s in %s", provideMethod, frame.getId())
                );
            }
        } else if (annotation.type() != Object.class) {
            subjectProperty = StringUtils.uncapitalize(annotation.type().getSimpleName());
        } else {
            subjectProperty = annotation.subject();
        }

        String subjectSetterName = "set" + StringUtils.capitalize(subjectProperty);
        // Check if addSubject is supported, e.g: addValidator(), addStyleProvider()
        String subjectAddName = "add" + StringUtils.capitalize(subjectProperty);

        MethodHandle targetSetterMethod = reflectionInspector.getInstallTargetMethod(instanceClass, subjectAddName);
        if (targetSetterMethod == null) {
            targetSetterMethod = reflectionInspector.getInstallTargetMethod(instanceClass, subjectSetterName);
        }

        if (targetSetterMethod == null) {
            throw new DevelopmentException(
                    String.format("Unable to find @Install target method %s in %s", subjectProperty, instanceClass)
            );
        }

        return targetSetterMethod;
    }

    @Nullable
    protected Object getInstallTargetInstance(FrameOwner frameOwner, Install annotation, Frame frame) {
        Object targetInstance;
        String target = UiDescriptorUtils.getInferredProvideId(annotation);
        if (Strings.isNullOrEmpty(target)) {

            switch (annotation.target()) {
                // if kept default value
                case COMPONENT:
                case CONTROLLER:
                    targetInstance = frameOwner;
                    break;

                case FRAME:
                    targetInstance = frame;
                    break;

                case PARENT_CONTROLLER:
                    if (frameOwner instanceof Screen) {
                        throw new DevelopmentException(
                                String.format("Screen %s cannot use @Install with target = PARENT_CONTROLLER",
                                        frame.getId())
                        );
                    }
                    targetInstance = ((ScreenFragment) frameOwner).getHostController();
                    break;

                case DATA_CONTEXT:
                    targetInstance = UiControllerUtils.getScreenData(frameOwner).getDataContext();
                    break;

                default:
                    throw new UnsupportedOperationException("Unsupported @Install target " + annotation.target());
            }
        } else if (annotation.target() == Target.DATA_LOADER) {
            targetInstance = UiControllerUtils.getScreenData(frameOwner).getLoader(target);
        } else {
            targetInstance = findInstallTarget(target, frame);
        }
        return targetInstance;
    }

    @Nullable
    protected Object findInstallTarget(String target, Frame frame) {
        String[] elements = ValuePathHelper.parse(target);
        if (elements.length == 1) {
            Object part = frame.getSubPart(target);
            if (part != null) {
                return part;
            }

            Component component = frame.getComponent(target);
            if (component != null) {
                return component;
            }
        } else if (elements.length > 1) {
            String id = elements[elements.length - 1];

            String[] subPath = ArrayUtils.subarray(elements, 0, elements.length - 1);
            Component component = frame.getComponent(ValuePathHelper.format(subPath));

            if (component != null) {
                if (component instanceof HasSubParts) {
                    Object part = ((HasSubParts) component).getSubPart(id);
                    if (part != null) {
                        return part;
                    }
                }

                if (component instanceof ComponentContainer) {
                    return ((ComponentContainer) component).getComponent(id);
                }
            }
        }

        return null;
    }

    protected Object createInstallHandler(FrameOwner frameOwner, Method method, Class<?> targetObjectType) {
        if (targetObjectType == Function.class) {
            return new InstalledFunction(frameOwner, method);
        } else if (targetObjectType == Consumer.class) {
            return new InstalledConsumer(frameOwner, method);
        } else if (targetObjectType == Supplier.class) {
            return new InstalledSupplier(frameOwner, method);
        } else if (targetObjectType == BiFunction.class) {
            return new InstalledBiFunction(frameOwner, method);
        } else {
            ClassLoader classLoader = getClass().getClassLoader();
            return newProxyInstance(classLoader, new Class[]{targetObjectType},
                    new InstalledProxyHandler(frameOwner, method)
            );
        }
    }

    protected void injectValues(@SuppressWarnings("unused") FrameOwner frameOwner,
                                ScreenIntrospectionData screenIntrospectionData) {
        List<InjectElement> injectElements = screenIntrospectionData.getInjectElements();

        for (InjectElement entry : injectElements) {
            doInjection(entry.getElement(), entry.getAnnotationClass());
        }
    }

    protected void initSubscribeListeners(FrameOwner frameOwner, ScreenIntrospectionData screenIntrospectionData) {
        Class<? extends FrameOwner> clazz = frameOwner.getClass();

        List<AnnotatedMethod<Subscribe>> eventListenerMethods = screenIntrospectionData.getSubscribeMethods();

        for (AnnotatedMethod<Subscribe> annotatedMethod : eventListenerMethods) {
            Method method = annotatedMethod.getMethod();
            Subscribe annotation = annotatedMethod.getAnnotation();

            String target = UiDescriptorUtils.getInferredSubscribeId(annotation);

            Parameter parameter = method.getParameters()[0];
            Class<?> eventType = parameter.getType();

            Frame frame = UiControllerUtils.getFrame(frameOwner);
            ScreenData screenData = UiControllerUtils.getScreenData(frameOwner);

            Object eventTarget = null;

            if (Strings.isNullOrEmpty(target)) {
                switch (annotation.target()) {
                    // if kept default value
                    case COMPONENT:
                    case CONTROLLER:
                        eventTarget = frameOwner;
                        break;

                    case FRAME:
                        eventTarget = frame;
                        break;

                    case PARENT_CONTROLLER:
                        if (frameOwner instanceof Screen) {
                            throw new DevelopmentException(
                                    String.format("Screen %s cannot use @Subscribe with target = PARENT_CONTROLLER",
                                            frame.getId())
                            );
                        }
                        eventTarget = ((ScreenFragment) frameOwner).getHostController();
                        break;

                    case DATA_CONTEXT:
                        eventTarget = UiControllerUtils.getScreenData(frameOwner).getDataContext();
                        break;

                    default:
                        throw new UnsupportedOperationException("Unsupported @Subscribe target " + annotation.target());
                }
            } else {
                switch (annotation.target()) {
                    case COMPONENT:
                        // component event
                        eventTarget = findEventTarget(frame, target);
                        break;

                    case DATA_LOADER:
                        if (screenData.getLoaderIds().contains(target)) {
                            eventTarget = screenData.getLoader(target);
                        }
                        break;

                    case DATA_CONTAINER:
                        if (screenData.getContainerIds().contains(target)) {
                            eventTarget = screenData.getContainer(target);
                        }
                        break;

                    default:
                        throw new UnsupportedOperationException("Unsupported @Subscribe target " + annotation.target());
                }
            }

            if (eventTarget == null) {
                if (annotation.required()) {
                    throw new DevelopmentException(String.format("Unable to find @Subscribe target %s in %s", target, frame.getId()));
                }

                log.trace("Skip @Subscribe method {} of {} : it is not required and target not found",
                        annotatedMethod.getMethod().getName(), frameOwner.getClass());

                continue;
            }

            Consumer listener;
            MethodHandle consumerMethodFactory =
                    reflectionInspector.getConsumerMethodFactory(clazz, annotatedMethod, eventType);
            try {
                listener = (Consumer) consumerMethodFactory.invoke(frameOwner);
            } catch (Error e) {
                throw e;
            } catch (Throwable e) {
                throw new RuntimeException("Unable to bind consumer handler", e);
            }

            MethodHandle addListenerMethod = reflectionInspector.getAddListenerMethod(eventTarget.getClass(), eventType);
            if (addListenerMethod == null) {
                throw new DevelopmentException(String.format("Target %s does not support event type %s",
                        eventTarget.getClass().getName(), eventType));
            }

            try {
                addListenerMethod.invoke(eventTarget, listener);
            } catch (Error e) {
                throw e;
            } catch (Throwable e) {
                throw new RuntimeException("Unable to add listener" + method, e);
            }
        }
    }

    @Nullable
    protected Object findEventTarget(Frame frame, String target) {
        String[] elements = ValuePathHelper.parse(target);
        if (elements.length > 1) {
            String id = elements[elements.length - 1];

            String[] subPath = ArrayUtils.subarray(elements, 0, elements.length - 1);
            Component component = frame.getComponent(ValuePathHelper.format(subPath));

            if (component != null) {
                if (component instanceof HasSubParts) {
                    Object part = ((HasSubParts) component).getSubPart(id);
                    if (part != null) {
                        return part;
                    }
                }

                if (component instanceof ComponentContainer) {
                    Component childComponent = ((ComponentContainer) component).getComponent(id);
                    if (childComponent != null) {
                        return childComponent;
                    }
                }
            }
        } else if (elements.length == 1) {
            Object part = frame.getSubPart(target);
            if (part != null) {
                return part;
            }

            Component component = frame.getComponent(target);
            if (component != null) {
                return component;
            }
        }

        return null;
    }

    protected void initUiEventListeners(FrameOwner frameOwner, ScreenIntrospectionData screenIntrospectionData) {
        Class<? extends FrameOwner> clazz = frameOwner.getClass();

        List<Method> eventListenerMethods = screenIntrospectionData.getEventListenerMethods();

        if (!eventListenerMethods.isEmpty()) {
            Events events = beanLocator.get(Events.NAME);

            List<ApplicationListener> listeners = eventListenerMethods.stream()
                    .map(m -> new UiEventListenerMethodAdapter(frameOwner, clazz, m, events))
                    .collect(Collectors.toList());

            UiControllerUtils.setUiEventListeners(frameOwner, listeners);
        }
    }

    protected void doInjection(AnnotatedElement element, Class annotationClass) {
        Class<?> type;
        String name = null;
        if (annotationClass == Named.class) {
            name = element.getAnnotation(Named.class).value();
        } else if (annotationClass == Resource.class) {
            name = element.getAnnotation(Resource.class).name();
        } else if (annotationClass == WindowParam.class) {
            name = element.getAnnotation(WindowParam.class).name();
        }

        boolean required = true;
        if (element.isAnnotationPresent(WindowParam.class)) {
            required = element.getAnnotation(WindowParam.class).required();
        } else if (element.isAnnotationPresent(Autowired.class)) {
            required = element.getAnnotation(Autowired.class).required();
        }

        if (element instanceof Field) {
            type = ((Field) element).getType();
            if (StringUtils.isEmpty(name)) {
                name = ((Field) element).getName();
            }
        } else if (element instanceof Method) {
            Class<?>[] types = ((Method) element).getParameterTypes();
            if (types.length != 1) {
                throw new IllegalStateException("Can inject to methods with one parameter only");
            }

            type = types[0];
            if (StringUtils.isEmpty(name)) {
                if (((Method) element).getName().startsWith("set")) {
                    name = StringUtils.uncapitalize(((Method) element).getName().substring(3));
                } else {
                    name = ((Method) element).getName();
                }
            }
        } else {
            throw new IllegalStateException("Can inject to fields and setter methods only");
        }

        Object instance = getInjectedInstance(type, name, annotationClass, element);

        if (instance != null) {
            assignValue(element, instance);
        } else if (required) {
            Class<?> declaringClass = ((Member) element).getDeclaringClass();
            Class<? extends FrameOwner> frameClass = frameOwner.getClass();

            String msg;
            if (frameClass == declaringClass) {
                msg = String.format(
                        "Unable to find an instance of type '%s' named '%s' for instance of '%s'",
                        type, name, frameClass.getCanonicalName());
            } else {
                msg = String.format(
                        "Unable to find an instance of type '%s' named '%s' declared in '%s' for instance of '%s'",
                        type, name, declaringClass.getCanonicalName(), frameClass.getCanonicalName());
            }

            if (!(frameOwner instanceof LegacyFrame)) {

                throw new DevelopmentException(msg);
            } else {
                log.warn(msg);
            }
        } else {
            log.trace("Skip injection {} of {} as it is optional and instance not found",
                    name, frameOwner.getClass());
        }
    }

    @Nullable
    protected Object getInjectedInstance(Class<?> type, String name, Class annotationClass, AnnotatedElement element) {
        Frame frame = UiControllerUtils.getFrame(frameOwner);

        if (annotationClass == WindowParam.class) {
            if (options instanceof MapScreenOptions) {
                return ((MapScreenOptions) options).getParams().get(name);
            }
            // Injecting a parameter
            return null;

        } else if (ScreenFragment.class.isAssignableFrom(type)) {
            // Injecting inner fragment controller
            Component fragment = frame.getComponent(name);
            if (fragment == null) {
                return null;
            }
            return ((Fragment) fragment).getFrameOwner();

        } else if (Component.class.isAssignableFrom(type)) {
            /// if legacy frame - inject controller
            Component component = frame.getComponent(name);
            if (component instanceof Fragment
                && ((Fragment) component).getFrameOwner() instanceof LegacyFrame) {

                ScreenFragment frameOwner = ((Fragment) component).getFrameOwner();
                if (type.isAssignableFrom(frameOwner.getClass())) {
                    return frameOwner;
                }
            }

            // for legacy screens only
            if (component == null
                    && frameOwner instanceof LegacyFrame) {
                // try to find using slow iteration
                component = ComponentsHelper.getComponent(frame, name);
            }

            // Injecting a UI component
            return component;

        } else if (InstanceContainer.class.isAssignableFrom(type)) {
            // Injecting a container
            ScreenData data = UiControllerUtils.getScreenData(frameOwner);
            return data.getContainer(name);

        } else if (DataLoader.class.isAssignableFrom(type)) {
            // Injecting a loader
            ScreenData data = UiControllerUtils.getScreenData(frameOwner);
            return data.getLoader(name);

        } else if (DataContext.class.isAssignableFrom(type)) {
            // Injecting the data context
            ScreenData data = UiControllerUtils.getScreenData(frameOwner);
            return data.getDataContext();

        } else if (Datasource.class.isAssignableFrom(type)) {
            // Injecting a datasource
            return ((LegacyFrame) frame.getFrameOwner()).getDsContext().get(name);

        } else if (DsContext.class.isAssignableFrom(type)) {
            if (frame.getFrameOwner() instanceof LegacyFrame) {
                // Injecting the DsContext
                return ((LegacyFrame) frame.getFrameOwner()).getDsContext();
            } else {
                throw new DevelopmentException("DsContext can be injected only into LegacyFrame inheritors");
            }

        } else if (DataSupplier.class.isAssignableFrom(type)) {
            if (frame.getFrameOwner() instanceof LegacyFrame) {
                // Injecting the DataSupplier
                return ((LegacyFrame) frame.getFrameOwner()).getDsContext().getDataSupplier();
            } else {
                throw new DevelopmentException("DataSupplier can be injected only into LegacyFrame inheritors");
            }

        } else if (FrameContext.class.isAssignableFrom(type)) {
            // Injecting the FrameContext
            return frame.getContext();

        } else if (Action.class.isAssignableFrom(type)) {
            // Injecting an action
            return ComponentsHelper.findAction(name, frame);

        } else if (ExportDisplay.class.isAssignableFrom(type)) {
            // Injecting an ExportDisplay
            ExportDisplay exportDisplay = beanLocator.get(ExportDisplay.NAME);
            exportDisplay.setFrame(frame);
            return exportDisplay;

        } else if (Config.class.isAssignableFrom(type)) {
            ClientConfiguration configuration = beanLocator.get(Configuration.NAME);
            //noinspection unchecked
            return configuration.getConfigCached((Class<? extends Config>) type);

        } else if (Logger.class == type && element instanceof Field) {
            // injecting logger
            return LoggerFactory.getLogger(((Field) element).getDeclaringClass());

        } else if (Screens.class.isAssignableFrom(type)) {
            // injecting screens
            return UiControllerUtils.getScreenContext(frameOwner).getScreens();

        } else if (Dialogs.class.isAssignableFrom(type)) {
            // injecting dialogs
            return UiControllerUtils.getScreenContext(frameOwner).getDialogs();

        } else if (Notifications.class.isAssignableFrom(type)) {
            // injecting notifications
            return UiControllerUtils.getScreenContext(frameOwner).getNotifications();

        } else if (Fragments.class.isAssignableFrom(type)) {
            // injecting fragments
            return UiControllerUtils.getScreenContext(frameOwner).getFragments();

        } else if (UrlRouting.class.isAssignableFrom(type)) {
            // injecting urlRouting
            return UiControllerUtils.getScreenContext(frameOwner).getUrlRouting();

        } else if (MessageBundle.class == type) {
            return createMessageBundle(element, frame);

        } else if (ThemeConstants.class == type) {
            // Injecting a Theme
            ThemeConstantsManager themeManager = beanLocator.get(ThemeConstantsManager.NAME);
            return themeManager.getConstants();

        } else {
            Object instance;
            // Try to find a Spring bean
            Map<String, ?> beans = beanLocator.getAll(type);
            if (!beans.isEmpty()) {
                instance = beans.get(name);
                // If a bean with required name found, return it. Otherwise return first found.
                if (instance != null) {
                    return instance;
                } else {
                    return beans.values().iterator().next();
                }
            }

            // There are no Spring beans of required type - the last option is Companion
            if (frameOwner instanceof LegacyFrame) {
                instance = ((LegacyFrame) frameOwner).getCompanion();
                if (instance != null && type.isAssignableFrom(instance.getClass())) {
                    return instance;
                }
            }
        }
        return null;
    }

    protected MessageBundle createMessageBundle(@SuppressWarnings("unused") AnnotatedElement element, Frame frame) {
        MessageBundle messageBundle = beanLocator.getPrototype(MessageBundle.NAME);

        Class<? extends FrameOwner> screenClass = frame.getFrameOwner().getClass();
        String packageName = UiControllerUtils.getPackage(screenClass);
        messageBundle.setMessagesPack(packageName);

        if (frame instanceof Component.HasXmlDescriptor) {
            Element xmlDescriptor = ((Component.HasXmlDescriptor) frame).getXmlDescriptor();
            if (xmlDescriptor != null) {
                String messagePack = xmlDescriptor.attributeValue("messagesPack");
                if (messagePack != null) {
                    messageBundle.setMessagesPack(messagePack);
                }
            }
        }

        return messageBundle;
    }

    protected void assignValue(AnnotatedElement element, Object value) {
        // element is already marked as accessible in UiControllerReflectionInspector

        if (element instanceof Field) {
            Field field = (Field) element;

            try {
                field.set(frameOwner, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("CDI - Unable to assign value to field " + field.getName(), e);
            }
        } else {
            Method method = (Method) element;

            Object[] params = new Object[1];
            params[0] = value;
            try {
                method.invoke(frameOwner, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("CDI - Unable to assign value through setter "
                        + method.getName(), e);
            }
        }
    }

    public static class InstalledFunction implements Function {
        private final FrameOwner frameOwner;
        private final Method method;

        public InstalledFunction(FrameOwner frameOwner, Method method) {
            this.frameOwner = frameOwner;
            this.method = method;
        }

        @Override
        public Object apply(Object o) {
            try {
                return method.invoke(frameOwner, o);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Exception on @Install invocation", e);
            }
        }

        @Override
        public String toString() {
            return "InstalledFunction{" +
                    "frameOwner=" + frameOwner.getClass() +
                    ", method=" + method +
                    '}';
        }
    }

    public static class InstalledConsumer implements Consumer {
        private final FrameOwner frameOwner;
        private final Method method;

        public InstalledConsumer(FrameOwner frameOwner, Method method) {
            this.frameOwner = frameOwner;
            this.method = method;
        }

        @Override
        public void accept(Object o) {
            try {
                method.invoke(frameOwner, o);
            } catch (IllegalAccessException | InvocationTargetException e) {
                if (e instanceof InvocationTargetException
                        && ((InvocationTargetException) e).getTargetException() instanceof RuntimeException) {
                    throw (RuntimeException) ((InvocationTargetException) e).getTargetException();
                }

                throw new RuntimeException("Exception on @Install invocation", e);
            }
        }

        @Override
        public String toString() {
            return "InstalledConsumer{" +
                    "frameOwner=" + frameOwner.getClass() +
                    ", method=" + method +
                    '}';
        }
    }

    public static class InstalledSupplier implements Supplier {
        private final FrameOwner frameOwner;
        private final Method method;

        public InstalledSupplier(FrameOwner frameOwner, Method method) {
            this.frameOwner = frameOwner;
            this.method = method;
        }

        @Override
        public Object get() {
            try {
                return method.invoke(frameOwner);
            } catch (IllegalAccessException | InvocationTargetException e) {
                if (e instanceof InvocationTargetException
                        && ((InvocationTargetException) e).getTargetException() instanceof RuntimeException) {
                    throw (RuntimeException) ((InvocationTargetException) e).getTargetException();
                }

                throw new RuntimeException("Exception on @Install invocation", e);
            }
        }

        @Override
        public String toString() {
            return "InstalledSupplier{" +
                    "target=" + frameOwner.getClass() +
                    ", method=" + method +
                    '}';
        }
    }

    public static class InstalledBiFunction implements BiFunction {
        private final FrameOwner frameOwner;
        private final Method method;

        public InstalledBiFunction(FrameOwner frameOwner, Method method) {
            this.frameOwner = frameOwner;
            this.method = method;
        }

        @Override
        public Object apply(Object o1, Object o2) {
            try {
                return method.invoke(frameOwner, o1, o2);
            } catch (IllegalAccessException | InvocationTargetException e) {
                if (e instanceof InvocationTargetException
                        && ((InvocationTargetException) e).getTargetException() instanceof RuntimeException) {
                    throw (RuntimeException) ((InvocationTargetException) e).getTargetException();
                }

                throw new RuntimeException("Exception on @Install invocation", e);
            }
        }

        @Override
        public String toString() {
            return "InstalledBiFunction{" +
                    "frameOwner=" + frameOwner.getClass() +
                    ", method=" + method +
                    '}';
        }
    }

    public static class InstalledProxyHandler implements InvocationHandler {
        private final FrameOwner frameOwner;
        private final Method method;

        public InstalledProxyHandler(FrameOwner frameOwner, Method method) {
            this.frameOwner = frameOwner;
            this.method = method;
        }

        @Override
        public Object invoke(Object proxy, Method invokedMethod, Object[] args) throws Throwable {
            if ("toString".equals(invokedMethod.getName())) {
                return this.toString();
            }
            if ("equals".equals(invokedMethod.getName())) {
                return args.length == 1 && args[0] == proxy;
            }
            if ("hashCode".equals(invokedMethod.getName())) {
                return this.hashCode();
            }

            if (invokedMethod.getParameterCount() == method.getParameterCount()) {
                try {
                    return this.method.invoke(frameOwner, args);
                } catch (InvocationTargetException e) {
                    if (e.getTargetException() instanceof RuntimeException) {
                        throw e.getTargetException();
                    }

                    throw e.getTargetException();
                }
            }

            throw new UnsupportedOperationException(
                    String.format("InstalledProxyHandler does not support method %s. Check types and number of parameters",
                            invokedMethod));
        }

        @Override
        public String toString() {
            return "InstalledProxyHandler{" +
                    "frameOwner=" + frameOwner.getClass() +
                    ", method=" + method +
                    '}';
        }
    }
}