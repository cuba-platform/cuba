/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.gui.model;

import com.haulmont.cuba.core.global.queryconditions.Condition;

import javax.annotation.Nullable;
import java.util.Map;

/**
 *
 */
public interface DataLoader {

    void load();

    @Nullable
    DataContext getDataContext();

    void setDataContext(DataContext dataContext);

    String getQuery();

    void setQuery(String query);

    Condition getCondition();

    void setCondition(Condition condition);

    Map<String, Object> getParameters();

    void setParameters(Map<String, Object> parameters);

    Object getParameter(String name);

    void setParameter(String name, Object value);

    boolean isSoftDeletion();

    void setSoftDeletion(boolean softDeletion);
}
