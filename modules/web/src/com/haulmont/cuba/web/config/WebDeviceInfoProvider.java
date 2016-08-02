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

package com.haulmont.cuba.web.config;

import com.haulmont.cuba.gui.config.DeviceInfo;
import com.haulmont.cuba.gui.config.DeviceInfo.OperatingSystem;
import com.haulmont.cuba.gui.config.DeviceInfoProvider;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.WebBrowser;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

@Component(DeviceInfoProvider.NAME)
public class WebDeviceInfoProvider implements DeviceInfoProvider {

    @Nullable
    @Override
    public DeviceInfo getDeviceInfo() {
        // per request cache
        HttpServletRequest currentServletRequest = VaadinServletService.getCurrentServletRequest();
        if (currentServletRequest == null) {
            return null;
        }

        DeviceInfo deviceInfo = (DeviceInfo) currentServletRequest.getAttribute(DeviceInfoProvider.NAME);
        if (deviceInfo != null) {
            return deviceInfo;
        }

        Page page = Page.getCurrent();

        if (page == null) {
            return null;
        }

        WebBrowser webBrowser = page.getWebBrowser();

        DeviceInfo di = new DeviceInfo();

        di.setAddress(webBrowser.getAddress());
        di.setBrowserApplication(webBrowser.getBrowserApplication());
        di.setBrowserMajorVersion(webBrowser.getBrowserMajorVersion());
        di.setBrowserMinorVersion(webBrowser.getBrowserMinorVersion());

        di.setChrome(webBrowser.isChrome());
        di.setChromeFrame(webBrowser.isChromeFrame());
        di.setChromeFrameCapable(webBrowser.isChromeFrameCapable());
        di.setEdge(webBrowser.isEdge());
        di.setFirefox(webBrowser.isFirefox());
        di.setOpera(webBrowser.isOpera());
        di.setIE(webBrowser.isIE());

        if (webBrowser.isWindows()) {
            di.setOperatingSystem(OperatingSystem.WINDOWS);
        } else if (webBrowser.isAndroid()) {
            di.setOperatingSystem(OperatingSystem.ANDROID);
        } else if (webBrowser.isIOS()) {
            di.setOperatingSystem(OperatingSystem.IOS);
        } else if (webBrowser.isMacOSX()) {
            di.setOperatingSystem(OperatingSystem.MACOSX);
        } else if (webBrowser.isLinux()) {
            di.setOperatingSystem(OperatingSystem.LINUX);
        }

        di.setIPad(webBrowser.isIPad());
        di.setIPhone(webBrowser.isIPhone());
        di.setWindowsPhone(webBrowser.isWindowsPhone());

        di.setSecureConnection(webBrowser.isSecureConnection());
        di.setLocale(webBrowser.getLocale());

        di.setScreenHeight(webBrowser.getScreenHeight());
        di.setScreenWidth(webBrowser.getScreenWidth());

        currentServletRequest.setAttribute(DeviceInfoProvider.NAME, di);

        return di;
    }
}