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
package com.haulmont.cuba.gui.app.core.locking;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.Formatter;

public class LockNameFormatter implements Formatter<String> {

    protected Metadata metadata = AppBeans.get(Metadata.NAME);
    protected MessageTools messageTools = AppBeans.get(MessageTools.NAME);

    @Override
    public String format(String value) {
        MetaClass mc = metadata.getSession().getClass(value);
        if (mc != null) {
            return messageTools.getEntityCaption(mc);
        } else
            return value;
    }
}