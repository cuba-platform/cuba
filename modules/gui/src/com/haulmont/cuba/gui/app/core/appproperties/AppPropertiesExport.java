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

package com.haulmont.cuba.gui.app.core.appproperties;

import com.haulmont.cuba.core.config.AppPropertyEntity;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.ResizableTextArea;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Controller of the {@code appproperties-export.xml} screen
 */
public class AppPropertiesExport extends AbstractWindow {

    @WindowParam
    protected List<AppPropertyEntity> exported;

    @Inject
    protected ResizableTextArea scriptArea;

    @Inject
    protected UserSessionSource uss;

    @Override
    public void init(Map<String, Object> params) {
        getDialogParams().setHeight(400);

        StringBuilder sb = new StringBuilder();
        for (AppPropertyEntity entity : exported) {
            sb.append("insert into SYS_CONFIG (ID, CREATE_TS, CREATED_BY, VERSION, NAME, VALUE)\n");
            sb.append("values ('").append(entity.getId())
                    .append("', current_timestamp, '")
                    .append(uss.getUserSession().getUser().getLogin())
                    .append("', 0, '")
                    .append(entity.getName())
                    .append("', '")
                    .append(entity.getCurrentValue()).append("');\n\n");
        }
        scriptArea.setValue(sb.toString());
    }
}
