/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.sys.navigation.navigationhandler;

import com.haulmont.cuba.gui.navigation.NavigationState;
import com.haulmont.cuba.gui.navigation.UrlParamsChangedEvent;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.sys.navigation.UrlChangeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Order(NavigationHandler.LOWEST_PLATFORM_PRECEDENCE - 20)
public class ParamsNavigationHandler extends AbstractNavigationHandler implements NavigationHandler {

    private static final Logger log = LoggerFactory.getLogger(ParamsNavigationHandler.class);

    @Override
    public boolean doHandle(NavigationState requestedState, AppUI ui) {
        if (!paramsChanged(requestedState, ui.getUrlChangeHandler())) {
            return false;
        }

        Screen screen = ui.getUrlChangeHandler().findActiveScreenByState(requestedState);
        if (screen == null) {
            log.debug("Unable to find a screen for state: '{}", requestedState);
            return true;
        }

        Map<String, String> params = requestedState.getParams() != null
                ? requestedState.getParams()
                : Collections.emptyMap();

        UiControllerUtils.fireEvent(screen, UrlParamsChangedEvent.class,
                new UrlParamsChangedEvent(screen, params));

        return true;
    }

    protected boolean paramsChanged(NavigationState requestedState, UrlChangeHandler urlChangeHandler) {
        Screen activeScreen = urlChangeHandler.getActiveScreen();

        String currentParams = urlChangeHandler.getResolvedState(activeScreen)
                .getParamsString();

        return !Objects.equals(currentParams, requestedState.getParamsString());
    }
}
