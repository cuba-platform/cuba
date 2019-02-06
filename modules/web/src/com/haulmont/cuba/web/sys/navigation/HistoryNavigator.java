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

import com.haulmont.cuba.gui.navigation.NavigationState;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.sys.navigation.accessfilter.NavigationFilter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import static com.haulmont.cuba.web.sys.navigation.UrlTools.pushState;
import static com.haulmont.cuba.web.sys.navigation.UrlTools.replaceState;

public class HistoryNavigator {

    protected UrlChangeHandler owner;
    protected AppUI ui;

    protected History history;

    public HistoryNavigator(UrlChangeHandler owner, AppUI ui) {
        this.owner = owner;
        this.ui = ui;

        history = ui.getHistory();
    }

    protected boolean handleHistoryNavigation(NavigationState requestedState) {
        boolean backward = history.searchBackward(requestedState);
        boolean forward = history.searchForward(requestedState);

        if (backward) {
            handleHistoryBackward(requestedState);
        } else if (forward) {
            handleHistoryForward();
        }

        return backward || forward;
    }

    protected void handleHistoryBackward(NavigationState requestedState) {
        NavigationState currentState = history.getNow();

        requestedState = findPreviousState(requestedState);
        if (requestedState == null) {
            owner.revertNavigationState();
            return;
        }

        NavigationFilter.AccessCheckResult accessCheckResult = owner.navigationAllowed(requestedState);
        if (accessCheckResult.isRejected()) {
            if (StringUtils.isNotEmpty(accessCheckResult.getMessage())) {
                owner.showNotification(accessCheckResult.getMessage());
            }

            owner.revertNavigationState();
            return;
        }

        if (owner.isRootState(requestedState)) {
            owner.getScreenNavigator().handleCurrentRootNavigation(requestedState);
        }

        Screen lastOpenedScreen = owner.findActiveScreenByState(currentState);
        if (lastOpenedScreen != null
                && owner.isNotCloseable(lastOpenedScreen.getWindow())) {

            owner.revertNavigationState();
            return;
        }

        if (lastOpenedScreen != null) {
            NavigationState _requestedState = requestedState;

            OperationResult screenCloseResult = lastOpenedScreen.getWindow()
                    .getFrameOwner()
                    .close(FrameOwner.WINDOW_CLOSE_ACTION)
                    .then(() -> proceedHistoryBackward(_requestedState));

            if (OperationResult.Status.FAIL == screenCloseResult.getStatus()
                    || OperationResult.Status.UNKNOWN == screenCloseResult.getStatus()) {
                owner.revertNavigationState();
            }
        } else {
            proceedHistoryBackward(requestedState);
        }
    }

    protected void proceedHistoryBackward(NavigationState requestedState) {
        owner.selectScreen(owner.findActiveScreenByState(requestedState));

        replaceState(requestedState.asRoute());

        history.backward();
    }

    protected void handleHistoryForward() {
        Screen currentScreen = owner.findActiveScreenByState(history.getNow());
        if (currentScreen == null) {
            currentScreen = owner.getActiveScreen();
        }

        String route = owner.getResolvedState(currentScreen)
                .asRoute();

        pushState(route);
    }

    protected NavigationState findPreviousState(NavigationState requestedState) {
        if (owner.isRootState(requestedState)) {
            return requestedState;
        }

        if (Objects.equals(requestedState, history.getNow())) {
            requestedState = history.getPrevious();
        }

        NavigationState prevState;
        Screen prevStateScreen = owner.findScreenByState(requestedState);

        if (prevStateScreen == null
                && !owner.isRootState(requestedState)) {

            while (history.getPrevious() != null) {
                history.backward();
                NavigationState previousState = history.getPrevious();

                if (owner.findActiveScreenByState(previousState) != null
                        || owner.isRootState(previousState)) {
                    break;
                }
            }

            prevState = history.getPrevious();
        } else {
            prevState = requestedState;
        }

        return prevState;
    }
}
