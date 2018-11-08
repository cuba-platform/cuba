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

package com.haulmont.cuba.web.sys.navigation;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.UrlHandlingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WebHistory implements History {

    private static final Logger log = LoggerFactory.getLogger(WebHistory.class);

    @Inject
    protected WebConfig webConfig;

    protected AppUI ui;

    protected int now;
    protected List<NavigationState> history;

    public WebHistory(AppUI ui) {
        this.ui = ui;
        this.now = -1;
        this.history = new ArrayList<>();
    }

    @Override
    public void forward(NavigationState navigationState) {
        if (checkNotNativeUrlHandlingMode()) {
            return;
        }

        Preconditions.checkNotNullArgument(navigationState);

        NavigationState state = ui.getUrlRouting().getState();
        if (!navigationState.equals(state)) {
            throw new IllegalStateException("New history entry doesn't match with actual state");
        }

        if (navigationState.equals(getNow())) {
            return;
        }

        dropFutureEntries();

        history.add(++now, navigationState);
    }

    @Override
    public NavigationState backward() {
        if (checkNotNativeUrlHandlingMode()) {
            return NavigationState.empty();
        }

        NavigationState prevState = history.get(now - 1);
        NavigationState state = ui.getUrlRouting().getState();
        if (now - 1 > 0 && !prevState.equals(state)) {
            throw new IllegalStateException("Previous history entry doesn't match with actual state");
        }

        return now - 1 >= 0 ? history.get(--now) : null;
    }

    @Override
    public NavigationState getNow() {
        if (checkNotNativeUrlHandlingMode()) {
            return NavigationState.empty();
        }

        return now >= 0 ? history.get(now) : null;
    }

    @Override
    public NavigationState getPrevious() {
        if (checkNotNativeUrlHandlingMode()) {
            return NavigationState.empty();
        }

        return now - 1 >= 0 ? history.get(now - 1) : null;
    }

    @Override
    public NavigationState getNext() {
        if (checkNotNativeUrlHandlingMode()) {
            return NavigationState.empty();
        }

        return now + 1 < history.size() ? history.get(now + 1) : null;
    }

    @Override
    public boolean searchBackward(NavigationState navigationState) {
        if (checkNotNativeUrlHandlingMode()) {
            return false;
        }

        Preconditions.checkNotNullArgument(navigationState);

        for (int i = now - 1; i >= 0; i--) {
            if (Objects.equals(history.get(i), navigationState)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean searchForward(NavigationState navigationState) {
        if (checkNotNativeUrlHandlingMode()) {
            return false;
        }

        Preconditions.checkNotNullArgument(navigationState);

        for (int i = now + 1; i < history.size(); i++) {
            if (Objects.equals(history.get(i), navigationState)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean has(NavigationState navigationState) {
        Preconditions.checkNotNullArgument(navigationState);

        return history.contains(navigationState);
    }

    protected void dropFutureEntries() {
        for (int i = now + 1; i < history.size(); i++) {
            //noinspection RedundantCast
            history.remove((int) i);
        }
    }

    protected boolean checkNotNativeUrlHandlingMode() {
        boolean nativeMode = UrlHandlingMode.URL_ROUTES == webConfig.getUrlHandlingMode();

        if (!nativeMode) {
            log.debug("History bean invocations are ignored for {} URL handling mode", webConfig.getUrlHandlingMode());
        }

        return !nativeMode;
    }
}
