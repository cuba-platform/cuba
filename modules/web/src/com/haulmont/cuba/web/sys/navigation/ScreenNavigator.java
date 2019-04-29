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

package com.haulmont.cuba.web.sys.navigation;

import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.navigation.NavigationState;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.sys.navigation.navigationhandler.NavigationHandler;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

/**
 * A facade bean that is intended for screen navigation using all available {@link NavigationHandler} beans.
 */
@Component(ScreenNavigator.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ScreenNavigator {

    public static final String NAME = "cuba_ScreenNavigator";

    @Inject
    protected List<NavigationHandler> navigationHandlers;

    protected UrlChangeHandler urlChangeHandler;
    protected AppUI ui;

    @SuppressWarnings("unused")
    public ScreenNavigator(UrlChangeHandler urlChangeHandler, AppUI ui) {
        this.urlChangeHandler = urlChangeHandler;
        this.ui = ui;
    }

    public void handleScreenNavigation(NavigationState requestedState) {
        for (NavigationHandler handler : navigationHandlers)
            if (handler.doHandle(requestedState, ui))
                return;
    }

    // TODO: get rid of usage in HistoryNavigator
    protected boolean handleCurrentRootNavigation(NavigationState requestedState) {
        if (!currentRootNavigated(requestedState)) {
            return true;
        }

        for (Screens.WindowStack windowStack : urlChangeHandler.getOpenedScreens().getWorkAreaStacks()) {
            boolean closed = urlChangeHandler.closeWindowStack(windowStack);
            if (!closed) {
                urlChangeHandler.revertNavigationState();
                return true;
            }
        }

        return true;
    }

    protected boolean currentRootNavigated(NavigationState requestedState) {
        NavigationState currentState = ui.getHistory().getNow();
        return !urlChangeHandler.isRootState(currentState)
                && urlChangeHandler.isRootState(requestedState);
    }
}
