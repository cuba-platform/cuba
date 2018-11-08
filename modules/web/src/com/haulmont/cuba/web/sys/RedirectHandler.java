/*
 * Copyright (c) 2008-2018 Haulmont.
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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.gui.components.RootWindow;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.UrlHandlingMode;
import com.haulmont.cuba.web.sys.navigation.NavigationState;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Component(RedirectHandler.NAME)
@Scope("prototype")
public class RedirectHandler {

    public static final String NAME = "cuba_RedirectHandler";

    private static final Logger log = LoggerFactory.getLogger(RedirectHandler.class);

    protected static final String REDIRECT_PARAM = "redirectTo";

    @Inject
    protected Events events;

    @Inject
    protected WebConfig webConfig;

    protected AppUI ui;

    protected NavigationState redirect;

    public RedirectHandler(AppUI ui) {
        this.ui = ui;
    }

    public void schedule(NavigationState redirect) {
        UrlHandlingMode urlHandlingMode = webConfig.getUrlHandlingMode();
        if (UrlHandlingMode.URL_ROUTES != urlHandlingMode) {
            log.debug("RedirectHandler is disabled for {} URL handling mode", urlHandlingMode);
            return;
        }

        Preconditions.checkNotNullArgument(redirect);

        this.redirect = redirect;

        String nestedRoute = redirect.getNestedRoute();
        if (StringUtils.isEmpty(nestedRoute)) {
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put(REDIRECT_PARAM, nestedRoute);

        if (redirect.getParams() != null) {
            params.putAll(redirect.getParams());
        }

        RootWindow rootWindow = ui.getTopLevelWindow();
        if (rootWindow != null) {
            ui.getUrlRouting().replaceState(rootWindow.getFrameOwner(), params);
        }
    }

    public boolean scheduled() {
        return redirect != null;
    }

    public void redirect() {
        UrlHandlingMode urlHandlingMode = webConfig.getUrlHandlingMode();
        if (UrlHandlingMode.URL_ROUTES != urlHandlingMode) {
            log.debug("RedirectHandler is disabled for {} URL handling mode", urlHandlingMode);
            return;
        }

        String nestedRoute = redirect.getNestedRoute();
        Map<String, String> params = redirect.getParams();

        String redirectTarget = null;

        if (StringUtils.isNotEmpty(nestedRoute)) {
            redirectTarget = nestedRoute;
        } else if (MapUtils.isNotEmpty(params) && params.containsKey(REDIRECT_PARAM)) {
            redirectTarget = params.remove(REDIRECT_PARAM);
        }

        if (StringUtils.isEmpty(redirectTarget)) {
            return;
        }

        NavigationState currentState = ui.getUrlRouting().getState();
        NavigationState newState = new NavigationState(currentState.getRoot(), "", redirectTarget, params);

        ui.getUrlChangeHandler().handleUrlChangeInternal(newState);

        redirect = null;
    }
}
