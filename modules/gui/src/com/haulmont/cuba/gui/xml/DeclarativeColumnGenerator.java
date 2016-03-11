/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author artamonov
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