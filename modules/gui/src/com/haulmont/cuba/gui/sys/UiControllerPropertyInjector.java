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

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@org.springframework.stereotype.Component(UiControllerPropertyInjector.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UiControllerPropertyInjector {

    private static final Logger log = LoggerFactory.getLogger(UiControllerPropertyInjector.class);

    public static final String NAME = "cuba_UiControllerPropertyInjector";

    protected final FrameOwner frameOwner;
    protected final List<UiControllerProperty> properties;

    protected UiControllerReflectionInspector reflectionInspector;

    public UiControllerPropertyInjector(FrameOwner frameOwner, List<UiControllerProperty> properties) {
        checkNotNullArgument(frameOwner, "Frame owner cannot be null");
        checkNotNullArgument(properties, "Properties cannot be null");

        this.frameOwner = frameOwner;
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
                log.info("Unable to find suitable setter for property '{}'. Its value will not be injected into '{}'",
                        propName, frameOwner);
                continue;
            }

            Class<?> propType = setter.getParameterTypes()[0];
            Object value = parseParamValue(property, propType);

            if (value == null) {
                log.info("Unable to parse '{}' as '{}' for property '{}'. It will not be injected into '{}'",
                        property.getValue(), propType, propName, frameOwner);
                continue;
            }

            try {
                setter.invoke(frameOwner, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.info("Unable to assign value through setter '{}' in '{}' for property '{}'",
                        setter.getName(), frameOwner, propName, e);
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

        try {
            if (Byte.class == propType || Byte.TYPE == propType) {
                value = Byte.valueOf(property.getValue());

            } else if (Short.class == propType || Short.TYPE == propType) {
                value = Short.valueOf(property.getValue());

            } else if (Integer.class == propType || Integer.TYPE == propType) {
                value = Integer.valueOf(property.getValue());

            } else if (Long.class == propType || Long.TYPE == propType) {
                value = Long.valueOf(property.getValue());

            } else if (Float.class == propType || Float.TYPE == propType) {
                value = Float.valueOf(property.getValue());

            } else if (Double.class == propType || Double.TYPE == propType) {
                value = Double.valueOf(property.getValue());
            }
        } catch (NumberFormatException e) {
            log.info("Unable to parse '{}' as '{}'. Property value '{}' will not be injected into '{}'",
                    property.getValue(), propType, property.getName(), frameOwner);
        }

        if (Boolean.class == propType || Boolean.TYPE == propType) {
            value = Boolean.valueOf(property.getValue());
        } else if (String.class == propType) {
            value = property.getValue();
        }

        return value;
    }

    @Nullable
    protected Object findObjectByRef(UiControllerProperty property, Class<?> propType) {
        Object value = null;

        if (Component.class.isAssignableFrom(propType)) {
            value = findComponent(property.getValue());
            if (value == null) {
                log.info("Unable to find component with id '{}'. Property value '{}' will not be injected into '{}'",
                        property.getValue(), property.getName(), frameOwner);
            }

        } else if (InstanceContainer.class.isAssignableFrom(propType)) {
            value = findDataContainer(property.getValue());
            if (value == null) {
                log.info("Unable to find data container with id '{}'. Property value '{}' will not be injected into '{}'",
                        property.getValue(), property.getName(), frameOwner);
            }

        } else if (Datasource.class.isAssignableFrom(propType)) {
            value = findDatasource(property.getValue());
            if (value == null) {
                log.info("Unable to find datasource with id '{}'. Property value '{}' will not be injected into '{}'",
                        property.getValue(), property.getName(), frameOwner);
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

        if (frameOwner instanceof ScreenFragment) {
            FrameOwner host = ((ScreenFragment) frameOwner).getHostController();

            if (host instanceof Screen) {
                component = ((Screen) host).getWindow().getComponent(componentId);
            }
        } else if (frameOwner instanceof Screen) {
            component = ((Screen) frameOwner).getWindow().getComponent(componentId);
        }

        return component;
    }

    @Nullable
    protected InstanceContainer findDataContainer(String containerId) {
        FrameOwner host = frameOwner instanceof ScreenFragment
                ? ((ScreenFragment) frameOwner).getHostController()
                : frameOwner;

        return UiControllerUtils.getScreenData(host)
                .getContainer(containerId);
    }

    @Nullable
    protected Datasource findDatasource(String datasourceId) {
        Datasource datasource = null;

        FrameOwner frameOwner = this.frameOwner instanceof ScreenFragment
                ? ((ScreenFragment) this.frameOwner).getHostController()
                : this.frameOwner;

        if (frameOwner instanceof LegacyFrame) {
            datasource = ((LegacyFrame) frameOwner).getDsContext().get(datasourceId);
        }

        return datasource;
    }

}
