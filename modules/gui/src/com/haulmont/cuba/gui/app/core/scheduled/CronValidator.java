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

/**
 *
 */
package com.haulmont.cuba.gui.app.core.scheduled;

import com.haulmont.cuba.core.app.ServerInfoService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import org.springframework.scheduling.support.CronSequenceGenerator;

/**
 */
public class CronValidator implements Field.Validator {

    @Override
    public void validate(Object value) throws ValidationException {
        if (value != null) {
            ServerInfoService serverInfoService = AppBeans.get(ServerInfoService.NAME);
            Messages messages = AppBeans.get(Messages.NAME);
            try {
                new CronSequenceGenerator(value.toString(), serverInfoService.getTimeZone());
            } catch (Exception e) {
                throw new ValidationException(messages.getMessage(getClass(), "validation.cronInvalid"));
            }
        }
    }
}