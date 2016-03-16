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

package com.haulmont.cuba.gui.app.core.license;

import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.security.app.UserSessionService;

import javax.inject.Inject;
import java.util.Map;

/**
 */
public class LicenseFrame extends AbstractFrame {

    @Inject
    private UserSessionService uss;
    @Inject
    private TextArea licenseTxtField;

    @Override
    public void init(Map<String, Object> params) {
        Map<String, Object> info = uss.getLicenseInfo();

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : info.entrySet()) {
            Object val = entry.getValue();
            if (entry.getKey().equals("licensedSessions") || entry.getKey().equals("licensedEntities")) {
                val = ((val instanceof Integer) && ((Integer) val == 0)) ? "Not restricted" : val;
            }
            sb.append(getMessage(entry.getKey())).append(": ").append(val).append("\n");
        }

        licenseTxtField.setValue(sb.toString());
        licenseTxtField.setEditable(false);

    }
}
