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

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.core.sys.ThreadLocalSecurityContextHolder;
import com.vaadin.server.VaadinSession;

public class WebVaadinCompatibleSecurityContextHolder extends ThreadLocalSecurityContextHolder {
    @Override
    public SecurityContext get() {
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        if (vaadinSession != null && vaadinSession.hasLock()) {
            return vaadinSession.getAttribute(SecurityContext.class);
        }

        return super.get();
    }

    @Override
    public void set(SecurityContext securityContext) {
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        if (vaadinSession != null && vaadinSession.hasLock()) {
            vaadinSession.setAttribute(SecurityContext.class, securityContext);
        } else {
            super.set(securityContext);
        }
    }
}