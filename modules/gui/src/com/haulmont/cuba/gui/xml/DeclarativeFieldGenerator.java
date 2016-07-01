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
 */

package com.haulmont.cuba.gui.xml;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.reflect.MethodUtils;

import java.lang.reflect.Method;

import static org.apache.commons.lang.reflect.MethodUtils.getAccessibleMethod;

public class DeclarativeFieldGenerator implements FieldGroup.CustomFieldGenerator {
    private FieldGroup fieldGroup;
    private String methodName;

    public DeclarativeFieldGenerator(FieldGroup fieldGroup, String methodName) {
        this.fieldGroup = fieldGroup;
        this.methodName = methodName;
    }

    @Override
    public Component generateField(Datasource datasource, String propertyId) {
        Frame frame = fieldGroup.getFrame();
        if (frame == null) {
            throw new IllegalStateException("Table should be attached to frame");
        }
        Frame controller = ComponentsHelper.getFrameController(frame);

        Class<? extends Frame> cCls = controller.getClass();
        Method exactMethod = getAccessibleMethod(cCls, methodName, new Class[]{Datasource.class, String.class});
        if (exactMethod != null) {
            try {
                return (Component) exactMethod.invoke(controller, datasource, propertyId);
            } catch (Exception e) {
                throw new RuntimeException("Exception in declarative FieldGroup Field generator " + methodName, e);
            }
        }

        Method dsMethod = getAccessibleMethod(cCls, methodName, new Class[]{Datasource.class});
        if (dsMethod != null) {
            try {
                return (Component) dsMethod.invoke(controller, datasource);
            } catch (Exception e) {
                throw new RuntimeException("Exception in declarative FieldGroup Field generator " + methodName, e);
            }
        }

        Method parameterLessMethod = getAccessibleMethod(cCls, methodName, new Class[]{});
        if (parameterLessMethod != null) {
            try {
                return (Component) parameterLessMethod.invoke(controller);
            } catch (Exception e) {
                throw new RuntimeException("Exception in declarative FieldGroup Field generator " + methodName, e);
            }
        }

        String fieldGroupId = fieldGroup.getId() == null ? "" : fieldGroup.getId();

        throw new IllegalStateException(
                String.format("No suitable method named %s for column generator of table %s", methodName, fieldGroupId));
    }
}