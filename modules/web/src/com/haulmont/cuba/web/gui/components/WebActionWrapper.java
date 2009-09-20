package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Action;

class WebActionWrapper extends com.vaadin.event.Action {
    private final Action action;

    public WebActionWrapper(Action action) {
        super(action.getCaption());
        this.action = action;
    }

    @Override
    public String getCaption() {
        return action.getCaption();
    }
}
