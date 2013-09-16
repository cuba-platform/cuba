/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.Table;

/**
 * Enumerates standard list action types. Can create a corresponding action instance with default parameters.
 *
 * @author krivopustov
 * @version $Id$
 */
public enum ListActionType {

    CREATE("create") {
        @Override
        public Action createAction(ListComponent holder) {
            return new CreateAction(holder);
        }
    },

    EDIT("edit") {
        @Override
        public Action createAction(ListComponent holder) {
            return new EditAction(holder);
        }
    },

    REMOVE("remove") {
        @Override
        public Action createAction(ListComponent holder) {
            return new RemoveAction(holder);
        }
    },

    REFRESH("refresh") {
        @Override
        public Action createAction(ListComponent holder) {
            return new RefreshAction(holder);
        }
    },

    ADD("add") {
        @Override
        public Action createAction(ListComponent holder) {
            return new AddAction(holder);
        }
    },

    EXCLUDE("exclude") {
        @Override
        public Action createAction(ListComponent holder) {
            return new ExcludeAction(holder);
        }
    },

    EXCEL("excel") {
        @Override
        public Action createAction(ListComponent holder) {
            if (holder instanceof Table)
                return new ExcelAction((Table) holder);
            else
                throw new IllegalArgumentException("Only Table can contain EXCEL action");
        }
    };

    private String id;

    ListActionType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract Action createAction(ListComponent holder);
}