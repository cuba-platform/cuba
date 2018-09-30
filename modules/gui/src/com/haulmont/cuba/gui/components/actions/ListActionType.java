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
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.gui.components.*;

/**
 * Enumerates standard list action types. Can create a corresponding action instance with default parameters.
 *
 * @deprecated Use {@link Actions} instead.
 */
@Deprecated
public enum ListActionType {

    CREATE("create") {
        @Override
        public Action createAction(ListComponent holder) {
            return CreateAction.create(holder);
        }
    },

    EDIT("edit") {
        @Override
        public Action createAction(ListComponent holder) {
            return EditAction.create(holder);
        }
    },

    REMOVE("remove") {
        @Override
        public Action createAction(ListComponent holder) {
            return RemoveAction.create(holder);
        }
    },

    REFRESH("refresh") {
        @Override
        public Action createAction(ListComponent holder) {
            return RefreshAction.create(holder);
        }
    },

    ADD("add") {
        @Override
        public Action createAction(ListComponent holder) {
            return AddAction.create(holder);
        }
    },

    EXCLUDE("exclude") {
        @Override
        public Action createAction(ListComponent holder) {
            return ExcludeAction.create(holder);
        }
    },

    EXCEL("excel") {
        @Override
        public Action createAction(ListComponent holder) {
            if (holder instanceof Table || holder instanceof DataGrid)
                return ExcelAction.create(holder);
            else
                throw new IllegalArgumentException("Only Table and DataGrid can contain EXCEL action");
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