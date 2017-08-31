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

package com.haulmont.cuba.web.sys.remoting.discovery;

import com.haulmont.cuba.core.sys.remoting.discovery.SessionUrlsHolder;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Stores the list of server URLs in the HttpSession.
 */
public class WebHttpSessionUrlsHolder implements SessionUrlsHolder {

    private static final String SESSION_ATTR = WebHttpSessionUrlsHolder.class.getName() + ".lastSessionUrls";

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public List<String> getUrls(String selectorId) {
        VaadinRequest vaadinRequest = VaadinService.getCurrentRequest();
        if (vaadinRequest != null)
            return (List) vaadinRequest.getWrappedSession().getAttribute(getAttributeName(selectorId));
        else {
            HttpSession httpSession = getHttpSession();
            return httpSession != null ? (List<String>) httpSession.getAttribute(getAttributeName(selectorId)) : null;
        }
    }

    @Override
    public void setUrls(String selectorId, List<String> urls) {
        VaadinRequest vaadinRequest = VaadinService.getCurrentRequest();
        if (vaadinRequest != null)
            vaadinRequest.getWrappedSession().setAttribute(getAttributeName(selectorId), urls);
        else {
            HttpSession httpSession = getHttpSession();
            if (httpSession != null) {
                httpSession.setAttribute(getAttributeName(selectorId), urls);
            }
        }
    }

    private HttpSession getHttpSession() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
        	HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return request.getSession();
        } else {
            return null;
        }
    }

    protected String getAttributeName(String selectorId) {
        return SESSION_ATTR + (selectorId != null ? "." + selectorId : "");
    }
}
