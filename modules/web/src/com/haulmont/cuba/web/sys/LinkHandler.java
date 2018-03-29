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
 *
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.sys.linkhandling.ExternalLinkContext;
import com.haulmont.cuba.web.sys.linkhandling.LinkHandlerProcessor;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.WrappedSession;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Handles links from outside of the application.
 * <br> This bean is used particularly when a request URL contains one of
 * {@link com.haulmont.cuba.web.WebConfig#getLinkHandlerActions()} actions.
 * <br> The bean traverses all implementations of {@link com.haulmont.cuba.web.sys.linkhandling.LinkHandlerProcessor}
 * by their priority and gives control to first possible to handle processor.
 */
@org.springframework.stereotype.Component(LinkHandler.NAME)
@Scope("prototype")
public class LinkHandler {

    public static final String NAME = "cuba_LinkHandler";

    @Inject
    protected List<LinkHandlerProcessor> processors;

    protected App app;
    protected String action;
    protected Map<String, String> requestParams;

    public LinkHandler(App app, String action, Map<String, String> requestParams) {
        this.app = app;
        this.action = action;
        this.requestParams = requestParams;
    }

    /**
     * Check state of LinkHandler and application.
     *
     * @return true if application and LinkHandler in an appropriate state.
     */
    public boolean canHandleLink() {
        return app.getTopLevelWindow() instanceof Window.HasWorkArea;
    }

    /**
     * Called to handle the link.
     */
    public void handle() {
        try {
            ExternalLinkContext linkContext = new ExternalLinkContext(requestParams, action, app);
            for (LinkHandlerProcessor processor : processors) {
                if (processor.canHandle(linkContext)) {
                    processor.handle(linkContext);
                    break;
                }
            }
        } finally {
            VaadinRequest request = VaadinService.getCurrentRequest();
            WrappedSession wrappedSession = request.getWrappedSession();
            wrappedSession.removeAttribute(AppUI.LAST_REQUEST_PARAMS_ATTR);
            wrappedSession.removeAttribute(AppUI.LAST_REQUEST_ACTION_ATTR);
        }
    }

}