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

package com.haulmont.cuba.gui.components.filter.edit;

import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.filter.condition.FtsCondition;

import javax.inject.Inject;
import java.util.Map;

public class FtsConditionFrame extends ConditionFrame<FtsCondition> {
    @Inject
    protected TextField<String> caption;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
    }

    @Override
    public void setCondition(FtsCondition condition) {
        super.setCondition(condition);
        caption.setValue(condition.getCaption());
    }

    @Override
    public boolean commit() {
        if (!super.commit())
            return false;
        condition.setCaption(caption.getValue());
        return true;
    }
}