/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.historycontrol;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.vaadin.client.BrowserInfo;

/**
 * @author gorelov
 * @version $Id$
 */
public abstract class HistoryGwtApi {
    private static final String TOP_HISTORY_TOKEN = "!";
    private static final String PREVIOUS_HISTORY_TOKEN = "_";

    private HandlerRegistration handlerRegistration;
    private String previousHistoryToken = TOP_HISTORY_TOKEN;
    private boolean isForward = false;
    private boolean isFireHistoryState = false;

    public HistoryGwtApi() {
        initStartState();
        initHandler();
    }

    protected void initStartState() {
        // we need to add new history tokens BEFORE ValueChangeHandler will added
        if (urlHasToken(TOP_HISTORY_TOKEN)) {
            History.newItem(PREVIOUS_HISTORY_TOKEN);
            // if init url has TOP_HISTORY_TOKEN, we inform what PREVIOUS_HISTORY_TOKEN should be fired,
            // otherwise not all state variables will have proper values
            isFireHistoryState = true;
        } else {
            History.newItem(TOP_HISTORY_TOKEN);
        }
    }

    protected void initHandler() {
        handlerRegistration = History.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                String historyToken = event.getValue();
                if (isForward) {
                    isForward = false;
                    // here we assume that "forward" action was performed
                    // and if current history token equals to TOP_HISTORY_TOKEN
                    // then this is true, otherwise we ignore
                    if (TOP_HISTORY_TOKEN.equals(historyToken)) {
                        handleHistoryStep();
                    }
                } else {
                    if (!TOP_HISTORY_TOKEN.equals(historyToken)) {
                        // if current history token equals to previous history token
                        // then we assume that "back" action was performed
                        if (previousHistoryToken.equals(historyToken)) {
                            isForward = true;
                        } else {
                            // otherwise we assume that some handmade token was processed
                            // and replace it with PREVIOUS_HISTORY_TOKEN
                            previousHistoryToken = historyToken;
                            History.newItem(PREVIOUS_HISTORY_TOKEN);
                        }
                    }
                    // we always must have TOP_HISTORY_TOKEN on top of history stack
                    // if it is, then History will ignore the call of newItem method.
                    History.newItem(TOP_HISTORY_TOKEN);
                }
            }
        });
        if (isFireHistoryState)
            History.fireCurrentHistoryState();
    }

    protected boolean urlHasToken(String token) {
        String url = Document.get().getURL();
        return url.contains("#" + token);
    }

    protected abstract void onHistoryBackPerformed();

    protected abstract boolean isEnabled();

    public void disable() {
        handlerRegistration.removeHandler();
    }

    protected void handleHistoryStep() {
        if (!isEnabled()) {
            disable();
            return;
        }

        if (!BrowserInfo.get().isIE()) {
            goForward();
        } else {
            goForwardDelayed();
        }
    }

    protected void goForward() {
        onHistoryBackPerformed();
    }

    protected void goForwardDelayed() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                goForward();
            }
        });
    }
}