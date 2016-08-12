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

package com.haulmont.cuba.web.agent;

import com.haulmont.cuba.gui.config.DeviceInfo;
import com.haulmont.cuba.gui.config.ScreenAgent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component(PhoneScreenAgent.NAME)
public class PhoneScreenAgent implements ScreenAgent, Ordered {

    public static final String NAME = "cuba_PhoneScreenAgent";

    @Override
    public boolean isSupported(DeviceInfo device) {
        return device.isIPhone()
                || ((device.isAndroid() || device.isWindowsPhone())
                    && (device.getAspectRatio() > 1.32 && device.getMaximumDimension() < 800));
    }

    @Override
    public String getAlias() {
        return "PHONE";
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 20;
    }
}