package com.haulmont.cuba.web.sys.linkhandling;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.web.App;

import java.util.Map;

public class ExternalLinkContext {
    protected Map<String, String> requestParams;
    protected String action;
    protected App app;

    public ExternalLinkContext(Map<String, String> requestParams, String action, App app) {
        this.requestParams = ImmutableMap.copyOf(requestParams);
        this.action = action;
        this.app = app;
    }

    public Map<String, String> getRequestParams() {
        return requestParams;
    }

    public String getAction() {
        return action;
    }

    public App getApp() {
        return app;
    }
}
