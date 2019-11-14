/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.gui.sys;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ScreenFacet;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@SuppressWarnings("unused")
@org.springframework.stereotype.Component(UiControllerPropertyInjector.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UiControllerPropertyInjector {

    public static final String NAME = "cuba_UiControllerPropertyInjector";

    protected final FrameOwner frameOwner;
    protected final Screen sourceScreen;
    protected final List<UiControllerProperty> properties;

    protected UiControllerReflectionInspector reflectionInspector;

    /**
     * Creates UiControllerPropertyInjector to inject properties into fragments
     *
     * @param frameOwner target screen
     * @param properties properties to inject
     */
    public UiControllerPropertyInjector(FrameOwner frameOwner, List<UiControllerProperty> properties) {
        checkNotNullArgument(frameOwner, "Frame owner cannot be null");
        checkNotNullArgument(properties, "Properties cannot be null");

        this.frameOwner = frameOwner;
        this.sourceScreen = null;
        this.properties = properties;
    }

    /**
     * Creates UiControllerPropertyInjector to inject properties into {@link ScreenFacet}.
     *
     * @param frameOwner   target screen
     * @param sourceScreen source screen that is used to load ref properties
     * @param properties   properties to inject
     */
    public UiControllerPropertyInjector(FrameOwner frameOwner, Screen sourceScreen, List<UiControllerProperty> properties) {
        checkNotNullArgument(frameOwner, "Frame owner cannot be null");
        checkNotNullArgument(properties, "Properties cannot be null");

        this.frameOwner = frameOwner;
        this.sourceScreen = sourceScreen;
        this.properties = properties;
    }

    @Inject
    public void setReflectionInspector(UiControllerReflectionInspector reflectionInspector) {
        this.reflectionInspector = reflectionInspector;
    }

    public void inject() {
        for (UiControllerProperty property : properties) {
            String propName = property.getName();
            Method setter = findSuitableSetter(propName);
            if (setter == null) {
                throw new GuiDevelopmentException(String.format(
                        "Unable to find suitable setter for property '%s'. Its value will not be injected into '%s'",
                        propName, frameOwner),
                        UiControllerUtils.getScreen(frameOwner).getId());
            }

            Object value = property.getValue();

            if (value instanceof String) {
                Class<?> propType = setter.getParameterTypes()[0];
                value = parseParamValue(property, propType);

                if (value == null) {
                    throw new GuiDevelopmentException(String.format(
                            "Unable to parse '%s' as '%s' for property '%s'. It will not be injected into '%s'",
                            property.getValue(), propType, propName, frameOwner),
                            UiControllerUtils.getScreen(frameOwner).getId());
                }
            }

            try {
                setter.invoke(frameOwner, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(String.format(
                        "Unable to assign value through setter '%s' in '%s' for property '%s'",
                        setter.getName(), frameOwner, propName), e);
            }
        }
    }

    @Nullable
    protected Method findSuitableSetter(String propName) {
        String setterName = getSetterName(propName);

        List<Method> propertySetters = reflectionInspector.getPropertySetters(frameOwner.getClass());

        return propertySetters.stream()
                .filter(method -> setterName.equals(method.getName()))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    protected Object parseParamValue(UiControllerProperty property, Class<?> propType) {
        Object value = null;

        if (UiControllerProperty.Type.VALUE == property.getType()) {
            value = parsePrimitive(property, propType);
        } else if (UiControllerProperty.Type.REFERENCE == property.getType()) {
            value = findObjectByRef(property, propType);
        }

        return value;
    }

    protected Object parsePrimitive(UiControllerProperty property, Class propType) {
        Object value = null;
        String stringProperty = ((String) property.getValue());

        if (Byte.class == propType || Byte.TYPE == propType) {
            value = parseNumber(property, Byte.class);

        } else if (Short.class == propType || Short.TYPE == propType) {
            value = parseNumber(property, Short.class);

        } else if (Integer.class == propType || Integer.TYPE == propType) {
            value = parseNumber(property, Integer.class);

        } else if (Long.class == propType || Long.TYPE == propType) {
            value = parseNumber(property, Long.class);

        } else if (Float.class == propType || Float.TYPE == propType) {
            value = parseNumber(property, Float.class);

        } else if (Double.class == propType || Double.TYPE == propType) {
            value = parseNumber(property, Double.class);
        }

        if (Boolean.class == propType || Boolean.TYPE == propType) {
            value = Boolean.valueOf(stringProperty);
        } else if (String.class == propType) {
            value = property.getValue();
        }

        return value;
    }

    protected Object parseNumber(UiControllerProperty property, Class<? extends Number> numberType) {
        String stringValue = (String) property.getValue();
        if (!NumberUtils.isParsable(stringValue)) {
            throw new GuiDevelopmentException(String.format(
                    "Unable to parse '%s' as '%s'. Property value '%s' will not be injected into '%s'",
                    property.getValue(), numberType, property.getName(), frameOwner),
                    UiControllerUtils.getScreen(frameOwner).getId());
        }
        return org.springframework.util.NumberUtils.parseNumber(stringValue, numberType);
    }

    @Nullable
    protected Object findObjectByRef(UiControllerProperty property, Class<?> propType) {
        Object value = null;
        String stringProp = (String) property.getValue();

        if (Component.class.isAssignableFrom(propType)) {
            value = findComponent(stringProp);
            if (value == null) {
                throw new GuiDevelopmentException(String.format(
                        "Unable to find component with id '%s'. Property value '%s' will not be injected into '%s'",
                        property.getValue(), property.getName(), frameOwner),
                        UiControllerUtils.getScreen(frameOwner).getId());
            }

        } else if (InstanceContainer.class.isAssignableFrom(propType)) {
            value = findDataContainer(stringProp);
            if (value == null) {
                throw new GuiDevelopmentException(String.format(
                        "Unable to find data container with id '%s'. Property value '%s' will not be injected into '%s'",
                        property.getValue(), property.getName(), frameOwner),
                        UiControllerUtils.getScreen(frameOwner).getId());
            }

        } else if (Datasource.class.isAssignableFrom(propType)) {
            value = findDatasource(stringProp);
            if (value == null) {
                throw new GuiDevelopmentException(String.format(
                        "Unable to find datasource with id '%s'. Property value '%s' will not be injected into '%s'",
                        property.getValue(), property.getName(), frameOwner),
                        UiControllerUtils.getScreen(frameOwner).getId());
            }
        }

        return value;
    }

    protected String getSetterName(String name) {
        return "set" + StringUtils.capitalize(name);
    }

    @Nullable
    protected Component findComponent(String componentId) {
        Component component = null;
        Window window = null;

        if (sourceScreen != null) {
            window = sourceScreen.getWindow();
        } else if (frameOwner instanceof ScreenFragment) {
            FrameOwner host = ((ScreenFragment) frameOwner).getHostController();

            if (host instanceof Screen) {
                window = ((Screen) host).getWindow();
            }
        } else if (frameOwner instanceof Screen) {
            window = ((Screen) frameOwner).getWindow();
        }

        if (window != null) {
            component = window.getComponent(componentId);
        }

        return component;
    }

    @Nullable
    protected InstanceContainer findDataContainer(String containerId) {
        FrameOwner host = frameOwner instanceof ScreenFragment
                ? ((ScreenFragment) frameOwner).getHostController()
                : frameOwner;

        if (sourceScreen != null) {
            return UiControllerUtils.getScreenData(sourceScreen)
                    .getContainer(containerId);
        }
        return UiControllerUtils.getScreenData(host)
                .getContainer(containerId);
    }

    @Nullable
    protected Datasource findDatasource(String datasourceId) {
        Datasource datasource = null;

        if (sourceScreen instanceof LegacyFrame) {
            ((LegacyFrame) sourceScreen).getDsContext().get(datasourceId);
        }

        FrameOwner frameOwner = this.frameOwner instanceof ScreenFragment
                ? ((ScreenFragment) this.frameOwner).getHostController()
                : this.frameOwner;

        if (frameOwner instanceof LegacyFrame) {
            datasource = ((LegacyFrame) frameOwner).getDsContext().get(datasourceId);
        }

        return datasource;
    }

}
