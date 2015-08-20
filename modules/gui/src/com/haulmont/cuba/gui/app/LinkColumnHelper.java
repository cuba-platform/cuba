/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

/**
 * @author krivopustov
 * @version $Id$
 */
public class LinkColumnHelper {

    public interface Handler {
        void onClick(Entity entity);
    }

    public static void initColumn(Table table, final String propertyName, final Handler handler) {

        final ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.NAME);

        table.addGeneratedColumn(propertyName, new Table.ColumnGenerator() {
            @Override
            public Component generateCell(final Entity entity) {
//                    //process properties like building.house.room
                String[] props = propertyName.split("\\.");
                Instance nestedEntity = entity;
                for (int i = 0; i < props.length - 1; i++) {
                    nestedEntity = nestedEntity.getValue(props[i]);
                    if (nestedEntity == null) {
                        break;
                    }
                }
                final Object value = (nestedEntity == null) ? null : nestedEntity.getValue(props[props.length - 1]);
                if (value != null) {
                    Button button = componentsFactory.createComponent(Button.class);
                    button.setStyleName("link");
                    button.setAction(new AbstractAction("open") {
                        @Override
                        public void actionPerform(Component component) {
                            handler.onClick(entity);
                        }

                        @Override
                        public String getCaption() {
                            String str;
                            Datatype datatype = Datatypes.get(value.getClass());
                            if (datatype != null) {
                                UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
                                str = datatype.format(value, sessionSource.getLocale());
                            } else {
                                str = value.toString();
                            }
                            return str;
                        }
                    });

                    button.setStyleName("link");
                    return button;
                }
                return null;
            }
        });
    }

    public static void removeColumn(Table table, final String propertyName) {
        table.removeGeneratedColumn(propertyName);
    }
}