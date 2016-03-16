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

package com.haulmont.cuba.gui.xml;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.Table;
import org.apache.commons.lang.reflect.MethodUtils;

import java.lang.reflect.Method;

/**
 */
public class DeclarativeColumnGenerator implements Table.ColumnGenerator {

    private final String methodName;
    private final Table table;

    private Method method;
    private boolean unableToFindMethod = false;

    public DeclarativeColumnGenerator(Table table, String methodName) {
        this.table = table;
        this.methodName = methodName;
    }

    @Override
    public Component generateCell(Entity entity) {
        if (unableToFindMethod) {
            return null;
        }

        Frame frame = table.getFrame();
        if (frame == null) {
            throw new IllegalStateException("Table should be attached to frame");
        }
        Frame controller = ComponentsHelper.getFrameController(frame);

        if (method == null) {
            method = findGeneratorMethod(controller.getClass(), methodName);

            if (method == null) {
                this.unableToFindMethod = true;

                String tableId = table.getId() == null ? "" : table.getId();

                throw new IllegalStateException(
                        String.format("No suitable method named %s for column generator of table %s", methodName, tableId));
            }
        }

        try {
            return (Component) method.invoke(controller, entity);
        } catch (Exception e) {
            throw new RuntimeException("Exception in declarative Table column generator " + methodName, e);
        }
    }

    // Find method with one parameter of type extends Entity and result extends Component
    protected Method findGeneratorMethod(Class cls, String methodName) {
        Method exactMethod = MethodUtils.getAccessibleMethod(cls, methodName, Entity.class);
        if (exactMethod != null) {
            return exactMethod;
        }

        // search through all methods
        Method[] methods = cls.getMethods();
        for (Method availableMethod : methods) {
            if (availableMethod.getName().equals(methodName)) {
                if (availableMethod.getParameterCount() == 1
                        && Component.class.isAssignableFrom(availableMethod.getReturnType())) {
                    if (Entity.class.isAssignableFrom(availableMethod.getParameterTypes()[0])) {
                        // get accessible version of method
                        return MethodUtils.getAccessibleMethod(availableMethod);
                    }
                }
            }
        }
        return null;
    }
}