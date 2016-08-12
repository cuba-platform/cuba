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

@Component(DesktopScreenAgent.NAME)
public class DesktopScreenAgent implements ScreenAgent, Ordered {

    public static final String NAME = "cuba_DesktopScreenAgent";

    @Override
    public boolean isSupported(DeviceInfo device) {
        return device.isMacOSX() || device.isLinux() || device.isWindows()
                || !(device.isAndroid() || device.isWindowsPhone() || device.isIPad() || device.isIPhone());
    }

    @Override
    public String getAlias() {
        return "DESKTOP";
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE;
    }
}